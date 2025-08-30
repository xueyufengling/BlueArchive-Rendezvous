package ba.entries.dimension.kivotos;

import lepus.math.field.ScalarField;
import lepus.mc.codec.annotation.AsDataField;
import lepus.mc.codec.annotation.CodecAutogen;
import lepus.mc.codec.annotation.CodecTarget;
import lepus.mc.terrain.algorithm.ErosionOperator;
import lepus.mc.terrain.algorithm.FractalNoise;
import lepus.mc.terrain.algorithm.FractalOctaveSimplexNoiseHeightMap;
import lepus.mc.terrain.algorithm.OctaveSimplexNoise;
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
				FractalNoise.Component.of(0.25, 4),
				FractalNoise.Component.of(0.125, 8));
		this.opThis(ErosionOperator.of(0.4, 2, 3), ErosionOperator.octaveSimplexErosion(1, 0.001, 0L, false, 1, 2, 4));
		// this.convThis(ConvolutionKernel.GaussianBlur_3x3(2));
		// this.blendThis(academyMainlandBlendFunc, ScalarField.constant(100));
	}

	public KivotosHeightMap() {
		super(56, -50, 288, 0.0005, 0L, true, 1, 2, 4);
	}
}
