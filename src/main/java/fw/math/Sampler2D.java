package fw.math;

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

	/**
	 * 卷积
	 * 
	 * @param func
	 * @param kernel
	 * @return
	 */
	public static Sampler2D constant(Sampler2D func, ConvolutionKernel kernel) {
		return (double x, double z) -> kernel.calculate(func, x, z);
	}

	@FunctionalInterface
	public static interface Operation {
		/**
		 * @param x            采样x坐标
		 * @param z            采样y坐标
		 * @param lastStepCalc 上一步的计算函数
		 * @param thisStepTerm 此项的函数
		 * @return
		 */
		public abstract double calculate(double x, double z, Sampler2D lastStepCalc, Sampler2D thisStepTerm);

		/**
		 * 作为HeightMap起始赋值
		 */
		public static final Operation ASSIGN = (double x, double z, Sampler2D lastStepCalc, Sampler2D thisStepTerm) -> thisStepTerm.value(x, z);
		public static final Operation DISCARD = (double x, double z, Sampler2D lastStepCalc, Sampler2D thisStepTerm) -> lastStepCalc.value(x, z);

		public static final Operation ADD = (double x, double z, Sampler2D lastStepCalc, Sampler2D thisStepTerm) -> lastStepCalc.value(x, z) + lastStepCalc.value(x, z);
		public static final Operation SUB = (double x, double z, Sampler2D lastStepCalc, Sampler2D thisStepTerm) -> lastStepCalc.value(x, z) - lastStepCalc.value(x, z);
		public static final Operation MUL = (double x, double z, Sampler2D lastStepCalc, Sampler2D thisStepTerm) -> lastStepCalc.value(x, z) * lastStepCalc.value(x, z);
		public static final Operation DIV = (double x, double z, Sampler2D lastStepCalc, Sampler2D thisStepTerm) -> lastStepCalc.value(x, z) / lastStepCalc.value(x, z);

		public static final Operation MAX = (double x, double z, Sampler2D lastStepCalc, Sampler2D thisStepTerm) -> Math.max(lastStepCalc.value(x, z), lastStepCalc.value(x, z));
		public static final Operation MIN = (double x, double z, Sampler2D lastStepCalc, Sampler2D thisStepTerm) -> Math.min(lastStepCalc.value(x, z), lastStepCalc.value(x, z));

		public static Operation CONV(ConvolutionKernel kernel) {
			return (double x, double z, Sampler2D lastStepCalc, Sampler2D thisStepTerm) -> kernel.calculate(lastStepCalc, x, z);
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
		 * 高度采样器
		 */
		public final Sampler2D term;

		private Entry(Operation op, Sampler2D sampler) {
			this.op = op;
			this.term = sampler;
		}

		public static final Entry of(Operation op, Sampler2D sampler) {
			return new Entry(op, sampler);
		}

		public static final Entry of(Sampler2D sampler) {
			return of(Operation.ADD, sampler);
		}

		public static final Entry of(double coefficient) {
			return of(Operation.MUL, Sampler2D.constant(coefficient));
		}

		public static final Entry of(Operation op) {
			return new Entry(op, null);
		}

		/**
		 * 将一个函数和Entry打包为一个新函数
		 * 
		 * @param lastStepCalc
		 * @param thisTerm
		 * @return
		 */
		public static final Sampler2D wrap(Sampler2D lastStepWrappedCalc, Entry thisTerm) {
			return (double x, double z) -> thisTerm.calculate(x, z, lastStepWrappedCalc);
		}

		public final double calculate(double x, double z, Sampler2D lastStepWrappedCalc) {
			return this.op.calculate(x, z, lastStepWrappedCalc, this.term);
		}
	}
}