package fw.math;

/**
 * 二维卷积运算
 */
public class ConvolutionKernel {
	/**
	 * 采样步长
	 */
	public double step_x;
	public double step_y;
	private final int line;
	private final int column;
	private final double[] values;

	public ConvolutionKernel(double step_x, double step_y, int line, int column) {
		this.step_x = step_x;
		this.step_y = step_y;
		this.line = line;
		this.column = column;
		values = new double[line * column];
	}

	public ConvolutionKernel(double step, int line, int column) {
		this(step, step, line, column);
	}

	public ConvolutionKernel(int line, int column) {
		this(1.0, line, column);
	}

	public ConvolutionKernel(double step_x, double step_y, int line, int column, double[] values) {
		this.step_x = step_x;
		this.step_y = step_y;
		this.line = line;
		this.column = column;
		int size = line * column;
		if (values.length != size)
			throw new IllegalArgumentException("Convolution kernel matrix size should be " + size);
		this.values = values;
	}

	public ConvolutionKernel(double step, int line, int column, double[] values) {
		this(step, step, line, column, values);
	}

	public ConvolutionKernel(int line, int column, double[] values) {
		this(1.0, line, column, values);
	}

	public ConvolutionKernel(int size, double[] values) {
		this(size, size, values);
	}

	public ConvolutionKernel(double step, int size, double[] values) {
		this(step, size, size, values);
	}

	public final int index(int x_idx, int y_idx) {
		return x_idx + y_idx * column;
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
	public final double calculate(ScalarField source, double x, double y) {
		double start_x = x - (column / 2) * step_x;
		double start_y = y - (line / 2) * step_y;
		double result = 0;
		for (int x_idx = 0; x_idx < column; ++x_idx) {
			for (int y_idx = 0; y_idx < line; ++y_idx) {
				result += this.value(x_idx, y_idx) * source.value(start_x + x_idx * step_x, start_y + y_idx * step_y);
			}
		}
		return result;
	}

	/**
	 * 恒等卷积核
	 */
	public static final ConvolutionKernel Identity_3x3 = new ConvolutionKernel(3,
			new double[] {
					0, 0, 0,
					0, 1, 0,
					0, 0, 0 });

	/**
	 * 锐化卷积核
	 * 
	 * @param step
	 * @return
	 */
	public static final ConvolutionKernel Sharpening_3x3(double step) {
		return new ConvolutionKernel(3,
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
		return new ConvolutionKernel(3,
				new double[] {
						1.0 / 16, 2.0 / 16, 1.0 / 16,
						2.0 / 16, 4.0 / 16, 2.0 / 16,
						1.0 / 16, 2.0 / 16, 1.0 / 16 });
	}
}
