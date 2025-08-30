package lepus.mc.client.render.item;

import java.util.ArrayList;

import lepus.mc.items.enchantment.EnchantmentLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EnchantmentBackground {
	public final static ItemBackground.Resolver<EnchantmentLevel> ENCHANTMENT_MATCHES = new ItemBackground.Resolver<EnchantmentLevel>() {
		@Override
		protected EnchantmentLevel key(LivingEntity entity, Level level, ItemStack stack, String itemId, int seed) {
			ArrayList<EnchantmentLevel> enchantments = EnchantmentLevel.getAllEnchantmentLevels(stack);
			for (EnchantmentLevel enchantment : enchantments) {
				int lv = enchantment.level;
				EnchantmentLevel key = enchantment;
				while (lv > 0 && !super.contains(key))
					key = EnchantmentLevel.of(enchantment.enchantmentId, --lv);
				return lv > 0 ? key : null;
			}
			return null;
		}
	};

	static {
		ItemBackground.registerResolver(ENCHANTMENT_MATCHES);// 仅当使用此类时才会注册
	}

	public static void setBackground(String enchantment_id, int lv, ItemBackground background) {
		ENCHANTMENT_MATCHES.register(EnchantmentLevel.of(enchantment_id, lv), background);
	}

	// 获取指定附魔的背景列表
	public ItemBackground getBackground(String enchantment_id, int lv) {
		return (ItemBackground) ENCHANTMENT_MATCHES.get(EnchantmentLevel.of(enchantment_id, lv));
	}
}
