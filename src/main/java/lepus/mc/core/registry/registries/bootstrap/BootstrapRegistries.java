package lepus.mc.core.registry.registries.bootstrap;

import net.minecraft.core.MappedRegistry;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;
import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.RuleBlockEntityModifierType;
import net.minecraft.world.level.levelgen.structure.templatesystem.PosRuleTestType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.gameevent.PositionSourceType;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.stats.StatType;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import net.minecraft.world.level.storage.loot.providers.nbt.LootNbtProviderType;
import net.minecraft.world.level.storage.loot.providers.score.LootScoreProviderType;
import net.minecraft.util.valueproviders.FloatProviderType;
import net.minecraft.util.valueproviders.IntProviderType;
import net.minecraft.world.level.levelgen.heightproviders.HeightProviderType;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicateType;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacerType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSizeType;
import com.mojang.serialization.MapCodec;

import lepus.mc.core.RegistryFieldsInitializer;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.network.chat.numbers.NumberFormatType;
import net.minecraft.advancements.critereon.ItemSubPredicate.Type;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;

@SuppressWarnings("rawtypes")
public class BootstrapRegistries {

	static {
		RegistryFieldsInitializer.forBootstrap(BootstrapRegistries.class);
	}

	public static final MappedRegistry<Attribute> ATTRIBUTE = null;

	public static final MappedRegistry<DataComponentType> DATA_COMPONENT_TYPE = null;

	public static final MappedRegistry<ArmorMaterial> ARMOR_MATERIAL = null;

	public static final MappedRegistry<GameEvent> GAME_EVENT = null;

	public static final MappedRegistry<SoundEvent> SOUND_EVENT = null;

	public static final MappedRegistry<Fluid> FLUID = null;

	public static final MappedRegistry<MobEffect> MOB_EFFECT = null;

	public static final MappedRegistry<Block> BLOCK = null;

	public static final MappedRegistry<EntityType> ENTITY_TYPE = null;

	public static final MappedRegistry<Item> ITEM = null;

	public static final MappedRegistry<Potion> POTION = null;

	public static final MappedRegistry<ParticleType> PARTICLE_TYPE = null;

	public static final MappedRegistry<BlockEntityType> BLOCK_ENTITY_TYPE = null;

	public static final MappedRegistry<ResourceLocation> CUSTOM_STAT = null;

	public static final MappedRegistry<ChunkStatus> CHUNK_STATUS = null;

	public static final MappedRegistry<RuleTestType> RULE_TEST = null;

	public static final MappedRegistry<RuleBlockEntityModifierType> RULE_BLOCK_ENTITY_MODIFIER = null;

	public static final MappedRegistry<PosRuleTestType> POS_RULE_TEST = null;

	public static final MappedRegistry<MenuType> MENU = null;

	public static final MappedRegistry<RecipeType> RECIPE_TYPE = null;

	public static final MappedRegistry<RecipeSerializer> RECIPE_SERIALIZER = null;

	public static final MappedRegistry<PositionSourceType> POSITION_SOURCE_TYPE = null;

	public static final MappedRegistry<ArgumentTypeInfo> COMMAND_ARGUMENT_TYPE = null;

	public static final MappedRegistry<StatType> STAT_TYPE = null;

	public static final MappedRegistry<VillagerType> VILLAGER_TYPE = null;

	public static final MappedRegistry<VillagerProfession> VILLAGER_PROFESSION = null;

	public static final MappedRegistry<PoiType> POINT_OF_INTEREST_TYPE = null;

	public static final MappedRegistry<MemoryModuleType> MEMORY_MODULE_TYPE = null;

	public static final MappedRegistry<SensorType> SENSOR_TYPE = null;

	public static final MappedRegistry<Schedule> SCHEDULE = null;

	public static final MappedRegistry<Activity> ACTIVITY = null;

	public static final MappedRegistry<LootPoolEntryType> LOOT_POOL_ENTRY_TYPE = null;

	public static final MappedRegistry<LootItemFunctionType> LOOT_FUNCTION_TYPE = null;

	public static final MappedRegistry<LootItemConditionType> LOOT_CONDITION_TYPE = null;

	public static final MappedRegistry<LootNumberProviderType> LOOT_NUMBER_PROVIDER_TYPE = null;

