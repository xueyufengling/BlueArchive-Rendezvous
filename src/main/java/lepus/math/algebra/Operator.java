package lepus.math.algebra;

/**
 * 算子
 * 
 * @param <R>  运算符返回类型
 * @param <O1> 运算符第一个操作数类型
 * @param <O2> 运算符后续操作数类型
 */
@FunctionalInterface
public interface Operator<R, O1, O2> {
	/**
	 * @param operand1 上一步的计算结果操作数
	 * @param operands 此项Term的操作数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public abstract R calculate(O1 operand1, O2... operands);

	/**
	 * 最终值只取右操作数
	 */
	@SuppressWarnings("rawtypes")
	public static final BinaryOperator ASSIGN = (Object obj1, Object obj2) -> obj2;

	/**
	 * 舍弃全部右操作数
	 */
	@SuppressWarnings("rawtypes")
	public static final Operator DISCARD = (Object obj, Object... objs) -> obj;
}