package ba.entries.items;

import ba.entries.BaCreativeTab;
import lepus.mc.datagen.annotation.ItemDatagen;
import lepus.mc.datagen.annotation.LangDatagen;
import lepus.mc.datagen.annotation.Translation;
import lepus.mc.items.ExtItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;

public class BaCurrency {

	static {
		ItemDatagen.ModelProvider.forDatagen(BaCurrency.class);
		LangDatagen.LangProvider.forDatagen(BaCurrency.class);
	}

	public static final String resourcePath = BaCreativeTab.TexPath.BA_CURRENCY;

	@LangDatagen(translations = { @Translation(locale = "en_us", text = "Arena Coin"), @Translation(locale = "zh_cn", text = "战术奖币") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> arena_coin = ExtItem.registerMod("arena_coin", BaCreativeTab.BA_CURRENCY);

	@LangDatagen(translations = { @Translation(locale = "en_us", text = "Chaser Coin"), @Translation(locale = "zh_cn", text = "悬赏通缉奖币") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> chaser_coin = ExtItem.registerMod("chaser_coin", BaCreativeTab.BA_CURRENCY);

	@LangDatagen(translations = { @Translation(locale = "en_us", text = "Great Coin"), @Translation(locale = "zh_cn", text = "﻿大决战奖币") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> eliminate_raid_coin = ExtItem.registerMod("eliminate_raid_coin", BaCreativeTab.BA_CURRENCY);

	@LangDatagen(translations = { @Translation(locale = "en_us", text = "Rare Great Coin"), @Translation(locale = "zh_cn", text = "高级大决战奖币") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> eliminate_raid_coin_high = ExtItem.registerMod("eliminate_raid_coin_high", BaCreativeTab.BA_CURRENCY);

	@LangDatagen(translations = { @Translation(locale = "en_us", text = "Pyroxene"), @Translation(locale = "zh_cn", text = "青辉石") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> gem = ExtItem.registerMod("gem", BaCreativeTab.BA_CURRENCY);

	@LangDatagen(translations = { @Translation(locale = "en_us", text = "Credit"), @Translation(locale = "zh_cn", text = "信用积分") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> gold = ExtItem.registerMod("gold", BaCreativeTab.BA_CURRENCY);

	@LangDatagen(translations = { @Translation(locale = "en_us", text = "Mastery Certificate"), @Translation(locale = "zh_cn", text = "熟练证书") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> master_coin = ExtItem.registerMod("master_coin", BaCreativeTab.BA_CURRENCY);

	@LangDatagen(translations = { @Translation(locale = "en_us", text = "Total War Coin"), @Translation(locale = "zh_cn", text = "总力战奖币") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> raid_coin = ExtItem.registerMod("raid_coin", BaCreativeTab.BA_CURRENCY);

	@LangDatagen(translations = { @Translation(locale = "en_us", text = "Rare Coin"), @Translation(locale = "zh_cn", text = "高级总力战奖币") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> raid_coin_high = ExtItem.registerMod("raid_coin_high", BaCreativeTab.BA_CURRENCY);

	@LangDatagen(translations = { @Translation(locale = "en_us", text = "Divine Fragment"), @Translation(locale = "zh_cn", text = "神名精髓") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> secret_stone = ExtItem.registerMod("secret_stone", BaCreativeTab.BA_CURRENCY);

	@LangDatagen(translations = { @Translation(locale = "en_us", text = "Firepower Exercise Coin"), @Translation(locale = "zh_cn", text = "综合战术测试奖币") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> time_attack_coin = ExtItem.registerMod("time_attack_coin", BaCreativeTab.BA_CURRENCY);
}
