package lepus.math.field;

import lepus.math.algebra.BinaryOperator;
import lepus.math.algebra.Operator;
import lepus.math.algebra.TernaryOperator;
import lepus.math.algebra.UnaryOperator;

/**
 * 二维标量场
 */
@FunctionalInterface
public interface ScalarField {
	/**
	 * 场的值
	 * 
	 * @param x
	 * @param z
	 * @return
	 */
	public double value(double x, double z);

	/**
	 * 无关xz坐标的常量场
	 * 
	 * @param c
	 * @return
	 */
	public static ScalarField constant(double c) {
		return (double x, double z) -> c;
	}

	/**
	 * 距离固定某个点的距离场
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
	 * 二维球形势场<br>
	 * 可用于势垒或势阱作为blend()的混合因子参数<br>
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

	@FunctionalInterface
	public interface Calculation {
		public double calculate(double v1, double... vs);
	}

	@FunctionalInterface
	public interface UnaryCalculation {
		public double calculate(double v);
	}

	@FunctionalInterface
	public interface BinaryCalculation {
		public double calculate(double v1, double v2);
	}

	@FunctionalInterface
	public interface TernaryCalculation {
		public double calculate(double v1, double v2, double v3);
	}

	/**
	 * 返回一个标量场算子
	 * 
	 * @param operation 两个参数为两个场的值
	 * @return 返回值为两个场值的运算结果
	 */
	public static Operator<ScalarField, ScalarField, ScalarField> operator(Calculation operation) {
		return (ScalarField field1, ScalarField... fields) -> (double x, double z) -> {
			double[] values = new double[fields.length];
			for (int idx = 0; idx < fields.length; ++idx)
				values[idx] = fields[idx].value(x, z);
			return operation.calculate(field1.value(x, z), values);
		};
	}

	public static UnaryOperator<ScalarField, ScalarField> unary_operator(UnaryCalculation operation) {
		return (ScalarField field) -> (double x, double z) -> operation.calculate(field.value(x, z));
	}

	public static BinaryOperator<ScalarField, ScalarField, ScalarField> binary_operator(BinaryCalculation operation) {
		return (ScalarField field1, ScalarField field2) -> (double x, double z) -> operation.calculate(field1.value(x, z), field2.value(x, z));
	}

	public static TernaryOperator<ScalarField, ScalarField, ScalarField> ternary_operator(TernaryCalculation operation) {
		return (ScalarField field1, ScalarField field2, ScalarField field3) -> (double x, double z) -> operation.calculate(field1.value(x, z), field2.value(x, z), field3.value(x, z));
	}

	/**
	 * 标量场算子
	 */
	public static class Operators {
		public static final BinaryOperator<ScalarField, ScalarField, ScalarField> ADD = binary_operator((double v1, double v2) -> v1 + v2);
		public static final BinaryOperator<ScalarField, ScalarField, ScalarField> SUB = binary_operator((double v1, double v2) -> v1 - v2);
		public static final BinaryOperator<ScalarField, ScalarField, ScalarField> MUL = binary_operator((double v1, double v2) -> v1 * v2);
		public static final BinaryOperator<ScalarField, ScalarField, ScalarField> DIV = binary_operator((double v1, double v2) -> v1 / v2);

		public static final BinaryOperator<ScalarField, ScalarField, ScalarField> MAX = binary_operator((double v1, double v2) -> Math.max(v1, v2));
		public static final BinaryOperator<ScalarField, ScalarField, ScalarField> MIN = binary_operator((double v1, double v2) -> Math.min(v1, v2));

		public static final UnaryOperator<ScalarField, ScalarField> ABS = unary_operator((double v) -> Math.abs(v));

		/**
		 * 标量场混合
		 */
		public static final TernaryOperator<ScalarField, ScalarField, ScalarField> BLEND = ternary_operator((double fv1, double fv2, double blendfactor) -> fv1 * (1 - blendfactor) + fv2 * blendfactor);

		/**
		 * 获取指定坐标的场值
		 */
		public static final TernaryOperator<Double, ScalarField, Double> VALUE = (ScalarField field, Double x, Double z) -> field.value(x, z);
	}
}