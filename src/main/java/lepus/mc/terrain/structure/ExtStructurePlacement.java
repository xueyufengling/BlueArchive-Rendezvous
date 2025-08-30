package lepus.mc.terrain.structure;

import java.util.Optional;

import com.mojang.serialization.MapCodec;

import lepus.mc.codec.derived.MapCodecHolder;
import lepus.mc.core.registry.RegistryMap;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.neoforged.neoforge.registries.DeferredHolder;

/**
 * 结构生成策略<br>
 * 类结构同ExtStructure和ExtStructure.Type<br>
 * 自动创建对应的StructurePlacementType
 */
public abstract class ExtStructurePlacement extends StructurePlacement implements MapCodecHolder<StructurePlacement> {
	@SuppressWarnings("deprecation")
	protected ExtStructurePlacement(Vec3i locateOffset, StructurePlacement.FrequencyReductionMethod frequencyReductionMethod, float frequency, int salt, Optional<StructurePlacement.ExclusionZone> exclusionZone, String type) {
		super(locateOffset, frequencyReductionMethod, frequency, salt, exclusionZone);
		MapCodecHolder.super.construct(StructurePlacement.class);
		this.structurePlacementType = Type.register(type, this);
	}

	@Override
	protected abstract boolean isPlacementChunk(ChunkGeneratorStructureState structureState, int x, int z);

	/**
	 * StructurePlacement的类型，无注册表。，<br>
	 */
	public static class Type {
		public static final RegistryMap<StructurePlacementType<?>> STRUCTURE_PLACEMENT = RegistryMap.of(Registries.STRUCTURE_PLACEMENT);

		public static DeferredHolder<StructurePlacementType<?>, StructurePlacementType<?>> register(String name, StructurePlacementType<?> placementType) {
			return STRUCTURE_PLACEMENT.register(name, () -> placementType);
		}

		public static DeferredHolder<StructurePlacementType<?>, StructurePlacementType<?>> register(String name, MapCodec<StructurePlacement> structurePlacementCodec) {
			return register(name, () -> structurePlacementCodec);
		}

		public static DeferredHolder<StructurePlacementType<?>, StructurePlacementType<?>> register(String name, ExtStructurePlacement placement) {
			return register(name, (StructurePlacementType<?>) placement.codec());
		}
	}

	protected final DeferredHolder<StructurePlacementType<?>, StructurePlacementType<?>> structurePlacementType;

	@Override
	public StructurePlacementType<?> type() {
		return structurePlacementType.get();
	}
}