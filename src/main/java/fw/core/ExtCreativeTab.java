package fw.core;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

public interface ExtCreativeTab extends Localizable {

	public static String header = "itemGroup";

	public static class Definition {

		public final String id;
		public final DeferredHolder<CreativeModeTab, CreativeModeTab> registry;

		private ExtCreativeTab derived;
		private String iconItem;
		private ArrayList<DeferredItem<Item>> itemsList = new ArrayList<>();

		private CreativeModeTab.Builder builder;

		private Definition(ExtCreativeTab derived, String id, String iconItem) {
			this.derived = derived;
			this.id = id;
			this.iconItem = iconItem;
			builder = new CreativeModeTab.Builder(CreativeModeTab.Row.TOP, 0);
			registry = RegistryFactory.CREATIVE_TABS.register(id, () -> {
				return builder
						.icon(() -> new ItemStack((ExtItems.contains(this.iconItem) ? ExtItems.get(this.iconItem) : ExtItems.register(this.iconItem)).get()))
						.title(this.derived.localizedComponent())
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
			return "CreativeModeTab{ id=" + id + ", iconItem=" + iconItem + ", localizationKey=" + derived.localizationKey() + " }";
		}

		private static final HashMap<String, Definition> definitions = new HashMap<>();

		public static final Definition get(String id) {
			return definitions.get(id);
		}

		public static final Definition define(ExtCreativeTab derived, String id, String iconItem) {
			// 第一次调用该方法时，此时derived.definition()为null，因此避免在本方法内使用derived.definition()
			Definition def = Definition.get(id);
			if (def == null) {
				def = new Definition(derived, id, iconItem);
				definitions.put(id, def);
			} else {
				def.derived = derived;
				def.iconItem = iconItem;
			}
			return def;
		}
	}

	/**
	 * 获取Definition实例，如果不存在id对应的Definition则创建，如果存在对应id的Definition则直接返回已经存在的
	 * 
	 * @param derived
	 * @param id
	 * @param iconItem
	 * @return
	 */
	public static Definition define(ExtCreativeTab derived, String id, String iconItem) {
		return Definition.define(derived, id, iconItem);
	}

	/**
	 * 子类需要有正确的Definition对象
	 * 
	 * @return
	 */
	public abstract Definition definition();

	public default ExtCreativeTab append(DeferredItem<Item> item) {
		definition().itemsList.add(item);
		return this;
	}

	@Override
	public default String localizationKey() {
		return header + "." + Core.ModId + "." + definition().id;
	}
}
