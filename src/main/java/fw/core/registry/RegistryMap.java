package fw.core.registry;

import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Supplier;

import fw.core.Core;
import fw.items.ExtCreativeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RegistryMap<R> {
	protected final HashMap<String, DeferredHolder<R, ? extends R>> entriesMap = new HashMap<>();
	protected final DeferredRegister<R> deferredRegister;

	public RegistryMap(String namespace, ResourceKey<? extends Registry<R>> registry_key) {
		deferredRegister = RegistryFactory.deferredRegister(registry_key);
	}

	public RegistryMap(ResourceKey<? extends Registry<R>> registry_key) {
		this(Core.ModId, registry_key);
	}

	/**
	 * @param <E>
	 * @param name
	 * @param sup  由于直接创建E实例需要修改注册表，而在Mod构建刚完成，RegisterEvent之前，注册表是冻结的，无法创建实例，因此只能传入Supplier待到时机正确时再构建E实例
	 * @param args
	 * @return
	 */
	public <E extends R> DeferredHolder<R, E> register(String name, Supplier<E> sup, Object... args) {
		DeferredHolder<R, E> entry = (DeferredHolder<R, E>) deferredRegister.register(name, sup);
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

		public ItemMap(String namespace) {
			super(namespace, Registries.ITEM);
		}

		public ItemMap() {
			this(Core.ModId);
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

		public BlockMap(String namespace) {
			super(namespace, Registries.BLOCK);
		}

		public BlockMap() {
			this(Core.ModId);
		}

		public <E extends Block> DeferredBlock<E> registerBlock(String name, Supplier<E> sup, Item.Properties blockitem_props, Object... args) {
			DeferredBlock<E> block = (DeferredBlock<E>) super.register(name, sup, args);
			if (blockitem_props != null) {
				RegistryFactory.ITEM.register(name, () -> new BlockItem(block.get(), blockitem_props));
			}
			return block;
		}

		public <E extends Block> DeferredBlock<E> registerBlock(String name, Supplier<E> sup, Object... args) {
			return this.registerBlock(name, sup, null, args);
		}

		public <E extends Block> DeferredBlock<E> registerBlock(String name, Function<BlockBehaviour.Properties, E> func, BlockBehaviour.Properties func_arg, Item.Properties blockitem_props, Object... args) {
			return this.registerBlock(name, () -> func.apply(func_arg), blockitem_props, args);
		}

		public <E extends Block> DeferredBlock<E> registerBlock(String name, Function<BlockBehaviour.Properties, E> func, BlockBehaviour.Properties func_arg, Object... args) {
			return this.registerBlock(name, func, func_arg, null, args);
		}

		public DeferredBlock<? extends Block> getBlock(String name) {
			return (DeferredBlock<? extends Block>) super.get(name);
		}
	}

}
