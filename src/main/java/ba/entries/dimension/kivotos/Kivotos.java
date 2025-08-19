package ba.entries.dimension.kivotos;

import java.util.List;
import java.util.OptionalLong;

import fw.datagen.EntryHolder;
import fw.datagen.annotation.RegistryEntry;
import fw.dimension.ExtDimension;
import fw.terrain.Df;
import fw.terrain.biome.ExtBiome;
import fw.terrain.decoration.Decoration;
import fw.terrain.decoration.TerrainDecorator;
import fw.terrain.decoration.TerrainTest;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.NoiseSettings;

public class Kivotos {
	static {
		RegistryEntry.RegistriesProvider.forDatagen();
	}

	public static final String ID = "ba:kivotos";

	public static final int MIN_Y = -256;
	public static final int MAX_Y = 384;
	public static final int HEIGHT = MAX_Y - MIN_Y;
	public static final int SEALEVEL = 63;

	/**
	 * 维度属性定义
	 */
	@RegistryEntry
	public static final EntryHolder<DimensionType> DIMENSION_TYPE = ExtDimension.Type.register(ID, new DimensionType(
			OptionalLong.empty(),
			true,
			false,
			false,
			true,
			1.0,
			true,
			true,
			MIN_Y,
			HEIGHT,
			HEIGHT,
			BlockTags.INFINIBURN_OVERWORLD,
			BuiltinDimensionTypes.OVERWORLD_EFFECTS,
			0.0F,
			new DimensionType.MonsterSettings(true, false, UniformInt.of(0, 15), 0)));

	/**
	 * 噪声地形生成器
	 */
	@RegistryEntry
	public static final EntryHolder<NoiseGeneratorSettings> NOISE_SETTINGS = ExtDimension.Noise.register(ID, (BootstrapContext<NoiseGeneratorSettings> context, RegistryAccess registryAccess) -> {
		Df df = Df.of(context);

		NoiseSettings noise = NoiseSettings.create(
				MIN_Y,
				HEIGHT,
				2,
				2);

		DensityFunction continents = df.func("minecraft:overworld/continents");

		return new NoiseGeneratorSettings(
				noise,
				Blocks.STONE.defaultBlockState(), // 世界生成时填充的默认方块
				Blocks.WATER.defaultBlockState(),
				// Blocks.WATER.defaultBlockState(), // 海平面处的默认流体
				new NoiseRouter(
						DensityFunctions.zero(), // barrier
						DensityFunctions.zero(), // fluid_level_floodedness
						DensityFunctions.zero(), // fluid_level_spread
						DensityFunctions.zero(), // lava
						DensityFunctions.zero(), // temperature
						DensityFunctions.zero(), // vegetation
						continents, // continents
						DensityFunctions.zero(), // erosion
						DensityFunctions.zero(), // depth
						DensityFunctions.zero(), // ridges
						DensityFunctions.zero(), // initial_density_without_jaggedness
						new KivotosHeightMap(), // final_density
						DensityFunctions.zero(), // vein_toggle
						DensityFunctions.zero(), // vein_ridged
						DensityFunctions.zero()), // vein_gap
				TerrainDecorator.begin()
						.layer(
								Decoration.begin(Blocks.SNOW_BLOCK)
										.when(
												TerrainTest.aboveYGradient(132, 164),
												TerrainTest.solidSurface(2)))
						.layer(
								Decoration.begin(Blocks.GRASS_BLOCK)
										.when(
												TerrainTest.aboveY(70),
												TerrainTest.solidSurface(1)),
								Decoration.begin(Blocks.DIRT)
										.when(
												TerrainTest.aboveY(70),
												TerrainTest.solidSurface(4)))
						.layer(
								Decoration.begin(Blocks.SAND)
										.when(
												TerrainTest.belowYGradient(70, 65),
												TerrainTest.solidSurface(4)))
						.toRuleSource(), // -54生成岩浆
				List.of(),
				SEALEVEL,
				false,
				true,
				true,
				false);

	});

	@RegistryEntry
	public static final EntryHolder<LevelStem> LEVEL_STEM = ExtDimension.Stem.register(ID, (BootstrapContext<LevelStem> context, RegistryAccess registryAccess) -> {
		HolderGetter<DimensionType> dimensionTypes = context.lookup(Registries.DIMENSION_TYPE);
		HolderGetter<NoiseGeneratorSettings> noiseSettings = context.lookup(Registries.NOISE_SETTINGS);
		Holder<DimensionType> dimType = dimensionTypes.getOrThrow(DIMENSION_TYPE.getKey());
		Holder<NoiseGeneratorSettings> noise = noiseSettings.getOrThrow(NOISE_SETTINGS.getKey());

		return new LevelStem(dimType, new NoiseBasedChunkGenerator(
				// new KivotosBiomes(context),
				new FixedBiomeSource(ExtBiome.datagenStageHolder(context, "minecraft:forest")),
				noise));
	});
}
