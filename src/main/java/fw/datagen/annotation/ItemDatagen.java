package fw.datagen.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.ArrayList;

import fw.core.Core;
import lyra.klass.KlassWalker;
import lyra.lang.GenericTypes;
import lyra.lang.Reflection;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@Retention(RetentionPolicy.RUNTIME)
public @interface ItemDatagen {
	/**
	 * 注册条目名称
	 * 
	 * @return
	 */
	String name();

	/**
	 * 注册类型
	 * 
	 * @return
	 */
	String type() default "generated";

	String path() default "";

	public static class ModelProvider extends ItemModelProvider {
		private static final ArrayList<Class<?>> itemsClasses = new ArrayList<>();

		public ModelProvider(PackOutput output, ExistingFileHelper helper) {
			super(output, Core.ModId, helper);
		}

		@Override
		protected void registerModels() {
			for (Class<?> itemClass : itemsClasses)
				KlassWalker.walkFields(itemClass, ItemDatagen.class, (Field f, boolean isStatic, Object value, ItemDatagen annotation) -> {
					if (Reflection.is(f, DeferredItem.class) && value != null) {
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

		/**
		 * 注册数据生成
		 * 
		 * @param itemClass
		 */
		public static final void forDatagen(Class<?> itemClass) {
			if (!itemsClasses.contains(itemClass))
				itemsClasses.add(itemClass);
		}
	}
}
