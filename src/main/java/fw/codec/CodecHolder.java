package fw.codec;

import com.mojang.serialization.MapCodec;

import fw.codec.annotation.CodecAutogen;
import lyra.klass.special.BaseClass;
import lyra.lang.JavaLang;

public interface CodecHolder<O> extends BaseClass<CodecHolder.DerivedCodec<O>> {

	public default MapCodec<? extends O> codec() {
		return this.definition().codec(this);
	}

	public default void construct(Class<O> codecClass) {
		this.construct(CodecHolder.DerivedCodec.class, new Class<?>[] { Class.class }, codecClass);
	}

	class DerivedCodec<O> extends BaseClass.Definition<CodecHolder<O>> {
		protected MapCodec<? extends O> derivedCodec;
		protected Class<O> codecClass;

		public DerivedCodec(Class<O> codecClass) {
			this.codecClass = codecClass;
		}

		/**
		 * 在子类中搜寻MapCodec<br>
		 * 数据生成和运行时均会调用，需要保证这两个阶段均构建好CODEC。
		 */
		public MapCodec<? extends O> codec(CodecHolder<O> holder) {
			if (derivedCodec == null)
				derivedCodec = Codecs.accessMapCodec(holder.getClass(), codecClass);
			return derivedCodec;
		}
	}

	public static void CODEC(Class<?>... ctorTypes) {
		Class<?> caller = JavaLang.getOuterCallerClass();
		CodecAutogen.CodecGenerator.forCodec(caller, ctorTypes);
	}
}
