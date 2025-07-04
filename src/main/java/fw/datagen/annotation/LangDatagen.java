package fw.datagen.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import fw.core.Core;
import fw.datagen.Localizable;
import lyra.klass.KlassWalker;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface LangDatagen {

	Translation[] translations() default {};

	public static class LangProvider extends LanguageProvider {
		static final HashMap<String, HashMap<String, String>> keyvalsMap = new HashMap<>();
		static final ArrayList<Class<?>> langClasses = new ArrayList<>();
		static final ArrayList<String> genLangs = new ArrayList<>();

		String locale;

		public LangProvider(PackOutput output, String locale) {
			super(output, Core.ModId, locale);
			this.locale = locale;
		}

		@Override
		protected void addTranslations() {
			for (Class<?> langClass : langClasses) {
				KlassWalker.walkAnnotatedFields(langClass, LangDatagen.class, (Field f, boolean isStatic, Object value, LangDatagen annotation) -> {
					Translation[] translations = annotation.translations();
					for (Translation translation : translations) {
						if (locale.equals(translation.locale())) {// 当前Translation的语言是LangProvider的语言
							String final_text = null;
							if (value instanceof Localizable localizable) {// 优先使用Localizable指定的key
								final_text = Translation.Resolver.resolveText(translation);
								if (final_text != null)
									super.add(localizable.localizationKey(), final_text);
							} else if (value instanceof Holder holder) {
								final_text = Translation.Resolver.resolveText(translation);
								if (final_text != null)
									super.add(Localizable.localizationKey(holder), final_text);
							}
						}
					}
					return true;
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

		public static final void genLang(String lang) {
			String formatLang = lang.toLowerCase();
			if (!genLangs.contains(formatLang))
				genLangs.add(formatLang);
		}

		public static final void genLangs(String... langs) {
			for (String lang : langs)
				genLang(lang);
		}
	}
}
