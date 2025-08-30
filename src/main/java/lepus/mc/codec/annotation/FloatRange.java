package lepus.mc.codec.annotation;

public @interface FloatRange {
	float min() default Float.MIN_VALUE;

	float max() default Float.MAX_VALUE;
};