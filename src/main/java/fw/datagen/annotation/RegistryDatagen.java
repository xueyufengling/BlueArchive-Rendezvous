package fw.datagen.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import fw.core.Core;
import fw.core.registry.DatagenHolder;
import lyra.klass.KlassWalker;
import lyra.klass.ObjectManipulator;
import lyra.lang.GenericTypes;
import lyra.lang.Reflection;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

@Retention(RetentionPolicy.RUNTIME)
public @interface RegistryDatagen {
	public static class RegistriesProvider extends DatapackBuiltinEntriesProvider {
		private static final ArrayList<Class<?>> registryClasses = new ArrayList<>();

		/**
		 * 判断目标字段是否是泛型参数为genericType的DatagenHolder
		 * 
		 * @param f
		 * @param genericType
		 * @return
		 */
		private static boolean isDatagenHolder(Field f, Class<?> genericType) {
			return Reflection.is(f, DatagenHolder.class) && GenericTypes.is(f, genericType);
		}

		/**
		 * 判断目标字段是否是泛型参数为genericType的Holder，包括DeferredHolder
		 * 
		 * @param f
		 * @param genericType
		 * @return
		 */
		private static boolean isHolder(Field f, Class<?> genericType) {
			return Reflection.is(f, Holder.class) && GenericTypes.startWith(f, genericType);
		}

		/**
		 * 使用反射绕过编译时泛型检查
		 */
		private static final Method BootstrapContext_register;

		static {
			BootstrapContext_register = Reflection.getDeclaredMethod(BootstrapContext.class, "register", ResourceKey.class, Object.class);
		}

		/**
		 * 注册某个类中的全部静态字段
		 * 
		 * @param <T>
		 * @param context
		 * @param registryClass
		 * @param registryType
		 */
		@SuppressWarnings({ "rawtypes", "unchecked" })
		private static final void registerFields(BootstrapContext<?> context, Class<?> registryClass, Class<?> registryType) {
			KlassWalker.walkFields(registryClass, RegistryDatagen.class, (Field f, boolean isStatic, Object value, RegistryDatagen annotation) -> {
				if (isStatic && value != null) {
					if (isDatagenHolder(f, registryType)) {
						DatagenHolder datagenHolder = (DatagenHolder) value;
						ObjectManipulator.invoke(context, BootstrapContext_register, datagenHolder.resourceKey, datagenHolder.value(context));
					} else if (isHolder(f, registryType)) {
						Holder holder = (Holder) value;
						ObjectManipulator.invoke(context, BootstrapContext_register, holder.getKey(), holder.value());
					}
				}
			});
		}

		private static final Method RegistrySetBuilder_add;

		static {
			RegistrySetBuilder_add = Reflection.getDeclaredMethod(RegistrySetBuilder.class, "add", ResourceKey.class, RegistrySetBuilder.RegistryBootstrap.class);
		}

		/**
		 * 过滤掉Registries的ResourceKey字段，只有位于该Set内的字段才会被被添加到数据生成。<br>
		 * Registries中的部分注册表可以通过数据包加载，这些注册表可以加入过滤器。那些不能通过数据包加载的，例如密度函数DENSITY_FUNCTION_TYPE，物品ITEM等则不能用于数据生成。
		 */
		public static final Set<String> registryFieldFilter = Set.of(
				"DIMENSION_TYPE",
				"LEVEL_STEM",
				"NOISE_SETTINGS");

		@SuppressWarnings({ "rawtypes" })
		private static final RegistrySetBuilder allRegistrySetBuilder() {
			RegistrySetBuilder registrySetBuilder = new RegistrySetBuilder();
			// 遍历net.minecraft.core.registries.Registries的所有静态字段并添加对应的数据生成器
			KlassWalker.walkFields(Registries.class, (Field f, boolean isStatic, Object registryKey) -> {
				if (isStatic && Reflection.is(f, ResourceKey.class) && registryKey != null) {
					if (registryFieldFilter.contains(f.getName())) {
						RegistrySetBuilder.RegistryBootstrap bootstrap = (BootstrapContext context) -> {
							for (Class<?> registryClass : registryClasses)
								registerFields(context, registryClass, GenericTypes.classes(f, 0)[0]);
						};
						ObjectManipulator.invoke(registrySetBuilder, RegistrySetBuilder_add, registryKey, bootstrap);
					}
				}
			});
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
			if (!registryClasses.contains(registryClass))
				registryClasses.add(registryClass);
		}
	}
}
