package lepus.mc.codec.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 具有该注解的类在数据生成时不会生成独立文件，而是整体作为一个字段属性存在于json文件中<br>
 * 纯编译时标记注解<br>
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.TYPE, ElementType.PARAMETER })
public @interface AsDataField {

}
