package ba.entries.dimension.kivotos;

import fw.codec.annotation.AsDataField;
import fw.codec.annotation.CodecAutogen;
import fw.codec.annotation.CodecTarget;
import fw.math.ConvolutionKernel;
import fw.math.ScalarField;
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

	/**
	 * 球形势阱，势阱内为学院平坦占地，势阱外为自然生成山地
	 */
	ScalarField academyMainlandBlendFunc = ScalarField.spherePotential(0, 0, 64, 0.0, 256, 1.0);

	@CodecTarget
	public KivotosHeightMap(@AsDataField OctaveSimplexNoise noise, FractalNoise fractal_settings) {
		super(noise, fractal_settings);
		this.convThis(ConvolutionKernel.GaussianBlur_3x3(5));
		this.applyAbsInvertRidges();
		// this.blendThis(academyMainlandBlendFunc, ScalarField.constant(100));
		// this.convThis(ConvolutionKernel.Sharpening_3x3(5));
	}

	public KivotosHeightMap() {
		super(64, 128, 0.0005, 0L, true, 1, 2, 4);
		this.addFractalComponents(FractalNoise.Entry.of(0.2, 5),
				FractalNoise.Entry.of(0.1, 10),
				FractalNoise.Entry.of(0.05, 20));
	}
}
