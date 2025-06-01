package fw.datagen.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import fw.core.Core;
import fw.datagen.Localizable;
import lyra.klass.KlassWalker;
import lyra.klass.ObjectManipulator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;

@Retention(RetentionPolicy.RUNTIME)
public @interface LangDatagen {
	public static final String undefined = "'undefined'";

	String zh_cn() default undefined;

	String en_us() default undefined;

	public static class Resolver {
		public static final String resolve(LangDatagen annotation, String locale) {
			String description = (String) ObjectManipulator.invoke(annotation, locale, null);
			if (LangDatagen.undefined.equals(description))
				description = null;
			return description;
		}
	}

	public static class LangProvider extends LanguageProvider {
		static final HashMap<String, HashMap<String, String>> keyvalsMap = new HashMap<>();
		static final ArrayList<Class<?>> langClasses = new ArrayList<>();

		String locale;

		public LangProvider(PackOutput output, String locale) {
			super(output, Core.ModId, locale);
			this.locale = locale;
		}

		@Override
		protected void addTranslations() {
			for (Class<?> langClass : langClasses) {
				KlassWalker.walkFields(langClass, LangDatagen.class, (Field f, boolean isStatic, Object value, LangDatagen annotation) -> {
					if (value instanceof Localizable localizable) {// 优先使用Localizable指定的key
						super.add(localizable.localizationKey(), LangDatagen.Resolver.resolve(annotation, locale));
					} else if (value instanceof DeferredHolder deferredHolder) {
						super.add(Localizable.localizationKey(deferredHolder), LangDatagen.Resolver.resolve(annotation, locale));
					}
				});
			}
			HashMap<String, String> map = keyvalsMap.get(locale);
			if (map != null) {
				for (Entry<String, String> entry : map.entrySet()) {
					super.add(entry.getKey(), entry.getValue());
				}
			}
		}

		public static void forDatagen(Class<?> langClass) {
			if (!langClasses.contains(langClass))
				langClasses.add(langClass);
		}

		public static void addTranslation(String locale, String key, String value) {
			HashMap<String, String> map = keyvalsMap.get(locale);
			if (map == null) {
				map = new HashMap<>();
				keyvalsMap.put(locale, map);
			}
			map.put(key, value);
		}
	}
}
