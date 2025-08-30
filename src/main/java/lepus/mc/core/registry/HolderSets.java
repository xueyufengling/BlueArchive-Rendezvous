package lepus.mc.core.registry;

import java.lang.invoke.MethodHandle;
import java.util.ArrayList;
import java.util.List;

import lepus.mc.resources.ResourceKeys;
import lepus.mc.resources.TagKeys;
import lyra.lang.Handles;
import lyra.object.ObjectManipulator;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.HolderSet;
import net.minecraft.core.HolderSet.Named;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

/**
 * Holder集合，即注册条目集合
 */
public class HolderSets {
	/**
	 * 空HolderSet
	 */
	@SuppressWarnings("rawtypes")
	public static final HolderSet.Direct EMPTY = HolderSet.direct();

	/**
	 * 创建不具名的HolderSet
	 * 
	 * @param <T>
	 * @param registry
	 * @param elements
	 * @return
	 */
	public static final <T> HolderSet.Direct<T> build(Registry<T> registry, List<String> elements) {
		ArrayList<Holder<T>> holders = new ArrayList<>();
		for (String id : elements)
			holders.add(registry.getHolderOrThrow(ResourceKeys.build(registry.key(), id)));
		return HolderSet.direct(holders);
	}

	public static final <T> HolderSet.Direct<T> build(Registry<T> registry, String... elements) {
		ArrayList<Holder<T>> holders = new ArrayList<>();
		for (String id : elements)
			holders.add(registry.getHolderOrThrow(ResourceKeys.build(registry.key(), id)));
		return HolderSet.direct(holders);
	}

	/**
	 * 数据注册阶段使用
	 * 
	 * @param <T>
	 * @param registry
	 * @param elements
	 * @return
	 */
	public static final <T> HolderSet.Direct<T> build(BootstrapContext<?> context, ResourceKey<? extends Registry<T>> registryKey, List<String> elements) {
		HolderGetter<T> holderGetter = context.lookup(registryKey);
		ArrayList<Holder<T>> holders = new ArrayList<>();
		for (String id : elements)
			holders.add(holderGetter.getOrThrow(ResourceKeys.build(registryKey, id)));
		return HolderSet.direct(holders);
	}

	public static final <T> HolderSet.Direct<T> build(BootstrapContext<?> context, ResourceKey<? extends Registry<T>> registryKey, String... elements) {
		HolderGetter<T> holderGetter = context.lookup(registryKey);
		ArrayList<Holder<T>> holders = new ArrayList<>();
		for (String id : elements)
			holders.add(holderGetter.getOrThrow(ResourceKeys.build(registryKey, id)));
		return HolderSet.direct(holders);
	}

	public static final <T> HolderSet.Direct<T> build(ResourceKey<? extends Registry<T>> registryKey, List<String> elements) {
		return build(MappedRegistryAccess.getServerRegistry(registryKey), elements);
	}

	public static final <T> HolderSet.Direct<T> build(ResourceKey<? extends Registry<T>> registryKey, String... elements) {
		return build(MappedRegistryAccess.getServerRegistry(registryKey), elements);
	}

	private static final MethodHandle HolderSet$Named;

	static {
		HolderSet$Named = Handles.findConstructor(HolderSet.Named.class, HolderOwner.class, TagKey.class);
	}