	public static final MappedRegistry<LootNbtProviderType> LOOT_NBT_PROVIDER_TYPE = null;

	public static final MappedRegistry<LootScoreProviderType> LOOT_SCORE_PROVIDER_TYPE = null;

	public static final MappedRegistry<FloatProviderType> FLOAT_PROVIDER_TYPE = null;

	public static final MappedRegistry<IntProviderType> INT_PROVIDER_TYPE = null;

	public static final MappedRegistry<HeightProviderType> HEIGHT_PROVIDER_TYPE = null;

	public static final MappedRegistry<BlockPredicateType> BLOCK_PREDICATE_TYPE = null;

	public static final MappedRegistry<WorldCarver> CARVER = null;

	public static final MappedRegistry<Feature> FEATURE = null;

	public static final MappedRegistry<StructurePlacementType> STRUCTURE_PLACEMENT = null;

	public static final MappedRegistry<StructurePieceType> STRUCTURE_PIECE = null;

	public static final MappedRegistry<StructureType> STRUCTURE_TYPE = null;

	public static final MappedRegistry<PlacementModifierType> PLACEMENT_MODIFIER_TYPE = null;

	public static final MappedRegistry<BlockStateProviderType> BLOCK_STATE_PROVIDER_TYPE = null;

	public static final MappedRegistry<FoliagePlacerType> FOLIAGE_PLACER_TYPE = null;

	public static final MappedRegistry<TrunkPlacerType> TRUNK_PLACER_TYPE = null;

	public static final MappedRegistry<RootPlacerType> ROOT_PLACER_TYPE = null;

	public static final MappedRegistry<TreeDecoratorType> TREE_DECORATOR_TYPE = null;

	public static final MappedRegistry<FeatureSizeType> FEATURE_SIZE_TYPE = null;

	public static final MappedRegistry<MapCodec> BIOME_SOURCE = null;

	public static final MappedRegistry<MapCodec> CHUNK_GENERATOR = null;

	public static final MappedRegistry<MapCodec> MATERIAL_CONDITION = null;

	public static final MappedRegistry<MapCodec> MATERIAL_RULE = null;

	public static final MappedRegistry<MapCodec> DENSITY_FUNCTION_TYPE = null;

	public static final MappedRegistry<MapCodec> BLOCK_TYPE = null;

	public static final MappedRegistry<StructureProcessorType> STRUCTURE_PROCESSOR = null;

	public static final MappedRegistry<StructurePoolElementType> STRUCTURE_POOL_ELEMENT = null;

	public static final MappedRegistry<MapCodec> POOL_ALIAS_BINDING = null;

	public static final MappedRegistry<CatVariant> CAT_VARIANT = null;

	public static final MappedRegistry<FrogVariant> FROG_VARIANT = null;

	public static final MappedRegistry<Instrument> INSTRUMENT = null;

	public static final MappedRegistry<DecoratedPotPattern> DECORATED_POT_PATTERN = null;

	public static final MappedRegistry<CreativeModeTab> CREATIVE_MODE_TAB = null;

	public static final MappedRegistry<CriterionTrigger> TRIGGER_TYPE = null;

	public static final MappedRegistry<NumberFormatType> NUMBER_FORMAT_TYPE = null;

	public static final MappedRegistry<MapCodec> ENTITY_SUB_PREDICATE_TYPE = null;

	public static final MappedRegistry<Type> ITEM_SUB_PREDICATE_TYPE = null;

	public static final MappedRegistry<MapDecorationType> MAP_DECORATION_TYPE = null;

	public static final MappedRegistry<DataComponentType> ENCHANTMENT_EFFECT_COMPONENT_TYPE = null;

	public static final MappedRegistry<MapCodec> ENCHANTMENT_LEVEL_BASED_VALUE_TYPE = null;

	public static final MappedRegistry<MapCodec> ENCHANTMENT_ENTITY_EFFECT_TYPE = null;

	public static final MappedRegistry<MapCodec> ENCHANTMENT_LOCATION_BASED_EFFECT_TYPE = null;

	public static final MappedRegistry<MapCodec> ENCHANTMENT_VALUE_EFFECT_TYPE = null;

	public static final MappedRegistry<MapCodec> ENCHANTMENT_PROVIDER_TYPE = null;

}
