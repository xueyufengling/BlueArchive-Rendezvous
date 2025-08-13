package fw.terrain.structure;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.mojang.serialization.MapCodec;

import fw.codec.CodecHolder;
import fw.codec.annotation.CodecEntry;
import fw.codec.annotation.CodecTarget;
import fw.codec.annotation.IntRange;
import fw.core.registry.HolderSets;
import fw.datagen.EntryHolder;
import fw.resources.ResourceKeyBuilder;
import fw.resources.ResourceLocationBuilder;
import lyra.klass.KlassWalker;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.DimensionPadding;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasLookup;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;

/**
 * 采用拼图模板的结构，自动创建对应的StructureType。<br>
 */
public abstract class ExtStructure extends Structure implements CodecHolder<Structure> {
	/**
	 * 所有构造函数传入且在实例方法中使用的字段需要全部作为CODEC字段。<br>
	 * 若构造函数传入了，但实例方法中未使用该参数（即仅构造函数内部使用的参数）则不需要序列化。
	 */

	@CodecEntry
	protected final String name;

	@CodecEntry
	protected final Structure.StructureSettings settings;

	/**
	 * 模板池
	 */
	@CodecEntry
	protected final Holder<StructureTemplatePool> template_pool;

	@CodecEntry
	protected final Optional<ResourceLocation> start_jigsaw_name;

	@CodecEntry(int_range = { @IntRange(min = 0, max = 30) })
	protected final int max_depth;

	@CodecEntry
	protected final HeightProvider start_height;

	@CodecEntry
	protected final Optional<Heightmap.Types> project_start_to_heightmap;

	@CodecEntry(int_range = { @IntRange(min = 1, max = 128) })
	protected final int max_distance_from_center;

	@CodecEntry(empty_codec_if_not_exist = true)
	protected final PoolAliasLookup alias_lookup;

	@CodecEntry
	protected final DimensionPadding dimension_padding;

	@CodecEntry
	protected final LiquidSettings liquid_settings;

	@CodecTarget
	protected ExtStructure(String name,
			Structure.StructureSettings settings,
			Holder<StructureTemplatePool> template_pool,
			Optional<ResourceLocation> start_jigsaw_name,
			int max_depth,
			HeightProvider start_height,
			Optional<Heightmap.Types> project_start_to_heightmap,
			int max_distance_from_center,
			PoolAliasLookup alias_lookup,
			DimensionPadding dimension_padding,
			LiquidSettings liquid_settings) {
		super(settings);
		CodecHolder.super.construct(Structure.class);
		this.name = name;
		this.settings = settings;
		this.template_pool = template_pool;
		this.start_jigsaw_name = start_jigsaw_name;
		this.max_depth = max_depth;
		this.start_height = start_height;
		this.project_start_to_heightmap = project_start_to_heightmap;
		this.max_distance_from_center = max_distance_from_center;
		this.alias_lookup = alias_lookup;
		this.dimension_padding = dimension_padding;
		this.liquid_settings = liquid_settings;
	}

	protected ExtStructure(String name,
			Structure.StructureSettings settings,
			Holder<StructureTemplatePool> template_pool,
			Optional<ResourceLocation> start_jigsaw_name,
			int max_depth,
			HeightProvider start_height,
			Optional<Heightmap.Types> project_start_to_heightmap,
			int max_distance_from_center,
			DimensionPadding dimension_padding,
			LiquidSettings liquid_settings) {
		this(name,
				settings,
				template_pool,
				start_jigsaw_name,
				max_depth,
				start_height,
				project_start_to_heightmap,
				max_distance_from_center,
				PoolAliasLookup.EMPTY,
				dimension_padding,
				liquid_settings);
	}

	protected ExtStructure(String name,
			Structure.StructureSettings settings,
			BootstrapContext<?> context,
			String template_pool,
			String start_jigsaw_name,
			int max_depth,
			HeightProvider start_height,
			Heightmap.Types project_start_to_heightmap,
			int max_distance_from_center,
			int dimension_padding_bottom,
			int dimension_padding_top,
			LiquidSettings liquid_settings) {
		this(name,
				settings,
				context.lookup(Registries.TEMPLATE_POOL).getOrThrow(ResourceKeyBuilder.build(Registries.TEMPLATE_POOL, template_pool)),
				start_jigsaw_name == null ? Optional.empty() : Optional.of(ResourceLocationBuilder.build(start_jigsaw_name)),
				max_depth,
				start_height,
				project_start_to_heightmap == null ? Optional.empty() : Optional.of(project_start_to_heightmap),
				max_distance_from_center,
				PoolAliasLookup.EMPTY,
				new DimensionPadding(dimension_padding_bottom, dimension_padding_top),
				liquid_settings);
	}

	protected ExtStructure(Structure.StructureSettings settings,
			String name,
			BootstrapContext<?> context,
			String template_pool,
			String start_jigsaw_name,
			int max_depth,
			HeightProvider start_height,
			Heightmap.Types project_start_to_heightmap,
			int max_distance_from_center,
			int dimension_padding_bottom,
			int dimension_padding_top,
			LiquidSettings liquid_settings) {
		this(name,
				settings,
				context,
				template_pool,
				start_jigsaw_name,
				max_distance_from_center,
				start_height,
				project_start_to_heightmap,
				max_distance_from_center,
				dimension_padding_bottom,
				dimension_padding_top,
				liquid_settings);
	}

