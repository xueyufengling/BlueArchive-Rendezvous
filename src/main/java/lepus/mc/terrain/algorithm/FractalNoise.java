package lepus.mc.terrain.algorithm;

import java.util.ArrayList;
import java.util.List;

import com.mojang.serialization.Codec;

import lepus.math.field.ScalarField;
import lepus.mc.codec.annotation.AsDataField;
import lepus.mc.codec.annotation.CodecAutogen;
import lepus.mc.codec.annotation.CodecEntry;
import lepus.mc.codec.annotation.CodecTarget;

/**
 * 分形噪声
 */
@AsDataField
public class FractalNoise implements ScalarField, Cloneable {
	@Override
	public FractalNoise clone() {
		try {
			FractalNoise result = (FractalNoise) super.clone();
			result.fractal_components = new ArrayList<>();
			result.fractal_components.addAll(this.fractal_components);
			return result;
		} catch (CloneNotSupportedException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	static {
		CodecAutogen.CodecGenerator.Codec();
	}

	@CodecAutogen(null_if_empty = true)
	public static final Codec<FractalNoise> CODEC = null;

	@FunctionalInterface
	public interface Transform {
		/**
		 * @param layer_idx
		 * @param x
		 * @param z
		 * @param calc_value
		 * @return 变换后的值
		 */
		public double transform(int layer_idx, double x, double z, double calc_value);

		public static Transform NONE = (int layer_idx, double x, double z, double calc_value) -> calc_value;
	}

	@AsDataField
	public static class Component {
		static {
			CodecAutogen.CodecGenerator.Codec();
		}

		@CodecAutogen(null_if_empty = true)
		public static final Codec<Component> CODEC = null;

		@CodecEntry
		public final double scale_amplitude;

		/**
		 * x坐标的缩放因子，该值越大，则相邻两个方块在函数中的采样点坐标差距越大。<br>
		 * 例如，设置为2.0则相对于函数整体频率变为两倍，也就是缩小1/2。<br>
		 */
		@CodecEntry
		public final double scale_x;

		/**
		 * y坐标的缩放因子
		 */
		@CodecEntry
		public final double scale_y;

		/**
		 * x坐标的偏移量
		 */
		@CodecEntry
		public final double offset_x;

		/**
		 * y坐标偏移量
		 */
		@CodecEntry
		public final double offset_y;

		@CodecTarget
		private Component(double scale_amplitude, double scale_x, double scale_y, double offset_x, double offset_y) {
			this.scale_amplitude = scale_amplitude;
			this.scale_x = scale_x;
			this.scale_y = scale_y;
			this.offset_x = offset_x;
			this.offset_y = offset_y;
		}

		public static final Component of(double scale_amplitude, double scale_x, double scale_y, double offset_x, double offset_y) {
			return new Component(scale_amplitude, scale_x, scale_y, offset_x, offset_y);
		}

		public static final Component of(double scale_amplitude, double scale_x, double scale_y) {
			return of(scale_amplitude, scale_x, scale_y, 0, 0);
		}

		public static final Component of(double scale_amplitude, double scale_xy) {
			return of(scale_amplitude, scale_xy, scale_xy);
		}

		/**
		 * 分形噪声额外造成的振幅变化比例值，该值乘以噪声振幅为实际增加的振幅
		 * 
		 * @param fractal_components
		 * @return
		 */
		public static final double scaledAdditionalHeight(FractalNoise.Component... fractal_components) {
			double additional_height = 0;
			for (FractalNoise.Component entry : fractal_components)
				additional_height += entry.scale_amplitude;
			return additional_height;
		}
	}

	private Transform layer_transform = Transform.NONE;

	private Transform final_transform = Transform.NONE;

	private ScalarField noise;

	@CodecEntry
	private List<Component> fractal_components;

	@CodecTarget
	private FractalNoise(List<Component> fractal_components) {
		this.fractal_components = fractal_components;
	}

	public FractalNoise() {
		this.fractal_components = new ArrayList<>();
	}

	public FractalNoise(ScalarField noise, Component... fractal_components) {
		this.fractal_components = new ArrayList<>();
		this.fractal_components.addAll(List.of(fractal_components));
		if (noise == null)
			throw new IllegalArgumentException("Invalid FractalNoise argument: noise cannot be null.");
		this.noise = noise;
	}

	public FractalNoise(ScalarField noise) {
		this.noise = noise;
		this.fractal_components = new ArrayList<>();
	}

	public FractalNoise setNoise(ScalarField noise) {
		this.noise = noise;
		return this;
	}

	/**
	 * 设置每层分形噪声的操作
	 * 
	 * @param layer_transform
	 * @return
	 */
	public FractalNoise setLayerNoiseTransform(Transform layer_transform) {
		this.layer_transform = layer_transform;
		return this;
	}

	/**
	 * 设置叠加后的总噪声的变换操作
	 * 
	 * @param final_transform
	 * @return
	 */
	public FractalNoise setFinalNoiseTransform(Transform final_transform) {
		this.final_transform = final_transform;
		return this;
	}

	public FractalNoise addComponents(Component... fractal_components) {
		this.fractal_components.addAll(List.of(fractal_components));
		return this;
	}

	@Override
	public double value(double x, double z) {
		double result = 0;
		for (int layer_idx = 0; layer_idx < fractal_components.size(); ++layer_idx) {
			Component entry = fractal_components.get(layer_idx);
			result += layer_transform.transform(layer_idx, x, z, noise.value(x * entry.scale_x + entry.offset_x, z * entry.scale_y + entry.offset_y) * entry.scale_amplitude);
		}
		return final_transform.transform(-1, x, z, result);
	}

	/**
	 * 直接修改原始噪声，将其取绝对值反转形成山脊效果层
	 * 
	 * @param ridgeThreshold   山脊起始点（零点），通常是噪声平均值或bias
	 * @param invertNoiseValue 噪声最大值
	 * @return
	 */
	public static final FractalNoise absInvertRidges(FractalNoise noise, double ridgeThreshold, double invertNoiseValue) {
		return noise
				.setLayerNoiseTransform((int layer_idx, double x, double z, double calc_value) -> Math.abs(calc_value - ridgeThreshold))
				.setFinalNoiseTransform((int layer_idx, double x, double z, double calc_value) -> invertNoiseValue - calc_value);
	}

	public static final FractalNoise absInvertRidgesOf(FractalNoise noise, double ridgeThreshold, double invertNoiseValue) {
		return absInvertRidges(noise.clone(), ridgeThreshold, invertNoiseValue);
	}

	public static final double scaledAdditionalHeight(FractalNoise noise) {
		double additional_height = 0;
		for (FractalNoise.Component entry : noise.fractal_components)
			additional_height += entry.scale_amplitude;
		return additional_height;
	}
}
