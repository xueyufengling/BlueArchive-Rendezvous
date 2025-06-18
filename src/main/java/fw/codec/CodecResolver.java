package fw.codec;

import java.lang.reflect.Type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import fw.codec.annotation.CodecAutogen.CodecGenerator;
import net.minecraft.util.KeyDispatchDataCodec;

/**
 * 根据额外参数确定具体某个类型的CODEC
 * 
 * @param <T>
 */
@FunctionalInterface
public interface CodecResolver<T> {
	public Entry resolve(Type type, T param);

	/**
	 * 打包某个字段的CODEC以及是否是可选字段
	 */
	public static final class Entry {
		public static enum Type {
			DEFAULT(-1),
			OPTINAL_NOERR(0),
			OPTIONAL(1),
			REQUIRED(2);

			int priority;

			private Type(int priority) {
				this.priority = priority;
			}

			public static final Type of(int priority) {
				switch (priority) {
				case -1:
					return DEFAULT;
				case 0:
					return OPTINAL_NOERR;
				case 1:
					return OPTIONAL;
				case 2:
					return REQUIRED;
				}
				return null;
			}

			/**
			 * 决定最终采用哪个类型。如果两个都是DEFAULT，那么就采用REQUIRED；<br>
			 * 如果其中一个是DEFAULT，则直接采用另一个；<br>
			 * 如果两个都不是DEFAULT，则采用priority最大的那个。<br>
			 * 
			 * @param t1
			 * @param t2
			 * @return
			 */
			public static final Type resolve(Type t1, Type t2) {
				int p = Math.max(t1.priority, t2.priority);
				if (p == -1)
					p = 2;
				return of(p);
			}
		}

		public final Object codec;
		public final Class<?> codecType;
		public final Type type;

		private Entry(Object codec, Type type) {
			this.codec = codec;
			this.codecType = codec.getClass();
			this.type = type;
		}

		private Entry(Object codec) {
			this(codec, Type.DEFAULT);
		}

		public final boolean isCodec() {
			return codecType == Codec.class;
		}

		public final boolean isMapCodec() {
			return codecType == MapCodec.class;
		}

		public final boolean isKeyDispatchDataCodec() {
			return codecType == KeyDispatchDataCodec.class;
		}

		public final Codec<?> codec() {
			if (codec instanceof Codec c)
				return c;
			return null;
		}

		public final MapCodec<?> mapCodec() {
			if (codec instanceof MapCodec mc)
				return mc;
			return null;
		}

		public final KeyDispatchDataCodec<?> keyDispatchDataCodec() {
			if (codec instanceof KeyDispatchDataCodec kddc)
				return kddc;
			return null;
		}

		public final Codec<?> asCodec() {
			return CodecGenerator.asCodec(codec);
		}

		public static final Entry of(Object codec, Type type) {
			return new Entry(codec, type);
		}

		public static final Entry of(Object codec) {
			return new Entry(codec);
		}
	}
}
