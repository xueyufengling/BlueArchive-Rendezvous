package ba.entries.dimension.ark_of_atrahasis;

import java.util.List;
import java.util.OptionalLong;

import fw.datagen.EntryHolder;
import fw.datagen.annotation.RegistryEntry;
import fw.dimension.ExtDimension;
import fw.terrain.biome.ExtBiome;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.SurfaceRuleData;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.NoiseSettings;

public class ArkOfAtrahasis {

	public static final String ID = "ba:ark_of_atrahasis";

	static {
		RegistryEntry.RegistriesProvider.forDatagen();

	}

	public static final int MIN_Y = -128;
	public static final int MAX_Y = 1024;
	public static final int HEIGHT = MAX_Y - MIN_Y;
	public static final int SEALEVEL = MIN_Y;

	/**
	 * 维度属性定义
	 */
	@RegistryEntry
	public static final EntryHolder<DimensionType> DIMENSION_TYPE = ExtDimension.Type.register(ID, new DimensionType(
			OptionalLong.of(18000),
			false,
			false,
			false,
			false,
			1.0,
			false,
			false,
			MIN_Y,
			HEIGHT,
			HEIGHT,
			BlockTags.VALID_SPAWN,
			BuiltinDimensionTypes.NETHER_EFFECTS,
			0.0F,
			new DimensionType.MonsterSettings(true, false, UniformInt.of(0, 0), 0)));

	/**
	 * 噪声地形生成器
	 */
	@RegistryEntry
	public static final EntryHolder<NoiseGeneratorSettings> NOISE_SETTINGS = ExtDimension.Noise.register(ID, (BootstrapContext<NoiseGeneratorSettings> context, RegistryAccess registryAccess) -> {
		NoiseSettings noise = NoiseSettings.create(
				MIN_Y,
				HEIGHT,
				2,
				2);

		return new NoiseGeneratorSettings(
				noise,
				Blocks.WATER.defaultBlockState(), // 世界生成时填充的默认方块
				Blocks.AIR.defaultBlockState(), // 海平面处的默认流体
				new NoiseRouter(
						DensityFunctions.zero(), // barrier
						DensityFunctions.zero(), // fluid_level_floodedness
						DensityFunctions.zero(), // fluid_level_spread
						DensityFunctions.zero(), // lava
						DensityFunctions.zero(), // temperature
						DensityFunctions.zero(), // vegetation
						DensityFunctions.zero(), // continents
						DensityFunctions.zero(), // erosion
						DensityFunctions.zero(), // depth
						DensityFunctions.zero(), // ridges
						DensityFunctions.zero(), // initial_density_without_jaggedness
						DensityFunctions.zero(), // final_density
						DensityFunctions.zero(), // vein_toggle
						DensityFunctions.zero(), // vein_ridged
						DensityFunctions.zero()), // vein_gap
				SurfaceRuleData.air(),
				List.of(),
				SEALEVEL,
				false,
				false,
				false,
				false);

	});

	@RegistryEntry
	public static final EntryHolder<LevelStem> LEVEL_STEM = ExtDimension.Stem.register(ID, (BootstrapContext<LevelStem> context, RegistryAccess registryAccess) -> {
		HolderGetter<DimensionType> dimensionTypes = context.lookup(Registries.DIMENSION_TYPE);
		HolderGetter<NoiseGeneratorSettings> noiseSettings = context.lookup(Registries.NOISE_SETTINGS);
		Holder<DimensionType> dimType = dimensionTypes.getOrThrow(DIMENSION_TYPE.getKey());
		Holder<NoiseGeneratorSettings> noise = noiseSettings.getOrThrow(NOISE_SETTINGS.getKey());

		return new LevelStem(dimType, new NoiseBasedChunkGenerator(
				new FixedBiomeSource(ExtBiome.datagenStageHolder(context, "minecraft:forest")),
				noise));
	});
}