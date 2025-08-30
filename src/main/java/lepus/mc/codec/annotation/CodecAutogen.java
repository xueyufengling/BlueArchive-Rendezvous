package lepus.mc.codec.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import lepus.mc.codec.Codecs;
import lepus.mc.core.Core;
import lepus.mc.core.registry.RegistryFactory;
import lepus.mc.core.registry.RegistryWalker;
import lyra.klass.GenericTypes;
import lyra.klass.KlassWalker;
import lyra.lang.DynamicConcurrentArrayList;
import lyra.lang.Handles;
import lyra.lang.JavaLang;
import lyra.lang.Reflection;
import lyra.lang.internal.HandleBase;
import lyra.lang.internal.MemberName;
import lyra.object.ObjectManipulator;
import lyra.object.Placeholders;
import lyra.object.Placeholders.TypeWrapper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.KeyDispatchDataCodec;
import net.neoforged.neoforge.registries.DeferredHolder;

/**
 * 自动生成CODEC的注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface CodecAutogen {

	public static final String classSimpleName = "'classSimpleName'";

	/**
	 * 如果需要注册，则需要指定命名空间；不需要注册留空即可。<br>
	 * 如果register()指定为true则该项不可为null或""
	 * 
	 * @return
	 */
	String namespace() default "";

	/**
	 * Codec的注册名称，不带命名空间
	 * 
	 * @return
	 */
	String name() default classSimpleName;

	/**
	 * 当构建的CODEC没有任何field时，即没有注解CodecEntry的字段，是否立即返回null。<br>
	 * 设置为true则立即返回null，false则返回一个没有field的空CODEC。<br>
	 * 如果本该生成CODEC的字段构建失败为null，则不论数据生成还是运行时均会报注册null值的错误，因此通常情况下不建议将该值设置为true。<br>
	 * 
	 * @return
	 */
	boolean null_if_empty() default false;

	/**
	 * 是否注册该CODEC
	 * 
	 * @return
	 */
	boolean register() default false;

	/**
	 * 当CODEC生成完成后注册阶段注册失败时，是否打印警告。<br>
	 * 并非所有CODEC都需要注册，这些无需注册的CODEC是在其父类中实现了CODEC相关功能，并且其注册表也不是MapCodec<T>类型。<br>
	 * 
	 * @return
	 */
	boolean warn_if_register_failed() default false;

	/**
	 * 是否也生成基类的带有CodecEntry注解的字段的CODEC条目
	 * 
	 * @return
	 */
	boolean include_base() default true;

	public static class CodecGenerator {
		/**
		 * 子类CODEC是否自动注册，如果标记为自动注册则无论CodecAutogen.register()值为多少均会注册
		 */
		private static final ArrayList<Class<?>> derivedAutoRegistered = new ArrayList<>();

		/**
		 * 标记该类的子类中的CodecAutogen自动注册
		 * 
		 * @param targetClass
		 */
		public static final void markDerivedAutoRegister(Class<?> targetClass) {
			derivedAutoRegistered.add(targetClass);
		}

		public static final void markDerivedAutoRegister() {
			Class<?> caller = JavaLang.getOuterCallerClass();
			markDerivedAutoRegister(caller);
		}

		public static final boolean isMarkedAsAutoRegister(Class<?> targetClass) {
			for (Class<?> autoRegisterdBase : derivedAutoRegistered) {
				if (Reflection.is(targetClass, autoRegisterdBase))
					return true;
			}
			return false;
		}

		private static final DynamicConcurrentArrayList<Class<?>> codecClasses = new DynamicConcurrentArrayList<Class<?>>();

		/**
		 * 目标类的构造函数。自动寻找构造函数需要与{@code @CodecEntry}注解字段的顺序和类型完全一致，如果不一致则需要手动指定构造函数。
		 */
		private static final HashMap<Class<?>, MethodHandle> codecCtors = new HashMap<>();

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
		public static final Object generate(Class<?> targetClass, MethodHandle buildMethod, boolean null_if_empty, boolean include_base) {
			final ArrayList<App> entries = new ArrayList<>();
			ArrayList<Class<?>> arg_types = new ArrayList<>();// 收集到的构造函数参数类型
			CodecEntry.Resolver.collectCodecEntries(targetClass, entries, arg_types, include_base);
			if (arg_types.isEmpty()) {
				if (null_if_empty)// null_if_empty为true，则直接返回null
					return null;
				else {
					Class<?> ret = buildMethod.type().returnType();
					if (ret == Codec.class)
						return Codecs.emptyCodec(targetClass);
					else if (ret == MapCodec.class)
						return Codecs.emptyMapCodec(targetClass);
				}
			}
			Class<?>[] __types = new Class<?>[arg_types.size()];
			arg_types.toArray(__types);
			MethodHandle ctor = codecCtors.computeIfAbsent(targetClass, (Class<?> target) -> HandleBase.findConstructor(target, __types));// 寻找目标类的构造函数，如果没有在forCodec()时手动指定构造函数参数类型，则依据收集到的字段声明类型严格匹配寻找构造函数
			if (ctor == null)
				throw new IllegalArgumentException("Cannot find constructor when generating CODEC for " + targetClass + ", check the fields annotated with CodecEntry's order and type, make sure that both order and type are completely the same.\n" +
						"Expected constructor form: " + MemberName.constructorDescription(targetClass, __types) + ";");
			Object CODEC = null;
			switch (entries.size()) {
			case 0:
				CODEC = Codecs.emptyCodec(targetClass);
				break;
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
				throw new IllegalArgumentException("Invalid Codec entries count: " + entries.size() + ", should be in [0, 16]");
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
		public static final DeferredHolder generateAndRegisterHolder(String namespace, String codecName, Class<?> target, MethodHandle buildMethod, Class<?> registryType, boolean null_if_empty, boolean include_base) {
			Object CODEC = generate(target, buildMethod, null_if_empty, include_base);
			if (CODEC == null)
				return null;
			TypeWrapper<DeferredHolder> holderWrapper = TypeWrapper.wrap();
			RegistryWalker.walkMapCodecRegistries((Field f, ResourceKey registryKey, Class<?> codecType) -> {
				if (Reflection.is(registryType, codecType)) {// 当注册表MapCodec的泛型参数和传入的registryType匹配时注册
					holderWrapper.value = RegistryFactory.deferredRegister(registryKey, namespace).register(codecName, () -> CODEC);
				}
				return true;
			});
			return holderWrapper.value;
		}

		/**
		 * 生成目标类的CODEC并注册到相应的注册表中，并返回CODEC，可用于给静态字段初始化
		 * 
		 * @param <T>
		 * @param namespace    如果要注册则传入命名空间
		 * @param codecName    注册名称
		 * @param target       要生成CODEC的类
		 * @param registryType 要将CODEC注册到哪个类型
		 * @return
		 */
		@SuppressWarnings({ "rawtypes", "unchecked" })
		private static final Object generateAndRegister(String namespace, String codecName, Class<?> target, MethodHandle buildMethod, Class<?> registryType, boolean null_if_empty, boolean register, boolean warn_if_register_failed, boolean include_base) {
			Object CODEC = generate(target, buildMethod, null_if_empty, include_base);
			if (CODEC == null)
				return null;
			Core.logInfo("Generated CODEC for " + target + " -> " + CODEC);
			if (register || isMarkedAsAutoRegister(target)) {
				if (namespace == null || "".equals(namespace))// 如果要注册则namespace不可留空
					throw new IllegalArgumentException("CODEC " + codecName + " for target " + target + " need a non-empty namespace to register.");
				Placeholders.TypeWrapper<Boolean> registered = Placeholders.TypeWrapper.wrap(false);
				RegistryWalker.walkMapCodecRegistries((Field f, ResourceKey registryKey, Class<?> codecType) -> {
					if (Reflection.is(registryType, codecType)) {// 当注册表MapCodec的泛型参数和传入的registryType匹配时注册
						RegistryFactory.deferredRegister(registryKey, namespace).register(codecName, () -> CODEC);
						registered.value = true;
						Core.logInfo("Registered CODEC " + codecName + " in registry [" + registryKey.location() + "].");
					}
					return true;
				});
				if (!registered.value && warn_if_register_failed)
					Core.logWarn("CODEC " + codecName + " was not registered, the registry for MapCodec<" + registryType.getName() + "> was not found. Maybe it isn't a MapCodec registry?");
			}
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
						registerName = Codecs.defaultCodecRegisterName(codecClass);
					Core.logInfo("Starting to generate CODEC for @CodecAutogen " + f);
					Object CODEC = generateAndRegister(annotation.namespace(), registerName, codecClass, internalBuildMethod, GenericTypes.getFirstGenericType(f), annotation.null_if_empty(), annotation.register(), annotation.warn_if_register_failed(), annotation.include_base());
					if (isKeyDispatchDataCodec)
						CODEC = KeyDispatchDataCodec.of((MapCodec) CODEC);
					ObjectManipulator.setObject(codecClass, f, CODEC);
				}
				return true;
			});
		}

		public static final void generateCodecs() {
			Core.logInfo("CodecAutogen starting to generate CODEC.");
			try {
				codecClasses.forEach((Class<?> codecClass) -> {
					generateAndRegisterCodecs(codecClass);
				});
			} catch (Throwable ex) {
				Core.logError("Generate CODEC failed.", ex);
			}
		}

		/**
		 * 注册CODEC生成<br>
		 * 当不指定ctorTypes时，将自动查找所有注解CodecEntry字段的类型的构造函数（构造函数参数顺序同注解字段声明顺序）<br>
		 * 如果指定了ctorTypes，则忽略CodecEntry注解并仅使用指定的参数类型的构造函数<br>
		 * 
		 * @param targetClass
		 */
		public static final void forCodec(Class<?> targetClass, Class<?>... ctorTypes) {
			codecClasses.add(targetClass);
			if (CodecTarget.TypeChecker.check(targetClass, ctorTypes) && ctorTypes.length != 0) {// 通过了类型检查，且手动传入了参数则寻找构造函数，否则就遍历字段时寻找
				MethodHandle ctor = HandleBase.findConstructor(targetClass, ctorTypes);
				if (ctor == null)
					throw new NullPointerException("Target CODEC constructor " + MethodType.methodType(targetClass, ctorTypes).toString() + " for class " + targetClass.getName() + " was not found.");
				codecCtors.put(targetClass, ctor);
			}
		}

		public static final void Codec(Class<?>... ctorTypes) {
			Class<?> caller = JavaLang.getOuterCallerClass();
			forCodec(caller, ctorTypes);
		}
	}
}
