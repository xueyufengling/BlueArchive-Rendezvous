package lepus.mc.codec.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.util.Arrays;

import lyra.klass.KlassWalker;
import lyra.object.Placeholders;

/**
 * 辅助注解，用于标记将作为CODEC反序列化时使用的构造函数。<br>
 * 具有该注解的构造函数将在调用CodecAutogen.CodecGenerator.forCodec()时检查传入的ctorTypes参数类型是否和本构造函数的参数类型匹配，<br>
 * 一个类只能有一个构造函数具有此标记，若有多个构造函数具有此标记则抛出异常。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.CONSTRUCTOR })
public @interface CodecTarget {

	/**
	 * 当ctorTypes为空时，是否强制匹配构造函数的参数列表也为空。<br>
	 * 默认为false，即传入ctorTypes为空时将在CodecAutogen.CodecGenerator.generate()生成CODEC阶段自动查找匹配的构造函数。<br>
	 * 若设置为true，即便存在匹配的未标记CodecTarget的构造函数，也会立即报错。
	 * 
	 * @return
	 */
	boolean instant_check() default false;

	/**
	 * 仅当instant_check为true时生效。<br>
	 * 开启后若匹配空构造函数失败则会抛出异常中断程序。<br>
	 * 
	 * @return
	 */
	boolean abort_mismatch() default true;

	public static class TypeChecker {
		@SuppressWarnings({ "unchecked" })
		public static <C> boolean check(Class<C> targetClass, Class<?>... ctorTypes) {
			Placeholders.TypeWrapper<Constructor<C>> target = Placeholders.TypeWrapper.wrap();
			Placeholders.TypeWrapper<CodecTarget> info = Placeholders.TypeWrapper.wrap();
			KlassWalker.walkAnnotatedConstructors(targetClass, CodecTarget.class, (Constructor<C> c, CodecTarget annotation) -> {
				if (target.value == null) {
					target.value = c;
					info.value = annotation;
				} else {
					throw new IllegalStateException("Class " + targetClass.getName() + " has multiple CodecTarget annotated constructor, at most 1 is valid.");
				}
				return true;
			});
			if (target.value == null)// 若目标类没有标记CodecTarget的构造函数，则通过检查
				return true;
			CodecTarget anno = info.value;
			boolean eq = Arrays.equals(ctorTypes, target.value.getParameterTypes());
			eq = anno.instant_check() ? eq : true;// 如果未启用instant_check，则始终可以通过该类型检查方法
			if (!eq && anno.abort_mismatch())
				throw new IllegalStateException("CODEC constructor args' types mismatch. Class " + targetClass.getName() + " should declare an empty constructor.");
			return eq;
		}
	}
}
