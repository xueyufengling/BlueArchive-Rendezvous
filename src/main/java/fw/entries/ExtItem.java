package fw.entries;

import java.lang.reflect.Field;

import fw.core.Core;
import fw.datagen.ItemDatagen;
import fw.datagen.Localizable;
import lyra.klass.KlassWalker;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/**
 * 没有任何功能的简单物品，通常是材料
 */
public class ExtItem extends Item implements Localizable {

	public ExtItem(Item.Properties properties) {
		super(properties);
	}

	public static class ModelProvider extends ItemModelProvider {
		public ModelProvider(PackOutput output, ExistingFileHelper helper) {
			super(output, Core.ModId, helper);
		}

		@Override
		protected void registerModels() {
			for (Class<? extends ExtItems> itemClass : ExtItems.itemsClasses)
				KlassWalker.walkFields(itemClass, ItemDatagen.class, (Field f, boolean isStatic, Object value, ItemDatagen annotation) -> {
					if (value != null) {
						switch (annotation.type()) {
						case "generated":
							asGeneratedItem(annotation.name(), annotation.path());
							break;
						case "block":
							asBlockItem(annotation.name(), annotation.path());
							break;
						}
					}
				});
		}

		private ItemModelBuilder asGeneratedItem(String registeredName, String path) {
			return getBuilder(registeredName)
					.parent(new ModelFile.UncheckedModelFile("item/generated"))
					.texture("layer0", modLoc("item/" + path + '/' + registeredName));
		}

		private ItemModelBuilder asBlockItem(String registeredName, String path) {
			return withExistingParent(registeredName, modLoc("block/" + path + '/' + registeredName));
		}
	}

	@Override
	public String localizationKey() {
		return this.getDescriptionId();
	}
}
