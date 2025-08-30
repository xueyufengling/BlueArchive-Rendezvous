package lepus.mc.codec;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.stream.Stream;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;

import lepus.mc.codec.CodecResolver.CodecType;
import lepus.mc.codec.annotation.CodecEntry;
import lepus.mc.codec.annotation.DoubleRange;
import lepus.mc.codec.annotation.FloatRange;
import lepus.mc.codec.annotation.IntRange;
import lepus.mc.codec.annotation.ListSize;
import lepus.mc.codec.annotation.StringLength;
import lepus.mc.core.registry.Holders;
import lyra.klass.GenericTypes;
import lyra.lang.Arrays;
import lyra.lang.JavaLang;
import lyra.lang.internal.HandleBase;
import lyra.object.ObjectManipulator;
import net.minecraft.core.Holder;
import net.minecraft.util.KeyDispatchDataCodec;

public class Codecs {

	/**
	 * 本类在构建CODEC时，将依据该Map查询哪个类使用哪个CODEC。
	 */
	@SuppressWarnings("rawtypes")
	public static final HashMap<Class<?>, CodecResolver> CODEC_RESOLVERS = new HashMap<>();

	static {
		setCodec(byte.class, Codec.BYTE);
		setCodec(Byte.class, Codec.BYTE);
		setCodec(boolean.class, Codec.BOOL);
		setCodec(Boolean.class, Codec.BOOL);
		setCodec(short.class, Codec.SHORT);
		setCodec(Short.class, Codec.SHORT);
		CodecResolver<CodecEntry> INT = (Type fieldType, CodecType codecType, CodecEntry param) -> {
			IntRange ir = param.int_range().length == 0 ? null : param.int_range()[0];
			if (ir == null)
				return CodecResolver.Entry.of(Codec.INT);
			else
				return CodecResolver.Entry.of(Codec.intRange(ir.min(), ir.max()));
		};
		setCodec(int.class, INT);
		setCodec(Integer.class, INT);
		setCodec(long.class, Codec.LONG);
		setCodec(Long.class, Codec.LONG);
		CodecResolver<CodecEntry> FLOAT = (Type fieldType, CodecType codecType, CodecEntry param) -> {
			FloatRange fr = param.float_range().length == 0 ? null : param.float_range()[0];
			if (fr == null)
				return CodecResolver.Entry.of(Codec.FLOAT);
			else
				return CodecResolver.Entry.of(Codec.floatRange(fr.min(), fr.max()));
		};
		setCodec(float.class, FLOAT);
		setCodec(Float.class, FLOAT);
		CodecResolver<CodecEntry> DOUBLE = (Type fieldType, CodecType codecType, CodecEntry param) -> {
			DoubleRange dr = param.double_range().length == 0 ? null : param.double_range()[0];
			if (dr == null)
				return CodecResolver.Entry.of(Codec.DOUBLE);
			else
				return CodecResolver.Entry.of(Codec.doubleRange(dr.min(), dr.max()));
		};
		setCodec(double.class, DOUBLE);
		setCodec(Double.class, DOUBLE);
		CodecResolver<CodecEntry> STRING = (Type fieldType, CodecType codecType, CodecEntry param) -> {
			StringLength sl = param.string_length().length == 0 ? null : param.string_length()[0];
			if (sl == null)
				return CodecResolver.Entry.of(Codec.STRING);
			else
				return CodecResolver.Entry.of(Codec.string(sl.min(), sl.max()));
		};
		setCodec(String.class, STRING);
		// Holder的CODEC从其泛型参数类中寻找
		CodecResolver<Object> HOLDER = (Type fieldType, CodecType codecType, Object param) -> {
			Class<?> holderType = Holders.getHolderType(fieldType);// Holder<holderType [<GenericTypes.type(type, 0)>] >
			Codec<?> codec = getCodec(holderType, GenericTypes.type(fieldType, 0), CodecType.HOLDER, param).codec();
			if (codec == null)
				throw new IllegalArgumentException("No CODEC found for type " + holderType + " in Holder entry.");
			return CodecResolver.Entry.of(codec);
		};
		setCodec(Holder.class, HOLDER);
		setCodec(Holder.Reference.class, HOLDER);
		// 列表/数组编解码器
		CodecResolver<CodecEntry> LIST = (Type fieldType, CodecType codecType, CodecEntry param) -> {
			Class<?> listType = Arrays.getListType(fieldType);
			Codec<?> codec = getCodec(listType, GenericTypes.type(fieldType, 0), codecType, param).codec();
			if (codec == null)
				throw new IllegalArgumentException("No CODEC found for type " + listType + " in List entry.");
			ListSize ls = param.list_size().length == 0 ? null : param.list_size()[0];
			if (ls == null)
				return CodecResolver.Entry.of(codec.listOf());
			else
				return CodecResolver.Entry.of(codec.listOf(ls.min(), ls.max()));
		};
		setCodec(List.class, LIST);
		setCodec(ArrayList.class, LIST);
		// Map编解码器
		CodecResolver<CodecEntry> MAP = (Type fieldType, CodecType codecType, CodecEntry param) -> {
			Class<?> keyType = GenericTypes.classes(fieldType)[0].type();
			Codec<?> keyCodec = getCodec(keyType, GenericTypes.type(fieldType, 0), codecType, param).codec();
			if (keyCodec == null)
				throw new IllegalArgumentException("No CODEC found for key type " + keyType + " in Map entry.");
			Class<?> valueType = GenericTypes.classes(fieldType)[0].type();
			Codec<?> valueCodec = getCodec(valueType, GenericTypes.type(fieldType, 1), codecType, param).codec();
			if (valueCodec == null)
				throw new IllegalArgumentException("No CODEC found for value type " + valueType + " in Map entry.");
			return CodecResolver.Entry.of(Codec.unboundedMap(keyCodec, valueCodec));
		};
		setCodec(Map.class, MAP);
		// 可选值编解码器
		CodecResolver<CodecEntry> OPTIONAL_LONG = (Type fieldType, CodecType codecType, CodecEntry param) -> {
			return CodecResolver.Entry.of(Codec.LONG, CodecResolver.Entry.RequirementType.OPTIONAL);
		};
		setCodec(OptionalLong.class, OPTIONAL_LONG);
		CodecResolver<CodecEntry> OPTIONAL = (Type fieldType, CodecType codecType, CodecEntry param) -> {
			Class<?> optionalType = GenericTypes.getFirstGenericType(fieldType);
			Codec<?> codec = getCodec(optionalType, GenericTypes.type(fieldType, 0), codecType, param).codec();
			if (codec == null)
				throw new IllegalArgumentException("No CODEC found for type " + optionalType + " in Optional entry.");
			return CodecResolver.Entry.of(codec, CodecResolver.Entry.RequirementType.OPTIONAL);
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
		if (codecFieldName == null)
			throw new IllegalArgumentException("Codec field name for " + targetClass + " is null");
		CODEC_RESOLVERS.put(targetClass, (Type fieldType, CodecType codecType, Object param) -> CodecResolver.Entry.of(ObjectManipulator.access(targetClass, codecFieldName)));
	}

	/**
	 * 直接设置某个类的CODEC
	 * 
	 * @param targetClass
	 * @param codec
	 */
	public static final void setCodec(Class<?> targetClass, Object codec) {
		CODEC_RESOLVERS.put(targetClass, (Type fieldType, CodecType codecType, Object param) -> CodecResolver.Entry.of(codec));
	}

	@SuppressWarnings("rawtypes")
	public static final void setCodec(Class<?> targetClass, CodecResolver resolver) {
		CODEC_RESOLVERS.put(targetClass, resolver);
	}

	@SuppressWarnings("unchecked")
	public static final CodecResolver.Entry getCodec(Class<?> targetClass, Type fieldType, CodecType codecType, Codec<?> default_codec_if_not_exist, Object param) {
		return CODEC_RESOLVERS.computeIfAbsent(targetClass, (Class<?> t) -> CodecResolver.findInRawClass(targetClass, default_codec_if_not_exist)).resolve(fieldType, codecType, param);
	}

	public static final CodecResolver.Entry getCodec(Class<?> targetClass, Type fieldType, CodecType codecType, boolean empty_codec_if_not_exist, Object param) {
		return getCodec(targetClass, fieldType, codecType, empty_codec_if_not_exist ? Codecs.emptyCodec(targetClass) : null, param);
	}

	public static final CodecResolver.Entry getCodec(Class<?> targetClass, Type fieldType, CodecType codecType, Object param) {
		return getCodec(targetClass, fieldType, codecType, null, param);
	}

	public static final CodecResolver.Entry getCodec(Class<?> targetClass, CodecType codecType) {
		return getCodec(targetClass, null, codecType, null);
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
}
