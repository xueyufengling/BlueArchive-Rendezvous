package lepus.mc.terrain.structure;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import com.mojang.serialization.MapCodec;

import lepus.mc.codec.annotation.AsDataField;
import lepus.mc.codec.annotation.CodecAutogen;
import lepus.mc.codec.annotation.CodecEntry;
import lepus.mc.codec.annotation.CodecTarget;
import lepus.mc.codec.derived.MapCodecHolder;
import lepus.mc.core.registry.HolderSets;
import lepus.mc.core.registry.RegistryMap;
import lepus.mc.datagen.EntryHolder;
import lepus.mc.resources.ResourceKeys;
import lepus.mc.terrain.structure.template.JigsawPlacementContext;
import lepus.mc.terrain.structure.template.JigsawPlacementContext.StartPosResolver;
import lyra.klass.KlassWalker;
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
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.DimensionPadding;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasBinding;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.neoforged.neoforge.registries.DeferredHolder;

/**
 * 采用拼图模板的结构，自动创建对应的StructureType。<br>
 */
public abstract class ExtStructure extends Structure implements MapCodecHolder<Structure> {
	static {
		CodecAutogen.CodecGenerator.markDerivedAutoRegister();
	}

	/**
	 * 所有构造函数传入且在实例方法中使用的字段需要全部作为CODEC字段。<br>
	 * 若构造函数传入了，但实例方法中未使用该参数（即仅构造函数内部使用的参数）则不需要序列化。
	 */

	@CodecEntry
	protected final String name;

	@CodecEntry
	protected final Structure.StructureSettings settings;

	@CodecEntry
	protected JigsawPlacementContext jigsaw_placement_settings;

	@CodecTarget
	protected ExtStructure(String name,
			Structure.StructureSettings settings,
			@AsDataField JigsawPlacementContext jigsaw_placement_settings) {
		super(settings);
		MapCodecHolder.super.construct(Structure.class);
		this.name = name;
		this.settings = settings;
		this.jigsaw_placement_settings = jigsaw_placement_settings;
	}

	public final ExtStructure setStartPosResolver(StartPosResolver generateStartPos) {
		this.jigsaw_placement_settings.setStartPosResolver(generateStartPos);
		return this;
	}

	/**
	 * 拼图结构完整定义
	 * 
	 * @param name
	 * @param settings
	 * @param template_pool
	 * @param start_jigsaw_name
	 * @param max_depth
	 * @param start_height
	 * @param project_start_to_heightmap
	 * @param max_distance_from_center
	 * @param alias
	 * @param dimension_padding
	 * @param liquid_settings
	 */
	protected ExtStructure(String name,
			Structure.StructureSettings settings,
			Holder<StructureTemplatePool> template_pool,
			Optional<ResourceLocation> start_jigsaw_name,
			int max_depth,
			HeightProvider start_height,
			Optional<Heightmap.Types> project_start_to_heightmap,
			int max_distance_from_center,
			List<PoolAliasBinding> alias,
			DimensionPadding dimension_padding,
			LiquidSettings liquid_settings) {
		this(name,
				settings,
				new JigsawPlacementContext(
						template_pool,
						start_jigsaw_name,
						max_depth,
						start_height,
						project_start_to_heightmap,
						max_distance_from_center,
						alias,
						dimension_padding,
						liquid_settings));
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
				List.of(),
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
			List<PoolAliasBinding> alias,
			int dimension_padding_bottom,
			int dimension_padding_top,
			LiquidSettings liquid_settings) {
		this(name,
				settings,
				new JigsawPlacementContext(
						context,
						template_pool,
						start_jigsaw_name,
						max_depth,
						start_height,
						project_start_to_heightmap,
						max_distance_from_center,
						alias,
						dimension_padding_bottom,
						dimension_padding_top,
						liquid_settings));
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
				List.of(),
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

	@Override
	protected Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
		if (this.validate(context))
			return this.jigsaw_placement_settings.findGenerationPoint(context);
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
			KlassWalker.walkGenericFields(this.getClass(), DeferredHolder.class, StructureType.class, (Field f, boolean isStatic, DeferredHolder holder) -> {
				type = (StructureType<?>) holder.value();
				return false;
			});
		}
		if (type == null)
			throw new IllegalStateException("Static DeferredHolder<StructureType<" + this.getClass().getSimpleName() + ">> field was not found in " + this.getClass());
		return type;
	}

	/**
	 * StructureType为函数式接口，只有返回Structure的codec的抽象方法
	 */
	public static class Type {
		public static final RegistryMap<StructureType<?>> STRUCTURE_TYPES = RegistryMap.of(Registries.STRUCTURE_TYPE);

		public static <S extends Structure> StructureType<S> build(MapCodec<S> structureCodec) {
			if (structureCodec == null)
				throw new IllegalArgumentException("Cannot build a StructureType<?> from null CODEC.");
			return () -> structureCodec;
		}

		public static final ResourceKey<StructureType<?>> key(String name) {
			return ResourceKeys.build(Registries.STRUCTURE_TYPE, name);
		}

		/**
		 * 立即初始化延迟注册
		 * 
		 * @param <S>
		 * @param name
		 * @param structureCodec
		 * @return
		 */
		public static <S extends Structure> DeferredHolder<StructureType<?>, StructureType<?>> register(String name, MapCodec<S> structureCodec) {
			if (structureCodec == null)
				throw new IllegalArgumentException("Cannot register a StructureType<?> from null CODEC.");
			return STRUCTURE_TYPES.register(name, () -> build(structureCodec));
		}

		/**
		 * 延迟初始化和注册，当使用CodecAutogen注解生成Structure的CODEC字段时，需要使用采用本方法注册。<br>
		 * StructureType必须后于CODEC初始化并注册，但CodecAutogen自动生成CODEC是先于RegisterEvent的，因此必须使用Supplier获取CODEC。<nr>
		 * 
		 * @param <S>
		 * @param name
		 * @param structureCodec
		 * @return
		 */
		public static <S extends Structure> DeferredHolder<StructureType<?>, StructureType<?>> register(String name, Supplier<MapCodec<S>> structureCodec) {
			return STRUCTURE_TYPES.register(name, () -> build(structureCodec.get()));
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
		public static final HolderSet<Biome> validBiomes(BootstrapContext<Structure> context, String... biomes) {
			return HolderSets.build(context, Registries.BIOME, biomes);
		}

		/**
		 * 查找指定tag的HolderSet，若该tag不存在则会抛出NullPointerException
		 * 
		 * @param context
		 * @param tagName
		 * @return
		 */
		public static final HolderSet<Biome> validTagBiomes(BootstrapContext<Structure> context, String tagName) {
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
