package lepus.mc.terrain.algorithm;

import java.util.List;

import lepus.mc.codec.annotation.AsDataField;
import lepus.mc.codec.annotation.CodecEntry;
import lepus.mc.codec.annotation.CodecTarget;
import lepus.mc.terrain.HeightMap;

public class FractalOctaveSimplexNoiseHeightMap extends HeightMap {
	@Override
	public FractalOctaveSimplexNoiseHeightMap clone() {
		FractalOctaveSimplexNoiseHeightMap result = (FractalOctaveSimplexNoiseHeightMap) super.clone();
		result.fractal_settings = this.fractal_settings.clone();
		return result;
	}

	private static final int NOISE_BIAS = 0;

	/**
	 * 整体高度偏移量
	 */
	@CodecEntry
	private double height_bias;

	@CodecEntry
	private double min_height;

	@CodecEntry
	private double max_height;

	@CodecEntry
	private OctaveSimplexNoise noise;

	@CodecEntry
	private FractalNoise fractal_settings;

	/**
	 * 地上高度<br>
	 * max_height - height_bias
	 */
	private double height;

	/**
	 * 地下深度<br>
	 * height_bias - min_height
	 */
	private double depth;

	/**
	 * 小于height_bias的振幅的系数。<br>
	 * 振幅以height_bias - min_height为单位。
	 */
	private double ka_neg;

	/**
	 * 大于height_bias的振幅的系数
	 */
	private double ka_pos;

	private FractalOctaveSimplexNoiseHeightMap(double height_bias, double min_height, double max_height) {
		this.height_bias = height_bias;
		this.min_height = min_height;
		this.max_height = max_height;
		this.height = Math.abs(max_height - height_bias);
		this.depth = Math.abs(height_bias - min_height);
		this.ka_neg = 1;
		this.ka_pos = height / depth;
	}

	@CodecTarget
	protected FractalOctaveSimplexNoiseHeightMap(double height_bias, double min_height, double max_height, @AsDataField OctaveSimplexNoise noise, @AsDataField FractalNoise fractal_settings) {
		this(height_bias, min_height, max_height);
		fractal_settings.setNoise(noise);// 反序列化后只有分量参数，没有噪声，需要手动设置
		this.noise = noise;
		this.fractal_settings = fractal_settings;
	}

	/**
	 * @param height_bias       噪声原点，地形高度平均值
	 * @param min_height        最低高度
	 * @param max_height        最高高度
	 * @param x_factor
	 * @param z_factor
	 * @param seed
	 * @param use_noise_offsets
	 * @param octaves
	 */
	public FractalOctaveSimplexNoiseHeightMap(double height_bias, double min_height, double max_height, double x_factor, double z_factor, long seed, boolean use_noise_offsets, Integer... octaves) {
		this(height_bias, min_height, max_height);
		this.noise = new OctaveSimplexNoise(NOISE_BIAS, Math.abs(height_bias - min_height), x_factor, z_factor, seed, use_noise_offsets, List.of(octaves));
		this.fractal_settings = new FractalNoise(noise, FractalNoise.Component.of(1.0, 1.0));// 添加本底
	}

	public FractalOctaveSimplexNoiseHeightMap(double height_bias, double min_height, double max_height, double xz_factor, long seed, boolean use_noise_offsets, Integer... octaves) {
		this(height_bias, min_height, max_height, xz_factor, xz_factor, seed, use_noise_offsets, octaves);
	}

	public final FractalOctaveSimplexNoiseHeightMap addFractalComponents(FractalNoise.Component... entries) {
		fractal_settings.addComponents(entries);
		return this;
	}

	@Override
	protected final double getHeightValue(double x, double z) {
		double a = fractal_settings.value(x, z);
		if (a > NOISE_BIAS) {
			return height_bias + a * ka_pos;
		} else {
			return height_bias + a * ka_neg;
		}
	}

	public final FractalOctaveSimplexNoiseHeightMap applyAbsInvertRidges(FractalNoise.Component... fractal_components) {
		this.addThis(FractalNoise.absInvertRidges(new FractalNoise(noise, fractal_components), noise.bias, noise.amplitude));
		double k = FractalNoise.Component.scaledAdditionalHeight(fractal_components);
		this.height_bias -= k * height;// 经验值
		return this;
	}
}