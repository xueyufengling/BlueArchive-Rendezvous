package fw.codec.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.OptionalLong;
import java.util.function.Function;

import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import fw.core.Core;
import fw.core.registry.RegistryFactory;
import fw.core.registry.RegistryWalker;
import lyra.klass.KlassWalker;
import lyra.klass.special.TypeWrapper;
import lyra.lang.Handles;
import lyra.lang.Reflection;
import lyra.lang.internal.HandleBase;
import lyra.object.ObjectManipulator;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

/**
 * 自动生成CODEC的注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface CodecAutogen {

	public static final String classSimpleName = "'classSimpleName'";

	/**
	 * Codec的注册名称
	 * 
	 * @return
	 */
	String name() default classSimpleName;

	@EventBusSubscriber(modid = Core.ModId, bus = Bus.MOD)
	public static class CodecGenerator {
		/**
		 * CODEC字段默认的名称
		 */
		public static final String DEFAULT_CODEC_FIELD = "CODEC";

		/**
		 * 手动指定某个类的CODEC字段名称，例如默认名称找不到时就手动指定正确的名称。
		 */
		public static final HashMap<Class<?>, String> CODEC_FIELD_NAMES = new HashMap<>();

		/**
		 * 目标CODEC所在类的列表
		 */
		private static final ArrayList<Class<?>> codecClasses = new ArrayList<>();

		/**
		 * 目标类的构造函数。自动寻找构造函数需要与{@code @CodecEntry}注解字段的顺序和类型完全一致，如果不一致则需要手动指定构造函数。
		 */
		private static final HashMap<Class<?>, MethodHandle> codecCtors = new HashMap<>();

		/**
		 * 获取CODEC的默认注册名称，将单词以下划线分隔并转换为全小写。
		 * 
		 * @param simpleName
		 * @return
		 */
		public static String defaultCodecRegisterName(String simpleName) {
			return simpleName.trim().replaceAll("([A-Z])", "_$1")// 正则匹配大写字母前插入下划线
					.toLowerCase()
					.replaceAll("^_", "");// 处理开头可能多出的下划线
		}

		public static String defaultCodecRegisterName(Class<?> codecClass) {
			return defaultCodecRegisterName(codecClass.getSimpleName());
		}

		public static final MethodHandle create;
		public static final MethodHandle mapCodec;

		static {
			create = Handles.findStaticMethodHandle(RecordCodecBuilder.class, "create", Codec.class, Function.class);
			mapCodec = Handles.findStaticMethodHandle(RecordCodecBuilder.class, "mapCodec", MapCodec.class, Function.class);
		}

		/**
		 * 扫描目标类的全部{@code @CodecEntry}注解字段并构造CODEC
		 * 
		 * @param <T>
		 * @param targetClass
		 * @param buildMethod 构造CODEC的方法
		 * @return
		 */
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public static final Object generate(Class<?> targetClass, MethodHandle buildMethod) {
			ArrayList<App> entries = new ArrayList<>();
			ArrayList<Class<?>> arg_types = new ArrayList<>();
			KlassWalker.walkFields(targetClass, CodecEntry.class, (Field f, boolean isStatic, Object value, CodecEntry annotation) -> {
				if (!isStatic) {// 只有非静态成员变量需要序列化
					String key = annotation.key();
					if (key.equals(CodecEntry.fieldName))
						key = f.getName();
					Function universalFieldGetter = (Object targetObj) -> {
						return ObjectManipulator.access(targetObj, f);
					};// 字段Getter
					Class<?> field_type = f.getType();
					Codec field_codec = null;
					boolean is_optional = false;// 是否已经绑定了字段名称
					if (field_type == byte.class || field_type == Byte.class) {
						field_codec = Codec.BYTE;
					} else if (field_type == boolean.class || field_type == Boolean.class) {
						field_codec = Codec.BOOL;
					} else if (field_type == short.class || field_type == Short.class) {
						field_codec = Codec.SHORT;
					} else if (field_type == int.class || field_type == Integer.class) {
						field_codec = Codec.INT;
						IntRange ir = annotation.int_range().length == 0 ? null : annotation.int_range()[0];
						if (ir != null)
							field_codec = Codec.intRange(ir.min(), ir.max());
					} else if (field_type == long.class || field_type == Long.class) {
						field_codec = Codec.LONG;
					} else if (field_type == OptionalLong.class) {
						field_codec = Codec.LONG;
						is_optional = true;
					} else if (field_type == float.class || field_type == Float.class) {
						field_codec = Codec.FLOAT;
						FloatRange fr = annotation.float_range().length == 0 ? null : annotation.float_range()[0];
						if (fr != null)
							field_codec = Codec.floatRange(fr.min(), fr.max());
					} else if (field_type == double.class || field_type == Double.class) {
						field_codec = Codec.DOUBLE;
						DoubleRange dr = annotation.double_range().length == 0 ? null : annotation.double_range()[0];
						if (dr != null)
							field_codec = Codec.doubleRange(dr.min(), dr.max());
					} else if (field_type == String.class) {
						field_codec = Codec.STRING;
						StringLength sl = annotation.string_length().length == 0 ? null : annotation.string_length()[0];
						if (sl != null)
							field_codec = Codec.string(sl.min(), sl.max());
					} else {// 如果不是内置的类型，则查找对应类的CODEC_FIELD字段作为CODEC。
						Object class_codec = ObjectManipulator.access(field_type, CODEC_FIELD_NAMES.computeIfAbsent(targetClass, (Class<?> t) -> DEFAULT_CODEC_FIELD));
						if (class_codec instanceof Codec c)
							field_codec = c;
						else if (class_codec instanceof MapCodec mc)
							field_codec = mc.codec();
					}
					arg_types.add(field_type);// 储存该字段的类型，该类型必须和构造函数的参数类型和顺序严格匹配
					String[] namespaces = annotation.path() == null ? new String[0] : annotation.path().split(CodecEntry.pathSeparator);
					for (int depth = 0; depth < namespaces.length; ++depth) {// 构建key的路径path，不包括key本身
						String namespace = namespaces[depth];
						if (namespace.equals(""))
							continue;// 略过空路径
						else
							field_codec = field_codec.fieldOf(namespace).codec();// 根据路径构造子键
					}
					MapCodec final_codec = null;
					if (is_optional)
						final_codec = field_codec.optionalFieldOf(key);
					else
						final_codec = field_codec.fieldOf(key);
					entries.add(final_codec.forGetter(universalFieldGetter));
				}
			});
			Class<?>[] __types = new Class<?>[arg_types.size()];
			arg_types.toArray(__types);
			final MethodHandle ctor = codecCtors.computeIfAbsent(targetClass, (Class<?> t) -> HandleBase.findConstructor(targetClass, __types));// 寻找目标类的构造函数，如果没有在forCodec()时手动指定构造函数参数类型，则依据收集到的字段声明类型严格匹配寻找构造函数
			if (ctor == null)
				System.err.println("Cannot find constructor when generating CODEC for " + targetClass + ", check the fields annotated with CodecEntry's order and type, make sure that both order and type are completely the same.");
			Object CODEC = null;
			switch (entries.size()) {
			case 1:
				try {
					CODEC = buildMethod.invoke((Function<RecordCodecBuilder.Instance, App>) (ins -> {
						App entry0 = entries.get(0);
						return ins.group(entry0).apply(ins, (Object arg0) -> {
							try {
								return ctor.invoke(arg0);
							} catch (Throwable ex) {
								ex.printStackTrace();
							}
							return null;
						});
					}));
				} catch (Throwable e) {
					e.printStackTrace();
				}
				break;
			case 2:
				try {
					CODEC = buildMethod.invoke((Function<RecordCodecBuilder.Instance, App>) (ins -> {
						App entry0 = entries.get(0);
						App entry1 = entries.get(1);
						return ins.group(entry0, entry1).apply(ins, (Object arg0, Object arg1) -> {
							try {
								return ctor.invoke(arg0, arg1);
							} catch (Throwable ex) {
								ex.printStackTrace();
							}
							return null;
						});
					}));
				} catch (Throwable e) {
					e.printStackTrace();
				}
				break;
			case 3:
				try {
					CODEC = buildMethod.invoke((Function<RecordCodecBuilder.Instance, App>) (ins -> {
						App entry0 = entries.get(0);
						App entry1 = entries.get(1);
						App entry2 = entries.get(2);
						return ins.group(entry0, entry1, entry2).apply(ins, (Object arg0, Object arg1, Object arg2) -> {
							try {
								return ctor.invoke(arg0, arg1, arg2);
							} catch (Throwable ex) {
								ex.printStackTrace();
							}
							return null;
						});
					}));
				} catch (Throwable e) {
					e.printStackTrace();
				}
				break;
			case 4:
				try {
					CODEC = buildMethod.invoke((Function<RecordCodecBuilder.Instance, App>) (ins -> {
						App entry0 = entries.get(0);
						App entry1 = entries.get(1);
						App entry2 = entries.get(2);
						App entry3 = entries.get(3);
						return ins.group(entry0, entry1, entry2, entry3).apply(ins, (Object arg0, Object arg1, Object arg2, Object arg3) -> {
							try {
								return ctor.invoke(arg0, arg1, arg2, arg3);
							} catch (Throwable ex) {
								ex.printStackTrace();
							}
							return null;
						});
					}));
				} catch (Throwable e) {
					e.printStackTrace();
				}
				break;
			case 5:
				try {
					CODEC = buildMethod.invoke((Function<RecordCodecBuilder.Instance, App>) (ins -> {
						App entry0 = entries.get(0);
						App entry1 = entries.get(1);
						App entry2 = entries.get(2);
						App entry3 = entries.get(3);
						App entry4 = entries.get(4);
						return ins.group(entry0, entry1, entry2, entry3, entry4).apply(ins, (Object arg0, Object arg1, Object arg2, Object arg3, Object arg4) -> {
							try {
								return ctor.invoke(arg0, arg1, arg2, arg3, arg4);
							} catch (Throwable ex) {
								ex.printStackTrace();
							}
							return null;
						});
					}));
				} catch (Throwable e) {
					e.printStackTrace();
				}
				break;
			case 6:
				try {
					CODEC = buildMethod.invoke((Function<RecordCodecBuilder.Instance, App>) (ins -> {
						App entry0 = entries.get(0);
						App entry1 = entries.get(1);
						App entry2 = entries.get(2);
						App entry3 = entries.get(3);
						App entry4 = entries.get(4);
						App entry5 = entries.get(5);
						return ins.group(entry0, entry1, entry2, entry3, entry4, entry5).apply(ins, (Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5) -> {
							try {
								return ctor.invoke(arg0, arg1, arg2, arg3, arg4, arg5);
							} catch (Throwable ex) {
								ex.printStackTrace();
							}
							return null;
						});
					}));
				} catch (Throwable e) {
					e.printStackTrace();
				}
			case 7:
				try {
					CODEC = buildMethod.invoke((Function<RecordCodecBuilder.Instance, App>) (ins -> {
						App entry0 = entries.get(0);
						App entry1 = entries.get(1);
						App entry2 = entries.get(2);
						App entry3 = entries.get(3);
						App entry4 = entries.get(4);
						App entry5 = entries.get(5);
						App entry6 = entries.get(6);
						return ins.group(entry0, entry1, entry2, entry3, entry4, entry5, entry6).apply(ins, (Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6) -> {
							try {
								return ctor.invoke(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
							} catch (Throwable ex) {
								ex.printStackTrace();
							}
							return null;
						});
					}));
				} catch (Throwable e) {
					e.printStackTrace();
				}
				break;
			case 8:
				try {
					CODEC = buildMethod.invoke((Function<RecordCodecBuilder.Instance, App>) (ins -> {
						App entry0 = entries.get(0);
						App entry1 = entries.get(1);
						App entry2 = entries.get(2);
						App entry3 = entries.get(3);
						App entry4 = entries.get(4);
						App entry5 = entries.get(5);
						App entry6 = entries.get(6);
						App entry7 = entries.get(7);
						return ins.group(entry0, entry1, entry2, entry3, entry4, entry5, entry6, entry7).apply(ins, (Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7) -> {
							try {
								return ctor.invoke(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
							} catch (Throwable ex) {
								ex.printStackTrace();
							}
							return null;
						});
					}));
				} catch (Throwable e) {
					e.printStackTrace();
				}
				break;
			case 9:
				try {
					CODEC = buildMethod.invoke((Function<RecordCodecBuilder.Instance, App>) (ins -> {
						App entry0 = entries.get(0);
						App entry1 = entries.get(1);
						App entry2 = entries.get(2);
						App entry3 = entries.get(3);
						App entry4 = entries.get(4);
						App entry5 = entries.get(5);
						App entry6 = entries.get(6);
						App entry7 = entries.get(7);
						App entry8 = entries.get(8);
						return ins.group(entry0, entry1, entry2, entry3, entry4, entry5, entry6, entry7, entry8).apply(ins, (Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8) -> {
							try {
								return ctor.invoke(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
							} catch (Throwable ex) {
								ex.printStackTrace();
							}
							return null;
						});
					}));
				} catch (Throwable e) {
					e.printStackTrace();
				}
				break;
			case 10:
				try {
					CODEC = buildMethod.invoke((Function<RecordCodecBuilder.Instance, App>) (ins -> {
						App entry0 = entries.get(0);
						App entry1 = entries.get(1);
						App entry2 = entries.get(2);
						App entry3 = entries.get(3);
						App entry4 = entries.get(4);
						App entry5 = entries.get(5);
						App entry6 = entries.get(6);
						App entry7 = entries.get(7);
						App entry8 = entries.get(8);
						App entry9 = entries.get(9);
						return ins.group(entry0, entry1, entry2, entry3, entry4, entry5, entry6, entry7, entry8, entry9).apply(ins, (Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9) -> {
							try {
								return ctor.invoke(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
							} catch (Throwable ex) {
								ex.printStackTrace();
							}
							return null;
						});
					}));
				} catch (Throwable e) {
					e.printStackTrace();
				}
				break;
			case 11:
				try {
					CODEC = buildMethod.invoke((Function<RecordCodecBuilder.Instance, App>) (ins -> {
						App entry0 = entries.get(0);
						App entry1 = entries.get(1);
						App entry2 = entries.get(2);
						App entry3 = entries.get(3);
						App entry4 = entries.get(4);
						App entry5 = entries.get(5);
						App entry6 = entries.get(6);
						App entry7 = entries.get(7);
						App entry8 = entries.get(8);
						App entry9 = entries.get(9);
						App entry10 = entries.get(10);
						return ins.group(entry0, entry1, entry2, entry3, entry4, entry5, entry6, entry7, entry8, entry9, entry10).apply(ins, (Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10) -> {
							try {
								return ctor.invoke(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
							} catch (Throwable ex) {
								ex.printStackTrace();
							}
							return null;
						});
					}));
				} catch (Throwable e) {
					e.printStackTrace();
				}
				break;
			case 12:
				try {
					CODEC = buildMethod.invoke((Function<RecordCodecBuilder.Instance, App>) (ins -> {
						App entry0 = entries.get(0);
						App entry1 = entries.get(1);
						App entry2 = entries.get(2);
						App entry3 = entries.get(3);
						App entry4 = entries.get(4);
						App entry5 = entries.get(5);
						App entry6 = entries.get(6);
						App entry7 = entries.get(7);
						App entry8 = entries.get(8);
						App entry9 = entries.get(9);
						App entry10 = entries.get(10);
						App entry11 = entries.get(11);
						return ins.group(entry0, entry1, entry2, entry3, entry4, entry5, entry6, entry7, entry8, entry9, entry10, entry11).apply(ins, (Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11) -> {
							try {
								return ctor.invoke(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
							} catch (Throwable ex) {
								ex.printStackTrace();
							}
							return null;
						});
					}));
				} catch (Throwable e) {
					e.printStackTrace();
				}
				break;
			case 13:
				try {
					CODEC = buildMethod.invoke((Function<RecordCodecBuilder.Instance, App>) (ins -> {
						App entry0 = entries.get(0);
						App entry1 = entries.get(1);
						App entry2 = entries.get(2);
						App entry3 = entries.get(3);
						App entry4 = entries.get(4);
						App entry5 = entries.get(5);
						App entry6 = entries.get(6);
						App entry7 = entries.get(7);
						App entry8 = entries.get(8);
						App entry9 = entries.get(9);
						App entry10 = entries.get(10);
						App entry11 = entries.get(11);
						App entry12 = entries.get(12);
						return ins.group(entry0, entry1, entry2, entry3, entry4, entry5, entry6, entry7, entry8, entry9, entry10, entry11, entry12).apply(ins, (Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12) -> {
							try {
								return ctor.invoke(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12);
							} catch (Throwable ex) {
								ex.printStackTrace();
							}
							return null;
						});
					}));
				} catch (Throwable e) {
					e.printStackTrace();
				}
				break;
			case 14:
				try {
					CODEC = buildMethod.invoke((Function<RecordCodecBuilder.Instance, App>) (ins -> {
						App entry0 = entries.get(0);
						App entry1 = entries.get(1);
						App entry2 = entries.get(2);
						App entry3 = entries.get(3);
						App entry4 = entries.get(4);
						App entry5 = entries.get(5);
						App entry6 = entries.get(6);
						App entry7 = entries.get(7);
						App entry8 = entries.get(8);
						App entry9 = entries.get(9);
						App entry10 = entries.get(10);
						App entry11 = entries.get(11);
						App entry12 = entries.get(12);
						App entry13 = entries.get(13);
						return ins.group(entry0, entry1, entry2, entry3, entry4, entry5, entry6, entry7, entry8, entry9, entry10, entry11, entry12, entry13).apply(ins, (Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13) -> {
							try {
								return ctor.invoke(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13);
							} catch (Throwable ex) {
								ex.printStackTrace();
							}
							return null;
						});
					}));
				} catch (Throwable e) {
					e.printStackTrace();
				}
				break;
			case 15:
				try {
					CODEC = buildMethod.invoke((Function<RecordCodecBuilder.Instance, App>) (ins -> {
						App entry0 = entries.get(0);
						App entry1 = entries.get(1);
						App entry2 = entries.get(2);
						App entry3 = entries.get(3);
						App entry4 = entries.get(4);
						App entry5 = entries.get(5);
						App entry6 = entries.get(6);
						App entry7 = entries.get(7);
						App entry8 = entries.get(8);
						App entry9 = entries.get(9);
						App entry10 = entries.get(10);
						App entry11 = entries.get(11);
						App entry12 = entries.get(12);
						App entry13 = entries.get(13);
						App entry14 = entries.get(14);
						return ins.group(entry0, entry1, entry2, entry3, entry4, entry5, entry6, entry7, entry8, entry9, entry10, entry11, entry12, entry13, entry14).apply(ins, (Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14) -> {
							try {
								return ctor.invoke(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14);
							} catch (Throwable ex) {
								ex.printStackTrace();
							}
							return null;
						});
					}));
				} catch (Throwable e) {
					e.printStackTrace();
				}
				break;
			case 16:
				try {
					CODEC = buildMethod.invoke((Function<RecordCodecBuilder.Instance, App>) (ins -> {
						App entry0 = entries.get(0);
						App entry1 = entries.get(1);
						App entry2 = entries.get(2);
						App entry3 = entries.get(3);
						App entry4 = entries.get(4);
						App entry5 = entries.get(5);
						App entry6 = entries.get(6);
						App entry7 = entries.get(7);
						App entry8 = entries.get(8);
						App entry9 = entries.get(9);
						App entry10 = entries.get(10);
						App entry11 = entries.get(11);
						App entry12 = entries.get(12);
						App entry13 = entries.get(13);
						App entry14 = entries.get(14);
						App entry15 = entries.get(15);
						return ins.group(entry0, entry1, entry2, entry3, entry4, entry5, entry6, entry7, entry8, entry9, entry10, entry11, entry12, entry13, entry14, entry15).apply(ins, (Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14, Object arg15) -> {
							try {
								return ctor.invoke(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15);
							} catch (Throwable ex) {
								ex.printStackTrace();
							}
							return null;
						});
					}));
				} catch (Throwable e) {
					e.printStackTrace();
				}
				break;
			default:
				System.err.println("Invalid Codec entries count: " + entries.size() + ", should be in (0, 16]");
			}
			return CODEC;
		}

		/**
		 * 生成一个MapCodec
		 * 
		 * @param <T>
		 * @param targetClass
		 * @return
		 */
		@SuppressWarnings("unchecked")
		public static final <T> MapCodec<T> generateMapCodec(Class<T> targetClass) {
			return (MapCodec<T>) generate(targetClass, mapCodec);
		}

		/**
		 * 生成一个Codec
		 * 
		 * @param <T>
		 * @param targetClass
		 * @return
		 */
		@SuppressWarnings("unchecked")
		public static final <T> Codec<T> generateCodec(Class<T> targetClass) {
			return (Codec<T>) generate(targetClass, create);
		}

		/**
		 * 生成目标类的CODEC并注册到相应的注册表中，并返回对应的DeferredHolder
		 * 
		 * @param <T>
		 * @param codecName    注册名称
		 * @param target       要生成CODEC的类
		 * @param registryType 要将CODEC注册到哪个类型
		 * @return
		 */
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public static final DeferredHolder generateAndRegisterHolder(String codecName, Class<?> target, MethodHandle buildMethod, Class<?> registryType) {
			Object CODEC = generate(target, buildMethod);
			TypeWrapper<DeferredHolder> holderWrapper = TypeWrapper.wrap();
			RegistryWalker.walkMapCodecRegistries((Field f, ResourceKey registryKey, Class<?> codecType) -> {
				if (Reflection.is(registryType, codecType)) {// 当注册表MapCodec的泛型参数和传入的registryType匹配时注册
					holderWrapper.value = RegistryFactory.deferredRegister(registryKey).register(codecName, () -> CODEC);
				}
			});
			return holderWrapper.value;
		}

		/**
		 * 生成目标类的CODEC并注册到相应的注册表中，并返回CODEC，可用于给静态字段初始化
		 * 
		 * @param <T>
		 * @param codecName    注册名称
		 * @param target       要生成CODEC的类
		 * @param registryType 要将CODEC注册到哪个类型
		 * @return
		 */
		@SuppressWarnings({ "rawtypes", "unchecked" })
		private static final Object generateAndRegister(String codecName, Class<?> target, MethodHandle buildMethod, Class<?> registryType) {
			Object CODEC = generate(target, buildMethod);
			RegistryWalker.walkMapCodecRegistries((Field f, ResourceKey registryKey, Class<?> codecType) -> {
				if (Reflection.is(registryType, codecType)) {// 当注册表MapCodec的泛型参数和传入的registryType匹配时注册
					RegistryFactory.deferredRegister(registryKey).register(codecName, () -> CODEC);
				}
			});
			return CODEC;
		}

		private static final void generateAndRegisterCodecs() {
			for (Class<?> codecClass : codecClasses) {
				KlassWalker.walkFields(codecClass, CodecAutogen.class, (Field f, boolean isStatic, Object value, CodecAutogen annotation) -> {
					if (isStatic) {
						MethodHandle internalBuildMethod = null;
						if (Reflection.is(f, Codec.class))
							internalBuildMethod = create;
						else if (Reflection.is(f, MapCodec.class))
							internalBuildMethod = mapCodec;
						else// 如果字段不是有效的CODEC类型，则直接略过
							return;
						String registerName = annotation.name();
						if (classSimpleName.equals(registerName))
							registerName = defaultCodecRegisterName(codecClass);
						ObjectManipulator.setObject(codecClass, f, generateAndRegister(registerName, codecClass, internalBuildMethod, RegistryWalker.getGenericType(f)));
					}
				});
			}
		}

		@SubscribeEvent(priority = EventPriority.HIGH)
		public static final void autoGenerateCodecs(FMLConstructModEvent event) {
			generateAndRegisterCodecs();
		}

		/**
		 * 注册CODEC生成
		 * 
		 * @param targetClass
		 */
		public static final void forCodec(Class<?> targetClass, Class<?>... ctorTypes) {
			if (!codecClasses.contains(targetClass)) {
				codecClasses.add(targetClass);
				if (ctorTypes.length != 0)// 手动传入了参数则寻找构造函数，否则就遍历字段时寻找
					codecCtors.put(targetClass, HandleBase.findConstructor(targetClass, ctorTypes));
			}
		}
	}
}
