package fw.terrain.structure;

import java.util.Optional;

import com.mojang.serialization.MapCodec;

import fw.codec.annotation.CodecAutogen;
import fw.codec.annotation.CodecEntry;
import fw.codec.annotation.CodecTarget;
import fw.codec.annotation.IntRange;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
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
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasLookup;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;

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
	public final Holder<StructureTemplatePool> template_pool;

	@CodecEntry
	public final Optional<ResourceLocation> start_jigsaw_name;

	@CodecEntry(int_range = { @IntRange(min = 0, max = 30) })
	public final int max_depth;

	@CodecEntry
	public final HeightProvider start_height;

	@CodecEntry
	public final Optional<Heightmap.Types> project_start_to_heightmap;

	@CodecEntry(int_range = { @IntRange(min = 1, max = 128) })
	public final int max_distance_from_center;

	@CodecEntry(empty_codec_if_not_exist = true)
	public final PoolAliasLookup alias_lookup;

	@CodecEntry
	public final DimensionPadding dimension_padding;

	@CodecEntry
	public final LiquidSettings liquid_settings;

	@CodecTarget
	public JigsawPlacementContext(
			Holder<StructureTemplatePool> template_pool,
			Optional<ResourceLocation> start_jigsaw_name,
			int max_depth,
			HeightProvider start_height,
			Optional<Heightmap.Types> project_start_to_heightmap,
			int max_distance_from_center,
			PoolAliasLookup alias_lookup,
			DimensionPadding dimension_padding,
			LiquidSettings liquid_settings) {
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

	public final Optional<Structure.GenerationStub> findJigsawPlacementGenerationPoint(Structure.GenerationContext context) {
		return JigsawPlacement.addPieces(
				context,
				this.template_pool,
				this.start_jigsaw_name,
				this.max_depth,
				this.generateStartPos.resolve(this.start_height, context.chunkPos(), context.random(), new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor())),
				false,
				this.project_start_to_heightmap,
				this.max_distance_from_center,
				this.alias_lookup,
				this.dimension_padding,
				this.liquid_settings);
	}
}
