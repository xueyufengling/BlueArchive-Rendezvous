package ba.entries.items;

import ba.entries.BaCreativeTab;
import fw.datagen.annotation.ItemDatagen;
import fw.datagen.annotation.LangDatagen;
import fw.datagen.annotation.Translation;
import fw.items.ExtItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;

public class BaKeyItems {

	static {
		ItemDatagen.ModelProvider.forDatagen(BaKeyItems.class);
		LangDatagen.LangProvider.forDatagen(BaKeyItems.class);
	}

	public static final String resourcePath = BaCreativeTab.Id.BA_KEY_ITEMS;

	@LangDatagen(translations = { @Translation(locale = "en_us", text = "Shittim Chest"), @Translation(locale = "zh_cn", text = "什亭之匣") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> shittim_chest = ExtItem.register("shittim_chest", BaCreativeTab.BA_KEY_ITEMS);

}