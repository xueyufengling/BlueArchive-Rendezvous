
package lepus.mc.datagen.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import lepus.mc.core.registry.RegistryMap;
import lepus.mc.datagen.Localizable;
import lyra.klass.KlassWalker;
import lyra.lang.JavaLang;
import net.minecraft.core.Holder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface LangDatagen {

	Translation[] translations() default {
	};

	public static class LangProvider extends LanguageProvider {
		static final HashMap<String, HashMap<String, String>> keyvalsMap = new HashMap<>();
		static final ArrayList<Class<?>> langClasses = new ArrayList<>();
		static final ArrayList<String> genLangs = new ArrayList<>();

		String namespace;
		String locale;

		private LangProvider(PackOutput output, String namespace, String locale) {
			super(output, namespace, locale);
			this.namespace = namespace;
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
								if (this.namespace.equals(localizable.namespace())) {
									final_text = Translation.Resolver.resolveText(translation);
									if (final_text != null)
										super.add(localizable.localizationKey(), final_text);
								}
							} else if (value instanceof Holder holder) {
								if (this.namespace.equals(holder.getKey().location().getNamespace())) {
									final_text = Translation.Resolver.resolveText(translation);
									if (final_text != null)
										super.add(Localizable.localizationKey(holder), final_text);
								}
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

		public static final void forDatagen() {
			Class<?> caller = JavaLang.getOuterCallerClass();
			forDatagen(caller);
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

		public static final void addProvider(DataGenerator generator, boolean run, PackOutput output) {
			for (String namespace : RegistryMap.namespaces()) {
				for (String lang : genLangs)
					generator.addProvider(run, new LangProvider(output, namespace, lang));
			}
		}
	}
}
