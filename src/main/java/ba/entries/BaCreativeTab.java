package ba.entries;

import fw.datagen.annotation.LangDatagen;
import fw.datagen.annotation.Translation;
import fw.items.ExtCreativeTab;

public enum BaCreativeTab implements ExtCreativeTab {
	@LangDatagen(translations = { @Translation(locale = "en_us", text = "BlueArchive Key Items"), @Translation(locale = "zh_cn", text = "蔚蓝档案 重要物品") })
	BA_KEY_ITEMS("ba:key_items", "shittim_chest"),

	@LangDatagen(translations = { @Translation(locale = "en_us", text = "BlueArchive Currency"), @Translation(locale = "zh_cn", text = "蔚蓝档案 货币") })
	BA_CURRENCY("ba:currency", "gem"),

	@LangDatagen(translations = { @Translation(locale = "en_us", text = "BlueArchive Materials"), @Translation(locale = "zh_cn", text = "蔚蓝档案 材料") })
	BA_MATERIALS("ba:materials", "ether_3"),

	@LangDatagen(translations = { @Translation(locale = "en_us", text = "BlueArchive Blocks"), @Translation(locale = "zh_cn", text = "蔚蓝档案 方块") })
	BA_BLOCKS("ba:blocks", ""),

	@LangDatagen(translations = { @Translation(locale = "en_us", text = "BlueArchive Weapons"), @Translation(locale = "zh_cn", text = "蔚蓝档案 武器") })
	BA_WEAPONS("ba:weapons", ""),

	@LangDatagen(translations = { @Translation(locale = "en_us", text = "BlueArchive Equipments"), @Translation(locale = "zh_cn", text = "蔚蓝档案 装备") })
	BA_EQUIPMENTS("ba:equipments", "bag_tier5"),

	@LangDatagen(translations = { @Translation(locale = "en_us", text = "BlueArchive Skill Books"), @Translation(locale = "zh_cn", text = "蔚蓝档案 技能书") })
	BA_SKILLBOOKS("ba:skillbooks", "skillbook_selection_3");

	private BaCreativeTab(String id, String iconItem) {
		ExtCreativeTab.define(this, id, iconItem);
	}

	public static class TexPath {
		public static final String BA_KEY_ITEMS = "key_items";
		public static final String BA_CURRENCY = "currency";
		public static final String BA_MATERIALS = "materials";
		public static final String BA_BLOCKS = "blocks";
		public static final String BA_WEAPONS = "weapons";
		public static final String BA_EQUIPMENTS = "equipments";
		public static final String BA_SKILLBOOKS = "skillbooks";
	}

	static {
		LangDatagen.LangProvider.forDatagen(BaCreativeTab.class);
	}
}
