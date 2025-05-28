package fw.entries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

import fw.core.RegistryFactory;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;

public class ExtItems {
	static final ArrayList<Class<? extends ExtItems>> itemsClasses = new ArrayList<>();

	private static final HashMap<String, DeferredItem<Item>> itemsMap = new HashMap<>();

	public static final DeferredItem<Item> register(String name, ExtCreativeTab creativeTab, Function<Item.Properties, ? extends Item> itemGetter) {
		DeferredItem<Item> item = RegistryFactory.ITEM.registerItem(name, itemGetter);
		itemsMap.put(name, item);
		creativeTab.append(item);// 添加物品进创造物品栏
		return item;
	}

	public static final DeferredItem<Item> register(String name, ExtCreativeTab creativeTab) {
		return register(name, creativeTab, (p) -> new ExtItem(p));
	}

	public static final DeferredItem<Item> register(String name, Function<Item.Properties, ? extends Item> itemGetter) {
		DeferredItem<Item> item = RegistryFactory.ITEM.registerItem(name, itemGetter);
		itemsMap.put(name, item);
		return item;
	}

	public static final DeferredItem<Item> register(String name) {
		return register(name, (p) -> new ExtItem(p));
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

	public static final void forDatagen(Class<? extends ExtItems> itemClass) {
		if (!itemsClasses.contains(itemClass))
			itemsClasses.add(itemClass);
	}
}