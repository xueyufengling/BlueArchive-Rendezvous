package fw.core.registry;

import java.lang.reflect.Field;

import com.mojang.serialization.MapCodec;

import lyra.klass.GenericTypes;
import lyra.klass.KlassWalker;
import lyra.lang.Reflection;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;

public class RegistryWalker {
	@FunctionalInterface
	public static interface RegistryOperation {
		/**
		 * 遍历每个注册表
		 * 
		 * @param f            注册表字段
		 * @param registryKey  注册表的键
		 * @param registryType 注册表的值类型
		 */
		@SuppressWarnings("rawtypes")
		public void operate(Field f, ResourceKey registryKey, Class<?> registryType);
	}

	@SuppressWarnings("rawtypes")
	public static final void walkRegistries(RegistryOperation op) {
		KlassWalker.walkFields(Registries.class, (Field f, boolean isStatic, Object registryKey) -> {
			if (isStatic && Reflection.is(f, ResourceKey.class) && registryKey != null) {
				op.operate(f, (ResourceKey) registryKey, getRegistryType(f));
			}
		});
	}

	@FunctionalInterface
	public static interface MapCodecRegistryOperation {
		/**
		 * 对MapCodec类型的注册表进行操作
		 * 
		 * @param f           注册表字段
		 * @param registryKey 注册表的键
		 * @param codecType   Codec的类型
		 */
		@SuppressWarnings("rawtypes")
		public void operate(Field f, ResourceKey registryKey, Class<?> codecType);
	}

	/**
	 * 专门用于遍历注册类型为MapCodec的注册表
	 * 
	 * @param op
	 */
	@SuppressWarnings("rawtypes")
	public static final void walkMapCodecRegistries(MapCodecRegistryOperation op) {
		walkRegistries((Field f, ResourceKey registryKey, Class<?> registryType) -> {
			if (Reflection.is(registryType, MapCodec.class)) {
				op.operate(f, (ResourceKey) registryKey, getMapCodecRegistryType(f));
			}
		});
	}

	/**
	 * 获取注册键对应的值类型。
	 * 
	 * @param registryKeyField 注册表对应的ResourceKey
	 * @return
	 */
	public static Class<?> getRegistryType(Field registryKeyField) {
		return GenericTypes.classes(registryKeyField, 0)[0].type();
	}

	/**
	 * 获取注册表中MapCodec的类型。
	 * 
	 * @param registryKeyField
	 * @return
	 */
	public static Class<?> getMapCodecRegistryType(Field registryKeyField) {
		return GenericTypes.classes(registryKeyField, 0, 0)[0].type();
	}

	/**
	 * 获取最外层的第一个泛型参数
	 * 
	 * @param registryKeyField
	 * @return
	 */
	public static Class<?> getGenericType(Field f) {
		return GenericTypes.classes(f)[0].type();
	}
}
