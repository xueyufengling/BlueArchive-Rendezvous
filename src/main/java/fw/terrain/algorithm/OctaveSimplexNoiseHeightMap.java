package fw.terrain.algorithm;

import java.util.List;

import fw.codec.annotation.AsDataField;
import fw.codec.annotation.CodecAutogen;
import fw.codec.annotation.CodecEntry;
import fw.codec.annotation.CodecTarget;
import fw.terrain.HeightMap;
import net.minecraft.util.KeyDispatchDataCodec;

public class OctaveSimplexNoiseHeightMap extends HeightMap {

	static {
		CodecAutogen.CodecGenerator.Codec();
	}

	@CodecAutogen(null_if_empty = true, register = true, warn_if_register_failed = true)
	public static final KeyDispatchDataCodec<OctaveSimplexNoiseHeightMap> CODEC = null;

	@CodecEntry
	protected final OctaveSimplexNoise noise;

	@CodecTarget
	public OctaveSimplexNoiseHeightMap(@AsDataField OctaveSimplexNoise noise) {
		this.noise = noise;
	}

	public OctaveSimplexNoiseHeightMap(double bias, double amplitude, double x_factor, double z_factor, long seed, boolean use_noise_offsets, Integer... octaves) {
		this.noise = new OctaveSimplexNoise(bias, amplitude, x_factor, z_factor, seed, use_noise_offsets, List.of(octaves));
	}

	public OctaveSimplexNoiseHeightMap(double bias, double amplitude, double xz_factor, long seed, boolean use_noise_offsets, Integer... octaves) {
		this.noise = new OctaveSimplexNoise(bias, amplitude, xz_factor, xz_factor, seed, use_noise_offsets, octaves);
	}

	@Override
	protected final double getHeightValue(double x, double z) {
		return noise.value(x, z);
	}
}