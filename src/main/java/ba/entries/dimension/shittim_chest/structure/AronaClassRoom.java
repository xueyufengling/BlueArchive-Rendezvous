package ba.entries.dimension.shittim_chest.structure;

import com.mojang.serialization.MapCodec;

import fw.codec.CodecHolder;
import fw.codec.annotation.CodecAutogen;
import fw.codec.annotation.CodecTarget;
import fw.terrain.ExtStructure;
import net.minecraft.client.Minecraft;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.levelgen.structure.Structure;

public class AronaClassRoom extends ExtStructure {

	static {
		CodecHolder.CODEC();
	}

	@CodecAutogen(null_if_empty = false)
	public static final MapCodec<? extends Structure> CODEC = null;

	@CodecTarget
	public AronaClassRoom(BootstrapContext<?> context) {
		super(null,
				"arona_class_room",
				context,
				template_pool,
				start_jigsaw_name,
				max_depth, start_height,
				project_start_to_heightmap,
				max_depth, alias_lookup,
				dimension_padding,
				liquid_settings);
	}

}
