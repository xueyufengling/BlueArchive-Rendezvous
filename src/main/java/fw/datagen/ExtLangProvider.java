package fw.datagen;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import fw.core.Core;
import lyra.klass.KlassWalker;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;

public interface ExtLangProvider {
	static final HashMap<String, HashMap<String, String>> keyvalsMap = new HashMap<>();
	static final ArrayList<Class<? extends ExtLangProvider>> langClasses = new ArrayList<>();

	public static class LangProvider extends LanguageProvider {
		String locale;

		public LangProvider(PackOutput output, String locale) {
			super(output, Core.ModId, locale);
			this.locale = locale;
		}

		@Override
		protected void addTranslations() {
			for (Class<? extends ExtLangProvider> langClass : ExtLangProvider.langClasses) {
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
	}

	public static void forDatagen(Class<? extends ExtLangProvider> langClass) {
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
