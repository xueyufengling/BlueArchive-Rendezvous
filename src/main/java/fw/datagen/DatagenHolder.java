package fw.datagen;

import fw.core.Core;
import fw.core.registry.MappedRegistries;
import fw.core.registry.RegistryFactory;
import fw.resources.ResourceKeyBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredHolder;

public class DatagenHolder<T> {
	@FunctionalInterface
	public static interface ValueSource<T> {
		public T value(BootstrapContext<T> context);
	}

	public final String namespace;
	public final String path;
	/**
	 * 该项对应的值，如果与BootstrapContext无关则可以显示赋值，此时就不需要再给valueSource赋值。
	 */
	private T value;
	public final ResourceKey<T> resourceKey;
	/**
	 * 仅用于数据生成，需要BootstrapContext。如果value没有直接赋值，那么就在数据生成阶段采用该函数计算value的值。
	 */
	private final ValueSource<T> valueSource;
	/**
	 * 不需要BootstrapContext的值，将直接注册到DeferredRegister
	 */
	public final DeferredHolder<T, T> deferredHolder;
	public final ResourceKey<? extends Registry<T>> registryKey;

	private DatagenHolder(ResourceKey<? extends Registry<T>> registryKey, String namespace, String path, ValueSource<T> valueSource, DeferredHolder<T, T> deferredHolder, T value) {
		this.registryKey = registryKey;
		this.namespace = namespace;
		this.path = path;
		this.valueSource = valueSource;
		this.resourceKey = ResourceKeyBuilder.build(registryKey, namespace, path);
		this.deferredHolder = deferredHolder;
		this.value = value;
	}

	/**
	 * 仅运行时可获取完整的注册表，runData期间的注册表是不完整的，缺失部分注册表。
	 * 
	 * @return
	 */
	public final Registry<T> registry() {
		return MappedRegistries.getRegistry(registryKey);
	}

	public final <C> ResourceKey<C> resourceKey(ResourceKey<? extends Registry<C>> registryKey) {
		return ResourceKeyBuilder.build(registryKey, namespace, path);
	}

	/**
	 * 数据生成、运行时获取注册值。运行时只能获取常量值
	 * 
	 * @param context
	 * @return
	 */
	public final T value(BootstrapContext<T> context) {
		if (value == null && context != null)
			value = valueSource.value(context);
		return value;
	}

	/**
	 * 获取常量值，不是常量值则返回null
	 * 
	 * @return
	 */
	public final T value() {
		return value;
	}

	/**
	 * 在运行时提供Holder.Reference，可用于MappedRegistries的操作
	 * 
	 * @return
	 */
	public final Holder.Reference<T> holderReference() {
		return registry().getHolderOrThrow(resourceKey);
	}

	/**
	 * 数据生成时注册到BootstrapContext的常量值。并且在运行时也会注册到MappedRegistry。
	 * 
	 * @param <T>
	 * @param registryKey
	 * @param namespace
	 * @param path
	 * @param value
	 * @return
	 */
	public static final <T> DatagenHolder<T> of(ResourceKey<? extends Registry<T>> registryKey, String namespace, String path, T value) {
		return new DatagenHolder<>(registryKey, namespace, path, null, null, value);
	}

	/**
	 * 仅数据生成时注册到BootstrapContext的值，可以通过HolderGetter动态生成依赖其他注册项的值，不需要是常量。在运行时不注册到MappedRegistry，也无法读取值。
	 * 
	 * @param <T>
	 * @param registryKey
	 * @param namespace
	 * @param path
	 * @param valueSource
	 * @return
	 */
	public static final <T> DatagenHolder<T> of(ResourceKey<? extends Registry<T>> registryKey, String namespace, String path, ValueSource<T> valueSource) {
		return new DatagenHolder<>(registryKey, namespace, path, valueSource, null, null);
	}

	public static final <T> DatagenHolder<T> of(ResourceKey<? extends Registry<T>> registryKey, String path, T value) {
		return of(registryKey, Core.ModId, path, value);
	}

	public static final <T> DatagenHolder<T> of(ResourceKey<? extends Registry<T>> registryKey, String path, ValueSource<T> valueSource) {
		return of(registryKey, Core.ModId, path, valueSource);
	}

	@Override
	public String toString() {
		return "{key=" + resourceKey + ", value=" + value + ", deferredHolder=" + deferredHolder + ", isBound=" + (deferredHolder == null ? false : deferredHolder.isBound()) + "}";
	}
}
