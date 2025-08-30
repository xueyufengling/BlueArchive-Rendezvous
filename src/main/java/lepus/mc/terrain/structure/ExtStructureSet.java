package lepus.mc.terrain.structure;

import java.util.ArrayList;
import java.util.List;

import lepus.mc.core.registry.RegistryMap;
import lepus.mc.resources.ResourceKeys;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.neoforged.neoforge.registries.DeferredHolder;

/**
 * 结构集，用于储存同一种结构类型的多个变体
 */
public class ExtStructureSet {
	public static final int DEFAULT_STRUCTURE_WEIGHT = 1;

	public static final RegistryMap<StructureSet> STRUCTURE_SET = RegistryMap.of(Registries.STRUCTURE_SET);

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
			return add(ResourceKeys.build(Registries.STRUCTURE, key), weight);
		}

		public StructureSet build() {
			if (placement == null)
				throw new IllegalArgumentException("StructureSet placement is null, make sure you have specified a correct StructurePlacement.");
			StructureSet.StructureSelectionEntry[] arr = new StructureSet.StructureSelectionEntry[entries.size()];
			return ExtStructureSet.build(placement, entries.toArray(arr));
		}
	}
}
