package lepus.mc.core.registry;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import lepus.mc.resources.ResourceKeys;
import lyra.alpha.reference.FieldReference;
import lyra.alpha.reference.Recoverable;
import lyra.object.ObjectManipulator;
import net.minecraft.core.Holder;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.RegistryLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.HolderSet.Named;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

/**
 * 可操作成员对象的MappedRegistry
 * 
 * @param <T>
 */
public class MutableMappedRegistry<T> implements Recoverable<MutableMappedRegistry<T>> {
	private final MappedRegistry<T> mappedRegistry;
	public ResourceKey<? extends Registry<T>> key;
	public ObjectList<Holder.Reference<T>> byId;
	public Reference2IntMap<T> toId;
	public Map<ResourceLocation, Holder.Reference<T>> byLocation;
	public Map<ResourceKey<T>, Holder.Reference<T>> byKey;
	public Map<T, Holder.Reference<T>> byValue;
	public Map<ResourceKey<T>, RegistrationInfo> registrationInfos;
	public Map<TagKey<T>, HolderSet.Named<T>> tags;
	public Map<T, Holder.Reference<T>> unregisteredIntrusiveHolders;
	public final HolderLookup.RegistryLookup<T> lookup;
	public final Object tagAdditionLock;

	@SuppressWarnings("unchecked")
	private MutableMappedRegistry(MappedRegistry<T> registry) {
		this.mappedRegistry = registry;
		key = (ResourceKey<? extends Registry<T>>) ObjectManipulator.access(registry, "key");
		byId = (ObjectList<Holder.Reference<T>>) ObjectManipulator.access(registry, "byId");
		toId = (Reference2IntMap<T>) ObjectManipulator.access(registry, "toId");
		byLocation = (Map<ResourceLocation, Holder.Reference<T>>) ObjectManipulator.access(registry, "byLocation");
		byKey = (Map<ResourceKey<T>, Holder.Reference<T>>) ObjectManipulator.access(registry, "byKey");
		byValue = (Map<T, Holder.Reference<T>>) ObjectManipulator.access(registry, "byValue");
		registrationInfos = (Map<ResourceKey<T>, RegistrationInfo>) ObjectManipulator.access(registry, "registrationInfos");
		tags = (Map<TagKey<T>, Named<T>>) ObjectManipulator.access(registry, "tags");
		unregisteredIntrusiveHolders = (Map<T, Holder.Reference<T>>) ObjectManipulator.access(registry, "unregisteredIntrusiveHolders");
		lookup = (RegistryLookup<T>) ObjectManipulator.access(registry, "lookup");
		tagAdditionLock = ObjectManipulator.access(registry, "tagAdditionLock");
		this.asPrimary();
	}

	/**
	 * 获取可操作的注册表对象
	 * 
	 * @param registry
	 * @return
	 */
	public static final <T> MutableMappedRegistry<T> from(MappedRegistry<T> registry) {
		if (registry == null)
			return null;
		return new MutableMappedRegistry<T>(registry);
	}

	/**
	 * 修改前的原始副本
	 */
	private ResourceKey<? extends Registry<T>> pkey;
	private ObjectArrayList<Holder.Reference<T>> pbyId;
	private Reference2IntOpenHashMap<T> ptoId;
	private HashMap<ResourceLocation, Holder.Reference<T>> pbyLocation;
	private HashMap<ResourceKey<T>, Holder.Reference<T>> pbyKey;
	private IdentityHashMap<T, Holder.Reference<T>> pbyValue;
	private IdentityHashMap<ResourceKey<T>, RegistrationInfo> pregistrationInfos;
	private IdentityHashMap<TagKey<T>, HolderSet.Named<T>> ptags;
	private IdentityHashMap<T, Holder.Reference<T>> punregisteredIntrusiveHolders;

