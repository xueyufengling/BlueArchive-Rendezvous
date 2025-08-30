package lepus.mc.core.registry;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.mojang.serialization.MapCodec;

import lyra.klass.GenericTypes;
import lyra.klass.KlassWalker;
import lyra.lang.Reflection;
import lyra.object.Placeholders.TypeWrapper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.LevelStem;

public class RegistryWalker {
	@FunctionalInterface
	public static interface RegistryOperation {
		/**
		 * 遍历每个注册表
		 * 
		 * @param f            注册表字段
		 * @param registryKey  注册表的键
		 * @param registryType 注册表的值类型
		 */
		public boolean operate(Field f, ResourceKey<? extends Registry<?>> registryKey, Class<?> registryType);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static final void walkRegistries(RegistryOperation op) {
		KlassWalker.walkTypeFields(Registries.class, ResourceKey.class, (Field f, boolean isStatic, ResourceKey registryKey) -> {
			if (isStatic && registryKey != null) {
				return op.operate(f, registryKey, getRegistryKeyType(f));
			}
			return true;
		});
	}

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
	public static final List<String> bootstrapRegistryFieldsFilter = List.of(
			// 维度
			"DIMENSION_TYPE",
			// 生物群系和地物
			"BIOME",
			"PLACED_FEATURE",
			"CONFIGURED_FEATURE",
			// 地形生成
			"DENSITY_FUNCTION",
			"NOISE",
			"NOISE_SETTINGS",
			"CONFIGURED_CARVER",
			"LEVEL_STEM",
			// 结构生成
			"PROCESSOR_LIST",
			"TEMPLATE_POOL",
			"STRUCTURE",
			"STRUCTURE_SET",
			// 伤害类型
			"DAMAGE_TYPE",
			// 附魔
			"ENCHANTMENT",
			"JUKEBOX_SONG",
			"TRIM_MATERIAL");

	/**
	 * 按照指定顺序遍历注册表，逐个字符串在整个Registries中无序查找，效率低。<br>
	 * 除非不同注册项存在先后注册依赖关系，否则不使用<br>
	 * 
	 * @param filterMap
	 * @param op
	 */
	public static final void walkFilteredRegistries(List<String> filterMap, RegistryOperation op) {
		for (String reg : filterMap) {
			accessRegistry(reg, (Field f, ResourceKey<? extends Registry<?>> registryKey, Class<?> registryType) -> {
				return op.operate(f, registryKey, registryType);
			});
		}
	}

	/**
	 * 遍历targetRegistryType类型的注册表，可能不止一个，例如MapCodec类型的注册表就不止一个，靠嵌套泛型参数区分。
	 * 
	 * @param targetRegistryType
	 * @param op
	 */
	public static final void walkTypeRegistries(Class<?> targetRegistryType, RegistryOperation op) {
		walkRegistries((Field f, ResourceKey<? extends Registry<?>> registryKey, Class<?> registryType) -> {
			if (Reflection.is(registryType, targetRegistryType))
				return op.operate(f, registryKey, registryType);
			else
				return true;
		});
	}

	/**
	 * 访问指定注册类型的注册表
	 * 
	 * @param targetRegistryType
	 * @param op
	 */
	public static final void accessRegistry(Class<?> targetRegistryType, RegistryOperation op) {
		walkRegistries((Field f, ResourceKey<? extends Registry<?>> registryKey, Class<?> registryType) -> {
			if (Reflection.is(registryType, targetRegistryType))
				return op.operate(f, registryKey, registryType);
			else
				return true;
		});
	}

	/**
	 * 访问指定字段名称的注册表
	 * 
	 * @param targetRegistryType
	 * @param op
	 */
	public static final void accessRegistry(String targetRegistryField, RegistryOperation op) {
		if (targetRegistryField == null)
			return;
		walkRegistries((Field f, ResourceKey<? extends Registry<?>> registryKey, Class<?> registryType) -> {
			if (f.getName().equals(targetRegistryField))
				return op.operate(f, registryKey, registryType);
			else
				return true;
		});
	}

	/**
	 * 数据注册阶段安全地遍历所有可用注册表
	 * 
	 * @param op
	 */
	public static final void walkBootstrapRegistries(RegistryOperation op) {
		walkFilteredRegistries(bootstrapRegistryFieldsFilter, op);
	}

	@FunctionalInterface
	public static interface MapCodecRegistryOperation {
		/**
		 * 对MapCodec类型的注册表进行操作
		 * 
		 * @param f           注册表字段
		 * @param registryKey 注册表的键
		 * @param codecType   Codec的类型
		 */
		@SuppressWarnings("rawtypes")
		public boolean operate(Field f, ResourceKey registryKey, Class<?> codecType);
	}

