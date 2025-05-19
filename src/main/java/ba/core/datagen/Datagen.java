package ba.core.datagen;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Datagen {
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
	String type() default "basic";
}
