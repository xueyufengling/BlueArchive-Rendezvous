package lepus.mc.datagen;

import java.util.ArrayList;

import lepus.mc.core.registry.MappedRegistryAccess;
import lepus.mc.core.registry.RegistryFactory;
import lepus.mc.core.registry.RegistryMap;
import lepus.mc.resources.ResourceKeys;
import lepus.mc.resources.ResourceLocations;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

/**
 * 数据包注册条目，可在数据生成时注册并生成json文件，或服务器启动时动态注册到服务器注册表
 * 
 * @param <T>
 */
public class EntryHolder<T> {
	private BootstrapContext<T> bootstrapContext;

	public final BootstrapContext<T> getBootstrapContext() {
		return bootstrapContext;
	}

	/**
	 * 该条目是否已经数据生成
	 * 
	 * @return
	 */
	public final boolean isDatagen() {
		return bootstrapContext != null;
	}

	/**
	 * 数据生成或服务器启动后注册，只能调用一次
	 */
	public final void register() {
		if (bootstrapContext != null)
			bootstrapContext.register(resourceKey, originalValue());
		else
			MappedRegistryAccess.getUnfrozenServerRegistry(registryKey).register(this.resourceKey, this.originalValue(), RegistrationInfo.BUILT_IN);
		RegistryMap.mutableNamespaces().add(this.namespace);// 将本条目的命名空间添加到统计集合
	}

	public final void registerDeferred() {
		RegistryFactory.deferredRegister(registryKey, namespace).register(path, () -> this.originalValue());
		RegistryMap.mutableNamespaces().add(this.namespace);// 将本条目的命名空间添加到统计集合
	}

	/**
	 * 延迟初始化的条目。<br>
	 * 数据生成阶段时registryAccess为null，服务器启动注册时BootstrapContext为null，两个只取一个。
	 * 
	 * @param <T>
	 */
	@FunctionalInterface
	public static interface BootstrapValue<T> {
		public T value(BootstrapContext<T> context, RegistryAccess registryAccess);
	}

	public final String namespace;
	public final String path;
	/**
	 * 该项对应的值，如果与BootstrapContext无关则可以显示赋值，此时就不需要再给valueSource赋值。<br>
	 * 该值为传入给DatagenHolder的原始值，通过value()方法获取的是注册表中的实时值，有可能被更改过。
	 */
	private T orininalValue;
	private final ResourceKey<T> resourceKey;

	public final ArrayList<TagKey<T>> tags = new ArrayList<>();
	/**
	 * 仅用于数据生成，需要BootstrapContext。如果value没有直接赋值，那么就在数据生成阶段采用该函数计算value的值。
	 */
	private final BootstrapValue<T> valueSource;
	private final ResourceKey<? extends Registry<T>> registryKey;

	private EntryHolder(ResourceKey<? extends Registry<T>> registryKey, String namespace, String path, BootstrapValue<T> valueSource, T value) {
		this.registryKey = registryKey;
		this.namespace = namespace;
		this.path = path;
		this.resourceKey = ResourceKeys.build(registryKey, namespace, path);
		this.valueSource = valueSource;
		this.orininalValue = value;
	}

	private EntryHolder(ResourceKey<? extends Registry<T>> registryKey, String namespacedId, BootstrapValue<T> valueSource, T value) {
		this.registryKey = registryKey;
		ResourceLocation loc = ResourceLocations.build(namespacedId);
		this.resourceKey = ResourceKeys.build(registryKey, loc);
		this.namespace = loc.getNamespace();
		this.path = loc.getPath();
		this.valueSource = valueSource;
		this.orininalValue = value;
	}

	/**
	 * 仅运行时可获取完整的注册表，runData期间的注册表是不完整的，缺失部分注册表。
	 * 
	 * @return
	 */
	public final Registry<T> registry() {
		return MappedRegistryAccess.getServerRegistry(registryKey);
	}

	public final ResourceKey<? extends Registry<T>> registryKey() {
		return this.registryKey;
	}

	public final HolderGetter<T> lookup() {
		return bootstrapContext.lookup(registryKey);
	}

	public final ResourceKey<T> getKey() {
		return this.resourceKey;
	}

	public final <R> ResourceKey<R> castKey(ResourceKey<? extends Registry<R>> registryKey) {
		return ResourceKeys.build(registryKey, namespace, path);
	}

	/**
	 * 获取该条目的注册原始值
	 * 
	 * @return
	 */
	public final T originalValue() {
		if (orininalValue == null)
			orininalValue = valueSource.value(bootstrapContext, MappedRegistryAccess.serverRegistryAccess);// BootstrapValue，只计算一次
		return orininalValue;
	}

	/**
	 * 数据值，数据生成开始及之后为bootstrap值。其他后时只能获取服务器注册表对应的实际条目值<br>
	 * 
	 * @return
	 */
	public final T value() {
		if (orininalValue == null)
			return originalValue();
		else
			return this.getHolder().value();
	}

	/**
	 * 在运行时提供Holder.Reference，可用于MappedRegistries的操作
	 * 
	 * @return
	 */
	public final Holder.Reference<T> getHolder() {
		if (this.isDatagen())
			this.lookup().getOrThrow(resourceKey);
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
	public static final <T> EntryHolder<T> of(ResourceKey<? extends Registry<T>> registryKey, String namespace, String path, T value) {
		return new EntryHolder<>(registryKey, namespace, path, null, value);
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
	public static final <T> EntryHolder<T> of(ResourceKey<? extends Registry<T>> registryKey, String namespace, String path, BootstrapValue<T> valueSource) {
		return new EntryHolder<>(registryKey, namespace, path, valueSource, null);
	}

	public static final <T> EntryHolder<T> of(ResourceKey<? extends Registry<T>> registryKey, String namespacedId, T value) {
		return new EntryHolder<>(registryKey, namespacedId, null, value);
	}

	public static final <T> EntryHolder<T> of(ResourceKey<? extends Registry<T>> registryKey, String namespacedId, BootstrapValue<T> valueSource) {
		return new EntryHolder<>(registryKey, namespacedId, valueSource, null);
	}

	@Override
	public String toString() {
		return "{key=" + resourceKey + ", value=" + orininalValue + ", isDatagen=" + this.isDatagen() + "}";
	}
}
