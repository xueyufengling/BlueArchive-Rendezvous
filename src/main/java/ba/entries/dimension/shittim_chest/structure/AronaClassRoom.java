package ba.entries.dimension.shittim_chest.structure;

import com.mojang.serialization.MapCodec;

import fw.codec.CodecHolder;
import fw.codec.annotation.CodecAutogen;
import fw.terrain.ExtStructure;
import net.minecraft.world.level.levelgen.structure.Structure;

public class AronaClassRoom extends ExtStructure {

	static {
		CodecHolder.CODEC();
	}

	@CodecAutogen(null_if_empty = false)
	public static final MapCodec<? extends Structure> CODEC = null;

	public AronaClassRoom(Structure.StructureSettings settings) {
		super(settings, "arona_class_room");
	}

}
