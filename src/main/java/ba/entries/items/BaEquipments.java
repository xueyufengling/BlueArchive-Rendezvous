package ba.entries.items;

import fw.datagen.annotation.ItemDatagen;
import fw.datagen.annotation.LangDatagen;
import fw.datagen.annotation.Translation;
import fw.items.ExtItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import ba.entries.BaCreativeTab;

public class BaEquipments {

	static {
		ItemDatagen.ModelProvider.forDatagen(BaEquipments.class);
		LangDatagen.LangProvider.forDatagen(BaEquipments.class);
	}

	public static final String resourcePath = BaCreativeTab.TexPath.BA_EQUIPMENTS;


	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Badge Tier1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> badge_tier1 = ExtItem.registerMod("badge_tier1", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Badge Tier2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> badge_tier2 = ExtItem.registerMod("badge_tier2", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Badge Tier2 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> badge_tier2_piece = ExtItem.registerMod("badge_tier2_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Badge Tier3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> badge_tier3 = ExtItem.registerMod("badge_tier3", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Badge Tier3 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> badge_tier3_piece = ExtItem.registerMod("badge_tier3_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Badge Tier4"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> badge_tier4 = ExtItem.registerMod("badge_tier4", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Badge Tier4 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> badge_tier4_piece = ExtItem.registerMod("badge_tier4_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Badge Tier5"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> badge_tier5 = ExtItem.registerMod("badge_tier5", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Badge Tier5 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> badge_tier5_piece = ExtItem.registerMod("badge_tier5_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Badge Tier6"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> badge_tier6 = ExtItem.registerMod("badge_tier6", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Badge Tier6 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> badge_tier6_piece = ExtItem.registerMod("badge_tier6_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Badge Tier7"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> badge_tier7 = ExtItem.registerMod("badge_tier7", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Badge Tier7 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> badge_tier7_piece = ExtItem.registerMod("badge_tier7_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Badge Tier8"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> badge_tier8 = ExtItem.registerMod("badge_tier8", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Badge Tier8 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> badge_tier8_piece = ExtItem.registerMod("badge_tier8_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Badge Tier9"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> badge_tier9 = ExtItem.registerMod("badge_tier9", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Badge Tier9 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> badge_tier9_piece = ExtItem.registerMod("badge_tier9_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Bag Tier1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> bag_tier1 = ExtItem.registerMod("bag_tier1", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Bag Tier2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> bag_tier2 = ExtItem.registerMod("bag_tier2", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Bag Tier2 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> bag_tier2_piece = ExtItem.registerMod("bag_tier2_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Bag Tier3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> bag_tier3 = ExtItem.registerMod("bag_tier3", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Bag Tier3 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> bag_tier3_piece = ExtItem.registerMod("bag_tier3_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Bag Tier4"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> bag_tier4 = ExtItem.registerMod("bag_tier4", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Bag Tier4 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> bag_tier4_piece = ExtItem.registerMod("bag_tier4_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Bag Tier5"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> bag_tier5 = ExtItem.registerMod("bag_tier5", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Bag Tier5 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> bag_tier5_piece = ExtItem.registerMod("bag_tier5_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Bag Tier6"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> bag_tier6 = ExtItem.registerMod("bag_tier6", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Bag Tier6 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> bag_tier6_piece = ExtItem.registerMod("bag_tier6_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Bag Tier7"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> bag_tier7 = ExtItem.registerMod("bag_tier7", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Bag Tier7 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> bag_tier7_piece = ExtItem.registerMod("bag_tier7_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Bag Tier8"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> bag_tier8 = ExtItem.registerMod("bag_tier8", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Bag Tier8 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> bag_tier8_piece = ExtItem.registerMod("bag_tier8_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Bag Tier9"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> bag_tier9 = ExtItem.registerMod("bag_tier9", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Bag Tier9 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> bag_tier9_piece = ExtItem.registerMod("bag_tier9_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Charm Tier1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> charm_tier1 = ExtItem.registerMod("charm_tier1", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Charm Tier2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> charm_tier2 = ExtItem.registerMod("charm_tier2", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Charm Tier2 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> charm_tier2_piece = ExtItem.registerMod("charm_tier2_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Charm Tier3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> charm_tier3 = ExtItem.registerMod("charm_tier3", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Charm Tier3 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> charm_tier3_piece = ExtItem.registerMod("charm_tier3_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Charm Tier4"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> charm_tier4 = ExtItem.registerMod("charm_tier4", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Charm Tier4 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> charm_tier4_piece = ExtItem.registerMod("charm_tier4_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Charm Tier5"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> charm_tier5 = ExtItem.registerMod("charm_tier5", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Charm Tier5 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> charm_tier5_piece = ExtItem.registerMod("charm_tier5_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Charm Tier6"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> charm_tier6 = ExtItem.registerMod("charm_tier6", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Charm Tier6 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> charm_tier6_piece = ExtItem.registerMod("charm_tier6_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Charm Tier7"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> charm_tier7 = ExtItem.registerMod("charm_tier7", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Charm Tier7 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> charm_tier7_piece = ExtItem.registerMod("charm_tier7_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Charm Tier8"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> charm_tier8 = ExtItem.registerMod("charm_tier8", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Charm Tier8 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> charm_tier8_piece = ExtItem.registerMod("charm_tier8_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Charm Tier9"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> charm_tier9 = ExtItem.registerMod("charm_tier9", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Charm Tier9 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> charm_tier9_piece = ExtItem.registerMod("charm_tier9_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Error"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> error = ExtItem.registerMod("error", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exp 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exp_0 = ExtItem.registerMod("exp_0", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exp 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exp_1 = ExtItem.registerMod("exp_1", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exp 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exp_2 = ExtItem.registerMod("exp_2", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exp 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exp_3 = ExtItem.registerMod("exp_3", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Gloves Tier1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> gloves_tier1 = ExtItem.registerMod("gloves_tier1", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Gloves Tier2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> gloves_tier2 = ExtItem.registerMod("gloves_tier2", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Gloves Tier2 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> gloves_tier2_piece = ExtItem.registerMod("gloves_tier2_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Gloves Tier3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> gloves_tier3 = ExtItem.registerMod("gloves_tier3", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Gloves Tier3 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> gloves_tier3_piece = ExtItem.registerMod("gloves_tier3_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Gloves Tier4"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> gloves_tier4 = ExtItem.registerMod("gloves_tier4", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Gloves Tier4 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> gloves_tier4_piece = ExtItem.registerMod("gloves_tier4_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Gloves Tier5"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> gloves_tier5 = ExtItem.registerMod("gloves_tier5", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Gloves Tier5 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> gloves_tier5_piece = ExtItem.registerMod("gloves_tier5_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Gloves Tier6"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> gloves_tier6 = ExtItem.registerMod("gloves_tier6", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Gloves Tier6 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> gloves_tier6_piece = ExtItem.registerMod("gloves_tier6_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Gloves Tier7"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> gloves_tier7 = ExtItem.registerMod("gloves_tier7", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Gloves Tier7 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> gloves_tier7_piece = ExtItem.registerMod("gloves_tier7_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Gloves Tier8"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> gloves_tier8 = ExtItem.registerMod("gloves_tier8", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Gloves Tier8 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> gloves_tier8_piece = ExtItem.registerMod("gloves_tier8_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Gloves Tier9"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> gloves_tier9 = ExtItem.registerMod("gloves_tier9", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Gloves Tier9 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> gloves_tier9_piece = ExtItem.registerMod("gloves_tier9_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Hairpin Tier1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> hairpin_tier1 = ExtItem.registerMod("hairpin_tier1", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Hairpin Tier2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> hairpin_tier2 = ExtItem.registerMod("hairpin_tier2", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Hairpin Tier2 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> hairpin_tier2_piece = ExtItem.registerMod("hairpin_tier2_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Hairpin Tier3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> hairpin_tier3 = ExtItem.registerMod("hairpin_tier3", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Hairpin Tier3 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> hairpin_tier3_piece = ExtItem.registerMod("hairpin_tier3_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Hairpin Tier4"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> hairpin_tier4 = ExtItem.registerMod("hairpin_tier4", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Hairpin Tier4 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> hairpin_tier4_piece = ExtItem.registerMod("hairpin_tier4_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Hairpin Tier5"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> hairpin_tier5 = ExtItem.registerMod("hairpin_tier5", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Hairpin Tier5 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> hairpin_tier5_piece = ExtItem.registerMod("hairpin_tier5_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Hairpin Tier6"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> hairpin_tier6 = ExtItem.registerMod("hairpin_tier6", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Hairpin Tier6 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> hairpin_tier6_piece = ExtItem.registerMod("hairpin_tier6_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Hairpin Tier7"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> hairpin_tier7 = ExtItem.registerMod("hairpin_tier7", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Hairpin Tier7 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> hairpin_tier7_piece = ExtItem.registerMod("hairpin_tier7_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Hairpin Tier8"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> hairpin_tier8 = ExtItem.registerMod("hairpin_tier8", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Hairpin Tier8 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> hairpin_tier8_piece = ExtItem.registerMod("hairpin_tier8_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Hairpin Tier9"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> hairpin_tier9 = ExtItem.registerMod("hairpin_tier9", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Hairpin Tier9 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> hairpin_tier9_piece = ExtItem.registerMod("hairpin_tier9_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Hat Tier1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> hat_tier1 = ExtItem.registerMod("hat_tier1", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Hat Tier2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> hat_tier2 = ExtItem.registerMod("hat_tier2", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Hat Tier2 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> hat_tier2_piece = ExtItem.registerMod("hat_tier2_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Hat Tier3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> hat_tier3 = ExtItem.registerMod("hat_tier3", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Hat Tier3 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> hat_tier3_piece = ExtItem.registerMod("hat_tier3_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Hat Tier4"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> hat_tier4 = ExtItem.registerMod("hat_tier4", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Hat Tier4 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> hat_tier4_piece = ExtItem.registerMod("hat_tier4_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Hat Tier5"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> hat_tier5 = ExtItem.registerMod("hat_tier5", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Hat Tier5 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> hat_tier5_piece = ExtItem.registerMod("hat_tier5_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Hat Tier6"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> hat_tier6 = ExtItem.registerMod("hat_tier6", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Hat Tier6 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> hat_tier6_piece = ExtItem.registerMod("hat_tier6_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Hat Tier7"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> hat_tier7 = ExtItem.registerMod("hat_tier7", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Hat Tier7 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> hat_tier7_piece = ExtItem.registerMod("hat_tier7_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Hat Tier8"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> hat_tier8 = ExtItem.registerMod("hat_tier8", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Hat Tier8 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> hat_tier8_piece = ExtItem.registerMod("hat_tier8_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Hat Tier9"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> hat_tier9 = ExtItem.registerMod("hat_tier9", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Hat Tier9 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> hat_tier9_piece = ExtItem.registerMod("hat_tier9_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Necklace Tier1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> necklace_tier1 = ExtItem.registerMod("necklace_tier1", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Necklace Tier2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> necklace_tier2 = ExtItem.registerMod("necklace_tier2", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Necklace Tier2 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> necklace_tier2_piece = ExtItem.registerMod("necklace_tier2_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Necklace Tier3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> necklace_tier3 = ExtItem.registerMod("necklace_tier3", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Necklace Tier3 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> necklace_tier3_piece = ExtItem.registerMod("necklace_tier3_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Necklace Tier4"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> necklace_tier4 = ExtItem.registerMod("necklace_tier4", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Necklace Tier4 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> necklace_tier4_piece = ExtItem.registerMod("necklace_tier4_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Necklace Tier5"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> necklace_tier5 = ExtItem.registerMod("necklace_tier5", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Necklace Tier5 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> necklace_tier5_piece = ExtItem.registerMod("necklace_tier5_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Necklace Tier6"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> necklace_tier6 = ExtItem.registerMod("necklace_tier6", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Necklace Tier6 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> necklace_tier6_piece = ExtItem.registerMod("necklace_tier6_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Necklace Tier7"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> necklace_tier7 = ExtItem.registerMod("necklace_tier7", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Necklace Tier7 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> necklace_tier7_piece = ExtItem.registerMod("necklace_tier7_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Necklace Tier8"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> necklace_tier8 = ExtItem.registerMod("necklace_tier8", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Necklace Tier8 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> necklace_tier8_piece = ExtItem.registerMod("necklace_tier8_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Necklace Tier9"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> necklace_tier9 = ExtItem.registerMod("necklace_tier9", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Necklace Tier9 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> necklace_tier9_piece = ExtItem.registerMod("necklace_tier9_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Selection Tier2 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> selection_tier2_piece = ExtItem.registerMod("selection_tier2_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Selection Tier3 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> selection_tier3_piece = ExtItem.registerMod("selection_tier3_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Selection Tier4 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> selection_tier4_piece = ExtItem.registerMod("selection_tier4_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Selection Tier5 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> selection_tier5_piece = ExtItem.registerMod("selection_tier5_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Selection Tier6 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> selection_tier6_piece = ExtItem.registerMod("selection_tier6_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Selection Tier7 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> selection_tier7_piece = ExtItem.registerMod("selection_tier7_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Selection Tier8 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> selection_tier8_piece = ExtItem.registerMod("selection_tier8_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Shoes Tier1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> shoes_tier1 = ExtItem.registerMod("shoes_tier1", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Shoes Tier2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> shoes_tier2 = ExtItem.registerMod("shoes_tier2", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Shoes Tier2 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> shoes_tier2_piece = ExtItem.registerMod("shoes_tier2_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Shoes Tier3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> shoes_tier3 = ExtItem.registerMod("shoes_tier3", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Shoes Tier3 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> shoes_tier3_piece = ExtItem.registerMod("shoes_tier3_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Shoes Tier4"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> shoes_tier4 = ExtItem.registerMod("shoes_tier4", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Shoes Tier4 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> shoes_tier4_piece = ExtItem.registerMod("shoes_tier4_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Shoes Tier5"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> shoes_tier5 = ExtItem.registerMod("shoes_tier5", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Shoes Tier5 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> shoes_tier5_piece = ExtItem.registerMod("shoes_tier5_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Shoes Tier6"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> shoes_tier6 = ExtItem.registerMod("shoes_tier6", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Shoes Tier6 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> shoes_tier6_piece = ExtItem.registerMod("shoes_tier6_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Shoes Tier7"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> shoes_tier7 = ExtItem.registerMod("shoes_tier7", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Shoes Tier7 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> shoes_tier7_piece = ExtItem.registerMod("shoes_tier7_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Shoes Tier8"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> shoes_tier8 = ExtItem.registerMod("shoes_tier8", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Shoes Tier8 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> shoes_tier8_piece = ExtItem.registerMod("shoes_tier8_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Shoes Tier9"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> shoes_tier9 = ExtItem.registerMod("shoes_tier9", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Shoes Tier9 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> shoes_tier9_piece = ExtItem.registerMod("shoes_tier9_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Watch Tier1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> watch_tier1 = ExtItem.registerMod("watch_tier1", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Watch Tier2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> watch_tier2 = ExtItem.registerMod("watch_tier2", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Watch Tier2 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> watch_tier2_piece = ExtItem.registerMod("watch_tier2_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Watch Tier3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> watch_tier3 = ExtItem.registerMod("watch_tier3", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Watch Tier3 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> watch_tier3_piece = ExtItem.registerMod("watch_tier3_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Watch Tier4"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> watch_tier4 = ExtItem.registerMod("watch_tier4", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Watch Tier4 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> watch_tier4_piece = ExtItem.registerMod("watch_tier4_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Watch Tier5"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> watch_tier5 = ExtItem.registerMod("watch_tier5", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Watch Tier5 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> watch_tier5_piece = ExtItem.registerMod("watch_tier5_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Watch Tier6"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> watch_tier6 = ExtItem.registerMod("watch_tier6", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Watch Tier6 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> watch_tier6_piece = ExtItem.registerMod("watch_tier6_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Watch Tier7"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> watch_tier7 = ExtItem.registerMod("watch_tier7", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Watch Tier7 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> watch_tier7_piece = ExtItem.registerMod("watch_tier7_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Watch Tier8"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> watch_tier8 = ExtItem.registerMod("watch_tier8", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Watch Tier8 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> watch_tier8_piece = ExtItem.registerMod("watch_tier8_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Watch Tier9"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> watch_tier9 = ExtItem.registerMod("watch_tier9", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Watch Tier9 Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> watch_tier9_piece = ExtItem.registerMod("watch_tier9_piece", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Weaponexpgrowtha 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> weaponexpgrowtha_0 = ExtItem.registerMod("weaponexpgrowtha_0", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Weaponexpgrowtha 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> weaponexpgrowtha_1 = ExtItem.registerMod("weaponexpgrowtha_1", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Weaponexpgrowtha 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> weaponexpgrowtha_2 = ExtItem.registerMod("weaponexpgrowtha_2", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Weaponexpgrowtha 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> weaponexpgrowtha_3 = ExtItem.registerMod("weaponexpgrowtha_3", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Weaponexpgrowthb 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> weaponexpgrowthb_0 = ExtItem.registerMod("weaponexpgrowthb_0", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Weaponexpgrowthb 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> weaponexpgrowthb_1 = ExtItem.registerMod("weaponexpgrowthb_1", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Weaponexpgrowthb 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> weaponexpgrowthb_2 = ExtItem.registerMod("weaponexpgrowthb_2", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Weaponexpgrowthb 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> weaponexpgrowthb_3 = ExtItem.registerMod("weaponexpgrowthb_3", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Weaponexpgrowthc 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> weaponexpgrowthc_0 = ExtItem.registerMod("weaponexpgrowthc_0", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Weaponexpgrowthc 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> weaponexpgrowthc_1 = ExtItem.registerMod("weaponexpgrowthc_1", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Weaponexpgrowthc 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> weaponexpgrowthc_2 = ExtItem.registerMod("weaponexpgrowthc_2", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Weaponexpgrowthc 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> weaponexpgrowthc_3 = ExtItem.registerMod("weaponexpgrowthc_3", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Weaponexpgrowthz 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> weaponexpgrowthz_0 = ExtItem.registerMod("weaponexpgrowthz_0", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Weaponexpgrowthz 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> weaponexpgrowthz_1 = ExtItem.registerMod("weaponexpgrowthz_1", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Weaponexpgrowthz 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> weaponexpgrowthz_2 = ExtItem.registerMod("weaponexpgrowthz_2", BaCreativeTab.BA_EQUIPMENTS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Weaponexpgrowthz 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> weaponexpgrowthz_3 = ExtItem.registerMod("weaponexpgrowthz_3", BaCreativeTab.BA_EQUIPMENTS);

}
