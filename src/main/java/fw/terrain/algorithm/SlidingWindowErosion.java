package fw.terrain.algorithm;

import fw.math.ScalarField;
import fw.math.SlidingWindowOperation;

/**
 * 侵蚀或膨胀
 */
public class SlidingWindowErosion extends SlidingWindowOperation {
	/**
	 * 当比中心值小或大的元素比例小于erosion_threshold时，则不会进行侵蚀操作。
	 */
	double erosion_threshold;
	ScalarField erosion;

	public SlidingWindowErosion(double step_x, double step_y, int line, int column, ScalarField erosion, double erosion_threshold) {
		super(step_x, step_y, line, column);
		this.erosion = erosion;
		this.erosion_threshold = erosion_threshold;
	}

	public SlidingWindowErosion(double step, int line, int column, ScalarField erosion, double erosion_threshold) {
		this(step, step, line, column, erosion, erosion_threshold);
	}

	public SlidingWindowErosion(double step, int size, ScalarField erosion, double erosion_threshold) {
		this(step, size, size, erosion, erosion_threshold);
	}

	/**
	 * 侵蚀，取值>=0.取0-1时不破坏原本地貌。<br>
	 * 使用侵蚀比例作为权重，所以小于中心点值的周围值权重为(0.5+0.5*erosion)，大于中心值的权重为(1-less_factor)
	 * 
	 * @param step
	 * @param size
	 * @param erosion 侵蚀比例，取值0~1。
	 * @return
	 */
	public static final SlidingWindowErosion erode(double step, int size, ScalarField erosion, double erosion_threshold) {
		return new SlidingWindowErosion(step, size, erosion, erosion_threshold);
	}

	public static final SlidingWindowErosion erode(double step, int size, double erosion, double erosion_threshold) {
		return erode(step, size, ScalarField.constant(erosion), erosion_threshold);
	}

	public static final SlidingWindowErosion octaveSimplexErode(double erosion_threshold, double step, int size, double minErosion, long seed, boolean use_noise_offsets, double x_factor, double z_factor, Integer... octaves) {
		return erode(step, size, new OctaveSimplexNoise(0.5 + minErosion, 0.5, x_factor, z_factor, seed, use_noise_offsets, octaves), erosion_threshold);
	}

	@Override
	public double resolve(double x, double z, double[] values) {
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
		if (lessCount == 0 || lessCount_ratio < erosion_threshold || lessCount_ratio > 1 - erosion_threshold)
			return centralValue;
		double erosion_value = erosion.value(x, z);
		double less_f = lessCount_ratio * erosion_value;
		return centralValue + (less / lessCount) * less_f;
	}
}
