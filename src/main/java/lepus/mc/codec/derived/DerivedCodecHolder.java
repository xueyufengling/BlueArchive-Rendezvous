package lepus.mc.codec.derived;

import lepus.mc.codec.CodecAccess;
import lepus.mc.codec.annotation.CodecAutogen;
import lyra.klass.special.BaseClass;
import lyra.lang.JavaLang;

/**
 * implements此接口标记实现此接口的类的子类中需要有静态CODEC<br>
 * 实现此接口的构造函数必须调用construct()方法以实例化储存字段的对象！
 * 
 * @param <C>
 * @param <O>
 */
public interface DerivedCodecHolder<C, O> extends BaseClass<DerivedCodecHolder.DerivedCodec<C, O>> {
	public default C codec() {
		return this.definition().codec(this);
	}

	abstract Class<C> codecClass();

	public static void CODEC(Class<?>... ctorTypes) {
		Class<?> caller = JavaLang.getOuterCallerClass();
		CodecAutogen.CodecGenerator.forCodec(caller, ctorTypes);
	}

	public default void construct(Class<O> codecTypeClass) {
		this.construct(DerivedCodecHolder.DerivedCodec.class, new Class<?>[] { Class.class, Class.class }, codecClass(), codecTypeClass);
	}

	class DerivedCodec<C, O> extends BaseClass.Definition<DerivedCodecHolder<C, O>> {
		/**
		 * 子类的CODEC
		 */
		protected C derivedCodec;
		/**
		 * MapCodec.class、Codec.class等
		 */
		protected Class<C> codecClass;
		/**
		 * CODEC泛型参数Class
		 */
		protected Class<O> codecTypeClass;

		public DerivedCodec(Class<C> codecClass, Class<O> codecTypeClass) {
			this.codecClass = codecClass;
			this.codecTypeClass = codecTypeClass;
		}

		/**
		 * 在子类中搜寻MapCodec<br>
		 * 数据生成和运行时均会调用，需要保证这两个阶段均构建好CODEC。
		 */
		@SuppressWarnings("unchecked")
		public C codec(DerivedCodecHolder<C, O> holder) {
			if (derivedCodec == null)
				derivedCodec = (C) CodecAccess.accessStatic(holder.getClass(), codecClass, codecTypeClass);
			return derivedCodec;
		}
	}
}
