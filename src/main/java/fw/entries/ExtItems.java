package fw.entries;

import java.util.HashMap;
import java.util.function.Function;

import fw.core.registry.RegistryFactory;
import fw.datagen.Localizable;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;

public class ExtItems {
	private static final HashMap<String, DeferredItem<Item>> itemsMap = new HashMap<>();

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

	public static final DeferredItem<Item> register(String name, ExtCreativeTab creativeTab, Function<Item.Properties, ? extends Item> itemGetter) {
		DeferredItem<Item> item = RegistryFactory.ITEM.registerItem(name, itemGetter);
		itemsMap.put(name, item);
		creativeTab.append(item);// 添加物品进创造物品栏
		return item;
	}

	public static final DeferredItem<Item> register(String name, ExtCreativeTab creativeTab) {
		return register(name, creativeTab, (p) -> new SimpleItem(p));
	}

	public static final DeferredItem<Item> register(String name, Function<Item.Properties, ? extends Item> itemGetter) {
		DeferredItem<Item> item = RegistryFactory.ITEM.registerItem(name, itemGetter);
		itemsMap.put(name, item);
		return item;
	}

	public static final DeferredItem<Item> register(String name) {
		return register(name, (p) -> new SimpleItem(p));
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