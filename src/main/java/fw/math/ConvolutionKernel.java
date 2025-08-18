package fw.math;

/**
 * 二维卷积运算
 */
public class ConvolutionKernel extends SlidingWindowOperation {

	private final double[] values;

	public ConvolutionKernel(double step_x, double step_y, int line, int column) {
		super(step_x, step_y, line, column);
		values = new double[line * column];
	}

	public ConvolutionKernel(double step, int line, int column) {
		this(step, step, line, column);
	}

	public ConvolutionKernel(double step, int size) {
		this(step, size, size);
	}

	public ConvolutionKernel(double step_x, double step_y, int line, int column, double[] values) {
		super(step_x, step_y, line, column);
		int size = line * column;
		if (values.length != size)
			throw new IllegalArgumentException("Convolution kernel matrix size should be " + size);
		this.values = values;
	}

	public ConvolutionKernel(double step, int line, int column, double[] values) {
		this(step, step, line, column, values);
	}

	public ConvolutionKernel(double step, int size, double[] values) {
		this(step, size, size, values);
	}

	public final double value(int x_idx, int y_idx) {
		return values[index(x_idx, y_idx)];
	}

	public final ConvolutionKernel set(int x, int y, double value) {
		this.values[index(x, y)] = value;
		return this;
	}

	public final ConvolutionKernel setLine(int y, double... values) {
		for (int c = 0; c < column; ++c)
			this.values[index(c, y)] = values[c];
		return this;
	}

	public final ConvolutionKernel setColumn(int x, double... values) {
		for (int l = 0; l < line; ++l)
			this.values[index(x, l)] = values[l];
		return this;
	}

	/**
	 * 从左上角向右下角遍历source并计算卷积
	 * 
	 * @param source
	 * @param x
	 * @param y
	 * @param sampledValue
	 * @return
	 */
	@Override
	public final double resolve(double x, double z, double[] values) {
		double result = 0;
		for (int idx = 0; idx < values.length; ++idx) {
			result += values[idx] * this.values[idx];
		}
		return result;
	}

	/**
	 * 恒等卷积核
	 */
	public static final ConvolutionKernel Identity_3x3(double step) {
		return new ConvolutionKernel(step, 3,
				new double[] {
						0, 0, 0,
						0, 1, 0,
						0, 0, 0 });
	}

	/**
	 * 锐化卷积核
	 * 
	 * @param step
	 * @return
	 */
	public static final ConvolutionKernel Sharpening_3x3(double step) {
		return new ConvolutionKernel(step, 3,
				new double[] {
						0, -1, 0,
						-1, 5, -1,
						0, -1, 0 });
	}

	/**
	 * 高斯模糊
	 * 
	 * @param step
	 * @return
	 */
	public static final ConvolutionKernel GaussianBlur_3x3(double step) {
		return new ConvolutionKernel(step, 3,
				new double[] {
						1.0 / 16, 2.0 / 16, 1.0 / 16,
						2.0 / 16, 4.0 / 16, 2.0 / 16,
						1.0 / 16, 2.0 / 16, 1.0 / 16 });
	}

	public static final ConvolutionKernel Erosion_3x3(double step) {
		return new ConvolutionKernel(step, 3,
				new double[] {
						1, 1, 1,
						1, 1, 1,
						1, 1, 1 });
	}
}
