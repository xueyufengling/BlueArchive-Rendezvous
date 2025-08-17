package fw.terrain.algorithm;

import java.util.List;

import fw.codec.annotation.AsDataField;
import fw.codec.annotation.CodecAutogen;
import fw.codec.annotation.CodecEntry;
import fw.codec.annotation.CodecTarget;
import fw.terrain.HeightMap;
import net.minecraft.util.KeyDispatchDataCodec;

public class FractalOctaveSimplexNoiseHeightMap extends HeightMap {

	static {
		CodecAutogen.CodecGenerator.Codec();
	}

	@CodecAutogen(null_if_empty = true, register = true, warn_if_register_failed = true)
	public static final KeyDispatchDataCodec<FractalOctaveSimplexNoiseHeightMap> CODEC = null;

	@CodecEntry
	protected final OctaveSimplexNoise noise;

	@CodecEntry
	protected final FractalNoise fractal_settings;

	@CodecTarget
	protected FractalOctaveSimplexNoiseHeightMap(@AsDataField OctaveSimplexNoise noise, @AsDataField FractalNoise fractal_settings) {
		this.noise = noise;
		this.fractal_settings = fractal_settings;
		fractal_settings.setNoise(noise.biasOf(0));
	}

	public FractalOctaveSimplexNoiseHeightMap(double bias, double amplitude, double x_factor, double z_factor, long seed, boolean use_noise_offsets, Integer... octaves) {
		this.noise = new OctaveSimplexNoise(bias, amplitude, x_factor, z_factor, seed, use_noise_offsets, List.of(octaves));
		this.fractal_settings = new FractalNoise(noise.biasOf(0));
	}

	public FractalOctaveSimplexNoiseHeightMap(double bias, double amplitude, double xz_factor, long seed, boolean use_noise_offsets, Integer... octaves) {
		this.noise = new OctaveSimplexNoise(bias, amplitude, xz_factor, xz_factor, seed, use_noise_offsets, octaves);
		this.fractal_settings = new FractalNoise(noise.biasOf(0));
	}

	public final FractalOctaveSimplexNoiseHeightMap addFractalComponents(FractalNoise.Entry... entries) {
		fractal_settings.addComponents(entries);
		return this;
	}

	@Override
	protected final double getHeightValue(double x, double z) {
		return noise.value(x, z) + fractal_settings.value(x, z);
	}

	public final FractalOctaveSimplexNoiseHeightMap applyAbsInvertRidges() {
		this.addThis(fractal_settings.absInvertRidges(0));
		return this;
	}
}