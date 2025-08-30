package lepus.mc.datagen.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;

import lepus.mc.core.registry.RegistryMap;
import lyra.klass.KlassWalker;
import lyra.lang.JavaLang;
import lyra.lang.Reflection;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface ItemDatagen {
	/**
	 * 默认材质文件名称就是注册名称
	 */
	public static final String registeredName = "'registered'";

	/**
	 * 材质文件名称
	 * 
	 * @return
	 */
	String tex_name() default registeredName;

	/**
	 * 注册类型
	 * 
	 * @return
	 */
	String model_type() default "generated";

	/**
	 * 材质文件路径
	 * 
	 * @return
	 */
	String tex_path() default "";

	public static class ModelProvider extends ItemModelProvider {
		private static final ArrayList<Class<?>> itemsClasses = new ArrayList<>();

		private ModelProvider(PackOutput output, String namespace, ExistingFileHelper helper) {
			super(output, namespace, helper);
		}

		@SuppressWarnings("rawtypes")
		protected void registerModels(Class<?> itemClass) {
			KlassWalker.walkAnnotatedFields(itemClass, ItemDatagen.class, (Field f, boolean isStatic, Object value, ItemDatagen annotation) -> {
				if (isStatic && Reflection.is(f, DeferredItem.class) && value != null) {
					DeferredItem item = (DeferredItem) value;
					if (item.getKey().location().getNamespace().equals(this.modid)) {// 物品对应材质文件只在物品注册的命名空间查找
						String tex_name = annotation.tex_name();
						if (tex_name.equals(registeredName))
							tex_name = item.getId().getPath();
						switch (annotation.model_type()) {
						case "generated":
							asGeneratedItem(tex_name, annotation.tex_path());
							break;
						case "block":
							asBlockItem(tex_name, annotation.tex_path());
							break;
						}
					}
				}
				return true;
			});
		}

		@Override
		protected void registerModels() {
			for (Class<?> itemClass : itemsClasses)
				registerModels(itemClass);
		}

		private ItemModelBuilder asGeneratedItem(String textureName, String path) {
			return getBuilder(textureName)
					.parent(new ModelFile.UncheckedModelFile("item/generated"))
					.texture("layer0", modLoc("item/" + path + '/' + textureName));
		}

		private ItemModelBuilder asBlockItem(String textureName, String path) {
			return withExistingParent(textureName, modLoc("block/" + path + '/' + textureName));
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

		public static final void forDatagen() {
			Class<?> caller = JavaLang.getOuterCallerClass();
			forDatagen(caller);
		}

		public static final void addProvider(DataGenerator generator, boolean run, PackOutput output, ExistingFileHelper helper) {
			for (String namespace : RegistryMap.namespaces()) {
				generator.addProvider(run, new ModelProvider(output, namespace, helper));
			}
		}
	}
}
