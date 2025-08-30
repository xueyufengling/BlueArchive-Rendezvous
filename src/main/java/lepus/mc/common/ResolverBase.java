package lepus.mc.common;

public interface ResolverBase<R, V> {
	/**
	 * 传入R决议器并得到结果
	 * 
	 * @param resolver
	 * @param orig
	 * @param args
	 * @return
	 */
	abstract V execute(R resolver, V orig, Object... args);

	public V resolve(V orig, Object... args);
}
