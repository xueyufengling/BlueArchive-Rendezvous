package lepus.math.field;

import lepus.math.algebra.Operator;

/**
 * 在标量场中进行滑窗操作的算子
 * 
 * @param <R> 操作返回值
 * @param <O>
 */
public abstract class SlidingWindowOperator<O> implements Operator<ScalarField, ScalarField, O> {
	/**
	 * 采样步长
	 */
	public double step_x;
	public double step_y;

	public final int line;
	public final int column;
	private final int size;

	protected SlidingWindowOperator(double step_x, double step_y, int line, int column) {
		this.step_x = step_x;
		this.step_y = step_y;
		this.line = line;
		this.column = column;
		this.size = line * column;
	}

	protected SlidingWindowOperator(double step, int line, int column) {
		this(step, step, line, column);
	}

	protected SlidingWindowOperator(double step, int size) {
		this(step, size, size);
	}

	public final int index(int x_idx, int y_idx) {
		return x_idx + y_idx * column;
	}

	public final int centralIndex() {
		return index(column / 2, line / 2);
	}

	@SuppressWarnings("unchecked")
	public abstract double calculate(double x, double z, double[] values, O... operands);

	/**
	 * 单目或多目操作符，如果第二个运算量不是标量场，则额外参数需要子类自己储存。
	 */
	@Override
	@SuppressWarnings("unchecked")
	public final ScalarField calculate(ScalarField source, O... operands) {
		return (double x, double z) -> {
			double start_x = x - (column / 2) * step_x;
			double start_z = z - (line / 2) * step_y;
			double[] cached_values = new double[size];// 区块生成为多线程的，因此需要给每个调用此方法的线程传入独立的cached_values，不能共用同一个数组
			for (int x_idx = 0; x_idx < column; ++x_idx) {
				for (int z_idx = 0; z_idx < line; ++z_idx) {
					cached_values[index(x_idx, z_idx)] = source.value(start_x + x_idx * step_x, start_z + z_idx * step_y);
				}
			}
			return this.calculate(x, z, cached_values, operands);
		};
	}

	public static abstract class Binary<O> extends SlidingWindowOperator<O> {

		protected Binary(double step_x, double step_y, int line, int column) {
			super(step_x, step_y, line, column);
		}

		protected Binary(double step, int line, int column) {
			super(step, step, line, column);
		}

		protected Binary(double step, int size) {
			super(step, size, size);
		}

		public abstract double calculate(double x, double z, double[] values, O operand);

		@Override
		@SuppressWarnings("unchecked")
		public final double calculate(double x, double z, double[] values, O... operands) {
			return this.calculate(x, z, values, operands[0]);
		}
	}
}
