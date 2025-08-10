package ba.entries.dimension.shittim_chest;

import java.util.List;
import java.util.OptionalLong;

import ba.entries.biome.shittim_chest.ShittimChestBiomes;
import fw.client.render.sky.CloudColor;
import fw.client.render.sky.SkyColor;
import fw.core.ExecuteIn;
import fw.datagen.DatagenHolder;
import fw.datagen.annotation.RegistryDatagen;
import fw.dimension.ExtDimension;
import fw.math.ColorLinearInterpolation;
import fw.math.Vec3LinearInterpolation;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.SurfaceRuleData;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
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

public class ShittimChest {
	static {
		RegistryDatagen.RegistriesProvider.forDatagen();
		ExecuteIn.Client(() -> {
			CloudColor.setLevelCloudColorResolver("ba:shittim_chest", ColorLinearInterpolation
					.begin(0, 214, 186, 159)// 6 h 灰黄
					.append(3000, 208, 228, 243)// 9 h 灰白
					.append(6000, 246, 242, 243)// 12 h 亮白
					.append(9000, 208, 228, 243)// 15 h 灰白
					.append(12000, 116, 83, 160)// 18 h 紫
					.append(15000, 68, 139, 203)// 21 h 灰
					.append(18000, 59, 106, 189)// 0 h 深黑蓝
					.append(21000, 68, 139, 203)// 3 h 灰
			);
			SkyColor.setLevelSkyColorResolver("ba:shittim_chest",
					ColorLinearInterpolation
							.begin(3000, 90, 145, 198)// 9 h 灰蓝
							.append(9000, 90, 145, 198)// 15 h 灰蓝
							.append(15000, 67, 130, 195)// 21 h 夜晚蓝
							.append(18000, 12, 29, 71)// 0 h 深黑蓝
							.append(21000, 67, 130, 195), // 3 h 夜晚蓝
					Vec3LinearInterpolation
							.begin(3000, 0.1)// 9 h
							.append(9000, 0.1)// 15 h
							.append(15000, 0.5)// 21 h
							.append(18000, 0.7)// 0 h
							.append(21000, 0.5) // 3 h
			);
		});
	}

	public static final String ID = "shittim_chest";

	public static final int MIN_Y = 0;
	public static final int MAX_Y = 368;
	public static final int HEIGHT = MAX_Y - MIN_Y;
	public static final int SEALEVEL = MIN_Y;

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
			HEIGHT,
			HEIGHT,
			BlockTags.VALID_SPAWN,
			BuiltinDimensionTypes.OVERWORLD_EFFECTS,
			0.0F,
			new DimensionType.MonsterSettings(true, false, UniformInt.of(0, 0), 0)));

	/**
	 * 噪声地形生成器
	 */
	@RegistryDatagen
	public static final DatagenHolder<NoiseGeneratorSettings> NOISE_SETTINGS = ExtDimension.Noise.register(ID, (BootstrapContext<?> context) -> {
		NoiseSettings noise = NoiseSettings.create(
				MIN_Y,
				HEIGHT,
				2,
				2);

		DensityFunction finalDensity = DensityFunctions.yClampedGradient(MIN_Y, MAX_Y, 1, -5);

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
						finalDensity, // final_density
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

	@RegistryDatagen
	public static final DatagenHolder<LevelStem> LEVEL_STEM = ExtDimension.Stem.register(ID, (BootstrapContext<?> context) -> {
		HolderGetter<DimensionType> dimensionTypes = context.lookup(Registries.DIMENSION_TYPE);
		HolderGetter<NoiseGeneratorSettings> noiseSettings = context.lookup(Registries.NOISE_SETTINGS);
		Holder<DimensionType> dimType = dimensionTypes.getOrThrow(DIMENSION_TYPE.resourceKey);
		Holder<NoiseGeneratorSettings> noise = noiseSettings.getOrThrow(NOISE_SETTINGS.resourceKey);

		return new LevelStem(dimType, new NoiseBasedChunkGenerator(
				new ShittimChestBiomes(context),
				noise));
	});
}
