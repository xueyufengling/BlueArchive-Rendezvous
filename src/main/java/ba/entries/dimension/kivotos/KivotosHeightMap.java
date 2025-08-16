package ba.entries.dimension.kivotos;

import java.util.List;

import fw.codec.annotation.CodecAutogen;
import fw.terrain.Sampler2D;
import fw.terrain.algorithm.OctaveSimplexNoiseHeightMap;
import net.minecraft.util.KeyDispatchDataCodec;

public class KivotosHeightMap extends OctaveSimplexNoiseHeightMap {

	static {
		CodecAutogen.CodecGenerator.Codec();
	}

	@CodecAutogen(null_if_empty = true, warn_if_register_failed = true)
	public static final KeyDispatchDataCodec<KivotosHeightMap> CODEC = null;

	public KivotosHeightMap(double bias, double amplitude, double x_factor, double z_factor, long seed, boolean use_noise_offsets, List<Integer> octaves) {
		super(bias, amplitude, x_factor, z_factor, seed, use_noise_offsets, octaves);
	}

	public KivotosHeightMap(double bias, double amplitude, double xz_factor, long seed, boolean use_noise_offsets, Integer... octaves) {
		super(bias, amplitude, xz_factor, seed, use_noise_offsets, octaves);
	}

	@Override
	protected double getCoefficient(double x, double z, double lastStepResult, Sampler2D sampler, double sampledValue) {
		double r = super.amplitudeRatio(Kivotos.SEALEVEL, sampledValue);
		return 1.5 * r;
	}
}
