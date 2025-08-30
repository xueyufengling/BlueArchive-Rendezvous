package lepus.mc.datagen.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import lepus.mc.core.Core;
import lepus.mc.core.registry.RegistryMap;
import lepus.mc.core.registry.RegistryWalker;
import lepus.mc.datagen.EntryHolder;
import lyra.klass.GenericTypes;
import lyra.klass.KlassWalker;
import lyra.lang.JavaLang;
import lyra.lang.Reflection;
import lyra.object.ObjectManipulator;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface RegistryEntry {
	/**
	 * 注册的阶段
	 */
	public static enum Stage {
		NONE,
		DATA_GEN,
		REGISTER_EVENT, // RegisterEvent时注册，即注册为DefferredHolder
		SERVER_RUNTIME
	}

	/**
	 * 选择注册该注解字段的时机。<br>
	 * DATA_GEN则只在数据生成阶段注册并生成json文件；SERVER_RUNTIME则在启动服务器后动态向注册表注册，不会生成json文件。<br>
	 * 动态注册是立即注册，注册时需要考虑先后顺序，否则注册表中先注册的条目查找不到后注册的值。<br>
	 * 此外，数据包的加载时机在动态注册之前，动态注册可以查找数据生成条目，但数据生成条目不能查找动态注册条目。
	 * 
	 * @return
	 */
	Stage reg_stage() default Stage.DATA_GEN;

	public static class RegistryType {

		/**
		 * 判断目标字段是否是泛型参数为genericType的EntryHolder
		 * 
		 * @param f
		 * @param genericType
		 * @return
		 */
		public static boolean isEntryHolder(Field f, Class<?> genericType) {
			return Reflection.is(f, EntryHolder.class) && GenericTypes.is(f, genericType);
		}

		public static boolean isEntryHolder(Field f) {
			return Reflection.is(f, EntryHolder.class);
		}

		/**
		 * 判断目标字段是否是泛型参数为genericType的Holder，包括DeferredHolder
		 * 
		 * @param f
		 * @param genericType
		 * @return
		 */
		public static boolean isHolder(Field f, Class<?> genericType) {
			return Reflection.is(f, Holder.class) && GenericTypes.startWith(f, genericType);
		}

		public static boolean isHolder(Field f) {
			return Reflection.is(f, Holder.class);
		}
	}

	@EventBusSubscriber(bus = Bus.GAME)
	public static class RuntimeEntryHolderRegister {
		/**
		 * 注册给定类中的所有EntryHolder到运行时的注册表。
		 * 
		 * @param registryClass
		 */
		@SuppressWarnings({ "rawtypes" })
		public static final void registerEntryHoldersToRegistry(Class<?> registryClass) {
			KlassWalker.walkAnnotatedFields(registryClass, RegistryEntry.class, (Field f, boolean isStatic, Object value, RegistryEntry annotation) -> {
				if (isStatic) {
					if (annotation.reg_stage() == Stage.SERVER_RUNTIME) {
						if (value == null) {
							Core.logWarn("@RegistryEntry(SERVER_RUNTIME) field " + f + " is expected to have a non-null value.");
							return true;
						}
						if (RegistryType.isEntryHolder(f)) {
							EntryHolder datagenHolder = (EntryHolder) value;
							datagenHolder.register();
							Core.logInfo("Registered [" + datagenHolder.registryKey().location() + "] with original value=" + datagenHolder.originalValue() + " to registry [" + datagenHolder.registryKey().location() + "] in runtime.");
						}
					}
				} else
					Core.logWarn("@RegistryEntry field " + f + " is expected to be declared as static.");
				return true;
			});
		}

		/**
		 * 服务器启动时注册需要注册的EntryHolder
		 * 
		 * @param event
		 */
		@SubscribeEvent(priority = EventPriority.HIGHEST)
		public static void onFMLConstructMod(ServerAboutToStartEvent event) {
			Core.logInfo("Starting to register server runtime RegistryEntry holders");
			for (Class<?> registryClass : RegistriesProvider.registryClasses) {
				registerEntryHoldersToRegistry(registryClass);
			}
		}
	}

	public static class DeferredEntryHolderRegister {
		/**
		 * 注册给定类中的所有EntryHolder到运行时的注册表。
		 * 
		 * @param registryClass
		 */
		@SuppressWarnings({ "rawtypes" })
		public static final void registerEntryHoldersToRegistry(Class<?> registryClass) {
			KlassWalker.walkAnnotatedFields(registryClass, RegistryEntry.class, (Field f, boolean isStatic, Object value, RegistryEntry annotation) -> {
				if (isStatic) {
					if (annotation.reg_stage() == Stage.REGISTER_EVENT) {
						if (value == null) {
							Core.logWarn("@RegistryEntry(REGISTER_EVENT) field " + f + " is expected to have a non-null value.");
							return true;
						}
						if (RegistryType.isEntryHolder(f)) {
							EntryHolder datagenHolder = (EntryHolder) value;
							datagenHolder.registerDeferred();
							Core.logInfo("Registered [" + datagenHolder.registryKey().location() + "] with original value=" + datagenHolder.originalValue() + " to registry [" + datagenHolder.registryKey().location() + "] in RegisterEvent.");
						}
					}
				} else
					Core.logWarn("@RegistryEntry field " + f + " is expected to be declared as static.");
				return true;
			});
		}

		/**
		 * RegisterEvent时注册需要注册的EntryHolder
		 * 
		 * @param event
		 */
		public static void registerAll() {
			Core.logInfo("Starting to register deferred RegistryEntry holders");
			for (Class<?> registryClass : RegistriesProvider.registryClasses) {
				registerEntryHoldersToRegistry(registryClass);
			}
		}
	}

	public static class RegistriesProvider extends DatapackBuiltinEntriesProvider {
		static final ArrayList<Class<?>> registryClasses = new ArrayList<>();

		private static final void setBootstrapContexts(EntryHolder<?> datagenHolder, BootstrapContext<?> context) {
			ObjectManipulator.setDeclaredMemberObject(datagenHolder, "bootstrapContext", context);
		}

		/**
		 * 注册某个类中的全部registryType类型的注解@RegistryEntry的Holder或EntryHolder静态字段
		 * 
		 * @param <T>
		 * @param context
		 * @param registryClass
		 * @param registryType
		 */
		@SuppressWarnings({ "rawtypes", "unchecked" })
		private static final void registerFields(BootstrapContext<?> context, Class<?> registryClass, Class<?> registryType, boolean allowRegister) {
			BootstrapContext this_reg_context = (BootstrapContext) context;
			KlassWalker.walkAnnotatedFields(registryClass, RegistryEntry.class, (Field f, boolean isStatic, Object value, RegistryEntry annotation) -> {
				if (isStatic) {
					if (annotation.reg_stage() == Stage.DATA_GEN) {
						if (value == null) {
							Core.logWarn("@RegistryEntry(DATA_GEN) field " + f + " is expected to have a non-null value.");
							return true;
						}
						if (RegistryType.isEntryHolder(f, registryType)) {
							EntryHolder datagenHolder = (EntryHolder) value;
							setBootstrapContexts(datagenHolder, context);// 为EntryHolder设置BootstrapContext
							if (allowRegister) {
								datagenHolder.register();
								Core.logInfo("Datagen BootstrapContext<" + registryType.getSimpleName() + "> registered [" + datagenHolder.registryKey().location() + "] entry [" + datagenHolder.getKey().location() + "] with EntryHolder value=" + datagenHolder.originalValue());
							} else {
								Core.logWarn("Datagen BootstrapContext<" + registryType.getSimpleName() + "> EntryHolder entry [" + datagenHolder.getKey().location() + "] with registry type [" + datagenHolder.registryKey().location() + "] is not allowed to register, check if this registry in RegistryEntry.RegistriesProvider.registryFieldFilter map.");
							}
						} else if (RegistryType.isHolder(f, registryType)) {
							Holder holder = (Holder) value;
							if (allowRegister) {
								this_reg_context.register(holder.getKey(), holder.value());
								Core.logInfo("Datagen BootstrapContext<" + registryType.getSimpleName() + "> registered [" + holder.getKey().registryKey().location() + "] entry [" + holder.getKey().location() + "] with Holder value=" + holder.value());
							} else {
								Core.logWarn("Datagen BootstrapContext<" + registryType.getSimpleName() + "> Holder entry [" + holder.getKey().location() + "] with registry type [" + holder.getKey().registryKey().location() + "] is not allowed to register, check if this registry in RegistryEntry.RegistriesProvider.registryFieldFilter map.");
							}
						}
					}
				} else
					Core.logWarn("@RegistryEntry field " + f + " is expected to be declared as static.");
				return true;
			});
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		private static final RegistrySetBuilder allRegistrySetBuilder() {
			RegistrySetBuilder registrySetBuilder = new RegistrySetBuilder();
			// 遍历net.minecraft.core.registries.Registries的所有静态字段并添加对应的数据生成器
			RegistryWalker.walkBootstrapRegistries((Field f, ResourceKey<? extends Registry<?>> registryKey, Class<?> registryType) -> {
				RegistrySetBuilder.RegistryBootstrap bootstrap = (BootstrapContext context) -> {
					for (Class<?> registryClass : registryClasses)
						registerFields(context, registryClass, RegistryWalker.getRegistryKeyType(f), true);
				};// 每个注册表都有对应的BootstrapContext，该context只能获取本注册表的lookup和注册条目
				registrySetBuilder.add((ResourceKey) registryKey, bootstrap);
				Core.logInfo("Complete collecting bootstrap values for registry " + registryKey.location());
				return true;
			});
			Core.logInfo("All bootstrap entries will be registered soon, if any entry was not registered, and no file generated while no error or exception was thrown, check if its registry is in RegistryWalker.bootstrapRegistryFieldsFilter map.");
			return registrySetBuilder;
		}

		public RegistriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
			super(output, provider, allRegistrySetBuilder(), RegistryMap.namespaces());
		}

		/**
		 * 注册数据生成
		 * 
		 * @param registryClass
		 */
		public static final void forDatagen(Class<?> registryClass) {
			if (!registryClasses.contains(registryClass)) {
				registryClasses.add(registryClass);
			}
		}

		public static final void forDatagen() {
			Class<?> caller = JavaLang.getOuterCallerClass();
			forDatagen(caller);
		}
	}
}
