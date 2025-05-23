package fw.datagen;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import lyra.klass.ObjectManipulator;

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
}
