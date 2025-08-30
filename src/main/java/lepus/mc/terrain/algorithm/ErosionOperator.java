package lepus.mc.terrain.algorithm;

import lepus.math.field.ScalarField;
import lepus.math.field.SlidingWindowOperator;

/**
 * 非线性侵蚀算子
 */
public class ErosionOperator extends SlidingWindowOperator.Binary<ScalarField> {
	/**
	 * 当比中心值小或大的元素比例小于erosion_threshold时，则不会进行侵蚀操作。
	 */
	double erosion_threshold;

	private ErosionOperator(double step_x, double step_y, int line, int column, double erosion_threshold) {
		super(step_x, step_y, line, column);
		this.erosion_threshold = erosion_threshold;
	}

	private ErosionOperator(double step, int line, int column, double erosion_threshold) {
		this(step, step, line, column, erosion_threshold);
	}

	private ErosionOperator(double step, int size, double erosion_threshold) {
		this(step, size, size, erosion_threshold);
	}

	public static final ErosionOperator of(double step_x, double step_y, int line, int column, double erosion_threshold) {
		return new ErosionOperator(step_x, step_y, line, column, erosion_threshold);
	}

	public static final ErosionOperator of(double step, int line, int column, double erosion_threshold) {
		return new ErosionOperator(step, line, column, erosion_threshold);
	}

	/**
	 * 侵蚀算子，取值>=0.取0-1时不破坏原本地貌。<br>
	 * 使用侵蚀比例作为权重，所以小于中心点值的周围值权重为(0.5+0.5*erosion)，大于中心值的权重为(1-less_factor)
	 * 
	 * @param step
	 * @param size
	 * @param erosion 侵蚀比例，取值0~1。
	 * @return
	 */
	public static final ErosionOperator of(double step, int size, double erosion_threshold) {
		return new ErosionOperator(step, size, erosion_threshold);
	}

	/**
	 * 返回遵循OctaveSimplexNoise分布的侵蚀源噪声，该噪声振幅为1/2，偏移量为minErosion
	 * 
	 * @param minErosion
	 * @param x_factor
	 * @param z_factor
	 * @param seed
	 * @param use_noise_offsets
	 * @param octaves
	 * @return
	 */
	public static final OctaveSimplexNoise octaveSimplexErosion(double minErosion, double x_factor, double z_factor, long seed, boolean use_noise_offsets, Integer... octaves) {
		return new OctaveSimplexNoise(0.5 + minErosion, 0.5, x_factor, z_factor, seed, use_noise_offsets, octaves);
	}

	public static final OctaveSimplexNoise octaveSimplexErosion(double minErosion, double xz_factor, long seed, boolean use_noise_offsets, Integer... octaves) {
		return octaveSimplexErosion(minErosion, xz_factor, xz_factor, seed, use_noise_offsets, octaves);
	}

	@Override
	public double calculate(double x, double z, double[] values, ScalarField erosion) {
		int lessCount = 0;
		double less = 0;
		double centralValue = values[this.centralIndex()];
		for (int idx = 0; idx < values.length; ++idx) {
			double delta = values[idx] - centralValue;
			if (delta < 0) {
				less += delta;
				++lessCount;
			}
		}
		double lessCount_ratio = ((double) lessCount) / values.length;
		double erosion_value = erosion.value(x, z);
		if (lessCount == 0)
			return centralValue - erosion_value;// 四周与中心点齐平时减少侵蚀值的高度
		else {
			double lessAvg = (less / lessCount);
			if (lessCount_ratio < erosion_threshold || lessCount_ratio > 1 - erosion_threshold)
				return centralValue + lessAvg * (1 - lessCount_ratio) * erosion_value;// 山脊的脊上的点需要降低高度以适配周围被大幅侵蚀的点
			else
				return centralValue + lessAvg * lessCount_ratio * erosion_value;// 山脊的脊周边的点会被大幅侵蚀
		}
	}
}
