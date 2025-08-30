package lepus.mc.codec;

import java.lang.reflect.Type;
import java.util.HashMap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import lepus.mc.codec.annotation.CodecEntry;
import lepus.mc.core.Core;
import lyra.object.ObjectManipulator;
import net.minecraft.util.KeyDispatchDataCodec;

/**
 * 根据额外参数确定具体某个类型的CODEC
 * 
 * @param <T>
 */
@FunctionalInterface
public interface CodecResolver<T> {
	public Entry resolve(Type fieldType, CodecType codecType, T param);

	public static CodecResolver<CodecEntry> findInRawClass(Class<?> targetClass, Object default_codec_if_not_exist) {
		return (Type fieldType, CodecType codecType, CodecEntry param) -> {
			Object result = Database.fetch(targetClass, codecType);
			result = result == null ? default_codec_if_not_exist : result;
			if (param == null)
				return Entry.of(result);
			else
				return Entry.of(result, param.type());
		};
	}

	public static final String DEFAULT_HOLDER_CODEC_FIELD = "CODEC";

	public static final String DEFAULT_DIRECT_CODEC_FIELD = "DIRECT_CODEC";

	public static final String DEFAULT_HOLDER_SET_CODEC_FIELD = "LIST_CODEC";

	static final HashMap<CodecType, HashMap<Class<?>, String>> CodecFields = new HashMap<>();

	public static enum CodecType {
		/**
		 * 对类对象直接编解码
		 */
		DIRECT(DEFAULT_DIRECT_CODEC_FIELD),

		/**
		 * 对类对象的Holder进行编解码
		 */
		HOLDER(DEFAULT_HOLDER_CODEC_FIELD),

		/**
		 * 对类对象的HolderSet进行编解码
		 */
		HOLDER_SET(DEFAULT_HOLDER_SET_CODEC_FIELD),

		/**
		 * CODEC字段默认的编解码器，有Holder的编解码器时该字段为编码Holder的编解码器，若没有则是编码类对象本身的（即DIRECT_CODEC）
		 */
		DEFAULT(null);

		private final String defaultField;
		private final HashMap<Class<?>, String> fieldsMap;

		private CodecType(String defaultField) {
			this.defaultField = defaultField;
			this.fieldsMap = CodecFields.computeIfAbsent(this, (CodecType t) -> new HashMap<>());
		}

		public HashMap<Class<?>, String> fieldsMap() {
			return fieldsMap;
		}

		/**
		 * 获取本枚举对应的CODEC字段名称
		 * 
		 * @param targetClass
		 * @return
		 */
		public String getField(Class<?> targetClass) {
			return fieldsMap.computeIfAbsent(targetClass, (Class<?> t) -> this.defaultField);
		}

		public String setField(Class<?> targetClass, String f) {
			return fieldsMap.put(targetClass, f);
		}

		/**
		 * 获取本枚举对应的CODEC
		 * 
		 * @param targetClass
		 * @param default_codec
		 * @return
		 */
		public Object accessOrDefault(Class<?> targetClass, Object default_codec) {
			return ObjectManipulator.accessOrDefault(targetClass, getField(targetClass), default_codec);
		}

		public Object access(Class<?> targetClass) {
			return accessOrDefault(targetClass, null);
		}

		public Object getCache(Class<?> targetClass) {
			return Database.codecs(this).get(targetClass);
		}
	}

	/**
	 * 记录每个类的各种编解码器
	 */
	public static final class Database {
		private static final HashMap<CodecType, HashMap<Class<?>, Object>> CODECS = new HashMap<>();

		public static HashMap<Class<?>, Object> codecs(CodecType targetType) {
			return CODECS.computeIfAbsent(targetType, (CodecType t) -> new HashMap<>());
		}

