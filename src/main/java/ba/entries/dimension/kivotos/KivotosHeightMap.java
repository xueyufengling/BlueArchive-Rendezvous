package ba.entries.dimension.kivotos;

import fw.codec.annotation.AsDataField;
import fw.codec.annotation.CodecAutogen;
import fw.codec.annotation.CodecTarget;
import fw.math.ConvolutionKernel;
import fw.terrain.algorithm.OctaveSimplexNoise;
import fw.terrain.algorithm.FractalNoise;
import fw.terrain.algorithm.FractalOctaveSimplexNoiseHeightMap;
import net.minecraft.util.KeyDispatchDataCodec;

public class KivotosHeightMap extends FractalOctaveSimplexNoiseHeightMap {

	static {
		CodecAutogen.CodecGenerator.Codec();
	}

	@CodecAutogen(null_if_empty = true, register = true, warn_if_register_failed = true)
	public static final KeyDispatchDataCodec<KivotosHeightMap> CODEC = null;

	@CodecTarget
	public KivotosHeightMap(@AsDataField OctaveSimplexNoise noise, FractalNoise fractal_settings) {
		super(noise, fractal_settings);

		/*
		 * this.convThis(new ConvolutionKernel(3, new double[] {
		 * 0, -1, 0,
		 * -1, 5, -1,
		 * 0, -1, 0 }));
		 */
	}

	public KivotosHeightMap() {
		super(128, 128, 0.0005, 0L, true, 1, 2, 4);
	}
}
