package fw.codec.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标记需要序列化的字段，字段的声明顺序、类型需要和构造函数的参数顺序、类型严格保持一致。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface CodecEntry {
	public static final String pathSeparator = "/";

	/**
	 * 默认字段名称就是key名称
	 */
	public static final String fieldName = "'field'";

	/**
	 * CODEC的key
	 * 
	 * @return
	 */
	String key() default fieldName;

	/**
	 * 该键的路径，使用pathSeparator分割，用于创建嵌套
	 * 
	 * @return
	 */
	String path() default "";

	/**
	 * 是否是可选字段
	 * 
	 * @return
	 */
	boolean is_optional() default false;

	/**
	 * 附带参数
	 * 
	 * @return
	 */
	IntRange[] int_range() default {};

	FloatRange[] float_range() default {};

	/**
	 * 限制double值范围
	 */
	DoubleRange[] double_range() default {};

	/**
	 * 字符串长度
	 */
	StringLength[] string_length() default {};

	/**
	 * List尺寸
	 * 
	 * @return
	 */
	ListSize[] list_size() default {};
}
