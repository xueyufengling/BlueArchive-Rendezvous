package fw.codec;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import fw.codec.annotation.CodecEntry;
import fw.codec.annotation.DoubleRange;
import fw.codec.annotation.FloatRange;
import fw.codec.annotation.IntRange;
import fw.codec.annotation.ListSize;
import fw.codec.annotation.StringLength;
import fw.core.registry.Holders;
import lyra.klass.GenericTypes;
import lyra.lang.Arrays;
import lyra.object.ObjectManipulator;
import net.minecraft.core.Holder;
import net.minecraft.util.KeyDispatchDataCodec;

public class Codecs {
	/**
	 * CODEC字段默认的名称
	 */
	public static final String DEFAULT_CODEC_FIELD = "CODEC";

	/**
	 * 本类在构建CODEC时，将依据该Map查询哪个类使用哪个CODEC。
	 */
	@SuppressWarnings("rawtypes")
	public static final HashMap<Class<?>, CodecResolver> CODECS = new HashMap<>();

	static {
		setCodec(byte.class, Codec.BYTE);
		setCodec(Byte.class, Codec.BYTE);
		setCodec(boolean.class, Codec.BOOL);
		setCodec(Boolean.class, Codec.BOOL);
		setCodec(short.class, Codec.SHORT);
		setCodec(Short.class, Codec.SHORT);
		CodecResolver<CodecEntry> INT = (Type type, CodecEntry params) -> {
			IntRange ir = params.int_range().length == 0 ? null : params.int_range()[0];
			if (ir == null)
				return CodecResolver.Entry.of(Codec.INT);
			else
				return CodecResolver.Entry.of(Codec.intRange(ir.min(), ir.max()));
		};
		setCodec(int.class, INT);
		setCodec(Integer.class, INT);
		setCodec(long.class, Codec.LONG);
		setCodec(Long.class, Codec.LONG);
		CodecResolver<CodecEntry> FLOAT = (Type type, CodecEntry params) -> {
			FloatRange fr = params.float_range().length == 0 ? null : params.float_range()[0];
			if (fr == null)
				return CodecResolver.Entry.of(Codec.FLOAT);
			else
				return CodecResolver.Entry.of(Codec.floatRange(fr.min(), fr.max()));
		};
		setCodec(float.class, FLOAT);
		setCodec(Float.class, FLOAT);
		CodecResolver<CodecEntry> DOUBLE = (Type type, CodecEntry params) -> {
			DoubleRange dr = params.double_range().length == 0 ? null : params.double_range()[0];
			if (dr == null)
				return CodecResolver.Entry.of(Codec.DOUBLE);
			else
				return CodecResolver.Entry.of(Codec.doubleRange(dr.min(), dr.max()));
		};
		setCodec(double.class, DOUBLE);
		setCodec(Double.class, DOUBLE);
		CodecResolver<CodecEntry> STRING = (Type type, CodecEntry params) -> {
			StringLength sl = params.string_length().length == 0 ? null : params.string_length()[0];
			if (sl == null)
				return CodecResolver.Entry.of(Codec.STRING);
			else
				return CodecResolver.Entry.of(Codec.string(sl.min(), sl.max()));
		};
		setCodec(String.class, STRING);
		CodecResolver<Object> HOLDER = (Type type, Object params) -> {
			Class<?> holderType = Holders.getHolderType(type);
			Codec<?> codec = getCodec(holderType, GenericTypes.type(type, 0), params).asCodec();
			if (codec == null)
				throw new IllegalArgumentException("No CODEC found for type " + holderType + " in Holder entry.");
			return CodecResolver.Entry.of(codec);
		};
		setCodec(Holder.class, HOLDER);
		setCodec(Holder.Reference.class, HOLDER);

		CodecResolver<CodecEntry> LIST = (Type type, CodecEntry params) -> {
			Class<?> listType = Arrays.getListType(type);
			Codec<?> codec = getCodec(listType, GenericTypes.type(type, 0), params).asCodec();
			if (codec == null)
				throw new IllegalArgumentException("No CODEC found for type " + listType + " in List entry.");
			ListSize ls = params.list_size().length == 0 ? null : params.list_size()[0];
			if (ls == null)
				return CodecResolver.Entry.of(codec.listOf());
			else
				return CodecResolver.Entry.of(codec.listOf(ls.min(), ls.max()));
		};
		setCodec(List.class, LIST);
		CodecResolver<CodecEntry> MAP = (Type type, CodecEntry params) -> {
			Class<?> keyType = GenericTypes.classes(type)[0].type();
			Codec<?> keyCodec = getCodec(keyType, GenericTypes.type(type, 0), params).asCodec();
			if (keyCodec == null)
				throw new IllegalArgumentException("No CODEC found for key type " + keyType + " in Map entry.");
			Class<?> valueType = GenericTypes.classes(type)[0].type();
			Codec<?> valueCodec = getCodec(valueType, GenericTypes.type(type, 1), params).asCodec();
			if (valueCodec == null)
				throw new IllegalArgumentException("No CODEC found for value type " + valueType + " in Map entry.");
			return CodecResolver.Entry.of(Codec.unboundedMap(keyCodec, valueCodec));
		};
		setCodec(Map.class, MAP);

		CodecResolver<CodecEntry> OPTIONAL_LONG = (Type type, CodecEntry params) -> {
			return CodecResolver.Entry.of(Codec.LONG, CodecResolver.Entry.Type.OPTIONAL);
		};
		setCodec(OptionalLong.class, OPTIONAL_LONG);
		CodecResolver<CodecEntry> OPTIONAL = (Type type, CodecEntry params) -> {
			Class<?> optionalType = GenericTypes.getFirstGenericType(type);
			Codec<?> codec = getCodec(optionalType, GenericTypes.type(type, 0), params).asCodec();
			if (codec == null)
				throw new IllegalArgumentException("No CODEC found for type " + optionalType + " in Optional entry.");
			return CodecResolver.Entry.of(codec, CodecResolver.Entry.Type.OPTIONAL);
		};
		setCodec(Optional.class, OPTIONAL);
	}

