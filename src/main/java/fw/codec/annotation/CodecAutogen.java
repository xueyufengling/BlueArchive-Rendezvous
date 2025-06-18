package fw.codec.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.function.Function;

import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import fw.codec.CodecFactory;
import fw.codec.CodecResolver;
import fw.core.Core;
import fw.core.registry.Holders;
import fw.core.registry.RegistryFactory;
import fw.core.registry.RegistryWalker;
import lyra.klass.GenericTypes;
import lyra.klass.KlassWalker;
import lyra.lang.Arrays;
import lyra.lang.Handles;
import lyra.lang.Reflection;
import lyra.lang.internal.HandleBase;
import lyra.object.ObjectManipulator;
import lyra.object.Placeholders.TypeWrapper;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.KeyDispatchDataCodec;
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
	String name()

	default classSimpleName;

	/**
	 * 当构建的CODEC没有任何field时，即没有注解CodecEntry的字段，是否立即返回null。<br>
	 * 设置为true则立即返回null，false则返回一个没有field的空CODEC。<br>
	 * 如果本该生成CODEC的字段构建失败为null，则不论数据生成还是运行时均会报注册null值的错误，因此通常情况下不建议将该值设置为true。<br>
	 * 
	 * @return
	 */
	boolean null_if_empty()

	default false;

	/**
	 * 是否也生成基类的带有CodecEntry注解的字段的CODEC条目
	 * 
	 * @return
	 */
	boolean include_base() default true;

	@EventBusSubscriber(modid = Core.ModId, bus = Bus.MOD)
	public static class CodecGenerator {
		/**
		 * CODEC字段默认的名称
		 */
		public static final String DEFAULT_CODEC_FIELD = "CODEC";

		/**
		 * 本类在构建CODEC时，将依据该Map查询哪个类使用哪个CODEC。
		 */
		@SuppressWarnings("rawtypes")
		public static final HashMap<Class<?>, CodecResolver> CODECS = new HashMap<>();

		static {
			setCodec(byte.class, Codec.BYTE);
			setCodec(Byte.class, Codec.BYTE);
			setCodec(boolean.class, Codec.BOOL);
			setCodec(Boolean.class, Codec.BOOL);
			setCodec(short.class, Codec.SHORT);
			setCodec(Short.class, Codec.SHORT);
			CodecResolver<CodecEntry> INT = (Type type, CodecEntry params) -> {
				IntRange ir = params.int_range().length == 0 ? null : params.int_range()[0];
				if (ir == null)
					return CodecResolver.Entry.of(Codec.INT);
				else
					return CodecResolver.Entry.of(Codec.intRange(ir.min(), ir.max()));
			};
			setCodec(int.class, INT);
			setCodec(Integer.class, INT);
			setCodec(long.class, Codec.LONG);
			setCodec(Long.class, Codec.LONG);
			CodecResolver<CodecEntry> FLOAT = (Type type, CodecEntry params) -> {
				FloatRange fr = params.float_range().length == 0 ? null : params.float_range()[0];
				if (fr == null)
					return CodecResolver.Entry.of(Codec.FLOAT);
				else
					return CodecResolver.Entry.of(Codec.floatRange(fr.min(), fr.max()));
			};
			setCodec(float.class, FLOAT);
			setCodec(Float.class, FLOAT);
			CodecResolver<CodecEntry> DOUBLE = (Type type, CodecEntry params) -> {
				DoubleRange dr = params.double_range().length == 0 ? null : params.double_range()[0];
				if (dr == null)
					return CodecResolver.Entry.of(Codec.DOUBLE);
				else
					return CodecResolver.Entry.of(Codec.doubleRange(dr.min(), dr.max()));
			};
			setCodec(double.class, DOUBLE);
			setCodec(Double.class, DOUBLE);
			CodecResolver<CodecEntry> STRING = (Type type, CodecEntry params) -> {
				StringLength sl = params.string_length().length == 0 ? null : params.string_length()[0];
				if (sl == null)
					return CodecResolver.Entry.of(Codec.STRING);
				else
					return CodecResolver.Entry.of(Codec.string(sl.min(), sl.max()));
			};
			setCodec(String.class, STRING);
			CodecResolver<Object> HOLDER = (Type type, Object params) -> {
				Class<?> holderType = Holders.getHolderType(type);
				Codec<?> codec = getCodec(holderType, GenericTypes.type(type, 0), params).asCodec();
				if (codec == null)
					throw new IllegalArgumentException("No CODEC found for type " + holderType + " in Holder entry.");
				return CodecResolver.Entry.of(codec);
			};
			setCodec(Holder.class, HOLDER);
			setCodec(Holder.Reference.class, HOLDER);

			CodecResolver<CodecEntry> LIST = (Type type, CodecEntry params) -> {
				Class<?> listType = Arrays.getListType(type);
				Codec<?> codec = getCodec(listType, GenericTypes.type(type, 0), params).asCodec();
				if (codec == null)
					throw new IllegalArgumentException("No CODEC found for type " + listType + " in List entry.");
				ListSize ls = params.list_size().length == 0 ? null : params.list_size()[0];
				if (ls == null)
					return CodecResolver.Entry.of(codec.listOf());
				else
					return CodecResolver.Entry.of(codec.listOf(ls.min(), ls.max()));
			};
			setCodec(List.class, LIST);
			CodecResolver<CodecEntry> MAP = (Type type, CodecEntry params) -> {
				Class<?> keyType = GenericTypes.classes(type)[0].type();
				Codec<?> keyCodec = getCodec(keyType, GenericTypes.type(type, 0), params).asCodec();
				if (keyCodec == null)
					throw new IllegalArgumentException("No CODEC found for key type " + keyType + " in Map entry.");
				Class<?> valueType = GenericTypes.classes(type)[0].type();
				Codec<?> valueCodec = getCodec(valueType, GenericTypes.type(type, 1), params).asCodec();
				if (valueCodec == null)
					throw new IllegalArgumentException("No CODEC found for value type " + valueType + " in Map entry.");
				return CodecResolver.Entry.of(Codec.unboundedMap(keyCodec, valueCodec));
			};
			setCodec(Map.class, MAP);

			CodecResolver<CodecEntry> OPTIONAL_LONG = (Type type, CodecEntry params) -> {
				return CodecResolver.Entry.of(Codec.LONG, CodecResolver.Entry.Type.OPTIONAL);
			};
			setCodec(OptionalLong.class, OPTIONAL_LONG);
			CodecResolver<CodecEntry> OPTIONAL = (Type type, CodecEntry params) -> {
				Class<?> optionalType = GenericTypes.getFirstGenericType(type);
				Codec<?> codec = getCodec(optionalType, GenericTypes.type(type, 0), params).asCodec();
				if (codec == null)
					throw new IllegalArgumentException("No CODEC found for type " + optionalType + " in Optional entry.");
				return CodecResolver.Entry.of(codec, CodecResolver.Entry.Type.OPTIONAL);
			};
			setCodec(Optional.class, OPTIONAL);
		}

		/**
		 * 目标CODEC所在类的列表
		 */
		private static final ArrayList<Class<?>> codecClasses = new ArrayList<>();

		/**
		 * 目标类的构造函数。自动寻找构造函数需要与{@code @CodecEntry}注解字段的顺序和类型完全一致，如果不一致则需要手动指定构造函数。
		 */
		private static final HashMap<Class<?>, MethodHandle> codecCtors = new HashMap<>();

		/**
		 * 根据字段名称寻找指定的CODEC并记录在CODECS中
		 * 
		 * @param targetClass
		 * @param codecFieldName
		 * @return
		 */
		public static final void setCodecByFieldName(Class<?> targetClass, String codecFieldName) {
			CODECS.put(targetClass, (Type type, Object param) -> CodecResolver.Entry.of(ObjectManipulator.access(targetClass, codecFieldName == null ? DEFAULT_CODEC_FIELD : codecFieldName)));
		}

		/**
		 * 直接设置某个类的CODEC
		 * 
		 * @param targetClass
		 * @param codec
		 */
		public static final void setCodec(Class<?> targetClass, Object codec) {
			CODECS.put(targetClass, (Type type, Object param) -> CodecResolver.Entry.of(codec));
		}

		@SuppressWarnings("rawtypes")
		public static final void setCodec(Class<?> targetClass, CodecResolver resolver) {
			CODECS.put(targetClass, resolver);
		}

		/**
		 * 获取某个类的CODEC，如果不存在则获取该类中默认字段名称的CODEC
		 * 
		 * @param targetClass
		 * @return
		 */
		public static final CodecResolver.Entry getCodec(Class<?> targetClass) {
			return getCodec(targetClass, null, null);
		}

		@SuppressWarnings("unchecked")
		public static final CodecResolver.Entry getCodec(Class<?> targetClass, Type type, Object param) {
			return CODECS.computeIfAbsent(targetClass, (Class<?> t) -> (Type c, Object p) -> CodecResolver.Entry.of(ObjectManipulator.access(targetClass, DEFAULT_CODEC_FIELD))).resolve(type, param);
		}

		public static final CodecResolver.Entry getCodec(Class<?> targetClass, Object param) {
			return getCodec(targetClass, null, param);
		}

		/**
		 * 获取CODEC的默认注册名称，将单词以下划线分隔并转换为全小写。<br>
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
		 * 终止时获取期望的构造函数形式
		 * 
		 * @param targetClass
		 * @param __types
		 * @return
		 */
		private static String expectedConstructor(Class<?> targetClass, Class<?>[] __types) {
			StringBuilder result = new StringBuilder();
			result.append(targetClass.getName()).append("(");
			for (int i = 0; i < __types.length; ++i) {
				result.append(__types[i].getName());
				if (i != __types.length)
					result.append(", ");
			}
			result.append(")");
			return result.toString();
		}

		/**
		 * 访问目标类的CODEC字段
		 * 
		 * @param <T>
		 * @param targetClass
		 * @return
		 */
		public static final <T> Codec<T> accessCodec(Class<T> targetClass) {
			return asCodec(getCodec(targetClass));
		}

		@SuppressWarnings("unchecked")
		public static final <T> Codec<T> asCodec(Object class_codec) {
			if (class_codec instanceof Codec c)
				return c;
			else if (class_codec instanceof MapCodec mc)
				return mc.codec();
			else if (class_codec instanceof KeyDispatchDataCodec kddc)
				return kddc.codec().codec();
			return null;
		}

		/**
		 * 返回拓展类型的Codec，包括List、Optional和自定义类的自定义CODEC。
		 * 
		 * @param f
		 * @param params
		 * @return
		 */
		public static final MapCodec<?> resolveFieldFinalCodec(Field f, CodecEntry params) {
			Class<?> field_type = f.getType();
			String key = params.key();
			if (key.equals(CodecEntry.fieldName))
				key = f.getName();
			CodecResolver.Entry entry = getCodec(field_type, f.getGenericType(), params);
			CodecResolver.Entry.Type fc_type = CodecResolver.Entry.Type.resolve(params.type(), entry.type);// 是否已经绑定了字段名称
			Codec<?> field_codec = asCodec(entry.codec);
			if (field_codec == null)
				throw new IllegalArgumentException("No CODEC found for type " + field_type + " in field " + f);
			String[] namespaces = params.path() == null ? new String[0] : params.path().split(CodecEntry.pathSeparator);
			for (int depth = 0; depth < namespaces.length; ++depth) {// 构建key的路径path，不包括key本身
				String namespace = namespaces[depth];
				if (namespace.trim().equals(""))
					continue;// 略过空路径
				else
					field_codec = field_codec.fieldOf(namespace).codec();// 根据路径构造子键
			}
			MapCodec<?> final_codec = null;
			switch (fc_type) {
			case OPTINAL_NOERR:
				final_codec = field_codec.lenientOptionalFieldOf(key);
				break;
			case OPTIONAL:
				final_codec = field_codec.optionalFieldOf(key);
				break;
			case REQUIRED:
			default:
				final_codec = field_codec.fieldOf(key);
				break;
			}
			return final_codec;
		}

		/**
		 * 收集目标类中的注解有CodecEntry的字段
		 * 
		 * @param targetClass
		 * @param entries
		 * @param arg_types
		 * @param include_base 是否包含其超类的字段
		 */
		@SuppressWarnings({ "rawtypes", "unchecked" })
		private static final void collectCodecEntries(Class<?> targetClass, ArrayList<App> entries, ArrayList<Class<?>> arg_types, boolean include_base) {
			if (include_base) {
				Class<?> superCls = targetClass.getSuperclass();
				if (superCls != Object.class)
					collectCodecEntries(superCls, entries, arg_types, include_base);
			}
			KlassWalker.walkAnnotatedFields(targetClass, CodecEntry.class, (Field f, boolean isStatic, Object value, CodecEntry annotation) -> {
				if (!isStatic) {// 只有非静态成员变量需要序列化
					Function universalFieldGetter = (Object targetObj) -> {
						return ObjectManipulator.access(targetObj, f);
					};// 字段Getter
					Class<?> field_type = f.getType();
					arg_types.add(field_type);// 储存该字段的类型，该类型必须和构造函数的参数类型和顺序严格匹配
					MapCodec final_codec = resolveFieldFinalCodec(f, annotation);
					entries.add(final_codec.forGetter(universalFieldGetter));
				}
				return true;
			});
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
		public static final Object generate(Class<?> targetClass, MethodHandle buildMethod, boolean null_if_empty, boolean include_base) {
			ArrayList<App> entries = new ArrayList<>();
			ArrayList<Class<?>> arg_types = new ArrayList<>();
			collectCodecEntries(targetClass, entries, arg_types, include_base);
			if (arg_types.isEmpty()) {
				if (null_if_empty)// null_if_empty为true，则直接返回null
					return null;
				else {
					Class<?> ret = buildMethod.type().returnType();
					if (ret == Codec.class)
						return CodecFactory.emptyCodec(targetClass);
					else if (ret == MapCodec.class)
						return CodecFactory.emptyMapCodec(targetClass);
				}
			}
			Class<?>[] __types = new Class<?>[arg_types.size()];
			arg_types.toArray(__types);
			final MethodHandle ctor = codecCtors.computeIfAbsent(targetClass, (Class<?> t) -> HandleBase.findConstructor(targetClass, __types));// 寻找目标类的构造函数，如果没有在forCodec()时手动指定构造函数参数类型，则依据收集到的字段声明类型严格匹配寻找构造函数
			if (ctor == null)
				throw new IllegalArgumentException("Cannot find constructor when generating CODEC for " + targetClass + ", check the fields annotated with CodecEntry's order and type, make sure that both order and type are completely the same.\n" +
						"Expected constructor form: " + expectedConstructor(targetClass, __types) + ";");
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
				throw new IllegalArgumentException("Invalid Codec entries count: " + entries.size() + ", should be in (0, 16]");
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
		public static final <T> MapCodec<T> generateMapCodec(Class<T> targetClass, boolean null_if_empty, boolean include_base) {
			return (MapCodec<T>) generate(targetClass, mapCodec, null_if_empty, include_base);
		}

		/**
		 * 生成一个Codec
		 * 
		 * @param <T>
		 * @param targetClass
		 * @return
		 */
		@SuppressWarnings("unchecked")
		public static final <T> Codec<T> generateCodec(Class<T> targetClass, boolean null_if_empty, boolean include_base) {
			return (Codec<T>) generate(targetClass, create, null_if_empty, include_base);
		}

		public static final <T> KeyDispatchDataCodec<T> generateKeyDispatchDataCodec(Class<T> targetClass, boolean null_if_empty, boolean include_base) {
			MapCodec<T> CODEC = generateMapCodec(targetClass, null_if_empty, include_base);
			return CODEC == null ? null : KeyDispatchDataCodec.of(CODEC);
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
		public static final DeferredHolder generateAndRegisterHolder(String codecName, Class<?> target, MethodHandle buildMethod, Class<?> registryType, boolean null_if_empty, boolean include_base) {
			Object CODEC = generate(target, buildMethod, null_if_empty, include_base);
			if (CODEC == null)
				return null;
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
		private static final Object generateAndRegister(String codecName, Class<?> target, MethodHandle buildMethod, Class<?> registryType, boolean null_if_empty, boolean include_base) {
			Object CODEC = generate(target, buildMethod, null_if_empty, include_base);
			if (CODEC == null)
				return null;
			RegistryWalker.walkMapCodecRegistries((Field f, ResourceKey registryKey, Class<?> codecType) -> {
				if (Reflection.is(registryType, codecType)) {// 当注册表MapCodec的泛型参数和传入的registryType匹配时注册
					RegistryFactory.deferredRegister(registryKey).register(codecName, () -> CODEC);
					Core.logInfo("Registered CODEC " + codecName + " in registry " + registryKey);
				}
			});
			return CODEC;
		}

		/**
		 * 扫描字段并构建、注册目标类的CODEC。
		 * 
		 * @param codecClass
		 */
		@SuppressWarnings({ "rawtypes", "unchecked" })
		private static final void generateAndRegisterCodecs(Class<?> codecClass) {
			KlassWalker.walkAnnotatedFields(codecClass, CodecAutogen.class, (Field f, boolean isStatic, Object value, CodecAutogen annotation) -> {
				if (isStatic) {
					MethodHandle internalBuildMethod = null;
					boolean isKeyDispatchDataCodec = false;
					if (Reflection.is(f, Codec.class))
						internalBuildMethod = create;
					else if (Reflection.is(f, MapCodec.class))
						internalBuildMethod = mapCodec;
					else if (Reflection.is(f, KeyDispatchDataCodec.class)) {
						internalBuildMethod = mapCodec;
						isKeyDispatchDataCodec = true;
					} else// 如果字段不是有效的CODEC类型，则直接略过
						return true;
					String registerName = annotation.name();
					if (classSimpleName.equals(registerName))
						registerName = defaultCodecRegisterName(codecClass);
					Object CODEC = generateAndRegister(registerName, codecClass, internalBuildMethod, GenericTypes.getFirstGenericType(f), annotation.null_if_empty(), annotation.include_base());
					if (isKeyDispatchDataCodec)
						CODEC = KeyDispatchDataCodec.of((MapCodec) CODEC);
					ObjectManipulator.setObject(codecClass, f, CODEC);
					Core.logInfo("Generated CODEC for " + codecClass + " in field " + f);
				}
				return true;
			});
		}

		@SubscribeEvent(priority = EventPriority.LOW)
		public static final void autoGenerateCodecs(FMLConstructModEvent event) {
			Core.logInfo("CodecAutogen start to generate CODEC.");
			for (Class<?> codecClass : codecClasses)
				generateAndRegisterCodecs(codecClass);
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
