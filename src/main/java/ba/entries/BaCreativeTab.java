package ba.entries;

import fw.datagen.annotation.LangDatagen;
import fw.items.ExtCreativeTab;

public enum BaCreativeTab implements ExtCreativeTab {
	@LangDatagen(en_us = "BlueArchive Currency", zh_cn = "蔚蓝档案 货币")
	BA_CURRENCY(Id.BA_CURRENCY, "gem"),

	@LangDatagen(en_us = "BlueArchive Materials", zh_cn = "蔚蓝档案 材料")
	BA_MATERIALS(Id.BA_MATERIALS, "ether_3"),

	@LangDatagen(en_us = "BlueArchive Blocks", zh_cn = "蔚蓝档案 方块")
	BA_BLOCKS(Id.BA_BLOCKS, ""),

	@LangDatagen(en_us = "BlueArchive Weapons", zh_cn = "蔚蓝档案 武器")
	BA_WEAPONS(Id.BA_WEAPONS, ""),

	@LangDatagen(en_us = "BlueArchive Equipments", zh_cn = "蔚蓝档案 装备")
	BA_EQUIPMENTS(Id.BA_EQUIPMENTS, "bag_tier5"),

	@LangDatagen(en_us = "BlueArchive Skill Books", zh_cn = "蔚蓝档案 技能书")
	BA_SKILLBOOKS(Id.BA_SKILLBOOKS, "skillbook_selection_3");

	public final ExtCreativeTab.Definition definition;

	private BaCreativeTab(String id, String iconItem) {
		definition = ExtCreativeTab.define(this, id, iconItem);
	}

	@Override
	public Definition definition() {
		return definition;
	}

	public static class Id {
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
