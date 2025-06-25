package fw.items;

import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Supplier;

import fw.core.registry.RegistryFactory;
import fw.datagen.Localizable;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredItem;

public class ExtItems {
	static final HashMap<String, DeferredItem<Item>> itemsMap = new HashMap<>();

	/**
	 * 没有任何功能的简单物品，通常是材料
	 */
	public static class SimpleItem extends Item implements Localizable {

		public SimpleItem(Item.Properties properties) {
			super(properties);
		}

		@Override
		public String localizationKey() {
			return this.getDescriptionId();
		}
	}

	public static DeferredItem<Item> register(String name, Supplier<? extends Item> sup) {
		DeferredItem<Item> item = RegistryFactory.ITEM.register(name, sup);
		itemsMap.put(name, item);
		return item;
	}

	public static DeferredItem<Item> register(String name, ExtCreativeTab creativeTab, Supplier<? extends Item> sup) {
		DeferredItem<Item> item = register(name, sup);
		creativeTab.append(item);// 添加物品进创造物品栏
		return item;
	}

	// ------------------------------
	public static final DeferredItem<Item> register(String name, Function<Item.Properties, ? extends Item> itemGetter, Item.Properties props) {
		DeferredItem<Item> item = RegistryFactory.ITEM.registerItem(name, itemGetter, props);
		itemsMap.put(name, item);
		return item;
	}

	public static final DeferredItem<Item> register(String name, ExtCreativeTab creativeTab, Function<Item.Properties, ? extends Item> itemGetter, Item.Properties props) {
		DeferredItem<Item> item = register(name, itemGetter, props);
		creativeTab.append(item);// 添加物品进创造物品栏
		return item;
	}

	// -------------------------------
	public static final DeferredItem<Item> register(String name, ExtCreativeTab creativeTab, Item.Properties props) {
		return register(name, creativeTab, (p) -> new SimpleItem(p), props);
	}

	public static final DeferredItem<Item> register(String name, Item.Properties props) {
		return register(name, (p) -> new SimpleItem(p), props);
	}

	// -------------------------------
	/**
	 * 创建并注册默认属性的物品
	 * 
	 * @param name
	 * @param creativeTab
	 * @return
	 */
	public static final DeferredItem<Item> register(String name, ExtCreativeTab creativeTab) {
		return register(name, creativeTab, new Item.Properties());
	}

	public static final DeferredItem<Item> register(String name) {
		return register(name, new Item.Properties());
	}

	// -------------------------------
	public static final DeferredItem<Item> registerBlockItem(String name, Block block, Item.Properties props) {
		return register(name, () -> new BlockItem(block, props));
	}

	public static final DeferredItem<Item> registerBlockItem(String name, Block block) {
		return register(name, () -> new BlockItem(block, new Item.Properties()));
	}

	// -------------------------------
	public static final DeferredItem<Item> registerBlockItem(String name, ExtCreativeTab creativeTab, Block block, Item.Properties props) {
		return register(name, creativeTab, () -> new BlockItem(block, props));
	}

	public static final DeferredItem<Item> registerBlockItem(String name, ExtCreativeTab creativeTab, Block block) {
		return registerBlockItem(name, creativeTab, block, new Item.Properties());
	}

	/**
	 * 根据注册名获取物品
	 * 
	 * @param name
	 * @return
	 */
	public static final DeferredItem<Item> get(String name) {
		return itemsMap.get(name);
	}

	/**
	 * 根据注册名判定是否已经注册物品
	 * 
	 * @param name
	 * @return
	 */
	public static final boolean contains(String name) {
		return itemsMap.containsKey(name);
	}
}