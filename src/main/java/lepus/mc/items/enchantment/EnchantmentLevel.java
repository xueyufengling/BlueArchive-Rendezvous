package lepus.mc.items.enchantment;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import lepus.mc.core.registry.registries.client.DynamicRegistries;
import lepus.mc.resources.ResourceKeys;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class EnchantmentLevel {
	public final String enchantmentId;
	public final int level;

	public EnchantmentLevel(ResourceKey<Enchantment> resourceKey, int level) {
		this.enchantmentId = getEnchantmentId(resourceKey);
		this.level = level;
	}

	public EnchantmentLevel(Enchantment enchantment, int level) {
		this.enchantmentId = getEnchantmentId(enchantment);
		this.level = level;
	}

	public EnchantmentLevel(String enchantment_id, int level) {
		this.enchantmentId = enchantment_id;
		this.level = level;
	}

	public static String getEnchantmentId(ResourceKey<Enchantment> resourceKey) {
		return ResourceKeys.toString(resourceKey);
	}

	public static String getEnchantmentId(Enchantment enchantment) {
		return getEnchantmentId(DynamicRegistries.ENCHANTMENT.getResourceKey(enchantment).orElse(null));
	}

	@Override
	public String toString() {
		return "{enchantmentId=" + enchantmentId + ", level=" + level + '}';
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(enchantmentId) ^ Objects.hashCode(level);
	}

	@SuppressWarnings({ "deprecation" })
	public static ArrayList<EnchantmentLevel> getAllEnchantmentLevels(ItemStack stack) {
		ArrayList<EnchantmentLevel> list = new ArrayList<>();
		Set<Object2IntMap.Entry<Holder<Enchantment>>> enchantments = stack.getEnchantments().entrySet();
		for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments) {
			list.add(new EnchantmentLevel(entry.getKey().getKey(), entry.getIntValue()));
		}
		return list;
	}

	public static final EnchantmentLevel of(String enchantment_id, int level) {
		return new EnchantmentLevel(enchantment_id, level);
	}
}