	@Override
	public final MutableMappedRegistry<T> redirect() {
		ObjectManipulator.setObject(mappedRegistry, "key", key);
		ObjectManipulator.setObject(mappedRegistry, "byId", byId);
		ObjectManipulator.setObject(mappedRegistry, "toId", toId);
		ObjectManipulator.setObject(mappedRegistry, "byLocation", byLocation);
		ObjectManipulator.setObject(mappedRegistry, "byKey", byKey);
		ObjectManipulator.setObject(mappedRegistry, "byValue", byValue);
		ObjectManipulator.setObject(mappedRegistry, "registrationInfos", registrationInfos);
		ObjectManipulator.setObject(mappedRegistry, "tags", tags);
		ObjectManipulator.setObject(mappedRegistry, "unregisteredIntrusiveHolders", unregisteredIntrusiveHolders);
		return this;
	}

	@Override
	public MutableMappedRegistry<T> recovery() {
		ObjectManipulator.setObject(mappedRegistry, "key", pkey);
		ObjectManipulator.setObject(mappedRegistry, "byId", pbyId);
		ObjectManipulator.setObject(mappedRegistry, "toId", ptoId);
		ObjectManipulator.setObject(mappedRegistry, "byLocation", pbyLocation);
		ObjectManipulator.setObject(mappedRegistry, "byKey", pbyKey);
		ObjectManipulator.setObject(mappedRegistry, "byValue", pbyValue);
		ObjectManipulator.setObject(mappedRegistry, "registrationInfos", pregistrationInfos);
		ObjectManipulator.setObject(mappedRegistry, "tags", ptags);
		ObjectManipulator.setObject(mappedRegistry, "unregisteredIntrusiveHolders", punregisteredIntrusiveHolders);
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MutableMappedRegistry<T> asPrimary() {
		pkey = key;
		pbyId = ((ObjectArrayList<Reference<T>>) byId).clone();
		ptoId = ((Reference2IntOpenHashMap<T>) toId).clone();
		pbyLocation = (HashMap<ResourceLocation, Reference<T>>) ((HashMap<ResourceLocation, Reference<T>>) byLocation).clone();
		pbyKey = (HashMap<ResourceKey<T>, Reference<T>>) ((HashMap<ResourceKey<T>, Reference<T>>) byKey).clone();
		pbyValue = (IdentityHashMap<T, Reference<T>>) ((IdentityHashMap<T, Reference<T>>) byValue).clone();
		pregistrationInfos = (IdentityHashMap<ResourceKey<T>, RegistrationInfo>) ((IdentityHashMap<ResourceKey<T>, RegistrationInfo>) registrationInfos).clone();
		ptags = (IdentityHashMap<TagKey<T>, Named<T>>) ((IdentityHashMap<TagKey<T>, Named<T>>) tags).clone();
		punregisteredIntrusiveHolders = (IdentityHashMap<T, Reference<T>>) ((IdentityHashMap<T, Reference<T>>) unregisteredIntrusiveHolders).clone();
		return this;
	}

	/**
	 * 安全地移除指定元素的ID相关Map和List并进行重排序。
	 * 
	 * @param value 要移除的元素
	 * @return 移除的元素ID
	 */
	@SuppressWarnings("deprecation")
	private int removeId(T value) {
		int id = mappedRegistry.getId(value);
		int restLength = byId.size() - id - 1;// 该id元素后的剩余元素总长度
		Object[] restValues = new Object[restLength];
		byId.remove(id);// 必须remove(id)，如果设置为null不移除，那么打包数据注册表的时候将引发空指针异常。但是该id后的所有元素的id（实际上就是索引）会前移
		byId.getElements(id, restValues, 0, restLength);
		toId.remove(value);
		for (int restIdx = 0; restIdx < restLength; ++restIdx) {
			int newId = id + restIdx;
			toId.put(byId.get(newId).value(), newId);
		}
		return id;
	}

	/**
	 * 进入世界时会验证注册表完整性，如果要修改原版注册表，需要在退出世界时将注册表恢复至完整的初始状态
	 */
	public MutableMappedRegistry<T> unregister(Holder.Reference<T> holder) {
		if (holder == null)
			return this;
		T value = holder.value();
		byValue.remove(value);
		removeId(value);
		ResourceKey<T> resource_key = holder.getKey();
		byKey.remove(resource_key);
		ResourceLocation resloc = resource_key.location();
		byLocation.remove(resloc);
		return this;
	}

	public Holder.Reference<T> unregister(int id) {
		Holder.Reference<T> holder = byId.get(id);
		this.unregister(holder);
		return holder;
	}

	public Holder.Reference<T> unregister(ResourceKey<T> resource_key) {
		Holder.Reference<T> holder = byKey.get(resource_key);
		this.unregister(holder);
		return holder;
	}

	public Holder.Reference<T> unregister(ResourceLocation resloc) {
		Holder.Reference<T> holder = byLocation.get(resloc);
		this.unregister(holder);
		return holder;
	}

	public Holder.Reference<T> unregister(T value) {
		Holder.Reference<T> holder = byValue.get(value);
		this.unregister(holder);
		return holder;
	}

	/**
	 * 修改注册表的值，如果要修改原版注册表，需要在退出世界时将注册表恢复至完整的初始状态
	 * 
	 * @param target    要被替换的目标Holder
	 * @param new_value 方法内部有null检查，因为该值不能为null否则引发空指针异常
	 * @return
	 */
	public MutableMappedRegistry<T> modify(Holder.Reference<T> target, Holder.Reference<T> new_value) {
		if (target == null || new_value == null)
			return this;
		T value = target.value();
		byValue.put(value, new_value);
		int id = mappedRegistry.getId(value);
		byId.set(id, new_value);
		toId.put(new_value.value(), id);
		ResourceKey<T> resource_key = target.getKey();
		byKey.put(resource_key, new_value);
		ResourceLocation resloc = resource_key.location();
		byLocation.put(resloc, new_value);
		return this;
	}

	public Holder.Reference<T> modify(int target, Holder.Reference<T> new_value) {
		Holder.Reference<T> holder = byId.get(target);
		this.modify(holder, new_value);
		return holder;
	}

	public Holder.Reference<T> modify(ResourceKey<T> target, Holder.Reference<T> new_value) {
		Holder.Reference<T> holder = byKey.get(target);
		this.modify(holder, new_value);
		return holder;
	}

	public Holder.Reference<T> modify(ResourceLocation target, Holder.Reference<T> new_value) {
		Holder.Reference<T> holder = byLocation.get(target);
		this.modify(holder, new_value);
		return holder;
	}

	public Holder.Reference<T> modify(T target, Holder.Reference<T> new_value) {
		Holder.Reference<T> holder = byValue.get(target);
		this.modify(holder, new_value);
		return holder;
	}

	public final MappedRegistry<T> toMappedRegistry() {
		return mappedRegistry;
	}

	/**
	 * 从注册表中删除一项并将其static final引用设置为redirectRef<br>
	 * 例如删除地狱就是deleteEntry(BuiltinDimensionTypes.NETHER, Level.class, "NETHER", null);
	 * 
	 * @param resource_key
	 * @param redirectRefs
	 * @return
	 */
	@Deprecated
	public Holder.Reference<T> unregister(ResourceKey<T> resource_key, FieldReference... redirectRefs) {
		Holder.Reference<T> holder = unregister(resource_key);
		for (FieldReference ref : redirectRefs)
			ref.redirect();
		return holder;
	}

	@Deprecated
	public Holder.Reference<T> modify(ResourceKey<T> resource_key, Holder.Reference<T> new_value, FieldReference... redirectRefs) {
		Holder.Reference<T> holder = modify(resource_key, new_value);
		for (FieldReference ref : redirectRefs)
			ref.redirect();
		return holder;
	}

	public Holder.Reference<T> register(ResourceKey<T> resource_key, T value, RegistrationInfo info) {
		return mappedRegistry.register(resource_key, value, info);
	}

	public Holder.Reference<T> register(ResourceKey<T> resource_key, T value) {
		return mappedRegistry.register(resource_key, value, RegistrationInfo.BUILT_IN);
	}

	public Holder.Reference<T> register(String reslocWithNamespace, T value) {
		return mappedRegistry.register(ResourceKeys.build(pkey, reslocWithNamespace), value, RegistrationInfo.BUILT_IN);
	}
}
