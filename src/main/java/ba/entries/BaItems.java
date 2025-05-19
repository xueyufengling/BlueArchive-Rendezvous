package ba.entries;

import ba.core.ExtItems;
import ba.core.datagen.Datagen;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;

public class BaItems extends ExtItems {

	static {
		forDatagen(BaItems.class);
	}

	@Datagen(name = "gem")
	public static final DeferredItem<Item> GEM = register("gem", BaCreativeTab.BA_ITEMS);

}
