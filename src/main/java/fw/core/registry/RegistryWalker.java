package fw.core.registry;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.mojang.serialization.MapCodec;

import lyra.klass.GenericTypes;
import lyra.klass.KlassWalker;
import lyra.lang.Reflection;
import lyra.object.Placeholders.TypeWrapper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.LevelStem;

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
		public boolean operate(Field f, ResourceKey registryKey, Class<?> registryType);
	}

	@SuppressWarnings("rawtypes")
	public static final void walkRegistries(RegistryOperation op) {
		KlassWalker.walkTypeFields(Registries.class, ResourceKey.class, (Field f, boolean isStatic, ResourceKey registryKey) -> {
			if (isStatic && registryKey != null) {
				return op.operate(f, (ResourceKey) registryKey, getRegistryKeyType(f));
			}
			return true;
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
		public boolean operate(Field f, ResourceKey registryKey, Class<?> codecType);
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
				return op.operate(f, (ResourceKey) registryKey, getMapCodecRegistryType(f));
			}
			return true;
		});
	}

	/**
	 * 获取注册键对应的值类型。
	 * 
	 * @param registryKeyField 注册表对应的ResourceKey
	 * @return
	 */
	public static Class<?> getRegistryKeyType(Field registryKeyField) {
		return GenericTypes.classes(registryKeyField, 0)[0].type();
	}

	public static Class<?> getRegistryType(Field registryField) {
		return GenericTypes.classes(registryField)[0].type();
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Class<?> getRegistryKeyType(ResourceKey<? extends Registry<?>> regKey) {
		TypeWrapper<Class<?>> wrapper = TypeWrapper.wrap();
		RegistryWalker.walkRegistries((Field f, ResourceKey registryKey, Class<?> registryType) -> {// 获取Registry key对应的值类型registryType
			if (registryKey == regKey) {
				wrapper.value = registryType;
				return false;
			}
			return true;
		});
		return wrapper.value;
	}

	public static Class<?> getRegistryType(Registry<?> reg) {
		return getRegistryKeyType(reg.key());
	}

	/**
	 * 获取指定注册表key在Registries中的字段名称
	 * 
	 * @param regKey
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String getRegistryFieldName(ResourceKey<? extends Registry<?>> regKey) {
		TypeWrapper<String> wrapper = TypeWrapper.wrap();
		RegistryWalker.walkRegistries((Field f, ResourceKey registryKey, Class<?> registryType) -> {
			if (registryKey == regKey) {
				wrapper.value = f.getName();
				return false;
			}
			return true;
		});
		return wrapper.value;
	}

	public static String getRegistryFieldName(Registry<?> reg) {
		return getRegistryFieldName(reg.key());
	}

	public static class RegistryInfo {
		public final ResourceKey<? extends Registry<?>> regKey;
		public final String fieldName;
		public final Class<?> regType;

		RegistryInfo(ResourceKey<? extends Registry<?>> regKey, String fieldName, Class<?> regType) {
			this.regKey = regKey;
			this.fieldName = fieldName;
			this.regType = regType;
		}

		@Override
		public String toString() {
			return "RegistryInfo {regKey=" + regKey + ", fieldName=" + fieldName + ", regType=" + regType.getName() + "}";
		}
	}

	/**
	 * 根据注册表key值获取字段信息。key完全相同的注册表则只返回最上面的那个。
	 * 
	 * @param regKey
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static RegistryInfo getRegistryInfo(ResourceKey<? extends Registry<?>> regKey) {
		TypeWrapper<RegistryInfo> wrapper = TypeWrapper.wrap();
		RegistryWalker.walkRegistries((Field f, ResourceKey registryKey, Class<?> registryType) -> {
			if (registryKey == regKey) {
				wrapper.value = new RegistryInfo(regKey, f.getName(), registryType);
				return false;
			}
			return true;
		});
		return wrapper.value;
	}

	public static RegistryInfo getRegistryInfo(Registry<?> reg) {
		return getRegistryInfo(reg.key());
	}

	public static ArrayList<RegistryInfo> collectRegistryInfo(List<ResourceKey<? extends Registry<?>>> registries) {
		ArrayList<RegistryWalker.RegistryInfo> regInfoList = new ArrayList<>();
		boolean genDim = false;// DIMENSION和LEVEL_STEM的key相同，会重复迭代，因此记录只要迭代过一次后续就不再迭代
		for (ResourceKey<? extends Registry<?>> reg : registries) {
			if (reg.equals(Registries.DIMENSION)) {// DIMENSION和LEVEL_STEM的key相同，需要单独处理
				if (genDim)
					continue;
				regInfoList.add(new RegistryInfo(Registries.DIMENSION, "DIMENSION", Level.class));
				regInfoList.add(new RegistryInfo(Registries.LEVEL_STEM, "LEVEL_STEM", LevelStem.class));
				genDim = true;
			} else {
				RegistryWalker.RegistryInfo regInfo = RegistryWalker.getRegistryInfo(reg);
				if (regInfo != null)
					regInfoList.add(regInfo);
			}
		}
		return regInfoList;
	}
}
