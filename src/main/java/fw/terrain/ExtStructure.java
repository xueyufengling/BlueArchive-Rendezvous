package fw.terrain;

import java.util.HashMap;
import java.util.Optional;

import com.mojang.serialization.MapCodec;

import fw.datagen.DatagenHolder;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ExtStructure<S> extends Structure {

	protected ExtStructure(StructureSettings settings) {
		super(settings);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public StructureType<?> type() {
		return null;
	}

	public static class Type<S extends Structure> implements StructureType<S> {
		private static final HashMap<String, DeferredHolder<StructureType<?>, StructureType<?>>> structureTypesMap = new HashMap<>();

		@Override
		public MapCodec<S> codec() {
			return null;
		}

		public static DatagenHolder<StructureType<?>> register(String name) {
			return null;
		}
	}
}
