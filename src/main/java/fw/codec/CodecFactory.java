package fw.codec;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Stream;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;

import lyra.lang.JavaLang;
import lyra.lang.Reflection;
import lyra.lang.internal.HandleBase;
import lyra.object.Placeholders;

public class CodecFactory {

	/**
	 * 没有任何field的空CODEC
	 * 
	 * @param <O>
	 * @param target
	 * @return
	 */
	public static final <O> MapCodec<O> emptyMapCodec(Class<O> target) {
		return new MapCodec<O>() {

			/**
			 * 反序列化，直接根据空构造函数构造对象
			 */
			@Override
			public <T> DataResult<O> decode(DynamicOps<T> ops, MapLike<T> input) {
				try {
					return (DataResult<O>) DataResult.success((O) HandleBase.findConstructor(target).invoke());
				} catch (Throwable ex) {
					ex.printStackTrace();
				}
				throw new IllegalArgumentException("Generate empty CODEC failed. No empty construct found in " + target);
			}

			@Override
			public <T> RecordBuilder<T> encode(O input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
				return prefix;
			}

			@Override
			@SuppressWarnings("unchecked")
			public <T> Stream<T> keys(DynamicOps<T> ops) {
				return (Stream<T>) List.of().parallelStream();
			}

			@Override
			public String toString() {
				return "EMPTY_MAPCODEC";
			}
		};
	}

	@SuppressWarnings("unchecked")
	public static final <O> MapCodec<O> emptyMapCodec() {
		Class<O> caller = (Class<O>) JavaLang.getOuterCallerClass();
		return emptyMapCodec(caller);
	}

	public static final <O> Codec<O> emptyCodec(Class<O> target) {
		return (Codec<O>) emptyMapCodec(target).codec();
	}

	/**
	 * 访问targetClass中的静态字段fieldName，并将其解释为codecTypeClass的MapCodec<br>
	 * 可用于在父类中访问子类的静态CODEC字段<br>
	 * 
	 * @param <O>
	 * @param targetClass
	 * @param codecTypeClass
	 * @param fieldName
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static final <O> MapCodec<O> accessMapCodec(Class<?> targetClass, Class<O> codecTypeClass, String fieldName) {
		Placeholders.TypeWrapper<MapCodec<O>> derivedCodec = Placeholders.TypeWrapper.wrap();
		CodecWalker.walkMapCodecs(targetClass, (Field f, MapCodec codec, Class<?> codecType) -> {
			if (codec != null && Reflection.is(codecType, codecTypeClass)) {
				derivedCodec.value = codec;
				return false;
			}
			return true;
		});
		if (derivedCodec.value == null)
			throw new NullPointerException("Accessed type " + codecTypeClass.getName() + " CODEC for class " + targetClass.getName() + " is null, make sure you have specified or generated a correct MapCodec.");
		return derivedCodec.value;
	}

	public static final <O> MapCodec<O> accessMapCodec(Class<?> targetClass, Class<O> codecTypeClass) {
		return accessMapCodec(targetClass, codecTypeClass, Codecs.DEFAULT_CODEC_FIELD);
	}
}
