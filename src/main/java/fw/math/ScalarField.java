package fw.math;

/**
 * 二维标量场
 */
@FunctionalInterface
public interface ScalarField {
	public double value(double x, double z);

	/**
	 * 无关xz坐标的常量高度
	 * 
	 * @param c
	 * @return
	 */
	public static ScalarField constant(double c) {
		return (double x, double z) -> c;
	}

	/**
	 * 距离固定某个点的距离
	 * 
	 * @param x
	 * @param z
	 * @return
	 */
	public static ScalarField distance(double fixed_x, double fixed_z) {
		return (double x, double z) -> {
			double dx = (x - fixed_x);
			double dz = (z - fixed_z);
			return Math.sqrt(dx * dx + dz * dz);
		};
	}

	/**
	 * 二维球形势垒或势阱<br>
	 * 可用于作为blend()的混合因子参数
	 * 
	 * @param fixed_x     球的球心x坐标
	 * @param fixed_z     球的球心z坐标
	 * @param innerRadius 靠内的球半径，该半径内的函数值为innerValue
	 * @param innerValue
	 * @param outerRadius 靠外的球半径，该半径内的函数值为outerValue
	 * @param outerValue
	 * @return 若点在innerRadius与outerRadius之间，则返回innerValue与outerValue的线性插值
	 */
	public static ScalarField spherePotential(double fixed_x, double fixed_z, double innerRadius, double innerValue, double outerRadius, double outerValue) {
		return new ScalarField() {
			double innerEdge = innerRadius * innerRadius;
			double outerEdge = outerRadius * outerRadius;
			double k = (outerValue - innerValue) / (outerRadius - innerRadius);

			public double value(double x, double z) {
				double dx = (x - fixed_x);
				double dz = (z - fixed_z);
				double r_sq = (dx * dx + dz * dz);
				if (r_sq <= innerEdge)
					return innerValue;
				else if (r_sq >= outerEdge)
					return outerValue;
				else {
					double r = Math.sqrt(r_sq);
					return innerValue + (r - innerRadius) * k;
				}
			}
		};
	}

	/**
	 * 卷积
	 * 
	 * @param func
	 * @param kernel
	 * @return
	 */
	public static ScalarField conv(ScalarField func, ConvolutionKernel kernel) {
		return (double x, double z) -> kernel.calculate(func, x, z);
	}

	/**
	 * 混合两个标量场
	 * 
	 * @param blendFactor 混合因子取值0-1，为func1的权重
	 * @param func1
	 * @param func2
	 * @return
	 */
	public static ScalarField blend(ScalarField blendFactor, ScalarField func1, ScalarField func2) {
		return (double x, double z) -> {
			double f = blendFactor.value(x, z);
			return func1.value(x, z) * f + func2.value(x, z) * (1 - f);
		};
	}

	/**
	 * 标量场算子
	 */
	@FunctionalInterface
	public static interface Operator {
		/**
		 * @param x            采样x坐标
		 * @param z            采样y坐标
		 * @param lastStepCalc 上一步的计算函数
		 * @param thisStepTerm 此项的函数
		 * @return
		 */
		public abstract double calculate(double x, double z, ScalarField lastStepCalc, ScalarField thisStepTerm);

		/**
		 * 作为HeightMap起始赋值
		 */
		public static final Operator ASSIGN = (double x, double z, ScalarField lastStepCalc, ScalarField thisStepTerm) -> thisStepTerm.value(x, z);
		public static final Operator DISCARD = (double x, double z, ScalarField lastStepCalc, ScalarField thisStepTerm) -> lastStepCalc.value(x, z);

		public static final Operator ADD = (double x, double z, ScalarField lastStepCalc, ScalarField thisStepTerm) -> lastStepCalc.value(x, z) + thisStepTerm.value(x, z);
		public static final Operator SUB = (double x, double z, ScalarField lastStepCalc, ScalarField thisStepTerm) -> lastStepCalc.value(x, z) - thisStepTerm.value(x, z);
		public static final Operator MUL = (double x, double z, ScalarField lastStepCalc, ScalarField thisStepTerm) -> lastStepCalc.value(x, z) * thisStepTerm.value(x, z);
		public static final Operator DIV = (double x, double z, ScalarField lastStepCalc, ScalarField thisStepTerm) -> lastStepCalc.value(x, z) / thisStepTerm.value(x, z);

		public static final Operator MAX = (double x, double z, ScalarField lastStepCalc, ScalarField thisStepTerm) -> Math.max(lastStepCalc.value(x, z), thisStepTerm.value(x, z));
		public static final Operator MIN = (double x, double z, ScalarField lastStepCalc, ScalarField thisStepTerm) -> Math.min(lastStepCalc.value(x, z), thisStepTerm.value(x, z));

		public static final Operator ABS = (double x, double z, ScalarField lastStepCalc, ScalarField thisStepTerm) -> Math.abs(lastStepCalc.value(x, z));

		public static Operator CONV(ConvolutionKernel kernel) {
			return (double x, double z, ScalarField lastStepCalc, ScalarField thisStepTerm) -> kernel.calculate(lastStepCalc, x, z);
		}

		public static Operator BLEND(ScalarField blendFactor) {
			return (double x, double z, ScalarField lastStepCalc, ScalarField thisStepTerm) -> ScalarField.blend(blendFactor, lastStepCalc, thisStepTerm).value(x, z);
		}

		public static Operator SLIDING_WINDOW(SlidingWindowOperation op) {
			return (double x, double z, ScalarField lastStepCalc, ScalarField thisStepTerm) -> op.calculate(lastStepCalc, x, z);
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
		public final Operator op;

		/**
		 * 高度采样器
		 */
		public final ScalarField term;

		private Entry(Operator op, ScalarField sampler) {
			this.op = op;
			this.term = sampler;
		}

		public static final Entry of(Operator op, ScalarField sampler) {
			return new Entry(op, sampler);
		}

		public static final Entry of(ScalarField sampler) {
			return of(Operator.ADD, sampler);
		}

		public static final Entry coefficient(ScalarField coefficient) {
			return of(Operator.MUL, coefficient);
		}

		public static final Entry coefficient(double coefficient) {
			return coefficient(ScalarField.constant(coefficient));
		}

		public static final Entry of(Operator op) {
			return new Entry(op, null);
		}

		/**
		 * 将一个函数和Entry打包为一个新函数
		 * 
		 * @param lastStepCalc
		 * @param thisTerm
		 * @return
		 */
		public static final ScalarField wrap(ScalarField lastStepWrappedCalc, Entry thisTerm) {
			return (double x, double z) -> thisTerm.calculate(x, z, lastStepWrappedCalc);
		}

		public final double calculate(double x, double z, ScalarField lastStepWrappedCalc) {
			return this.op.calculate(x, z, lastStepWrappedCalc, this.term);
		}
	}
}