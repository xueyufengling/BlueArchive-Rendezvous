package ba.core;

import java.lang.reflect.Field;

import ba.core.datagen.Datagen;
import jvm.klass.KlassWalker;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/**
 * 没有任何功能的简单物品，通常是材料
 */
public class ExtItem extends Item {

	public ExtItem(Item.Properties properties) {
		super(properties);
	}

	public static class ModelProvider extends ItemModelProvider {
		public ModelProvider(PackOutput output, ExistingFileHelper helper) {
			super(output, Core.ModId, helper);
		}

		@Override
		protected void registerModels() {
			System.err.println("in registerModels");
			for (Class<? extends ExtItems> itemClass : ExtItems.itemsClasses)
				KlassWalker.walkFields(itemClass, Datagen.class, (Field f, boolean isStatic, Object value, Datagen annotation) -> {
					System.err.println("value " + value + " ano " + annotation);
					if (value != null) {
						switch (annotation.type()) {
						case "basic":
							basicItem(ExtItems.get(annotation.name()).get());

							break;
						case "parent":
							withExistingParent(annotation.name(), modLoc("block/" + annotation.name()));
							break;
						}
					}
				});
		}
	}
}
