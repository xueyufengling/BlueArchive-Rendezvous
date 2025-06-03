package ba.entries.dimension.kivotos;

import java.util.List;
import java.util.OptionalLong;

import com.mojang.serialization.MapCodec;

import fw.core.registry.DatagenHolder;
import fw.datagen.annotation.RegistryDatagen;
import fw.terrain.Df;
import fw.terrain.ExtDimension;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.SurfaceRuleData;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.Biomes;
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
import net.minecraft.world.level.levelgen.Noises;
import net.neoforged.neoforge.registries.DeferredHolder;

public class Kivotos {
	static {
		RegistryDatagen.RegistriesProvider.forDatagen(Kivotos.class);
	}

	public static final String ID = "kivotos";

	public static final int MIN_Y = -256;
	public static final int MAX_Y = 384;
	public static final int SEALEVEL = 0;

	/**
	 * 维度属性定义
	 */
	@RegistryDatagen
	public static final DatagenHolder<DimensionType> DIMENSION_TYPE = ExtDimension.Type.register(ID, new DimensionType(
			OptionalLong.empty(),
			true,
			false,
			false,
			true,
			1.0,
			true,
			true,
			MIN_Y,
			MAX_Y,
			MAX_Y,
			BlockTags.INFINIBURN_OVERWORLD,
			BuiltinDimensionTypes.OVERWORLD_EFFECTS,
			0.0F,
			new DimensionType.MonsterSettings(true, false, UniformInt.of(0, 15), 0)));

	public static final String DF_ID = ID + "/land";

	@RegistryDatagen
	public static final DeferredHolder<MapCodec<? extends DensityFunction>, MapCodec<KivotosDensityFunction>> LAND_DENSITY_FUNCTION = ExtDimension.Df.registerType(DF_ID, KivotosDensityFunction.CODEC);

	/**
	 * 噪声地形生成器
	 */
	@RegistryDatagen
	public static final DatagenHolder<NoiseGeneratorSettings> NOISE_SETTINGS = ExtDimension.Noise.register(ID, (BootstrapContext<NoiseGeneratorSettings> context) -> {
		Df df = Df.of(context);

		NoiseSettings noise = NoiseSettings.create(
				MIN_Y,
				MAX_Y,
				2,
				2);

		// 1. 大陆生成 (板块构造模拟)
		DensityFunction continents = DensityFunctions.add(
				DensityFunctions.constant(-0.2), // 全局海平面调整
				df.func("minecraft:overworld/continents"));

		// 2. 板块碰撞形成的山脉
		DensityFunction plateCollision = DensityFunctions.mul(
				DensityFunctions.constant(2.5),
				DensityFunctions.max(
						DensityFunctions.min(
								DensityFunctions.add(
										df.func("minecraft:overworld/erosion"),
										df.func("minecraft:overworld/ridges")),
								DensityFunctions.constant(0.5)),
						DensityFunctions.constant(-0.5)));

		// 3. 海拔梯度 (从海岸到内陆)
		DensityFunction elevationGradient = DensityFunctions.mul(
				DensityFunctions.constant(2.0),
				DensityFunctions.add(
						continents,
						DensityFunctions.constant(0.3))
						.clamp(-1.0, 1.0));

		// 4. 细节噪声 (侵蚀效果)
		DensityFunction detailNoise = DensityFunctions.mul(
				DensityFunctions.constant(0.8),
				df.func("minecraft:overworld/jaggedness"));

		// 5. 最终地形密度计算
		DensityFunction initialDensity = DensityFunctions.add(
				DensityFunctions.add(
						continents,
						plateCollision),
				elevationGradient);

		DensityFunction finalDensity = DensityFunctions.add(
				initialDensity,
				detailNoise);

		// 6. 河流系统 (基于侵蚀噪声)
		DensityFunction riverSystem = DensityFunctions.add(
				DensityFunctions.constant(-0.7),
				df.func("minecraft:overworld/erosion"));

		return new NoiseGeneratorSettings(
				noise,
				Blocks.STONE.defaultBlockState(), // 世界生成时填充的默认方块
				Blocks.WATER.defaultBlockState(), // 海平面处的默认流体
				new NoiseRouter(
						DensityFunctions.zero(), // barrier
						DensityFunctions.min(
								DensityFunctions.mul(
										DensityFunctions.constant(1.5),
										riverSystem),
								DensityFunctions.constant(1.0)), // fluid_level_floodedness
						DensityFunctions.noise(df.param(Noises.AQUIFER_FLUID_LEVEL_SPREAD), 0.7), // fluid_level_spread
						DensityFunctions.zero(), // lava
						DensityFunctions.shiftedNoise2d(
								df.func("minecraft:shift_x"),
								df.func("minecraft:shift_z"),
								0.25,
								df.param(Noises.TEMPERATURE_LARGE)), // temperature
						DensityFunctions.shiftedNoise2d(
								df.func("minecraft:shift_x"),
								df.func("minecraft:shift_z"),
								0.25,
								df.param(Noises.VEGETATION_LARGE)), // vegetation
						continents, // continents
						df.func("minecraft:overworld/erosion"), // erosion
						df.func("minecraft:overworld/depth"), // depth
						df.func("minecraft:overworld/ridges"), // ridges
						initialDensity, // initial_density_without_jaggedness
						finalDensity,//df.func("ba:" + DF_ID), // final_density
						DensityFunctions.zero(), // vein_toggle
						DensityFunctions.zero(), // vein_ridged
						DensityFunctions.zero()), // vein_gap
				SurfaceRuleData.overworld(),
				List.of(),
				SEALEVEL,
				false,
				false,
				true,
				false);

	});

	@RegistryDatagen
	public static final DatagenHolder<LevelStem> LEVEL_STEM = ExtDimension.Stem.register(ID, (BootstrapContext<LevelStem> context) -> {
		HolderGetter<DimensionType> dimensionTypes = context.lookup(Registries.DIMENSION_TYPE);
		HolderGetter<NoiseGeneratorSettings> noiseSettings = context.lookup(Registries.NOISE_SETTINGS);
		Holder<DimensionType> dimType = dimensionTypes.getOrThrow(DIMENSION_TYPE.resourceKey);
		Holder<NoiseGeneratorSettings> noise = noiseSettings.getOrThrow(NOISE_SETTINGS.resourceKey);

		// MultiNoiseBiomeSource biomeSource = preset.getBiomeSource(biomes);

		return new LevelStem(dimType, new NoiseBasedChunkGenerator(
				new FixedBiomeSource(context.lookup(Registries.BIOME).getOrThrow(Biomes.PLAINS)),
				noise));
	});
}