	/**
	 * 专门用于遍历注册类型为MapCodec的注册表
	 * 
	 * @param op
	 */
	@SuppressWarnings("rawtypes")
	public static final void walkMapCodecRegistries(MapCodecRegistryOperation op) {
		walkRegistries((Field f, ResourceKey<? extends Registry<?>> registryKey, Class<?> registryType) -> {
			if (Reflection.is(registryType, MapCodec.class)) {
				return op.operate(f, (ResourceKey) registryKey, getMapCodecRegistryType(f));
			}
			return true;
		});
	}

	/**
	 * 获取注册键对应的值类型。
	 * 
	 * @param registryKeyField 注册表对应的ResourceKey
	 * @return
	 */
	public static Class<?> getRegistryKeyType(Field registryKeyField) {
		return GenericTypes.classes(registryKeyField, 0)[0].type();
	}

	public static Class<?> getRegistryType(Field registryField) {
		return GenericTypes.classes(registryField)[0].type();
	}

	/**
	 * 获取注册表中MapCodec的类型。
	 * 
	 * @param registryKeyField
	 * @return
	 */
	public static Class<?> getMapCodecRegistryType(Field registryKeyField) {
		return GenericTypes.classes(registryKeyField, 0, 0)[0].type();
	}

	@SuppressWarnings({ "unchecked" })
	public static Class<?> getRegistryKeyType(ResourceKey<? extends Registry<?>> regKey) {
		TypeWrapper<Class<?>> wrapper = TypeWrapper.wrap();
		RegistryWalker.walkRegistries((Field f, ResourceKey<? extends Registry<?>> registryKey, Class<?> registryType) -> {// 获取Registry key对应的值类型registryType
			if (registryKey == regKey) {
				wrapper.value = registryType;
				return false;
			}
			return true;
		});
		return wrapper.value;
	}

	public static Class<?> getRegistryType(Registry<?> reg) {
		return getRegistryKeyType(reg.key());
	}

	/**
	 * 获取指定注册表key在Registries中的字段名称
	 * 
	 * @param regKey
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	public static String getRegistryFieldName(ResourceKey<? extends Registry<?>> regKey) {
		TypeWrapper<String> wrapper = TypeWrapper.wrap();
		RegistryWalker.walkRegistries((Field f, ResourceKey<? extends Registry<?>> registryKey, Class<?> registryType) -> {
			if (registryKey == regKey) {
				wrapper.value = f.getName();
				return false;
			}
			return true;
		});
		return wrapper.value;
	}

	public static String getRegistryFieldName(Registry<?> reg) {
		return getRegistryFieldName(reg.key());
	}

	public static class RegistryInfo {
		public final ResourceKey<? extends Registry<?>> regKey;
		public final String fieldName;
		public final Class<?> regType;

		RegistryInfo(ResourceKey<? extends Registry<?>> regKey, String fieldName, Class<?> regType) {
			this.regKey = regKey;
			this.fieldName = fieldName;
			this.regType = regType;
		}

		@Override
		public String toString() {
			return "RegistryInfo {regKey=" + regKey + ", fieldName=" + fieldName + ", regType=" + regType.getName() + "}";
		}
	}

	/**
	 * 根据注册表key值获取字段信息。key完全相同的注册表则只返回最上面的那个。
	 * 
	 * @param regKey
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	public static RegistryInfo getRegistryInfo(ResourceKey<? extends Registry<?>> regKey) {
		TypeWrapper<RegistryInfo> wrapper = TypeWrapper.wrap();
		RegistryWalker.walkRegistries((Field f, ResourceKey<? extends Registry<?>> registryKey, Class<?> registryType) -> {
			if (registryKey == regKey) {
				wrapper.value = new RegistryInfo(regKey, f.getName(), registryType);
				return false;
			}
			return true;
		});
		return wrapper.value;
	}

	public static RegistryInfo getRegistryInfo(Registry<?> reg) {
		return getRegistryInfo(reg.key());
	}

	public static ArrayList<RegistryInfo> collectRegistryInfo(List<ResourceKey<? extends Registry<?>>> registries) {
		ArrayList<RegistryWalker.RegistryInfo> regInfoList = new ArrayList<>();
		boolean genDim = false;// DIMENSION和LEVEL_STEM的key相同，会重复迭代，因此记录只要迭代过一次后续就不再迭代
		for (ResourceKey<? extends Registry<?>> reg : registries) {
			if (reg.equals(Registries.DIMENSION)) {// DIMENSION和LEVEL_STEM的key相同，需要单独处理
				if (genDim)
					continue;
				regInfoList.add(new RegistryInfo(Registries.DIMENSION, "DIMENSION", Level.class));
				regInfoList.add(new RegistryInfo(Registries.LEVEL_STEM, "LEVEL_STEM", LevelStem.class));
				genDim = true;
			} else {
				RegistryWalker.RegistryInfo regInfo = RegistryWalker.getRegistryInfo(reg);
				if (regInfo != null)
					regInfoList.add(regInfo);
			}
		}
		return regInfoList;
	}
}
