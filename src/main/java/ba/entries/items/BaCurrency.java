package ba.entries.items;

import ba.entries.BaCreativeTab;
import fw.datagen.annotation.ItemDatagen;
import fw.datagen.annotation.LangDatagen;
import fw.datagen.annotation.Translation;
import fw.items.ExtItems;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;

public class BaCurrency {

	static {
		ItemDatagen.ModelProvider.forDatagen(BaCurrency.class);
		LangDatagen.LangProvider.forDatagen(BaCurrency.class);
	}

	public static final String resourcePath = BaCreativeTab.Id.BA_CURRENCY;

	@LangDatagen(translations = { @Translation(locale = "en_us", text = "Arena Coin"), @Translation(locale = "zh_cn", text = "战术奖币") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> arena_coin = ExtItems.register("arena_coin", BaCreativeTab.BA_CURRENCY);

	@LangDatagen(translations = { @Translation(locale = "en_us", text = "Chaser Coin"), @Translation(locale = "zh_cn", text = "悬赏通缉奖币") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> chaser_coin = ExtItems.register("chaser_coin", BaCreativeTab.BA_CURRENCY);

	@LangDatagen(translations = { @Translation(locale = "en_us", text = "Great Coin"), @Translation(locale = "zh_cn", text = "﻿大决战奖币") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> eliminate_raid_coin = ExtItems.register("eliminate_raid_coin", BaCreativeTab.BA_CURRENCY);

	@LangDatagen(translations = { @Translation(locale = "en_us", text = "Rare Great Coin"), @Translation(locale = "zh_cn", text = "高级大决战奖币") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> eliminate_raid_coin_high = ExtItems.register("eliminate_raid_coin_high", BaCreativeTab.BA_CURRENCY);

	@LangDatagen(translations = { @Translation(locale = "en_us", text = "Pyroxene"), @Translation(locale = "zh_cn", text = "青辉石") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> gem = ExtItems.register("gem", BaCreativeTab.BA_CURRENCY);

	@LangDatagen(translations = { @Translation(locale = "en_us", text = "Credit"), @Translation(locale = "zh_cn", text = "信用积分") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> gold = ExtItems.register("gold", BaCreativeTab.BA_CURRENCY);

	@LangDatagen(translations = { @Translation(locale = "en_us", text = "Mastery Certificate"), @Translation(locale = "zh_cn", text = "熟练证书") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> master_coin = ExtItems.register("master_coin", BaCreativeTab.BA_CURRENCY);

	@LangDatagen(translations = { @Translation(locale = "en_us", text = "Total War Coin"), @Translation(locale = "zh_cn", text = "总力战奖币") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> raid_coin = ExtItems.register("raid_coin", BaCreativeTab.BA_CURRENCY);

	@LangDatagen(translations = { @Translation(locale = "en_us", text = "Rare Coin"), @Translation(locale = "zh_cn", text = "高级总力战奖币") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> raid_coin_high = ExtItems.register("raid_coin_high", BaCreativeTab.BA_CURRENCY);

	@LangDatagen(translations = { @Translation(locale = "en_us", text = "Divine Fragment"), @Translation(locale = "zh_cn", text = "神名精髓") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> secret_stone = ExtItems.register("secret_stone", BaCreativeTab.BA_CURRENCY);

	@LangDatagen(translations = { @Translation(locale = "en_us", text = "Firepower Exercise Coin"), @Translation(locale = "zh_cn", text = "综合战术测试奖币") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> time_attack_coin = ExtItems.register("time_attack_coin", BaCreativeTab.BA_CURRENCY);
}
