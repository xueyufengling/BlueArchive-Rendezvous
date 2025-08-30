package lepus.mc.core.registry.registries.server;

import net.minecraft.core.MappedRegistry;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.entity.animal.WolfVariant;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.network.chat.ChatType;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.providers.EnchantmentProvider;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPreset;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.synth.NormalNoise.NoiseParameters;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import lepus.mc.core.RegistryFieldsInitializer;
import net.minecraft.advancements.Advancement;
import net.minecraft.world.item.crafting.Recipe;

@SuppressWarnings("rawtypes")
public class DynamicRegistries {

	static {
		RegistryFieldsInitializer.forDynamicServer(DynamicRegistries.class);
	}

	public static final MappedRegistry<BannerPattern> BANNER_PATTERN = null;

	public static final MappedRegistry<WolfVariant> WOLF_VARIANT = null;

	public static final MappedRegistry<DamageType> DAMAGE_TYPE = null;

	public static final MappedRegistry<JukeboxSong> JUKEBOX_SONG = null;

	public static final MappedRegistry<PaintingVariant> PAINTING_VARIANT = null;

	public static final MappedRegistry<Biome> BIOME = null;

	public static final MappedRegistry<ChatType> CHAT_TYPE = null;

	public static final MappedRegistry<ConfiguredWorldCarver> CONFIGURED_CARVER = null;

	public static final MappedRegistry<ConfiguredFeature> CONFIGURED_FEATURE = null;

	public static final MappedRegistry<DensityFunction> DENSITY_FUNCTION = null;

	public static final MappedRegistry<DimensionType> DIMENSION_TYPE = null;

	public static final MappedRegistry<Enchantment> ENCHANTMENT = null;

	public static final MappedRegistry<EnchantmentProvider> ENCHANTMENT_PROVIDER = null;

	public static final MappedRegistry<FlatLevelGeneratorPreset> FLAT_LEVEL_GENERATOR_PRESET = null;

	public static final MappedRegistry<NoiseGeneratorSettings> NOISE_SETTINGS = null;

	public static final MappedRegistry<NoiseParameters> NOISE = null;

	public static final MappedRegistry<PlacedFeature> PLACED_FEATURE = null;

	public static final MappedRegistry<Structure> STRUCTURE = null;

	public static final MappedRegistry<StructureProcessorList> PROCESSOR_LIST = null;

	public static final MappedRegistry<StructureSet> STRUCTURE_SET = null;

	public static final MappedRegistry<StructureTemplatePool> TEMPLATE_POOL = null;

	public static final MappedRegistry<TrimMaterial> TRIM_MATERIAL = null;

	public static final MappedRegistry<TrimPattern> TRIM_PATTERN = null;

	public static final MappedRegistry<WorldPreset> WORLD_PRESET = null;

	public static final MappedRegistry<MultiNoiseBiomeSourceParameterList> MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST = null;

	public static final MappedRegistry<Level> DIMENSION = null;

	public static final MappedRegistry<LevelStem> LEVEL_STEM = null;

	public static final MappedRegistry<LootTable> LOOT_TABLE = null;

	public static final MappedRegistry<LootItemFunction> ITEM_MODIFIER = null;

	public static final MappedRegistry<LootItemCondition> PREDICATE = null;

	public static final MappedRegistry<Advancement> ADVANCEMENT = null;

	public static final MappedRegistry<Recipe> RECIPE = null;

}
