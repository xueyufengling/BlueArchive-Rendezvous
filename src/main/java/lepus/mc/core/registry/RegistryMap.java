package lepus.mc.core.registry;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import lepus.mc.items.ExtCreativeTab;
import lepus.mc.items.ExtItem;
import lepus.mc.resources.ResourceLocations;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * DeferredRegister的包装，包含记录功能，应当总是使用该类注册，而非DeferredRegister。
 * 
 * @param <R>
 */
public class RegistryMap<R> {
	/**
	 * 本mod包含的全部命名空间（通常为ModId）
	 */
	private static final HashSet<String> namespaces = new HashSet<>();

	public static final Set<String> namespaces() {
		return Collections.unmodifiableSet(namespaces);
	}

	public static final HashSet<String> mutableNamespaces() {
		return namespaces;
	}

	protected final ResourceKey<? extends Registry<R>> registryKey;
	protected final HashMap<String, DeferredHolder<R, ? extends R>> entriesMap = new HashMap<>();
	protected final HashMap<String, DeferredRegister<R>> deferredRegisters = new HashMap<>();

	private RegistryMap(ResourceKey<? extends Registry<R>> registry_key) {
		this.registryKey = registry_key;
	}

	/**
	 * 保证每个注册类型只有单一RegistryMap<R>实例
	 */
	private static final HashMap<ResourceKey<? extends Registry<?>>, RegistryMap<?>> RegistryMaps = new HashMap<>();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static final <R> RegistryMap<R> of(ResourceKey<? extends Registry<R>> registry_key) {
		if (Registries.ITEM.equals(registry_key))
			return (RegistryMap<R>) RegistryMaps.computeIfAbsent(registry_key, (ResourceKey<? extends Registry<?>> key) -> new RegistryMap.ItemMap());
		else if (Registries.BLOCK.equals(registry_key))
			return (RegistryMap<R>) RegistryMaps.computeIfAbsent(registry_key, (ResourceKey<? extends Registry<?>> key) -> new RegistryMap.BlockMap());
		else
			return (RegistryMap<R>) RegistryMaps.computeIfAbsent(registry_key, (ResourceKey<? extends Registry<?>> key) -> new RegistryMap(key));
	}

	public final DeferredRegister<R> getDeferredRegister(String namespace) {
		return deferredRegisters.computeIfAbsent(namespace, (String ns) -> RegistryFactory.deferredRegister(registryKey, ns));
	}

	/**
	 * @param <E>
	 * @param name 带modId的名称
	 * @param sup  由于直接创建E实例需要修改注册表，而在Mod构建刚完成，RegisterEvent之前，注册表是冻结的，无法创建实例，因此只能传入Supplier待到时机正确时再构建E实例
	 * @param args
	 * @return
	 */
	public <E extends R> DeferredHolder<R, E> register(String name, Supplier<E> sup, Object... args) {
		ResourceLocation loc = ResourceLocations.build(name);
		namespaces.add(loc.getNamespace());// 将当前注册的元素命名空间加入集合
		DeferredHolder<R, E> entry = (DeferredHolder<R, E>) getDeferredRegister(loc.getNamespace()).register(loc.getPath(), sup);
		entriesMap.put(name, entry);
		this.applyExtraArgs(name, entry, args);
		return entry;
	}

	public <A, E extends R> DeferredHolder<R, E> register(String name, Function<A, E> func, A func_arg, Object... args) {
		return register(name, () -> func.apply(func_arg), args);
	}

	/**
	 * 处理额外参数
	 * 
	 * @param name
	 * @param entry
	 * @param args
	 */
	protected <E extends R> void applyExtraArgs(String name, DeferredHolder<R, E> entry, Object... args) {

	}

	@SuppressWarnings("unchecked")
	public static final <A> A fetchArg(int idx, Class<A> cls, Object... args) {
		if (idx < 0 || idx > args.length - 1)
			return null;
		Object arg = args[idx];
		if (cls.isInstance(arg))
			return (A) arg;
		else
			return null;
	}

	public DeferredHolder<R, ? extends R> get(String name) {
		return entriesMap.get(name);
	}

	public boolean contains(String name) {
		return entriesMap.containsKey(name);
	}

	public static class ItemMap extends RegistryMap<Item> {

		private ItemMap() {
			super(Registries.ITEM);
		}

		public <E extends Item> DeferredItem<E> registerItem(String name, Supplier<E> sup, Object... args) {
			return (DeferredItem<E>) super.register(name, sup, args);
		}

		public <E extends Item> DeferredItem<E> registerItem(String name, Function<Item.Properties, E> func, Item.Properties func_arg, Object... args) {
			return this.registerItem(name, () -> func.apply(func_arg), args);
		}

		@Override
		@SuppressWarnings("unchecked")
		public <E extends Item> void applyExtraArgs(String name, DeferredHolder<Item, E> entry, Object... args) {
			ExtCreativeTab creativeTab = fetchArg(0, ExtCreativeTab.class, args);
			if (creativeTab != null)
				creativeTab.append((DeferredItem<Item>) entry);// 添加物品进创造物品栏
		}

		public DeferredItem<? extends Item> getItem(String name) {
			return (DeferredItem<? extends Item>) super.get(name);
		}
	}

	public static class BlockMap extends RegistryMap<Block> {

		private BlockMap() {
			super(Registries.BLOCK);
		}

		public <E extends Block> DeferredBlock<E> registerBlock(String name, Supplier<E> sup, Item.Properties blockitem_props, Object... args) {
			return (DeferredBlock<E>) super.register(name, sup, args);
		}

		@Override
		public <E extends Block> void applyExtraArgs(String name, DeferredHolder<Block, E> entry, Object... args) {
			Item.Properties blockitem_props = fetchArg(0, Item.Properties.class, args);
			if (blockitem_props != null) {
				DeferredItem<?> block_item = ExtItem.ITEMS.registerItem(name, () -> new BlockItem(entry.get(), blockitem_props));
				ExtCreativeTab creativeTab = fetchArg(1, ExtCreativeTab.class, args);
				if (creativeTab != null)
					creativeTab.append(block_item);// 添加物品进创造物品栏
			}
		}

		public <E extends Block> DeferredBlock<E> registerBlock(String name, Supplier<E> sup, Object... args) {
			return this.registerBlock(name, sup, ExtItem.defaultItemProperties(), args);
		}

		public <E extends Block> DeferredBlock<E> registerBlock(String name, Function<BlockBehaviour.Properties, E> func, BlockBehaviour.Properties func_arg, Item.Properties blockitem_props, Object... args) {
			return this.registerBlock(name, () -> func.apply(func_arg), blockitem_props, args);
		}

		public <E extends Block> DeferredBlock<E> registerBlock(String name, Function<BlockBehaviour.Properties, E> func, BlockBehaviour.Properties func_arg, Object... args) {
			return this.registerBlock(name, func, func_arg, ExtItem.defaultItemProperties(), args);
		}

		public DeferredBlock<? extends Block> getBlock(String name) {
			return (DeferredBlock<? extends Block>) super.get(name);
		}
	}

}
