package fw.terrain.algorithm;

import java.util.ArrayList;
import java.util.List;

import com.mojang.serialization.Codec;

import fw.codec.annotation.AsDataField;
import fw.codec.annotation.CodecAutogen;
import fw.codec.annotation.CodecEntry;
import fw.codec.annotation.CodecTarget;
import fw.math.Sampler2D;

/**
 * 分形噪声
 */
@AsDataField
public class FractalNoise implements Sampler2D {
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

		@CodecEntry
		public final double scale_x;

		@CodecEntry
		public final double scale_y;

		@CodecEntry
		public final double offset_x;

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

	private Sampler2D noise;

	@CodecEntry
	private List<Entry> fractal_components;

	@CodecTarget
	private FractalNoise(List<Entry> fractal_components) {
		this.fractal_components = fractal_components;
	}

	public FractalNoise(Sampler2D noise, Entry... fractal_components) {
		this.fractal_components = new ArrayList<>();
		this.fractal_components.addAll(List.of(fractal_components));
		if (noise == null)
			throw new IllegalArgumentException("Invalid FractalNoise argument: noise cannot be null.");
		this.noise = noise;
	}

	public FractalNoise setNoise(Sampler2D noise) {
		this.noise = noise;
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
			result += noise.value(x * entry.scale_x + entry.offset_x, z * entry.scale_y + entry.offset_y) * entry.scale_amplitude;
		}
		return result;
	}
}
