package fw.math;

/**
 * 二维卷积算子
 */
public class ConvolutionOperator extends SlidingWindowOperator.Binary<double[]> {
	private ConvolutionOperator(double step_x, double step_y, int line, int column) {
		super(step_x, step_y, line, column);
	}

	private ConvolutionOperator(double step, int line, int column) {
		super(step, step, line, column);
	}

	private ConvolutionOperator(double step, int size) {
		super(step, size, size);
	}

	public static final ConvolutionOperator of(double step_x, double step_y, int line, int column) {
		return new ConvolutionOperator(step_x, step_y, line, column);
	}

	public static final ConvolutionOperator of(double step, int line, int column) {
		return new ConvolutionOperator(step, step, line, column);
	}

	public static final ConvolutionOperator of(double step, int size) {
		return new ConvolutionOperator(step, size, size);
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
	public final double calculate(double x, double z, double[] values, double[] kernel) {
		double result = 0;
		for (int idx = 0; idx < values.length; ++idx) {
			result += values[idx] * kernel[idx];
		}
		return result;
	}
}
