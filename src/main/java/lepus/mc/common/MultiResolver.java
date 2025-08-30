package lepus.mc.common;

import java.util.ArrayList;

/**
 * 多个Resolver决断出一个最终结果
 * 
 * @param <R>
 * @param <V>
 */
public abstract class MultiResolver<R, V> implements ResolverBase<R, V> {
	@FunctionalInterface
	public static interface Strategy {
		/**
		 * 如果返回的索引的resolver为null，则继续执行下一个索引
		 * 
		 * @param idx            当前索引，-1表示此表达式计算的是迭代的初始索引
		 * @param result         当前索引的计算结果值，索引-1时此值为传入null
		 * @param resolver_count resolver总数目
		 * @return 下一个要迭代的索引，小于-1或大于resolver_count则停止迭代
		 */
		public int nextIdx(int idx, Object result, int resolver_count);

		/**
		 * 只使用第一个非null的决议值
		 */
		public static final Strategy ONLY_FIRST = (int idx, Object result, int resolver_count) -> {
			if (idx == -1)
				return 0;
			else {
				if (result == null)
					return ++idx;
				else
					return -2;
			}
		};

		/**
		 * 只使用最后一个非null的决议值
		 */
		public static final Strategy ONLY_LAST = (int idx, Object result, int resolver_count) -> {
			if (idx == -1)
				return resolver_count - 1;
			else {
				if (result == null)
					return --idx;
				else
					return -2;
			}
		};

		/**
		 * 正向依次遍历
		 */
		public static final Strategy FORWARD_SEQUENCE = (int idx, Object result, int resolver_count) -> {
			if (idx == -1)
				return 0;
			else
				return --idx;
		};

		/**
		 * 反向依次遍历
		 */
		public static final Strategy BACKWARD_SEQUENCE = (int idx, Object result, int resolver_count) -> {
			if (idx == -1)
				return resolver_count - 1;
			else
				return --idx;
		};
	}

	private final ArrayList<R> resolvers = new ArrayList<>();

	/**
	 * 默认只有最后一个resolver生效
	 */
	public static final Strategy DEFAULT_STRATEGY = Strategy.ONLY_LAST;

	/**
	 * 迭代策略
	 */
	private Strategy strategy = DEFAULT_STRATEGY;

	public Strategy strategy() {
		return strategy;
	}

	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}

	public abstract V execute(R resolver, V orig, Object... args);

	/**
	 * 按照strategy策略遍历resolver并计算值
	 * 
	 * @param orig
	 * @param args
	 * @return
	 */
	@Override
	public final V resolve(V orig, Object... args) {
		int resolver_count = resolvers.size();
		V current_result = null;
		V result = orig;
		int idx = strategy.nextIdx(-1, null, resolver_count);// 获取第一个resolver的索引
		while (idx > -1 && idx < resolver_count) {
			R resolver = resolvers.get(idx);
			if (resolver != null) {
				current_result = execute(resolver, result, args);
			}
			idx = strategy.nextIdx(idx, current_result, resolver_count);
			if (current_result != null)
				result = current_result;
		}
		return result;
	}

	public final void addResolver(R resolver) {
		resolvers.add(resolver);
	}

	public final void removeResolver(R resolver) {
		resolvers.remove(resolver);
	}
}
