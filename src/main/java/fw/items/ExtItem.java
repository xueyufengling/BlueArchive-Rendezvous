package fw.items;

import fw.core.registry.RegistryMap;
import fw.datagen.Localizable;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;

/**
 * 没有任何功能的简单物品，通常是材料
 */
public class ExtItem extends Item implements Localizable {
	public static final RegistryMap.ItemMap ITEMS = new RegistryMap.ItemMap();

	public ExtItem(Item.Properties properties) {
		super(properties);
	}

	public ExtItem() {
		this(new Item.Properties());
	}

	@Override
	public String localizationKey() {
		return this.getDescriptionId();
	}

	public static final DeferredItem<Item> register(String name, ExtCreativeTab creativeTab) {
		return ITEMS.registerItem(name, () -> new ExtItem(), creativeTab);
	}

	public static final DeferredItem<Item> register(String name) {
		return register(name, null);
	}
}
