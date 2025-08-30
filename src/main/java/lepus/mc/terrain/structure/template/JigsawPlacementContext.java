package lepus.mc.terrain.structure.template;

import java.util.List;
import java.util.Optional;

import com.mojang.serialization.MapCodec;

import lepus.mc.codec.annotation.AsDataField;
import lepus.mc.codec.annotation.CodecAutogen;
import lepus.mc.codec.annotation.CodecEntry;
import lepus.mc.codec.annotation.CodecTarget;
import lepus.mc.resources.ResourceLocations;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pools.DimensionPadding;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasBinding;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasLookup;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;

@AsDataField
public class JigsawPlacementContext {

	@CodecAutogen(null_if_empty = false)
	public static final MapCodec<JigsawPlacementContext> CODEC = null;

	static {
		CodecAutogen.CodecGenerator.Codec();
	}

	/**
	 * 模板池
	 */
	@CodecEntry
	public Holder<StructureTemplatePool> template_pool;

	@CodecEntry
	public Optional<ResourceLocation> start_jigsaw_name;

	@CodecEntry
	public int max_depth;

	@CodecEntry
	public HeightProvider start_height;

	@CodecEntry
	public Optional<Heightmap.Types> project_start_to_heightmap;

	@CodecEntry
	public int max_distance_from_center;

	@CodecEntry
	public List<PoolAliasBinding> alias;

	@CodecEntry
	public DimensionPadding dimension_padding;

	@CodecEntry
	public LiquidSettings liquid_settings;

	@CodecTarget
	public JigsawPlacementContext(
			Holder<StructureTemplatePool> template_pool,
			Optional<ResourceLocation> start_jigsaw_name,
			int max_depth,
			HeightProvider start_height,
			Optional<Heightmap.Types> project_start_to_heightmap,
			int max_distance_from_center,
			List<PoolAliasBinding> alias,
			DimensionPadding dimension_padding,
			LiquidSettings liquid_settings) {
		this.template_pool = template_pool;
		this.start_jigsaw_name = start_jigsaw_name;
		this.max_depth = max_depth;
		this.start_height = start_height;
		this.project_start_to_heightmap = project_start_to_heightmap;
		this.max_distance_from_center = max_distance_from_center;
		this.alias = alias;
		this.dimension_padding = dimension_padding;
		this.liquid_settings = liquid_settings;
	}

	public JigsawPlacementContext(
			Holder<StructureTemplatePool> template_pool,
			Optional<ResourceLocation> start_jigsaw_name,
			int max_depth,
			HeightProvider start_height,
			Optional<Heightmap.Types> project_start_to_heightmap,
			int max_distance_from_center,
			DimensionPadding dimension_padding,
			LiquidSettings liquid_settings) {
		this(template_pool,
				start_jigsaw_name,
				max_depth,
				start_height,
				project_start_to_heightmap,
				max_distance_from_center,
				List.of(),
				dimension_padding,
				liquid_settings);
	}

	public JigsawPlacementContext(
			Holder<StructureTemplatePool> template_pool,
			int max_depth,
			HeightProvider start_height,
			Optional<Heightmap.Types> project_start_to_heightmap,
			int max_distance_from_center,
			DimensionPadding dimension_padding,
			LiquidSettings liquid_settings) {
		this(template_pool,
				Optional.empty(),
				max_depth,
				start_height,
				project_start_to_heightmap,
				max_distance_from_center,
				List.of(),
				dimension_padding,
				liquid_settings);
	}

	/**
	 * 数据生成阶段使用
	 * 
	 * @param context
	 * @param template_pool
	 * @param start_jigsaw_name
	 * @param max_depth
	 * @param start_height
	 * @param project_start_to_heightmap
	 * @param max_distance_from_center
	 * @param alias
	 * @param dimension_padding_bottom
	 * @param dimension_padding_top
	 * @param liquid_settings
	 */
	public JigsawPlacementContext(
			BootstrapContext<?> context,
			String template_pool,
			String start_jigsaw_name,
			int max_depth,
			HeightProvider start_height,
			Heightmap.Types project_start_to_heightmap,
			int max_distance_from_center,
			List<PoolAliasBinding> alias,
			int dimension_padding_bottom,
			int dimension_padding_top,
			LiquidSettings liquid_settings) {
		this(context.lookup(Registries.TEMPLATE_POOL).getOrThrow(TemplatePool.key(template_pool)),
				start_jigsaw_name == null ? Optional.empty() : Optional.of(ResourceLocations.build(start_jigsaw_name)),
				max_depth,
				start_height,
				project_start_to_heightmap == null ? Optional.empty() : Optional.of(project_start_to_heightmap),
				max_distance_from_center,
				alias,
				new DimensionPadding(dimension_padding_bottom, dimension_padding_top),
				liquid_settings);
	}

	public JigsawPlacementContext(
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
		this(context,
				template_pool,
				start_jigsaw_name,
				max_depth,
				start_height,
				project_start_to_heightmap,
				max_distance_from_center,
				List.of(),
				dimension_padding_bottom,
				dimension_padding_top,
				liquid_settings);
	}

	public JigsawPlacementContext(
			BootstrapContext<?> context,
			String template_pool,
			int max_depth,
			HeightProvider start_height,
			Heightmap.Types project_start_to_heightmap,
			int max_distance_from_center,
			int dimension_padding_bottom,
			int dimension_padding_top,
			LiquidSettings liquid_settings) {
		this(context,
				template_pool,
				null,
				max_depth,
				start_height,
				project_start_to_heightmap,
				max_distance_from_center,
				dimension_padding_bottom,
				dimension_padding_top,
				liquid_settings);
	}

	@FunctionalInterface
	public static interface StartPosResolver {
		/**
		 * 结构生成的方块坐标
		 * 
		 * @param start_height
		 * @param chunkPos
		 * @param random
		 * @param worldGenerationContext
		 * @return
		 */
		public BlockPos resolve(HeightProvider start_height, ChunkPos chunkPos, WorldgenRandom random, WorldGenerationContext worldGenerationContext);

		public static final StartPosResolver DEFAULT_JIGSAW_PLACEMENT = (HeightProvider start_height, ChunkPos chunkPos, WorldgenRandom random, WorldGenerationContext worldGenerationContext) -> {
			return new BlockPos(chunkPos.getMinBlockX(),
					start_height.sample(random, worldGenerationContext),
					chunkPos.getMinBlockZ());
		};
	}

	public StartPosResolver generateStartPos = StartPosResolver.DEFAULT_JIGSAW_PLACEMENT;

	/**
	 * 设置拼图起始点
	 * 
	 * @param generateStartPos
	 * @return
	 */
	public JigsawPlacementContext setStartPosResolver(StartPosResolver generateStartPos) {
		this.generateStartPos = generateStartPos;
		return this;
	}

	public final Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
		BlockPos startPos = this.generateStartPos.resolve(this.start_height, context.chunkPos(), context.random(), new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor()));
		return JigsawPlacement.addPieces(
				context,
				this.template_pool,
				this.start_jigsaw_name,
				this.max_depth,
				startPos,
				false,
				this.project_start_to_heightmap,
				this.max_distance_from_center,
				alias.isEmpty() ? PoolAliasLookup.EMPTY : PoolAliasLookup.create(alias, startPos, context.seed()),
				this.dimension_padding,
				this.liquid_settings);
	}
}
