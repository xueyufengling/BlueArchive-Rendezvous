package ba.entries.dimension.kivotos;

import fw.codec.annotation.AsDataField;
import fw.codec.annotation.CodecAutogen;
import fw.codec.annotation.CodecTarget;
import fw.terrain.algorithm.OctaveSimplexNoise;
import fw.terrain.algorithm.OctaveSimplexNoiseHeightMap;
import net.minecraft.util.KeyDispatchDataCodec;

public class KivotosHeightMap extends OctaveSimplexNoiseHeightMap {

	static {
		CodecAutogen.CodecGenerator.Codec();
	}

	@CodecAutogen(null_if_empty = true, register = true, warn_if_register_failed = true)
	public static final KeyDispatchDataCodec<KivotosHeightMap> CODEC = null;

	@CodecTarget
	public KivotosHeightMap(@AsDataField OctaveSimplexNoise noise) {
		super(noise);
	}

	public KivotosHeightMap() {
		super(-50, 300, 0.005, 0L, true, 1, 2, 4);
	}
}
