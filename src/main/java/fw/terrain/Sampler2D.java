package fw.terrain;

@FunctionalInterface
public interface Sampler2D {
	public double value(double x, double z);

	/**
	 * 无关xz坐标的常量高度
	 * 
	 * @param c
	 * @return
	 */
	public static Sampler2D constant(double c) {
		return (double x, double z) -> c;
	}

	@FunctionalInterface
	public static interface Operation {
		public abstract double calculate(double lastStepResult, double v2);

		/**
		 * 作为HeightMap起始赋值
		 */
		public static Operation ASSIGN = (double lastStepResult, double v2) -> v2;
		public static Operation DISCARD = (double lastStepResult, double v2) -> lastStepResult;

		public static Operation ADD = (double lastStepResult, double v2) -> lastStepResult + v2;
		public static Operation SUB = (double lastStepResult, double v2) -> lastStepResult - v2;
		public static Operation MUL = (double lastStepResult, double v2) -> lastStepResult * v2;
		public static Operation DIV = (double lastStepResult, double v2) -> lastStepResult / v2;

		public static Operation MAX = (double lastStepResult, double v2) -> Math.max(lastStepResult, v2);
		public static Operation MIN = (double lastStepResult, double v2) -> Math.min(lastStepResult, v2);
	}

	@FunctionalInterface
	public static interface Coefficient {
		/**
		 * 该项的系数
		 * 
		 * @param x              x坐标
		 * @param z              z坐标
		 * @param lastStepResult 上一步的最终结果
		 * @param sampler        高度采样器
		 * @param sampledValue   此项的sampler原始采样值
		 * @return 此项的系数
		 */
		public abstract double coefficient(double x, double z, double lastStepResult, Sampler2D sampler, double sampledValue);

		public static Coefficient constant(double c) {
			return (double x, double z, double lastStepResult, Sampler2D sampler, double sampledValue) -> c;
		}
	}

	public static class Entry implements Cloneable {

		@Override
		public Entry clone() {
			try {
				return (Entry) super.clone();
			} catch (CloneNotSupportedException ex) {
				ex.printStackTrace();
			}
			return null;
		}

		/**
		 * 执行的运算操作
		 */
		public final Operation op;
		/**
		 * 此项的系数
		 */
		public final Coefficient coefficient;

		/**
		 * 高度采样器
		 */
		public final Sampler2D sampler;

		private Entry(Operation op, Coefficient coefficient, Sampler2D sampler) {
			this.op = op;
			this.coefficient = coefficient;
			this.sampler = sampler;
		}

		private Entry(Operation op, double coefficient, Sampler2D sampler) {
			this(op, Coefficient.constant(coefficient), sampler);
		}

		public static final Entry of(Operation op, Coefficient coefficient, Sampler2D sampler) {
			return new Entry(op, coefficient, sampler);
		}

		public static final Entry of(Operation op, double coefficient, Sampler2D sampler) {
			return new Entry(op, coefficient, sampler);
		}

		public static final Entry of(Coefficient coefficient, Sampler2D sampler) {
			return of(Operation.ADD, coefficient, sampler);
		}

		public static final Entry of(double coefficient, Sampler2D sampler) {
			return of(Coefficient.constant(coefficient), sampler);
		}

		public static final Entry of(Sampler2D sampler) {
			return of(1.0, sampler);
		}

		public final double calculate(double lastStepResult, double x, double z) {
			double sampledValue = sampler.value(x, z);
			return this.op.calculate(lastStepResult, coefficient.coefficient(x, z, lastStepResult, sampler, sampledValue) * sampledValue);
		}

		public static final Entry of(Operation op, Coefficient coefficient, HeightMap height) {
			return of(op, coefficient, (double x, double z) -> height.clone().compute(x, z));
		}

		public static final Entry of(Operation op, double coefficient, HeightMap height) {
			return of(op, Coefficient.constant(coefficient), height);
		}

		public static final Entry of(Coefficient coefficient, HeightMap height) {
			return of(Operation.ADD, coefficient, height);
		}

		public static final Entry of(double coefficient, HeightMap height) {
			return of(Coefficient.constant(coefficient), height);
		}

		public static final Entry of(HeightMap height) {
			return of(1.0, height);
		}
	}
}