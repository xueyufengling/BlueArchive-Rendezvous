package fw.terrain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mojang.serialization.MapCodec;

import fw.codec.CodecHolder;
import fw.codec.annotation.CodecEntry;
import fw.codec.annotation.CodecTarget;
import fw.codec.annotation.IntRange;
import fw.core.registry.RegistryMap;
import fw.resources.ResourceKeyBuilder;
import fw.resources.ResourceLocationBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.minecraft.world.level.levelgen.structure.pools.DimensionPadding;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasLookup;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.neoforged.neoforge.registries.DeferredHolder;

/**
 * 采用拼图模板的结构，自动创建对应的StructureType。<br>
 */
public abstract class ExtStructure extends Structure implements CodecHolder<Structure> {
	/**
	 * 所有构造函数传入且在实例方法中使用的字段需要全部作为CODEC字段。<br>
	 * 若构造函数传入了，但实例方法中未使用该参数（即仅构造函数内部使用的参数）则不需要序列化。
	 */

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

	@CodecEntry
	protected final PoolAliasLookup alias_lookup;

	@CodecEntry
	protected final DimensionPadding dimension_padding;

	@CodecEntry
	protected final LiquidSettings liquid_settings;

	@CodecTarget
	protected ExtStructure(Structure.StructureSettings settings,
			String name,
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
		this.structureType = Type.register(name, this);
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

	protected ExtStructure(Structure.StructureSettings settings,
			String name,
			Holder<StructureTemplatePool> template_pool,
			Optional<ResourceLocation> start_jigsaw_name,
			int max_depth,
			HeightProvider start_height,
			Optional<Heightmap.Types> project_start_to_heightmap,
			int max_distance_from_center,
			DimensionPadding dimension_padding,
			LiquidSettings liquid_settings) {
		this(settings,
				name,
				template_pool,
				start_jigsaw_name,
				max_distance_from_center,
				start_height,
				project_start_to_heightmap,
				max_distance_from_center,
				PoolAliasLookup.EMPTY,
				dimension_padding,
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
			PoolAliasLookup alias_lookup,
			int dimension_padding_bottom,
			int dimension_padding_top,
			LiquidSettings liquid_settings) {
		super(settings);
		CodecHolder.super.construct(Structure.class);
		this.structureType = Type.register(name, this);
		this.template_pool = context.lookup(Registries.TEMPLATE_POOL).getOrThrow(ResourceKeyBuilder.build(Registries.TEMPLATE_POOL, template_pool));
		this.start_jigsaw_name = start_jigsaw_name == null ? Optional.empty() : Optional.of(ResourceLocationBuilder.build(start_jigsaw_name));
		this.max_depth = max_depth;
		this.start_height = start_height;
		this.project_start_to_heightmap = project_start_to_heightmap == null ? Optional.empty() : Optional.of(project_start_to_heightmap);
		this.max_distance_from_center = max_distance_from_center;
		this.alias_lookup = alias_lookup;
		this.dimension_padding = new DimensionPadding(dimension_padding_bottom, dimension_padding_top);
		this.liquid_settings = liquid_settings;
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
		this(settings,
				name,
				context,
				template_pool,
				start_jigsaw_name,
				max_distance_from_center,
				start_height,
				project_start_to_heightmap,
				max_distance_from_center,
				PoolAliasLookup.EMPTY,
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

	protected final DeferredHolder<StructureType<?>, StructureType<?>> structureType;

	/**
	 * 其作用实际上就是为了获取CODEC，同方法codec()
	 */
	@Override
	public StructureType<?> type() {
		return (StructureType<?>) structureType.get();
	}

	/**
	 * StructureType为函数式接口，只有返回Structure的codec的抽象方法
	 */
	public static class Type {
		public static final RegistryMap<StructureType<?>> STRUCTURE_TYPES = new RegistryMap<>(Registries.STRUCTURE_TYPE);

		public static DeferredHolder<StructureType<?>, StructureType<?>> register(String name, StructureType<?> structureType) {
			return STRUCTURE_TYPES.register(name, () -> structureType);
		}

		public static DeferredHolder<StructureType<?>, StructureType<?>> register(String name, MapCodec<Structure> structureCodec) {
			return register(name, () -> structureCodec);
		}

		@SuppressWarnings("unchecked")
		public static <S extends Structure> DeferredHolder<StructureType<?>, StructureType<?>> register(String name, ExtStructure structure) {
			return register(name, (MapCodec<Structure>) structure.codec());
		}
	}

	/**
	 * 类结构同ExtStructure和ExtStructure.Type<br>
	 * 自动创建对应的StructurePlacementType
	 */
	public static abstract class Placement extends StructurePlacement implements CodecHolder<StructurePlacement> {
		@SuppressWarnings("deprecation")
		protected Placement(Vec3i locateOffset, FrequencyReductionMethod frequencyReductionMethod, float frequency, int salt, Optional<ExclusionZone> exclusionZone, String type) {
			super(locateOffset, frequencyReductionMethod, frequency, salt, exclusionZone);
			CodecHolder.super.construct(StructurePlacement.class);
			this.structurePlacementType = Type.register(type, this);
		}

		@Override
		protected abstract boolean isPlacementChunk(ChunkGeneratorStructureState structureState, int x, int z);

		/**
		 * StructurePlacement的类型，无注册表。，<br>
		 */
		public static class Type {
			public static final RegistryMap<StructurePlacementType<?>> STRUCTURE_PLACEMENT = new RegistryMap<>(Registries.STRUCTURE_PLACEMENT);

			public static DeferredHolder<StructurePlacementType<?>, StructurePlacementType<?>> register(String name, StructurePlacementType<?> placementType) {
				return STRUCTURE_PLACEMENT.register(name, () -> placementType);
			}

			public static DeferredHolder<StructurePlacementType<?>, StructurePlacementType<?>> register(String name, MapCodec<StructurePlacement> structurePlacementCodec) {
				return register(name, () -> structurePlacementCodec);
			}

			public static DeferredHolder<StructurePlacementType<?>, StructurePlacementType<?>> register(String name, Placement placement) {
				return register(name, (StructurePlacementType<?>) placement.codec());
			}
		}

		protected final DeferredHolder<StructurePlacementType<?>, StructurePlacementType<?>> structurePlacementType;

		@Override
		public StructurePlacementType<?> type() {
			return structurePlacementType.get();
		}
	}

	/**
	 * 结构集，用于储存同一种结构类型的多个变体
	 */
	public static class Set {
		public static final int DEFAULT_STRUCTURE_WEIGHT = 1;

		public static final RegistryMap<StructureSet> STRUCTURE_SET = new RegistryMap<>(Registries.STRUCTURE_SET);

		public static DeferredHolder<StructureSet, StructureSet> register(String name, StructureSet set) {
			return STRUCTURE_SET.register(name, () -> set);
		}

		public static StructureSet build(StructurePlacement placement, StructureSet.StructureSelectionEntry... entries) {
			return new StructureSet(List.of(entries), placement);
		}

		/**
		 * 构建一个均等权重的结构集
		 * 
		 * @param weight
		 * @param placement
		 * @param structures
		 * @return
		 */
		@SuppressWarnings("unchecked")
		public static StructureSet build(int weight, StructurePlacement placement, Holder<Structure>... structures) {
			StructureSet.StructureSelectionEntry[] entries = new StructureSet.StructureSelectionEntry[structures.length];
			for (int idx = 0; idx < structures.length; ++idx)
				entries[idx] = new StructureSet.StructureSelectionEntry(structures[idx], weight);
			return new StructureSet(List.of(entries), placement);
		}

		/**
		 * 用于创建具有不同权重的自定义结构集
		 */
		public static class Builder {
			private HolderGetter<Structure> structureLookup;
			StructurePlacement placement;
			private ArrayList<StructureSet.StructureSelectionEntry> entries = new ArrayList<>();

			public Builder(BootstrapContext<?> context) {
				this.structureLookup = context.lookup(Registries.STRUCTURE);
			}

			public Builder(BootstrapContext<?> context, StructurePlacement placement) {
				this(context);
				this.placement = placement;
			}

			public Builder placement(StructurePlacement placement) {
				this.placement = placement;
				return this;
			}

			/**
			 * 为该结构集添加一个结构
			 * 
			 * @param key
			 * @param weight
			 * @return
			 */
			@SuppressWarnings("unchecked")
			public Builder add(ResourceKey<? extends Structure> key, int weight) {
				entries.add(new StructureSet.StructureSelectionEntry(structureLookup.getOrThrow((ResourceKey<Structure>) key), weight));
				return this;
			}

			public Builder add(String key, int weight) {
				return add(ResourceKeyBuilder.build(Registries.STRUCTURE, key), weight);
			}

			public StructureSet build() {
				if (placement == null)
					throw new IllegalArgumentException("StructureSet placement is null, make sure you have specified a correct StructurePlacement.");
				StructureSet.StructureSelectionEntry[] arr = new StructureSet.StructureSelectionEntry[entries.size()];
				return Set.build(placement, entries.toArray(arr));
			}
		}
	}

	/**
	 * 模板池,亦称之为结构池StructurePool。<br>
	 * 用于储存同一个结构类型的多个拼图
	 */
	public static class Template {
		// public static final RegistryMap<StructureSet> STRUCTURE_TEMPLATE = new RegistryMap<>(Registries.STRUCTURE_POOL_ELEMENT);

	}
}