	/**
	 * 通过区块xz坐标获取指定类型的高度y坐标，在validate()中使用。<br>
	 * 例如用于获取非空气方块的y坐标。<br>
	 * 
	 * @param context
	 * @param type
	 * @return
	 */
	public static final int getFirstOccupiedHeight(Structure.GenerationContext context, Heightmap.Types type) {
		ChunkPos chunkpos = context.chunkPos();
		return context.chunkGenerator().getFirstOccupiedHeight(
				chunkpos.getMinBlockX(),
				chunkpos.getMinBlockZ(),
				type,
				context.heightAccessor(),
				context.randomState());
	}

	/**
	 * 不满足这个条件则不会生成
	 * 
	 * @param chunkpos
	 * @param firstOccupiedHeight
	 * @return
	 */
	public boolean validate(Structure.GenerationContext context) {
		return true;
	}

	/**
	 * 结构生成的方块坐标
	 * 
	 * @param chunkPos
	 * @param random
	 * @param worldGenerationContext
	 * @return
	 */
	public BlockPos generateStartPos(ChunkPos chunkPos, WorldgenRandom random, WorldGenerationContext worldGenerationContext) {
		return new BlockPos(chunkPos.getMinBlockX(),
				this.start_height.sample(random, worldGenerationContext),
				chunkPos.getMinBlockZ());
	}

	@Override
	protected Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
		if (this.validate(context))
			return JigsawPlacement.addPieces(
					context,
					this.template_pool,
					this.start_jigsaw_name,
					this.max_depth,
					this.generateStartPos(context.chunkPos(), context.random(), new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor())),
					false,
					this.project_start_to_heightmap,
					this.max_distance_from_center,
					this.alias_lookup,
					this.dimension_padding,
					this.liquid_settings);
		else
			return Optional.empty();
	}

	/**
	 * 从子类中的静态字段获取的StructureType
	 */
	private StructureType<?> type;

	/**
	 * 其作用实际上就是为了获取CODEC，同方法codec()
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public StructureType<?> type() {
		if (type == null) {
			KlassWalker.walkGenericFields(this.getClass(), Holder.class, StructureType.class, (Field f, boolean isStatic, Holder holder) -> {
				type = (StructureType<?>) holder.value();
				return false;
			});
		}
		return type;
	}

	/**
	 * StructureType为函数式接口，只有返回Structure的codec的抽象方法
	 */
	public static class Type {

		@SuppressWarnings("unchecked")
		public static StructureType<?> build(MapCodec<? extends Structure> structureCodec) {
			return () -> (MapCodec<Structure>) structureCodec;
		}

		@SuppressWarnings("unchecked")
		public static <S extends Structure> StructureType<?> get(ExtStructure structure) {
			return build((MapCodec<Structure>) structure.codec());
		}

		public static final ResourceKey<StructureType<?>> typeKey(String name) {
			return ResourceKeyBuilder.build(Registries.STRUCTURE_TYPE, name);
		}

		public static <S extends Structure> EntryHolder<StructureType<?>> register(String name, EntryHolder.BootstrapValue<StructureType<?>> structureType) {
			return EntryHolder.of(Registries.STRUCTURE_TYPE, name, structureType);
		}
	}

	/**
	 * 数据生成阶段注册结构
	 * 
	 * @param name
	 * @param structure
	 * @return
	 */
	public static final EntryHolder<Structure> register(String name, EntryHolder.BootstrapValue<Structure> structure) {
		return EntryHolder.of(Registries.STRUCTURE, name, structure);
	}

	/**
	 * 结构生成的设置。<br>
	 */
	public static class Settings {

		/**
		 * 直接构建一个Holder列表
		 * 
		 * @param context
		 * @param biomes
		 * @return
		 */
		public static final HolderSet<Biome> validBiomes(BootstrapContext<?> context, String... biomes) {
			return HolderSets.build(context, Registries.BIOME, biomes);
		}

		/**
		 * 查找指定tag的HolderSet，若该tag不存在则会抛出NullPointerException
		 * 
		 * @param context
		 * @param tagName
		 * @return
		 */
		public static final HolderSet<Biome> validTagBiomes(BootstrapContext<?> context, String tagName) {
			return HolderSets.get(context, Registries.BIOME, tagName);
		}

		public static class SpawnOverrideEntry {
			public final MobCategory mobCategory;
			public final StructureSpawnOverride spawnOverride;

			private SpawnOverrideEntry(MobCategory mobCategory, StructureSpawnOverride spawnOverride) {
				this.mobCategory = mobCategory;
				this.spawnOverride = spawnOverride;
			}

			public static SpawnOverrideEntry of(MobCategory mobCategory, StructureSpawnOverride spawnOverride) {
				return new SpawnOverrideEntry(mobCategory, spawnOverride);
			}
		}

		public static final Map<MobCategory, StructureSpawnOverride> spawnOverrides(SpawnOverrideEntry... entries) {
			HashMap<MobCategory, StructureSpawnOverride> map = new HashMap<>();
			for (SpawnOverrideEntry entry : entries)
				map.put(entry.mobCategory, entry.spawnOverride);
			return map;
		}

		public static final Structure.StructureSettings of(HolderSet<Biome> biomes, Map<MobCategory, StructureSpawnOverride> spawnOverrides, GenerationStep.Decoration step, TerrainAdjustment terrainAdaptation) {
			return new Structure.StructureSettings(biomes, spawnOverrides, step, terrainAdaptation);
		}

		public static final Structure.StructureSettings of(HolderSet<Biome> biomes, GenerationStep.Decoration step, TerrainAdjustment terrainAdaptation) {
			return of(biomes, spawnOverrides(), step, terrainAdaptation);
		}
	}
}
