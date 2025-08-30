package lepus.math.algebra;

/**
 * 单目算子，只作用在前一个操作数上
 */
@FunctionalInterface
public interface UnaryOperator<R, O> extends Operator<R, O, O> {
	public abstract R calculate(O operand);

	/**
	 * 后面变长参数无效
	 */
	@SuppressWarnings("unchecked")
	public default R calculate(O operand1, O... none) {
		return calculate(operand1);
	}
}