	/**
	 * 根据字段名称寻找指定的CODEC并记录在CODECS中
	 * 
	 * @param targetClass
	 * @param codecFieldName
	 * @return
	 */
	public static final void setCodecByFieldName(Class<?> targetClass, String codecFieldName) {
		CODECS.put(targetClass, (Type type, Object param) -> CodecResolver.Entry.of(ObjectManipulator.access(targetClass, codecFieldName == null ? DEFAULT_CODEC_FIELD : codecFieldName)));
	}

	/**
	 * 直接设置某个类的CODEC
	 * 
	 * @param targetClass
	 * @param codec
	 */
	public static final void setCodec(Class<?> targetClass, Object codec) {
		CODECS.put(targetClass, (Type type, Object param) -> CodecResolver.Entry.of(codec));
	}

	@SuppressWarnings("rawtypes")
	public static final void setCodec(Class<?> targetClass, CodecResolver resolver) {
		CODECS.put(targetClass, resolver);
	}

	/**
	 * 获取某个类的CODEC，如果不存在则获取该类中默认字段名称的CODEC
	 * 
	 * @param targetClass
	 * @return
	 */
	public static final CodecResolver.Entry getCodec(Class<?> targetClass) {
		return getCodec(targetClass, null, null);
	}

	@SuppressWarnings("unchecked")
	public static final CodecResolver.Entry getCodec(Class<?> targetClass, Type type, Object param) {
		return CODECS.computeIfAbsent(targetClass, (Class<?> t) -> (Type c, Object p) -> CodecResolver.Entry.of(ObjectManipulator.access(targetClass, DEFAULT_CODEC_FIELD))).resolve(type, param);
	}

	public static final CodecResolver.Entry getCodec(Class<?> targetClass, Object param) {
		return getCodec(targetClass, null, param);
	}

	/**
	 * 获取CODEC的默认注册名称，将单词以下划线分隔并转换为全小写。<br>
	 * 
	 * @param simpleName
	 * @return
	 */
	public static String defaultCodecRegisterName(String simpleName) {
		return simpleName.trim().replaceAll("([A-Z])", "_$1")// 正则匹配大写字母前插入下划线
				.toLowerCase()
				.replaceAll("^_", "");// 处理开头可能多出的下划线
	}

	public static String defaultCodecRegisterName(Class<?> codecClass) {
		return defaultCodecRegisterName(codecClass.getSimpleName());
	}

	/**
	 * 访问目标类的CODEC字段
	 * 
	 * @param <T>
	 * @param targetClass
	 * @return
	 */
	public static final <T> Codec<T> accessCodec(Class<T> targetClass) {
		return asCodec(getCodec(targetClass));
	}

	@SuppressWarnings("unchecked")
	public static final <T> Codec<T> asCodec(Object class_codec) {
		if (class_codec instanceof Codec c)
			return c;
		else if (class_codec instanceof MapCodec mc)
			return mc.codec();
		else if (class_codec instanceof KeyDispatchDataCodec kddc)
			return kddc.codec().codec();
		return null;
	}

	@SuppressWarnings("unchecked")
	public static final <T> MapCodec<T> asMapCodec(Object class_codec) {
		if (class_codec instanceof Codec c)
			return c.fieldOf(null);
		else if (class_codec instanceof MapCodec mc)
			return mc;
		else if (class_codec instanceof KeyDispatchDataCodec kddc)
			return kddc.codec().codec().fieldOf(null);
		return null;
	}
}
