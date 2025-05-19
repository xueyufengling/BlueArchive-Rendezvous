package ba.entries;

import ba.core.ExtCreativeTab;

public enum BaCreativeTab implements ExtCreativeTab {
	BA_ITEMS("items", "gem"),
	BA_BLOCKS("blocks", ""),
	BA_WEAPONS("weapons", ""),
	BA_EQUIPMENTS("equipments", "");

	public final ExtCreativeTab.Definition definition;

	private BaCreativeTab(String id, String iconItem) {
		definition = ExtCreativeTab.define(this, id, iconItem);
		System.err.println(definition);
	}

	@Override
	public Definition definition() {
		return definition;
	}
}
