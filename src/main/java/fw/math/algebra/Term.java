package fw.math.algebra;

/**
 * 代数项
 * 
 * @param <O>
 */
public class Term<R, O> implements Cloneable {

	@Override
	@SuppressWarnings("unchecked")
	public O clone() {
		try {
			return (O) super.clone();
		} catch (CloneNotSupportedException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 该项要执行的运算操作算子
	 */
	public final Operator<R, ?, ?> op;

	/**
	 * 待操作的操作数
	 */
	public final O[] operands;

	@SuppressWarnings("unchecked")
	private Term(Operator<R, ?, ?> op, O... operands) {
		this.op = op;
		this.operands = operands;
	}

	/**
	 * 对项本应用算子
	 * 
	 * @param lastStepWrappedCalc 上一步计算的结果，作为操作数1,本类对象的operand作为操作数2
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public final R calculate(Object lastStepResult) {
		return (R) ((Operator) this.op).calculate(lastStepResult, this.operands);
	}

	@SuppressWarnings("unchecked")
	public static final <R, O> Term<R, O> of(Operator<R, ?, O> op, O... operands) {
		return new Term<>(op, operands);
	}
}
