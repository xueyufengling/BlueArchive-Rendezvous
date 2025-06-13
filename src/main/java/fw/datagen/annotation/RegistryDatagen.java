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
import lyra.lang.Reflection;
import lyra.object.ObjectManipulator;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
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
	 * 是否在运行时注册到DefferedRegister，默认为false
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
			KlassWalker.walkFields(registryClass, RegistryDatagen.class, (Field f, boolean isStatic, Object value, RegistryDatagen annotation) -> {
				if (isStatic && value != null) {
					if (annotation.reg_runtime() && RegistryType.isDatagenHolder(f)) {
						DatagenHolder datagenHolder = (DatagenHolder) value;
						ObjectManipulator.setObject(value, "deferredHolder", RegistryFactory.deferredRegister(datagenHolder.registryKey, datagenHolder.namespace).register(datagenHolder.path, () -> datagenHolder.value()));
						Core.logInfo("Registered " + datagenHolder.resourceKey + " with value=" + datagenHolder.value() + " to registry " + datagenHolder.registryKey + " in runtime.");
					}
				}
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
		 * 过滤掉Registries的ResourceKey字段，只有位于该Set内的字段才会被被添加到数据生成。<br>
		 * Registries中的部分注册表可以通过数据包加载，这些注册表可以加入过滤器。那些不能通过数据包加载的，例如密度函数DENSITY_FUNCTION，物品ITEM等则不能用于数据生成。<br>
		 * 见{@link net.minecraft.data.registries.VanillaRegistries}<br>
		 * 参考runData时{@link net.minecraft.core.registries.BuiltInRegistries}{@code .REGISTRY}存在的注册表：<br>
		 * minecraft:command_argument_type<br>
		 * minecraft:decorated_pot_pattern<br>
		 * minecraft:item<br>
		 * neoforge:global_loot_modifier_serializers<br>
		 * minecraft:block_entity_type<br>
		 * minecraft:custom_stat<br>
		 * minecraft:worldgen/foliage_placer_type<br>
		 * minecraft:number_format_type<br>
		 * minecraft:stat_type<br>
		 * neoforge:ingredient_serializer<br>
		 * minecraft:worldgen/material_rule<br>
		 * minecraft:worldgen/structure_type<br>
		 * minecraft:attribute<br>
		 * minecraft:position_source_type<br>
		 * minecraft:height_provider_type<br>
		 * minecraft:data_component_type<br>
		 * neoforge:fluid_ingredient_type<br>
		 * minecraft:rule_block_entity_modifier<br>
		 * neoforge:attachment_types<br>
		 * minecraft:worldgen/density_function_type<br>
		 * minecraft:fluid<br>
		 * minecraft:loot_condition_type<br>
		 * minecraft:worldgen/structure_pool_element<br>
		 * minecraft:activity<br>
		 * minecraft:block_type<br>
		 * minecraft:recipe_serializer<br>
		 * neoforge:fluid_type<br>
		 * minecraft:enchantment_provider_type<br>
		 * neoforge:biome_modifier_serializers<br>
		 * minecraft:frog_variant<br>
		 * minecraft:instrument<br>
		 * neoforge:holder_set_type<br>
		 * minecraft:worldgen/feature_size_type<br>
		 * minecraft:point_of_interest_type<br>
		 * minecraft:mob_effect<br>
		 * minecraft:loot_pool_entry_type<br>
		 * minecraft:worldgen/block_state_provider_type<br>
		 * minecraft:worldgen/chunk_generator<br>
		 * minecraft:float_provider_type<br>
		 * minecraft:chunk_status<br>
		 * minecraft:loot_function_type<br>
		 * minecraft:worldgen/structure_processor<br>
		 * minecraft:enchantment_effect_component_type<br>
		 * minecraft:loot_score_provider_type<br>
		 * minecraft:worldgen/tree_decorator_type<br>
		 * minecraft:schedule<br>
		 * minecraft:worldgen/material_condition<br>
		 * minecraft:worldgen/pool_alias_binding<br>
		 * minecraft:item_sub_predicate_type<br>
		 * minecraft:entity_type<br>
		 * minecraft:villager_profession<br>
		 * minecraft:potion<br>
		 * minecraft:enchantment_entity_effect_type<br>
		 * minecraft:recipe_type<br>
		 * minecraft:int_provider_type<br>
		 * minecraft:worldgen/feature<br>
		 * minecraft:enchantment_level_based_value_type<br>
		 * minecraft:cat_variant<br>
		 * minecraft:pos_rule_test<br>
		 * minecraft:worldgen/structure_placement<br>
		 * minecraft:enchantment_value_effect_type<br>
		 * minecraft:loot_nbt_provider_type<br>
		 * minecraft:menu<br>
		 * minecraft:worldgen/trunk_placer_type<br>
		 * minecraft:creative_mode_tab<br>
		 * minecraft:entity_sub_predicate_type<br>
		 * minecraft:enchantment_location_based_effect_type<br>
		 * minecraft:worldgen/placement_modifier_type<br>
		 * minecraft:worldgen/carver<br>
		 * minecraft:loot_number_provider_type<br>
		 * minecraft:worldgen/structure_piece<br>
		 * minecraft:sound_event<br>
		 * minecraft:particle_type<br>
		 * minecraft:game_event<br>
		 * minecraft:worldgen/biome_source<br>
		 * neoforge:entity_data_serializers<br>
		 * minecraft:worldgen/root_placer_type<br>
		 * minecraft:villager_type<br>
		 * minecraft:block_predicate_type<br>
		 * minecraft:block<br>
		 * neoforge:structure_modifier_serializers<br>
		 * minecraft:trigger_type<br>
		 * minecraft:sensor_type<br>
		 * minecraft:rule_test<br>
		 * minecraft:armor_material<br>
		 * minecraft:map_decoration_type<br>
		 * minecraft:memory_module_type<br>
		 * neoforge:condition_codecs<br>
		 */
		public static final Set<String> registryFieldFilter = Set.of(
				"DIMENSION_TYPE",
				"LEVEL_STEM",
				"DENSITY_FUNCTION",
				"NOISE_SETTINGS");

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
			BootstrapContext raw_context = (BootstrapContext) context;
			KlassWalker.walkFields(registryClass, RegistryDatagen.class, (Field f, boolean isStatic, Object value, RegistryDatagen annotation) -> {
				if (isStatic && value != null) {
					if (RegistryType.isDatagenHolder(f, registryType)) {
						DatagenHolder datagenHolder = (DatagenHolder) value;
						raw_context.register(datagenHolder.resourceKey, datagenHolder.value(context));
					} else if (RegistryType.isHolder(f, registryType)) {
						Holder holder = (Holder) value;
						raw_context.register(holder.getKey(), holder.value());
					}
				}
			});
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		private static final RegistrySetBuilder allRegistrySetBuilder() {
			RegistrySetBuilder registrySetBuilder = new RegistrySetBuilder();
			// 遍历net.minecraft.core.registries.Registries的所有静态字段并添加对应的数据生成器
			RegistryWalker.walkRegistries((Field f, ResourceKey registryKey, Class<?> registryType) -> {
				if (registryFieldFilter.contains(f.getName())) {
					RegistrySetBuilder.RegistryBootstrap bootstrap = (BootstrapContext context) -> {
						for (Class<?> registryClass : registryClasses)
							registerFields(context, registryClass, RegistryWalker.getRegistryType(f));
					};
					registrySetBuilder.add((ResourceKey) registryKey, bootstrap);
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
