package lepus.mc.items;

import java.util.ArrayList;
import java.util.HashMap;

import lepus.mc.core.registry.RegistryMap;
import lepus.mc.datagen.Localizable;
import lyra.klass.special.BaseClass;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

/**
 * 创造模式物品栏
 */
public interface ExtCreativeTab extends BaseClass<ExtCreativeTab.Definition>, Localizable {
	public static final RegistryMap<CreativeModeTab> CREATIVE_TABS = RegistryMap.of(Registries.CREATIVE_MODE_TAB);

	public static String header = "itemGroup";

	/**
	 * 物品栏的定义
	 */
	class Definition extends BaseClass.Definition<ExtCreativeTab> {
		/**
		 * 创造模式物品栏ID，带命名空间
		 */
		public final String id;
		public final DeferredHolder<CreativeModeTab, CreativeModeTab> deferredHolder;

		private String iconItem;
		private ArrayList<DeferredItem<?>> itemsList = new ArrayList<>();

		private CreativeModeTab.Builder builder;

		/**
		 * @param id       带命名空间的ID
		 * @param iconItem
		 */
		private Definition(String id, String iconItem) {
			this.id = id;
			this.iconItem = iconItem;
			builder = new CreativeModeTab.Builder(CreativeModeTab.Row.TOP, 0);
			deferredHolder = CREATIVE_TABS.register(id, () -> {
				return builder
						.icon(() -> new ItemStack((ExtItem.ITEMS.contains(this.iconItem) ? ExtItem.ITEMS.get(this.iconItem) : ExtItem.registerMod(this.iconItem)).get()))
						.title(this_.localizedComponent())
						.displayItems((featureFlagSet, tabOutput) -> {
							this.itemsList.forEach(item -> tabOutput.accept(new ItemStack(item.get())));
						})
						.build();
			});
		}

		public final String iconItem() {
			return this.iconItem;
		}

		@Override
		public String toString() {
			return "CreativeModeTab{ id=" + id + ", iconItem=" + iconItem + ", localizationKey=" + this_.localizationKey() + " }";
		}
	}

	static final HashMap<String, ExtCreativeTab> creative_tabs = new HashMap<>();

	public static ExtCreativeTab get(String id) {
		return creative_tabs.get(id);
	}

	/**
	 * 定义一个物品栏，一个id的物品栏只会定义和注册一次
	 * 
	 * @param derived
	 * @param id
	 * @param iconItem
	 * @return
	 */
	static ExtCreativeTab define(ExtCreativeTab derived, String id, String iconItem) {
		derived.construct(Definition.class, new Class<?>[] { String.class, String.class }, id, iconItem);
		creative_tabs.put(id, derived);
		return derived;
	}

	public default ExtCreativeTab append(DeferredItem<?> item) {
		definition().itemsList.add(item);
		return this;
	}

	@Override
	public default String localizationType() {
		return header;
	}

	@Override
	public default String localizationPath() {
		return Localizable.stdLocalizationKey(definition().id);
	}
}
