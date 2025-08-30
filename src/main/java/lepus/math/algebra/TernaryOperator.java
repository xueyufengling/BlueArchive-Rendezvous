package lepus.math.algebra;

/**
 * 三目算子
 */
@FunctionalInterface
public interface TernaryOperator<R, O1, O2> extends Operator<R, O1, O2> {
	public abstract R calculate(O1 operand1, O2 operand2, O2 operand3);

	/**
	 * 只取后面变长参数的第一、二个参数作为操作数
	 */
	@SuppressWarnings("unchecked")
	public default R calculate(O1 operand1, O2... operand2) {
		return calculate(operand1, operand2[0], operand2[1]);
	}
}