package fw.math;

public abstract class SlidingWindowOperation {
	/**
	 * 采样步长
	 */
	public double step_x;
	public double step_y;

	public final int line;
	public final int column;
	private final int size;

	public SlidingWindowOperation(double step_x, double step_y, int line, int column) {
		this.step_x = step_x;
		this.step_y = step_y;
		this.line = line;
		this.column = column;
		this.size = line * column;
	}

	public SlidingWindowOperation(double step, int line, int column) {
		this(step, step, line, column);
	}

	public SlidingWindowOperation(double step, int size) {
		this(step, size, size);
	}

	public final int index(int x_idx, int y_idx) {
		return x_idx + y_idx * column;
	}

	public final int centralIndex() {
		return index(column / 2, line / 2);
	}

	public abstract double resolve(double x, double z, double[] values);

	public final double calculate(ScalarField source, double x, double y) {
		double start_x = x - (column / 2) * step_x;
		double start_y = y - (line / 2) * step_y;
		double[] cached_values = new double[size];// 区块生成为多线程的，因此需要给每个调用此方法的线程传入独立的cached_values，不能共用同一个数组
		for (int x_idx = 0; x_idx < column; ++x_idx) {
			for (int y_idx = 0; y_idx < line; ++y_idx) {
				cached_values[index(x_idx, y_idx)] = source.value(start_x + x_idx * step_x, start_y + y_idx * step_y);
			}
		}
		return this.resolve(x, y, cached_values);
	}
}
