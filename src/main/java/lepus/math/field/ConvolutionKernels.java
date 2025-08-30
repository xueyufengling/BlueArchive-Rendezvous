package lepus.math.field;

/**
 * 离散二维卷积核
 */
public class ConvolutionKernels {
	/**
	 * 恒等卷积核
	 */
	public static final double[] Identity_3x3(double step) {
		return new double[] {
				0, 0, 0,
				0, 1, 0,
				0, 0, 0 };
	}

	/**
	 * 锐化卷积核
	 * 
	 * @param step
	 * @return
	 */
	public static final double[] Sharpening_3x3(double step) {
		return new double[] {
				0, -1, 0,
				-1, 5, -1,
				0, -1, 0 };
	}

	/**
	 * 高斯模糊
	 * 
	 * @param step
	 * @return
	 */
	public static final double[] GaussianBlur_3x3(double step) {
		return new double[] {
				1.0 / 16, 2.0 / 16, 1.0 / 16,
				2.0 / 16, 4.0 / 16, 2.0 / 16,
				1.0 / 16, 2.0 / 16, 1.0 / 16 };
	}

	/**
	 * 二值图像侵蚀卷积核
	 * 
	 * @param step
	 * @return
	 */
	public static final double[] Erosion_3x3(double step) {
		return new double[] {
				1, 1, 1,
				1, 1, 1,
				1, 1, 1 };
	}
}
