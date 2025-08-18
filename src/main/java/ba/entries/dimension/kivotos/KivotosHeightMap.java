package ba.entries.dimension.kivotos;

import fw.codec.annotation.AsDataField;
import fw.codec.annotation.CodecAutogen;
import fw.codec.annotation.CodecTarget;
import fw.math.ConvolutionKernel;
import fw.math.ScalarField;
import fw.terrain.algorithm.OctaveSimplexNoise;
import fw.terrain.algorithm.SlidingWindowErosion;
import fw.terrain.algorithm.FractalNoise;
import fw.terrain.algorithm.FractalOctaveSimplexNoiseHeightMap;
import net.minecraft.util.KeyDispatchDataCodec;

public class KivotosHeightMap extends FractalOctaveSimplexNoiseHeightMap {

	static {
		CodecAutogen.CodecGenerator.Codec();
	}

	@CodecAutogen(null_if_empty = true, register = true, warn_if_register_failed = true)
	public static final KeyDispatchDataCodec<KivotosHeightMap> CODEC = null;

	/**
	 * 球形势阱，势阱内为学院平坦占地，势阱外为自然生成山地
	 */
	ScalarField academyMainlandBlendFunc = ScalarField.spherePotential(0, 0, 64, 0.0, 256, 1.0);

	@CodecTarget
	public KivotosHeightMap(double height_bias, double min_height, double max_height, @AsDataField OctaveSimplexNoise noise, @AsDataField FractalNoise fractal_settings) {
		super(height_bias, min_height, max_height, noise, fractal_settings);
		this.applyAbsInvertRidges(
				FractalNoise.Entry.of(0.25, 4),
				FractalNoise.Entry.of(0.125, 8));
		this.slidingWindowThis(SlidingWindowErosion.octaveSimplexErode(0.4, 2, 3, 9, 0L, false, 0.001, 0.001, 1, 2, 4));
		// this.convThis(ConvolutionKernel.GaussianBlur_3x3(2));
		// this.blendThis(academyMainlandBlendFunc, ScalarField.constant(100));
	}

	public KivotosHeightMap() {
		super(56, -50, 288, 0.0005, 0L, true, 1, 2, 4);
	}
}
