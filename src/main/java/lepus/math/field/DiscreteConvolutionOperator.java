package lepus.math.field;

/**
 * 离散二维卷积算子
 */
public class DiscreteConvolutionOperator extends SlidingWindowOperator.Binary<double[]> {
	private DiscreteConvolutionOperator(double step_x, double step_y, int line, int column) {
		super(step_x, step_y, line, column);
	}

	private DiscreteConvolutionOperator(double step, int line, int column) {
		super(step, step, line, column);
	}

	private DiscreteConvolutionOperator(double step, int size) {
		super(step, size, size);
	}

	public static final DiscreteConvolutionOperator of(double step_x, double step_y, int line, int column) {
		return new DiscreteConvolutionOperator(step_x, step_y, line, column);
	}

	public static final DiscreteConvolutionOperator of(double step, int line, int column) {
		return new DiscreteConvolutionOperator(step, step, line, column);
	}

	public static final DiscreteConvolutionOperator of(double step, int size) {
		return new DiscreteConvolutionOperator(step, size, size);
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
