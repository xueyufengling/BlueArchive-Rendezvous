package fw.codec;

import java.lang.reflect.Field;

import com.mojang.serialization.MapCodec;

import lyra.klass.GenericTypes;
import lyra.klass.KlassWalker;

public class CodecWalker {

	@FunctionalInterface
	public static interface MapCodecOperation {
		/**
		 * 对MapCodec类型的注册表进行操作
		 * 
		 * @param f           注册表字段
		 * @param registryKey 注册表的键
		 * @param codecType   Codec的类型
		 */
		@SuppressWarnings("rawtypes")
		public boolean operate(Field f, MapCodec codec, Class<?> codecType);
	}

	/**
	 * 专门用于遍历注册类型为MapCodec的注册表
	 * 
	 * @param op
	 */
	@SuppressWarnings("rawtypes")
	public static final void walkMapCodecs(Class<?> target, MapCodecOperation op) {
		KlassWalker.walkTypeFields(target, MapCodec.class, (Field f, boolean isStatic, MapCodec codec) -> {
			if (isStatic) {
				return op.operate(f, codec, GenericTypes.getFirstGenericType(f));
			}
			return true;
		});
	}
}
