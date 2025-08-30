package lepus.mc.codec;

import java.lang.reflect.Field;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import lyra.lang.Reflection;
import lyra.object.Placeholders;
import net.minecraft.util.KeyDispatchDataCodec;

public class CodecAccess {

	/**
	 * 访问匹配的MapCodec
	 * 
	 * @param obj
	 * @param defaultValue
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static final <T> MapCodec<?> accessMapCodecOrDefault(Object obj, MapCodec<?> defaultValue) {
		Class<T> cls;
		if (obj instanceof Class c)
			cls = c;
		else
			cls = (Class<T>) obj.getClass();
		Placeholders.TypeWrapper<MapCodec<T>> mc = Placeholders.TypeWrapper.wrap();
		CodecWalker.walkMapCodecs(cls, cls, (Field f, MapCodec<T> codec) -> {
			mc.value = codec;
			return false;
		});
		if (mc.value != null)
			return mc.value;
		return defaultValue;
	}

	@SuppressWarnings("unchecked")
	public static final <T> Codec<?> accessCodecOrDefault(Object obj, Codec<?> defaultValue) {
		Class<T> cls;
		if (obj instanceof Class c)
			cls = c;
		else
			cls = (Class<T>) obj.getClass();
		Placeholders.TypeWrapper<Codec<T>> c = Placeholders.TypeWrapper.wrap();
		CodecWalker.walkCodecs(cls, cls, (Field f, Codec<T> codec) -> {
			c.value = codec;
			return false;
		});
		if (c.value != null)
			return c.value;
		return defaultValue;
	}

	@SuppressWarnings("unchecked")
	public static final <T> KeyDispatchDataCodec<?> accessKeyDispatchDataCodecOrDefault(Object obj, KeyDispatchDataCodec<?> defaultValue) {
		Class<T> cls;
		if (obj instanceof Class c)
			cls = c;
		else
			cls = (Class<T>) obj.getClass();
		Placeholders.TypeWrapper<KeyDispatchDataCodec<T>> kddc = Placeholders.TypeWrapper.wrap();
		CodecWalker.walkKeyDispatchDataCodecs(cls, cls, (Field f, KeyDispatchDataCodec<T> codec) -> {
			kddc.value = codec;
			return false;
		});
		if (kddc.value != null)
			return kddc.value;
		return defaultValue;
	}

	/**
	 * 访问targetClass中的静态字段fieldName，并将其解释为codecTypeClass的MapCodec<br>
	 * 可用于在父类中访问子类的静态CODEC字段<br>
	 * 
	 * @param <O>
	 * @param targetClass
	 * @param codecTypeClass
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	public static final <O> MapCodec<O> accessStaticMapCodec(Class<?> targetClass, Class<O> codecTypeClass) {
		Placeholders.TypeWrapper<MapCodec<O>> derivedCodec = Placeholders.TypeWrapper.wrap();
		CodecWalker.walkMapCodecs(targetClass, (Field f, MapCodec<?> codec, Class<?> codecType) -> {
			if (codec != null && Reflection.is(codecType, codecTypeClass)) {
				derivedCodec.value = (MapCodec<O>) codec;
				return false;
			}
			return true;
		});
		if (derivedCodec.value == null)
			throw new NullPointerException("Accessed type " + codecTypeClass.getName() + " MapCodec for class " + targetClass.getName() + " is null, make sure you have specified or generated a correct MapCodec.");
		return derivedCodec.value;
	}

	@SuppressWarnings({ "unchecked" })
	public static final <O> Codec<O> accessStaticCodec(Class<?> targetClass, Class<O> codecTypeClass) {
		Placeholders.TypeWrapper<Codec<O>> derivedCodec = Placeholders.TypeWrapper.wrap();
		CodecWalker.walkCodecs(targetClass, (Field f, Codec<?> codec, Class<?> codecType) -> {
			if (codec != null && Reflection.is(codecType, codecTypeClass)) {
				derivedCodec.value = (Codec<O>) codec;
				return false;
			}
			return true;
		});
		if (derivedCodec.value == null)
			throw new NullPointerException("Accessed type " + codecTypeClass.getName() + " Codec for class " + targetClass.getName() + " is null, make sure you have specified or generated a correct MapCodec.");
		return derivedCodec.value;
	}

	@SuppressWarnings("unchecked")
	public static final <O> KeyDispatchDataCodec<O> accessStaticKeyDispatchDataCodec(Class<?> targetClass, Class<O> codecTypeClass) {
		Placeholders.TypeWrapper<KeyDispatchDataCodec<O>> derivedCodec = Placeholders.TypeWrapper.wrap();
		CodecWalker.walkKeyDispatchDataCodecs(targetClass, (Field f, KeyDispatchDataCodec<?> codec, Class<?> codecType) -> {
			if (codec != null && Reflection.is(codecType, codecTypeClass)) {
				derivedCodec.value = (KeyDispatchDataCodec<O>) codec;
				return false;
			}
			return true;
		});
		if (derivedCodec.value == null)
			throw new NullPointerException("Accessed type " + codecTypeClass.getName() + " KeyDispatchDataCodec for class " + targetClass.getName() + " is null, make sure you have specified or generated a correct MapCodec.");
		return derivedCodec.value;
	}

	public static final <O> Object accessStatic(Class<?> targetClass, Class<?> codecClass, Class<O> codecTypeClass) {
		if (codecClass == Codec.class)
			return accessStaticCodec(targetClass, codecTypeClass);
		else if (codecClass == MapCodec.class)
			return accessStaticMapCodec(targetClass, codecTypeClass);
		else if (codecClass == KeyDispatchDataCodec.class)
			return accessStaticKeyDispatchDataCodec(targetClass, codecTypeClass);
		else
			return null;
	}
}
