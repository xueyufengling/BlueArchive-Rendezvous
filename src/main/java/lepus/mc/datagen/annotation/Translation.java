package lepus.mc.datagen.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface Translation {
	public static final String undefined = "'undefined'";

	/**
	 * 语言类型
	 * 
	 * @return
	 */
	String locale();

	/**
	 * 该语言对应翻译的文本内容
	 * 
	 * @return
	 */
	String text() default undefined;

	public static class Resolver {
		public static final String resolveText(Translation translation) {
			return undefined.equals(translation.text()) ? null : translation.text();
		}
	}

	public static final String EN_US = "en_us";
	public static final String ZH_CN = "zh_cn";
}
