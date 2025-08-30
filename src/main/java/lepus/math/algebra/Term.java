package lepus.math.algebra;

import java.util.List;

/**
 * 代数项<br>
 * 绑定右操作数，不绑定左操作数<br>
 * 有缓存值，不可复用，使用resolve()来创建使用副本的计算表达式并使用value()获取表达式值
 * 
 * @param <O>
 */
public class Term<V, O> implements Cloneable {
	/**
	 * 拷贝项如果原本具有左操作数，则丢弃缓存值cached_value
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Term<V, O> clone() {
		try {
			Term<V, O> term = (Term<V, O>) super.clone();
			if (left_operand != null)
				term.cached_value = null;
			return term;
		} catch (CloneNotSupportedException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private V cached_value;

	/**
	 * 左操作数
	 */
	private Term<?, ?> left_operand;

	/**
	 * 该项要执行的运算操作算子
	 */
	private Operator<V, ?, O> op;

	/**
	 * 待操作的操作数
	 */
	private O[] operands;

	private Term(V instant_value) {
		this.cached_value = instant_value;
	}

	private Term(Operator<V, ?, O> op) {
		this.op = op;
	}

	@SuppressWarnings("unchecked")
	private Term(Operator<V, ?, O> op, O... operands) {
		this.op = op;
		this.operands = operands;
	}

	/**
	 * 绑定左操作数
	 * 
	 * @param left_operand
	 * @return
	 */
	protected Term<V, O> operateOn(Term<?, ?> left_operand) {
		this.left_operand = left_operand;
		return this;
	}

	/**
	 * 对项本应用算子<br>
	 * 仅resolve()方法返回的对象可以调用此方法，因为resolve()中绑定了左操作数。
	 * 
	 * @param lastStepWrappedCalc 上一步计算的结果，作为操作数1,本类对象的operand作为操作数2
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public final V value() {
		return cached_value == null ? (cached_value = (V) ((Operator) this.op).calculate(left_operand == null ? null : left_operand.value(), this.operands)) : cached_value;
	}

	/**
	 * 立即值
	 * 
	 * @param <V>
	 * @param instant_value
	 * @return
	 */
	public static final <V> Term<V, V> of(V instant_value) {
		return new Term<>(instant_value);
	}

	@SuppressWarnings("unchecked")
	public static final <V, O> Term<V, O> of(Operator<V, ?, O> op, O... operands) {
		return new Term<>(op, operands);
	}

	public static final <V, O> Term<V, O> of(UnaryOperator<V, O> op) {
		return new Term<>(op);
	}

	/**
	 * 第一项必须是立即值，因为它没有左操作数
	 * 
	 * @param <V>
	 * @param first
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static final <V> Term<V, ?> startTerm(Term<V, ?> first) {
		return new Term<>(Operator.ASSIGN, first.clone().value());// 第一项没有左操作数
	}

	/**
	 * 将一系列项的副本绑定为一个计算表达式
	 * 
	 * @param <V>
	 * @param terms
	 * @return
	 */
	public static final <V> Term<V, ?> resolve(List<Term<V, ?>> terms) {
		Term<V, ?> start = startTerm(terms.get(0));
		Term<V, ?> prev = start;
		for (int idx = 1; idx < terms.size(); ++idx) {
			Term<V, ?> next = terms.get(idx).clone();
			next.operateOn(prev);
			prev = next;
		}
		return prev;
	}
}