		/**
		 * 从类中查找指定类型的CODEC并储存到Map中
		 * 第一次传入一个类时，将查找其所有CODEC并储存
		 * 
		 * @param target
		 */
		public static final void storeAllFoundInClass(Class<?> target) {
			for (CodecType type : CodecType.values()) {
				HashMap<Class<?>, Object> codecMap = codecs(type);
				if (type == CodecType.DEFAULT) {
					HashMap<Class<?>, Object> h_codecs = codecs(CodecType.HOLDER);
					HashMap<Class<?>, Object> d_codecs = codecs(CodecType.DIRECT);
					Object hc = h_codecs.get(target);
					Object dc = d_codecs.get(target);
					String hf = CodecType.HOLDER.getField(target);
					if (dc == null && hc != null) {// 只有一个名为CODEC的直接编解码器
						CodecType.DIRECT.setField(target, hf);
						d_codecs.put(target, hc);
						CodecType.HOLDER.setField(target, null);
						h_codecs.put(target, null);
					}
					CodecType.DEFAULT.setField(target, hf);
				}
				Object codec = type.access(target);
				if (codec == null)
					type.setField(target, null);
				else
					codecMap.put(target, codec);
			}
			for (CodecType type : CodecType.values()) {
				Core.logDebug("Stored type " + type + " of field " + type.getField(target) + " for " + target + " -> " + type.getCache(target));
			}
		}

		public static final Object fetch(Class<?> target, CodecType type) {
			Object codec = codecs(type).get(target);
			if (codec == null) {
				Core.logDebug("Codec of " + target.getSimpleName() + " with type " + type + " is null, starting to store all CODEC found in target class.");
				storeAllFoundInClass(target);
				codec = codecs(type).get(target);
			}
			return codec;
		}

		public static final Object fetchHolderCodec(Class<?> target) {
			return fetch(target, CodecType.HOLDER);
		}

		public static final Object fetchDirectCodec(Class<?> target) {
			return fetch(target, CodecType.DIRECT);
		}

		public static final Object fetchHolderSetCodec(Class<?> target) {
			return fetch(target, CodecType.HOLDER_SET);
		}

		public static final Object fetchDefault(Class<?> target) {
			Object holderCodec = fetchHolderCodec(target);
			if (holderCodec == null)
				return fetchDirectCodec(target);
			return null;
		}
	}

	/**
	 * 打包某个字段的CODEC（可能是Codec、MapCodec或KeyDispatchDataCodec）以及是否是可选字段
	 */
	public static final class Entry {
		public static enum RequirementType {
			DEFAULT(-1),
			OPTINAL_NOERR(0),
			OPTIONAL(1),
			REQUIRED(2);

			int priority;

			private RequirementType(int priority) {
				this.priority = priority;
			}

			public static final RequirementType of(int priority) {
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
			public static final RequirementType resolve(RequirementType t1, RequirementType t2) {
				int p = Math.max(t1.priority, t2.priority);
				if (p == -1)
					p = 2;
				return of(p);
			}
		}

		public final Object codec;
		public final RequirementType type;

		private Entry(Object codec, RequirementType type) {
			if (codec == null)
				throw new IllegalStateException("CodecResolver.Entry cannot be create from null CODEC.");
			this.codec = codec;
			this.type = type;
		}

		private Entry(Object codec) {
			this(codec, RequirementType.DEFAULT);
		}

		public final boolean isCodec() {
			return codec instanceof Codec;
		}

		public final boolean isMapCodec() {
			return codec instanceof MapCodec;
		}

		public final boolean isKeyDispatchDataCodec() {
			return codec instanceof KeyDispatchDataCodec;
		}

		public final Codec<?> codec() {
			return Codecs.asCodec(codec);
		}

		public final MapCodec<?> mapCodec() {
			if (codec instanceof MapCodec mc)
				return mc;
			else if (codec instanceof KeyDispatchDataCodec kddc)
				return kddc.codec();
			return null;
		}

		public final KeyDispatchDataCodec<?> keyDispatchDataCodec() {
			if (codec instanceof KeyDispatchDataCodec kddc)
				return kddc;
			return null;
		}

		public static final Entry of(Object codec, RequirementType type) {
			return new Entry(codec, type);
		}

		public static final Entry of(Object codec) {
			return new Entry(codec);
		}
	}
}
