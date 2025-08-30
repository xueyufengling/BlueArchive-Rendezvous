package lepus.mc.items;

import lepus.mc.core.registry.RegistryMap;
import lepus.mc.core.registry.RegistryMap.ItemMap;
import lepus.mc.datagen.Localizable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;

/**
 * 没有任何功能的简单物品，通常是材料
 */
public class ExtItem extends Item implements Localizable {
	public static final RegistryMap.ItemMap ITEMS = (ItemMap) RegistryMap.of(Registries.ITEM);

	public ExtItem(Item.Properties properties) {
		super(properties);
	}

	public static Item.Properties defaultItemProperties() {
		return new Item.Properties();
	}

	public ExtItem() {
		this(defaultItemProperties());
	}

	@Override
	public String localizationKey() {
		return Localizable.localizationKey(BuiltInRegistries.ITEM.wrapAsHolder(this));
	}

	public static final DeferredItem<Item> register(String name, ExtCreativeTab creativeTab) {
		return ITEMS.registerItem(name, () -> new ExtItem(), creativeTab);
	}

	public static final DeferredItem<Item> register(String name) {
		return register(name, null);
	}

	public static final DeferredItem<Item> registerMod(String name) {
		return registerMod(name);
	}
}
