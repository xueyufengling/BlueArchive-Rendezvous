package fw.terrain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mojang.serialization.MapCodec;

import fw.codec.CodecHolder;
import fw.core.registry.RegistryMap;
import fw.resources.ResourceKeyBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.neoforged.neoforge.registries.DeferredHolder;

/**
 * 结构，自动创建对应的StructureType。<br>
 */
public abstract class ExtStructure extends Structure implements CodecHolder<Structure> {

	protected ExtStructure(Structure.StructureSettings settings, String name) {
		super(settings);
		CodecHolder.super.construct(Structure.class);
		this.structureType = Type.register(name, this);
	}

	@Override
	protected Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
		// TODO Auto-generated method stub
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
