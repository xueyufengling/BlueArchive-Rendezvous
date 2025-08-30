package lepus.mc.terrain.feature;

import java.util.ArrayList;
import java.util.List;

import lepus.mc.codec.annotation.AsDataField;
import lepus.mc.codec.annotation.CodecEntry;
import lepus.mc.codec.annotation.CodecTarget;
import lepus.mc.core.registry.RegistryMap;
import lepus.mc.datagen.EntryHolder;
import lepus.mc.terrain.LevelWriterFlag;
import lepus.mc.terrain.structure.StructureManipulator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.neoforged.neoforge.registries.DeferredHolder;

/**
 * 一个地物，包含地物生成参数。<br>
 * Feature为具体的地物本身，FeatureConfiguration为地物生成的参数。<br>
 * 地物可以选择是否有生成参数以及选择哪个FeatureConfiguration作为生成参数<br>
 * 本类的默认实现是从结构nbt文件读取建筑作为地物
 */
public class ExtFeature extends Feature<FeatureGenerationParams> {
	/**
	 * 本库所在jar内的结构nbt文件路径，该结构将作为地物放置。
	 */
	@CodecEntry
	protected final List<String> variants_nbt_path;

	/**
	 * 本地物的生成参数
	 */
	@CodecEntry
	protected FeatureGenerationParams generation_params;

	/**
	 * 生成地物要如何写入存档文件
	 */
	@CodecEntry
	protected int writing_flag;

	@CodecTarget
	@SuppressWarnings("unchecked")
	public ExtFeature(List<String> variants_nbt_path, @AsDataField FeatureGenerationParams generation_params, int writing_flag) {
		super(generation_params.codec());
		this.variants_nbt_path = variants_nbt_path;
		this.generation_params = generation_params;
		this.writing_flag = writing_flag;
	}

	public ExtFeature(List<String> variants_nbt_path, @AsDataField FeatureGenerationParams generation_params) {
		this(variants_nbt_path, generation_params, LevelWriterFlag.CLIENT_UPDATE);
	}

	public ExtFeature(String... variants_nbt_path) {
		this(List.of(variants_nbt_path), FeatureGenerationParams.NONE);
	}

	public static final RegistryMap<Feature<?>> FEATURES = RegistryMap.of(Registries.FEATURE);

	/**
	 * 仅注册Feature<br>
	 * 包含地物本身的放置策略，但不包含具体的生成参数
	 * 
	 * @param name
	 * @param feature
	 * @return
	 */
	public static DeferredHolder<Feature<?>, Feature<?>> register(String name, Feature<? extends FeatureConfiguration> feature) {
		return FEATURES.register(name, () -> feature);
	}

	/**
	 * 仅注册ConfigureFeature<br>
	 * 亦即给地物feature设置地物生成的参数列表。<br>
	 * 
	 * @param <FC>
	 * @param <F>
	 * @param name
	 * @param feature
	 * @param config
	 * @return
	 */
	public static <FC extends FeatureConfiguration, F extends Feature<FC>> EntryHolder<ConfiguredFeature<?, ?>> configure(String name, F feature, FC config) {
		return EntryHolder.of(Registries.CONFIGURED_FEATURE, name, new EntryHolder.BootstrapValue<>() {
			@Override
			public ConfiguredFeature<?, ?> value(BootstrapContext<ConfiguredFeature<?, ?>> context, RegistryAccess registryAccess) {
				return new ConfiguredFeature<>(feature, config);
			}
		});
	}

	/**
	 * 结构本体，延迟获取
	 */
	private List<StructureTemplate> feature_generators;

	private List<StructureTemplate> featureGenerators() {
		if (feature_generators == null) {
			feature_generators = new ArrayList<>();
			for (String path : variants_nbt_path)
				feature_generators.add(StructureManipulator.loadStructureInJar(path));
		}
		return feature_generators;
	}

	protected boolean validate(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin) {
		return true;
	}

	/**
	 * 从变体列表选择一个变体放置，默认随机选择
	 * 
	 * @param feature_structures
	 * @param level
	 * @param chunkGenerator
	 * @param random
	 * @param origin
	 * @return
	 */
	protected StructureTemplate selectVariant(List<StructureTemplate> feature_structures, WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin) {
		return feature_structures.get(random.nextIntBetweenInclusive(0, feature_structures.size() - 1));
	}

	/**
	 * 决定放置的朝向、镜像和是否放置其中实体等
	 * 
	 * @param level
	 * @param chunkGenerator
	 * @param random
	 * @param origin
	 * @return
	 */
	protected StructurePlaceSettings placeSettings(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin) {
		return new StructurePlaceSettings();
	}

	/**
	 * 实际放置地物。<br>
	 * 默认放置结构
	 */
	@Override
	public boolean place(FeaturePlaceContext<FeatureGenerationParams> context) {
		WorldGenLevel level = context.level();
		ChunkGenerator chunkGenerator = context.chunkGenerator();
		RandomSource random = context.random();
		BlockPos origin = context.origin();
		if (!validate(level, chunkGenerator, random, origin))
			return false;
		else
			selectVariant(featureGenerators(), level, chunkGenerator, random, origin).placeInWorld(level, BlockPos.ZERO, origin, this.placeSettings(level, chunkGenerator, random, origin), random, writing_flag);
		return true;
	}
}
