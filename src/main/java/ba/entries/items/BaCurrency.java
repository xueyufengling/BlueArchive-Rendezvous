package ba.entries.items;

import ba.entries.BaCreativeTab;
import fw.datagen.annotation.ItemDatagen;
import fw.datagen.annotation.LangDatagen;
import fw.items.ExtItems;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;

public class BaCurrency {

	static {
		ItemDatagen.ModelProvider.forDatagen(BaCurrency.class);
		LangDatagen.LangProvider.forDatagen(BaCurrency.class);
	}

	public static final String resourcePath = BaCreativeTab.Id.BA_CURRENCY;

	@LangDatagen(en_us = "Arena Coin", zh_cn = "战术奖币")
	@ItemDatagen(name = "arena_coin", path = resourcePath)
	public static final DeferredItem<Item> arena_coin = ExtItems.register("arena_coin", BaCreativeTab.BA_CURRENCY);

	@LangDatagen(en_us = "Chaser Coin", zh_cn = "悬赏通缉奖币")
	@ItemDatagen(name = "chaser_coin", path = resourcePath)
	public static final DeferredItem<Item> chaser_coin = ExtItems.register("chaser_coin", BaCreativeTab.BA_CURRENCY);

	@LangDatagen(en_us = "Great Coin", zh_cn = "﻿大决战奖币")
	@ItemDatagen(name = "eliminate_raid_coin", path = resourcePath)
	public static final DeferredItem<Item> eliminate_raid_coin = ExtItems.register("eliminate_raid_coin", BaCreativeTab.BA_CURRENCY);

	@LangDatagen(en_us = "Rare Great Coin", zh_cn = "高级大决战奖币")
	@ItemDatagen(name = "eliminate_raid_coin_high", path = resourcePath)
	public static final DeferredItem<Item> eliminate_raid_coin_high = ExtItems.register("eliminate_raid_coin_high", BaCreativeTab.BA_CURRENCY);

	@LangDatagen(en_us = "Pyroxene", zh_cn = "青辉石")
	@ItemDatagen(name = "gem", path = resourcePath)
	public static final DeferredItem<Item> gem = ExtItems.register("gem", BaCreativeTab.BA_CURRENCY);

	@LangDatagen(en_us = "Credit", zh_cn = "信用积分")
	@ItemDatagen(name = "gold", path = resourcePath)
	public static final DeferredItem<Item> gold = ExtItems.register("gold", BaCreativeTab.BA_CURRENCY);

	@LangDatagen(en_us = "Mastery Certificate", zh_cn = "熟练证书")
	@ItemDatagen(name = "master_coin", path = resourcePath)
	public static final DeferredItem<Item> master_coin = ExtItems.register("master_coin", BaCreativeTab.BA_CURRENCY);

	@LangDatagen(en_us = "Total War Coin", zh_cn = "总力战奖币")
	@ItemDatagen(name = "raid_coin", path = resourcePath)
	public static final DeferredItem<Item> raid_coin = ExtItems.register("raid_coin", BaCreativeTab.BA_CURRENCY);

	@LangDatagen(en_us = "Rare Coin", zh_cn = "高级总力战奖币")
	@ItemDatagen(name = "raid_coin_high", path = resourcePath)
	public static final DeferredItem<Item> raid_coin_high = ExtItems.register("raid_coin_high", BaCreativeTab.BA_CURRENCY);

	@LangDatagen(en_us = "Divine Fragment", zh_cn = "神名精髓")
	@ItemDatagen(name = "secret_stone", path = resourcePath)
	public static final DeferredItem<Item> secret_stone = ExtItems.register("secret_stone", BaCreativeTab.BA_CURRENCY);

	@LangDatagen(en_us = "Firepower Exercise Coin", zh_cn = "综合战术测试奖币")
	@ItemDatagen(name = "time_attack_coin", path = resourcePath)
	public static final DeferredItem<Item> time_attack_coin = ExtItems.register("time_attack_coin", BaCreativeTab.BA_CURRENCY);
}
