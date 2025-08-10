package fw.datagen.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import fw.core.Core;
import fw.core.registry.RegistryFactory;
import fw.core.registry.RegistryWalker;
import fw.datagen.DatagenHolder;
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
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface RegistryDatagen {

	/**
	 * 是否在运行时注册到DefferedRegister，默认为false。<br>
	 * 实际上运行时会从生成的数据包中加载这些注册表条目。<br>
	 * 
	 * @return
	 */
	boolean reg_runtime() default false;

	public static class RegistryType {

		/**
		 * 判断目标字段是否是泛型参数为genericType的DatagenHolder
		 * 
		 * @param f
		 * @param genericType
		 * @return
		 */
		public static boolean isDatagenHolder(Field f, Class<?> genericType) {
			return Reflection.is(f, DatagenHolder.class) && GenericTypes.is(f, genericType);
		}

		public static boolean isDatagenHolder(Field f) {
			return Reflection.is(f, DatagenHolder.class);
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

	@EventBusSubscriber(modid = Core.ModId, bus = Bus.MOD)
	public static class RuntimeDatagenHolderRegister {
		/**
		 * 注册给定类中的所有DatagenHolder到运行时的注册表。
		 * 
		 * @param registryClass
		 */
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public static final void registerDatagenHoldersToRegistry(Class<?> registryClass) {
			KlassWalker.walkAnnotatedFields(registryClass, RegistryDatagen.class, (Field f, boolean isStatic, Object value, RegistryDatagen annotation) -> {
				if (isStatic && value != null) {
					if (annotation.reg_runtime() && RegistryType.isDatagenHolder(f)) {
						DatagenHolder datagenHolder = (DatagenHolder) value;
						ObjectManipulator.setObject(value, "deferredHolder", RegistryFactory.deferredRegister(datagenHolder.registryKey, datagenHolder.namespace).register(datagenHolder.path, () -> datagenHolder.value()));
						Core.logInfo("Registered " + datagenHolder.resourceKey + " with value=" + datagenHolder.value() + " to registry " + datagenHolder.registryKey + " in runtime.");
					}
				}
				return true;
			});
		}

		/**
		 * mod构建完成以后注册需要注册的DatagenHolder
		 * 
		 * @param event
		 */
		@SubscribeEvent(priority = EventPriority.HIGHEST)
		public static void onFMLConstructMod(FMLConstructModEvent event) {
			for (Class<?> registryClass : RegistriesProvider.registryClasses) {
				registerDatagenHoldersToRegistry(registryClass);
			}
		}
	}

	public static class RegistriesProvider extends DatapackBuiltinEntriesProvider {
		static final ArrayList<Class<?>> registryClasses = new ArrayList<>();

		/**
		 * 注册某个类中的全部registryType类型的注解@RegistryDatagen的Holder或DatagenHolder静态字段
		 * 
		 * @param <T>
		 * @param context
		 * @param registryClass
		 * @param registryType
		 */
		@SuppressWarnings({ "rawtypes", "unchecked" })
		private static final void registerFields(BootstrapContext<?> context, Class<?> registryClass, Class<?> registryType, boolean allowRegister) {
			BootstrapContext raw_context = (BootstrapContext) context;
			KlassWalker.walkAnnotatedFields(registryClass, RegistryDatagen.class, (Field f, boolean isStatic, Object value, RegistryDatagen annotation) -> {
				if (isStatic) {
					if (value == null) {
						Core.logWarn("@RegistryDatagen field " + f + " is expected to have a non-null value.");
						return true;
					}
					if (RegistryType.isHolder(f, registryType)) {
						Holder holder = (Holder) value;
						if (holder instanceof DatagenHolder datagenHolder) {
							if (allowRegister) {
								raw_context.register(datagenHolder.resourceKey, datagenHolder.value(context));
								Core.logInfo("Datagen registered [" + datagenHolder.registryKey.location() + "] entry [" + datagenHolder.resourceKey.location() + "] with DatagenHolder value=" + datagenHolder.value());
							} else {
								Core.logWarn("DatagenHolder entry [" + datagenHolder.resourceKey.location() + "] with registry type [" + datagenHolder.registryKey.location() + "] is not allowed to register, check if this registry in RegistryDatagen.RegistriesProvider.registryFieldFilter map.");
							}
						} else {
							if (allowRegister) {
								raw_context.register(holder.getKey(), holder.value());
								Core.logInfo("Datagen registered [" + holder.getKey().registryKey().location() + "] entry [" + holder.getKey().location() + "] with Holder value=" + holder.value());
							} else {
								Core.logWarn("Holder entry [" + holder.getKey().location() + "] with registry type [" + holder.getKey().registryKey().location() + "] is not allowed to register, check if this registry in RegistryDatagen.RegistriesProvider.registryFieldFilter map.");
							}
						}
					}
				} else
					Core.logWarn("@RegistryDatagen field " + f + " is expected to be declared as static.");
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
				};
				registrySetBuilder.add((ResourceKey) registryKey, bootstrap);
				return true;
			});
			Core.logInfo("All bootstrap entries registered, if any entry was not registered, check if its registry is in RegistryWalker.bootstrapRegistryFieldsFilter map.");
			return registrySetBuilder;
		}

		public RegistriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
			super(output, provider, allRegistrySetBuilder(), Set.of(ResourceLocation.DEFAULT_NAMESPACE, Core.ModId));
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
