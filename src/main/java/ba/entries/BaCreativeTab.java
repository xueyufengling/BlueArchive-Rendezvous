package ba.entries;

import fw.core.ExtCreativeTab;
import fw.datagen.ExtLangProvider;
import fw.datagen.LangDatagen;

public enum BaCreativeTab implements ExtCreativeTab, ExtLangProvider {
	@LangDatagen(en_us = "Blue Archive Currency", zh_cn = "蔚蓝档案货币")
	BA_CURRENCY(Id.BA_CURRENCY, "gem"),

	@LangDatagen(en_us = "Blue Archive Materials", zh_cn = "蔚蓝档案材料")
	BA_MATERIALS(Id.BA_MATERIALS, "gem"),

	@LangDatagen(en_us = "Blue Archive Blocks", zh_cn = "蔚蓝档案方块")
	BA_BLOCKS(Id.BA_BLOCKS, ""),

	@LangDatagen(en_us = "Blue Archive Weapons", zh_cn = "蔚蓝档案武器")
	BA_WEAPONS(Id.BA_WEAPONS, ""),

	@LangDatagen(en_us = "Blue Archive Equipments", zh_cn = "蔚蓝档案装备")
	BA_EQUIPMENTS(Id.BA_EQUIPMENTS, "");

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
	}

	static {
		ExtLangProvider.forDatagen(BaCreativeTab.class);
	}
}
