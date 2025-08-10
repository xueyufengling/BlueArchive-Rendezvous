package fw.codec;

import java.lang.reflect.Field;

import com.mojang.serialization.MapCodec;

import lyra.klass.KlassWalker;
import lyra.lang.Reflection;

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
}
