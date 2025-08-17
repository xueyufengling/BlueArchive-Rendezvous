package fw.terrain.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.mojang.serialization.Codec;

import fw.codec.annotation.AsDataField;
import fw.codec.annotation.CodecAutogen;
import fw.codec.annotation.CodecEntry;
import fw.codec.annotation.CodecTarget;
import fw.math.ScalarField;

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

	@AsDataField
	public static class Entry {
		static {
			CodecAutogen.CodecGenerator.Codec();
		}

		@CodecAutogen(null_if_empty = true)
		public static final Codec<Entry> CODEC = null;

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
		private Entry(double scale_amplitude, double scale_x, double scale_y, double offset_x, double offset_y) {
			this.scale_amplitude = scale_amplitude;
			this.scale_x = scale_x;
			this.scale_y = scale_y;
			this.offset_x = offset_x;
			this.offset_y = offset_y;
		}

		public static final Entry of(double scale_amplitude, double scale_x, double scale_y, double offset_x, double offset_y) {
			return new Entry(scale_amplitude, scale_x, scale_y, offset_x, offset_y);
		}

		public static final Entry of(double scale_amplitude, double scale_x, double scale_y) {
			return of(scale_amplitude, scale_x, scale_y, 0, 0);
		}

		public static final Entry of(double scale_amplitude, double scale_xy) {
			return of(scale_amplitude, scale_xy, scale_xy);
		}
	}

	private Function<Double, Double> layer_transform = (Double value) -> value;

	private Function<Double, Double> final_transform = (Double value) -> value;

	private ScalarField noise;

	@CodecEntry
	private List<Entry> fractal_components;

	@CodecTarget
	private FractalNoise(List<Entry> fractal_components) {
		this.fractal_components = fractal_components;
	}

	public FractalNoise(ScalarField noise, Entry... fractal_components) {
		this.fractal_components = new ArrayList<>();
		this.fractal_components.addAll(List.of(fractal_components));
		if (noise == null)
			throw new IllegalArgumentException("Invalid FractalNoise argument: noise cannot be null.");
		this.noise = noise;
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
	public FractalNoise setLayerNoiseTransform(Function<Double, Double> layer_transform) {
		this.layer_transform = layer_transform;
		return this;
	}

	/**
	 * 设置叠加后的总噪声的变换操作
	 * 
	 * @param final_transform
	 * @return
	 */
	public FractalNoise setFinalNoiseTransform(Function<Double, Double> final_transform) {
		this.final_transform = final_transform;
		return this;
	}

	public FractalNoise addComponents(Entry... fractal_components) {
		this.fractal_components.addAll(List.of(fractal_components));
		return this;
	}

	@Override
	public double value(double x, double z) {
		double result = 0;
		for (Entry entry : fractal_components) {
			result += layer_transform.apply(noise.value(x * entry.scale_x + entry.offset_x, z * entry.scale_y + entry.offset_y) * entry.scale_amplitude);
		}
		return final_transform.apply(result);
	}

	public final FractalNoise absInvertRidges(double invertNoiseValue) {
		return this.clone().setLayerNoiseTransform((Double value) -> Math.abs(value)).setFinalNoiseTransform((Double value) -> invertNoiseValue - value);
	}
}