	/**
	 * 新建一个HolderSet.Named对象
	 * 
	 * @param <T>
	 * @param owner
	 * @param key
	 * @return
	 */
	public static final <T> HolderSet.Named<T> newHolderSet$Named(HolderOwner<T> owner, TagKey<T> key) {
		try {
			return (HolderSet.Named<T>) HolderSet$Named.invokeExact(owner, key);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	public static final <T> HolderSet.Named<T> newHolderSet$Named(HolderOwner<T> owner, ResourceKey<T> key) {
		return newHolderSet$Named(owner, TagKeys.build(key));
	}

	public static final <T> HolderSet.Named<T> newHolderSet$Named(HolderOwner<T> owner, ResourceKey<? extends Registry<T>> registryKey, ResourceLocation loc) {
		return newHolderSet$Named(owner, TagKeys.build(registryKey, loc));
	}

	public static final <T> HolderSet.Named<T> newHolderSet$Named(HolderOwner<T> owner, ResourceKey<? extends Registry<T>> registryKey, String loc) {
		return newHolderSet$Named(owner, TagKeys.build(registryKey, loc));
	}

	/**
	 * 从注册表获取或不存在时创建HolderSet，并且将tag关联到该注册表
	 * 
	 * @param <T>
	 * @param registry
	 * @param key
	 * @return
	 */
	public static final <T> HolderSet.Named<T> getOrCreate(Registry<T> registry, ResourceKey<T> key) {
		return registry.getOrCreateTag(TagKeys.build(key));
	}

	public static final <T> HolderSet.Named<T> getOrCreate(Registry<T> registry, TagKey<T> key) {
		return registry.getOrCreateTag(key);
	}

	public static final <T> HolderSet.Named<T> getOrCreate(Registry<T> registry, String key) {
		return getOrCreate(registry, registry.key(), key);
	}

	public static final <T> HolderSet.Named<T> getOrCreate(Registry<T> registry, ResourceKey<? extends Registry<T>> registryKey, ResourceLocation loc) {
		return getOrCreate(registry, TagKeys.build(registryKey, loc));
	}

	public static final <T> HolderSet.Named<T> getOrCreate(Registry<T> registry, ResourceKey<? extends Registry<T>> registryKey, String loc) {
		return getOrCreate(registry, TagKeys.build(registryKey, loc));
	}

	/**
	 * 从Bootstrap中获取HolderSet.Named
	 * 
	 * @param <T>
	 * @param context
	 * @param registryKey
	 * @param key
	 * @return
	 */
	public static final <T> Named<T> get(BootstrapContext<?> context, ResourceKey<? extends Registry<T>> registryKey, TagKey<T> key) {
		return context.lookup(registryKey).getOrThrow(key);
	}

	public static final <T> Named<T> get(BootstrapContext<?> context, ResourceKey<? extends Registry<T>> registryKey, ResourceKey<T> key) {
		return get(context, registryKey, TagKeys.build(registryKey, key));
	}

	public static final <T> Named<T> get(BootstrapContext<?> context, ResourceKey<? extends Registry<T>> registryKey, ResourceLocation loc) {
		return get(context, registryKey, TagKeys.build(registryKey, loc));
	}

	public static final <T> Named<T> get(BootstrapContext<?> context, ResourceKey<? extends Registry<T>> registryKey, String key) {
		return get(context, registryKey, TagKeys.build(registryKey, key));
	}

	/**
	 * 根据ResourceKey查找指定的注册表并获取或创建指定的HolderSet
	 * 
	 * @param <T>
	 * @param registryKey
	 * @param key
	 * @return
	 */
	public static final <T> HolderSet.Named<T> getOrCreate(ResourceKey<? extends Registry<T>> registryKey, ResourceKey<T> key) {
		return getOrCreate(MappedRegistryAccess.getServerRegistry(registryKey), key);
	}

	public static final <T> HolderSet.Named<T> getOrCreate(ResourceKey<? extends Registry<T>> registryKey, TagKey<T> key) {
		return getOrCreate(MappedRegistryAccess.getServerRegistry(registryKey), key);
	}

	public static final <T> HolderSet.Named<T> getOrCreate(ResourceKey<? extends Registry<T>> registryKey, String key) {
		return getOrCreate(MappedRegistryAccess.getServerRegistry(registryKey), key);
	}

	public static final <T> HolderSet.Named<T> getOrCreate(ResourceKey<? extends Registry<T>> registryKey, ResourceLocation loc) {
		return getOrCreate(MappedRegistryAccess.getServerRegistry(registryKey), registryKey, loc);
	}

	/**
	 * 设置HolderSet的contents字段，直接替换不会触发tag rebind事件
	 * 
	 * @param <T>
	 * @param holderSet
	 * @param contents
	 * @return
	 */
	public static final <T> HolderSet.Named<T> setContents(HolderSet.Named<T> holderSet, List<Holder<T>> contents) {
		ObjectManipulator.setDeclaredMemberObject(holderSet, "contents", contents);
		return holderSet;
	}

	@SuppressWarnings("unchecked")
	public static final <T> List<Holder<T>> getContents(HolderSet.Named<T> holderSet) {
		return (List<Holder<T>>) ObjectManipulator.access(holderSet, "contents");
	}

	private static final MethodHandle HolderSet$Named_bind;

	static {
		HolderSet$Named_bind = Handles.findMethodHandle(HolderSet.Named.class, "bind", List.class);
	}

	/**
	 * 修改HolderSet的集合元素，会触发tag rebind事件
	 * 
	 * @param <T>
	 * @param holderSet
	 * @param contents
	 * @return
	 */
	public static final <T> HolderSet.Named<T> bindContents(HolderSet.Named<T> holderSet, List<Holder<T>> contents) {
		try {
			HolderSet$Named_bind.invokeExact(holderSet, contents);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return holderSet;
	}

	/**
	 * 获取或创建name指定名称（tag）的HolderSet并绑定到registry<br>
	 * 如果目标HolderSet已存在，则替换其集合元素
	 * 
	 * @param <T>
	 * @param registry
	 * @param elements
	 * @return
	 */
	public static final <T> HolderSet.Named<T> set(String name, Registry<T> registry, List<String> elements) {
		ArrayList<Holder<T>> holders = new ArrayList<>();
		for (String id : elements)
			holders.add(registry.getHolderOrThrow(ResourceKeys.build(registry.key(), id)));
		return setContents(getOrCreate(registry, name), holders);
	}

	public static final <T> HolderSet.Named<T> set(String name, Registry<T> registry, String... elements) {
		ArrayList<Holder<T>> holders = new ArrayList<>();
		for (String id : elements)
			holders.add(registry.getHolderOrThrow(ResourceKeys.build(registry.key(), id)));
		return setContents(getOrCreate(registry, name), holders);
	}

	public static final <T> HolderSet.Named<T> set(String name, ResourceKey<? extends Registry<T>> registryKey, List<String> elements) {
		return set(name, MappedRegistryAccess.getServerRegistry(registryKey), elements);
	}

	public static final <T> HolderSet.Named<T> set(String name, ResourceKey<? extends Registry<T>> registryKey, String... elements) {
		return set(name, MappedRegistryAccess.getServerRegistry(registryKey), elements);
	}

	/**
	 * 获取或创建name指定名称（tag）的HolderSet并绑定到registry<br>
	 * 如果目标HolderSet已存在，则忽略elements；若不存在则新建并用elements作为元素初始化<br>
	 * 
	 * @param <T>
	 * @param name
	 * @param registry
	 * @param elements
	 * @return
	 */
	public static final <T> HolderSet.Named<T> createIfAbsent(String name, Registry<T> registry, List<String> elements) {
		HolderSet.Named<T> holderSet = getOrCreate(registry, name);
		List<Holder<T>> existHolders = getContents(holderSet);
		if (existHolders.size() > 0)
			return holderSet;
		else {
			ArrayList<Holder<T>> holders = new ArrayList<>();
			for (String id : elements)
				holders.add(registry.getHolderOrThrow(ResourceKeys.build(registry.key(), id)));
			return setContents(holderSet, holders);
		}
	}

	public static final <T> HolderSet.Named<T> createIfAbsent(String name, Registry<T> registry, String... elements) {
		HolderSet.Named<T> holderSet = getOrCreate(registry, name);
		List<Holder<T>> existHolders = getContents(holderSet);
		if (existHolders.size() > 0)
			return holderSet;
		else {
			ArrayList<Holder<T>> holders = new ArrayList<>();
			for (String id : elements)
				holders.add(registry.getHolderOrThrow(ResourceKeys.build(registry.key(), id)));
			return setContents(holderSet, holders);
		}
	}

	public static final <T> HolderSet.Named<T> createIfAbsent(String name, ResourceKey<? extends Registry<T>> registryKey, List<String> elements) {
		return createIfAbsent(name, MappedRegistryAccess.getServerRegistry(registryKey), elements);
	}

	public static final <T> HolderSet.Named<T> createIfAbsent(String name, ResourceKey<? extends Registry<T>> registryKey, String... elements) {
		return createIfAbsent(name, MappedRegistryAccess.getServerRegistry(registryKey), elements);
	}

	/**
	 * 获取或创建绑定到registry的name指定名称（tag）的HolderSet，并为其添加新的集合元素
	 * 
	 * @param <T>
	 * @param name
	 * @param registry
	 * @param elements
	 * @return
	 */
	public static final <T> HolderSet.Named<T> add(String name, Registry<T> registry, List<String> elements) {
		HolderSet.Named<T> holderSet = getOrCreate(registry, name);
		ArrayList<Holder<T>> holders = new ArrayList<>();
		for (String id : elements)
			holders.add(registry.getHolderOrThrow(ResourceKeys.build(registry.key(), id)));
		holders.addAll(getContents(holderSet));
		return setContents(holderSet, holders);
	}

	public static final <T> HolderSet.Named<T> add(String name, Registry<T> registry, String... elements) {
		HolderSet.Named<T> holderSet = getOrCreate(registry, name);
		ArrayList<Holder<T>> holders = new ArrayList<>();
		for (String id : elements)
			holders.add(registry.getHolderOrThrow(ResourceKeys.build(registry.key(), id)));
		holders.addAll(getContents(holderSet));
		return setContents(holderSet, holders);
	}

	public static final <T> HolderSet.Named<T> add(String name, ResourceKey<? extends Registry<T>> registryKey, List<String> elements) {
		return add(name, MappedRegistryAccess.getServerRegistry(registryKey), elements);
	}

	public static final <T> HolderSet.Named<T> add(String name, ResourceKey<? extends Registry<T>> registryKey, String... elements) {
		return add(name, MappedRegistryAccess.getServerRegistry(registryKey), elements);
	}
}