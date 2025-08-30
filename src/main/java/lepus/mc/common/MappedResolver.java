package lepus.mc.common;

import java.util.HashMap;

public abstract class MappedResolver<K, R, V> implements ResolverBase<R, V> {
	private final HashMap<K, R> resolversMap = new HashMap<>();

	@Override
	public final V resolve(V orig, Object... args) {
		R resolver = resolversMap.get(key(orig, args));
		if (resolver == null)
			return null;
		else
			return execute(resolver, orig, args);
	}

	public abstract K key(V orig, Object... args);

	public final MappedResolver<K, R, V> addEntry(K key, R resolver) {
		resolversMap.put(key, resolver);
		return this;
	}
}
