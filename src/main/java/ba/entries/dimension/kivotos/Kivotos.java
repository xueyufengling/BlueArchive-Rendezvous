package ba.entries.dimension.kivotos;

import java.util.List;
import java.util.OptionalLong;

import fw.client.render.renderable.Texture;
import fw.client.render.scene.RenderableObject;
import fw.client.render.scene.SceneGraphNode;
import fw.client.render.sky.CloudColor;
import fw.client.render.sky.NearEarthObject;
import fw.client.render.sky.Sky;
import fw.client.render.sky.SkyColor;
import fw.core.ExecuteIn;
import fw.core.ModInit;
import fw.datagen.EntryHolder;
import fw.datagen.annotation.RegistryEntry;
import fw.dimension.ExtDimension;
import fw.math.interpolation.ColorLinearInterpolation;
import fw.math.interpolation.Vec3LinearInterpolation;
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

	public static final String ID = "ba:kivotos";

	static {
		RegistryEntry.RegistriesProvider.forDatagen();
		ModInit.Initializer.forInit();
		ExecuteIn.Client(() -> {
			CloudColor.setLevelCloudColorResolver(ID,
					ColorLinearInterpolation
							.begin(0, 214, 186, 159)// 6 h 灰黄
							.append(3000, 208, 228, 243)// 9 h 灰白
							.append(6000, 246, 242, 243)// 12 h 亮白
							.append(9000, 208, 228, 243)// 15 h 灰白
							.append(12000, 116, 83, 160)// 18 h 紫
							.append(15000, 68, 139, 203)// 21 h 灰
							.append(18000, 59, 106, 189)// 0 h 深黑蓝
							.append(21000, 68, 139, 203)// 3 h 灰
			);
			SkyColor.setLevelSkyColorResolver(ID,
					ColorLinearInterpolation
							.begin(3000, 90, 145, 198)// 9 h 灰蓝
							.append(9000, 90, 145, 198)// 15 h 灰蓝
							.append(15000, 67, 130, 195)// 21 h 夜晚蓝
							.append(18000, 12, 29, 71)// 0 h 深黑蓝
							.append(21000, 67, 130, 195), // 3 h 夜晚蓝
					Vec3LinearInterpolation
							.begin(3000, 0.1)// 9 h
							.append(9000, 0.1)// 15 h
							.append(15000, 0.3)// 21 h
							.append(18000, 0.7)// 0 h
							.append(21000, 0.3) // 3 h
							.append(22500, 0.3)// 4.5 h
			);
			Sky.setFixedCelestialColor(0.5f, 0.5f, 0.5f, 1);
		});
	}

	@ModInit(exec_stage = ModInit.Stage.CLIENT_CONNECT)
	private static void initHalos() {
		ExecuteIn.Client(() -> {
			SceneGraphNode node = Sky.render("new_sun", RenderableObject.quad(Texture.of("ba:textures/sky/halo/halo.png"), 0.4f, 0.8f));
			NearEarthObject.bind(node, NearEarthObject.Orbit.circle(0, 0, 64, 1, 3.14f / 200f));
			node.setShaderColor(1, 0, 0, 1);
		});
	}

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
								Decoration.begin(Blocks.SNOW)
										.when(
												TerrainTest.aboveYGradient(132, 384),
												TerrainTest.solidSurface(2)),
								Decoration.begin(Blocks.SNOW_BLOCK)
										.when(
												TerrainTest.aboveYGradient(164, 180),
												TerrainTest.solidSurface(1)))
						.layer(
								Decoration.begin(Blocks.SAND)
										.when(
												TerrainTest.belowYGradient(75, 70),
												TerrainTest.solidSurface(4)))
						.layer(
								Decoration.begin(Blocks.GRASS_BLOCK)
										.when(
												TerrainTest.aboveY(70),
												TerrainTest.solidSurface(1)),
								Decoration.begin(Blocks.DIRT)
										.when(
												TerrainTest.aboveY(70),
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
