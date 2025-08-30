package lepus.mc.codec;

import java.lang.reflect.Field;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import lyra.klass.KlassWalker;
import lyra.lang.Reflection;
import net.minecraft.util.KeyDispatchDataCodec;

public class CodecWalker {

	@FunctionalInterface
	public static interface GenericMapCodecOperation {
		/**
		 * 对MapCodec类型的注册表进行操作
		 * 
		 * @param f           注册表字段
		 * @param registryKey 注册表的键
		 * @param codecType   Codec的类型
		 */
		public boolean operate(Field f, MapCodec<?> codec, Class<?> codecType);
	}

	/**
	 * 专门用于遍历注册类型为MapCodec的注册表
	 * 
	 * @param op
	 */
	@SuppressWarnings("rawtypes")
	public static final void walkMapCodecs(Class<?> target, GenericMapCodecOperation op) {
		KlassWalker.walkGenericFields(target, MapCodec.class, (Field f, boolean isStatic, Class<?> codecType, MapCodec codec) -> {
			if (isStatic) {
				return op.operate(f, codec, codecType);
			}
			return true;
		});
	}

	@FunctionalInterface
	public static interface MapCodecOperation<C> {
		/**
		 * 对MapCodec类型的注册表进行操作
		 * 
		 * @param f           注册表字段
		 * @param registryKey 注册表的键
		 * @param codecType   Codec的类型
		 */
		public boolean operate(Field f, MapCodec<C> codec);
	}

	@SuppressWarnings({ "unchecked" })
	public static final <C> void walkMapCodecs(Class<?> target, Class<C> targetCodecType, MapCodecOperation<C> op) {
		walkMapCodecs(target, (Field f, MapCodec<?> codec, Class<?> codecType) -> {
			if (Reflection.is(codecType, targetCodecType)) {
				return op.operate(f, (MapCodec<C>) codec);
			}
			return true;
		});
	}

	@FunctionalInterface
	public static interface GenericCodecOperation {
		/**
		 * 对Codec类型的注册表进行操作
		 * 
		 * @param f           注册表字段
		 * @param registryKey 注册表的键
		 * @param codecType   Codec的类型
		 */
		public boolean operate(Field f, Codec<?> codec, Class<?> codecType);
	}

	/**
	 * 专门用于遍历注册类型为MapCodec的注册表
	 * 
	 * @param op
	 */
	@SuppressWarnings("rawtypes")
	public static final void walkCodecs(Class<?> target, GenericCodecOperation op) {
		KlassWalker.walkGenericFields(target, Codec.class, (Field f, boolean isStatic, Class<?> codecType, Codec codec) -> {
			if (isStatic) {
				return op.operate(f, codec, codecType);
			}
			return true;
		});
	}

	@FunctionalInterface
	public static interface CodecOperation<C> {
		/**
		 * 对Codec类型的注册表进行操作
		 * 
		 * @param f           注册表字段
		 * @param registryKey 注册表的键
		 * @param codecType   Codec的类型
		 */
		public boolean operate(Field f, Codec<C> codec);
	}

	@SuppressWarnings({ "unchecked" })
	public static final <C> void walkCodecs(Class<?> target, Class<C> targetCodecType, CodecOperation<C> op) {
		walkCodecs(target, (Field f, Codec<?> codec, Class<?> codecType) -> {
			if (Reflection.is(codecType, targetCodecType)) {
				return op.operate(f, (Codec<C>) codec);
			}
			return true;
		});
	}

	@FunctionalInterface
	public static interface GenericKeyDispatchDataCodecOperation {
		/**
		 * 对KeyDispatchDataCodec类型的注册表进行操作
		 * 
		 * @param f           注册表字段
		 * @param registryKey 注册表的键
		 * @param codecType   Codec的类型
		 */
		public boolean operate(Field f, KeyDispatchDataCodec<?> codec, Class<?> codecType);
	}

	/**
	 * 专门用于遍历注册类型为MapCodec的注册表
	 * 
	 * @param op
	 */
	@SuppressWarnings("rawtypes")
	public static final void walkKeyDispatchDataCodecs(Class<?> target, GenericKeyDispatchDataCodecOperation op) {
		KlassWalker.walkGenericFields(target, KeyDispatchDataCodec.class, (Field f, boolean isStatic, Class<?> codecType, KeyDispatchDataCodec codec) -> {
			if (isStatic) {
				return op.operate(f, codec, codecType);
			}
			return true;
		});
	}

	@FunctionalInterface
	public static interface KeyDispatchDataCodecOperation<C> {
		/**
		 * 对KeyDispatchDataCodec类型的注册表进行操作
		 * 
		 * @param f           注册表字段
		 * @param registryKey 注册表的键
		 * @param codecType   Codec的类型
		 */
		public boolean operate(Field f, KeyDispatchDataCodec<C> codec);
	}

	@SuppressWarnings({ "unchecked" })
	public static final <C> void walkKeyDispatchDataCodecs(Class<?> target, Class<C> targetCodecType, KeyDispatchDataCodecOperation<C> op) {
		walkKeyDispatchDataCodecs(target, (Field f, KeyDispatchDataCodec<?> codec, Class<?> codecType) -> {
			if (Reflection.is(codecType, targetCodecType)) {
				return op.operate(f, (KeyDispatchDataCodec<C>) codec);
			}
			return true;
		});
	}
}
