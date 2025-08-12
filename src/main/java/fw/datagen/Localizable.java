package fw.datagen;

import fw.core.Core;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public interface Localizable {
	public static final char NAMESPACE_SEPARATOR = '.';
	public static final char PATH_SEPARATOR = '.';

	public default String localizationType() {
		return "";
	}

	public default String localizationNamespace() {
		return Core.ModId;
	}

	/**
	 * localizationPath()和localizationKey()至少需要重写其中一个
	 * 
	 * @return
	 */
	public default String localizationPath() {
		throw new UnsupportedOperationException("No localizationPath() or localizationKey() overrided in Localizable " + this.getClass());
	}

	public static String joinKey(String path1, String path2) {
		if (path1 == null || path1.equals(""))
			return path2;
		if (path2 == null || path2.equals(""))
			return path1;
		else
			return path1 + NAMESPACE_SEPARATOR + path2;
	}

	/**
	 * 获取key实际上只基于该方法，覆写该方法则localizationType()、localizationNamespace()、localizationPath()全部无效
	 * 
	 * @return
	 */
	public default String localizationKey() {
		return joinKey(localizationType(), joinKey(localizationNamespace(), localizationPath()));
	}

	public default Component localizedComponent() {
		return Component.translatable(localizationKey());
	}

	/**
	 * 注册表类型路径为localizationType
	 * 
	 * @param key
	 * @return
	 */
	public static String localizationType(ResourceKey<?> key) {
		return key.registryKey().location().getPath();
	}

	/**
	 * 注册条目类型命名空间为localizationNamespace
	 * 
	 * @param key
	 * @return
	 */
	public static String localizationNamespace(ResourceKey<?> key) {
		return key.location().getNamespace();
	}

	/**
	 * 注册条目类型路径为localizationPath
	 * 
	 * @param key
	 * @return
	 */
	public static String localizationPath(ResourceKey<?> key) {
		return key.location().getPath().replace('/', PATH_SEPARATOR);
	}

	@SuppressWarnings("rawtypes")
	public static String localizationKey(Holder holder) {
		ResourceKey<?> key = holder.getKey();
		return joinKey(localizationType(key), joinKey(localizationNamespace(key), localizationPath(key)));
	}

	/**
	 * localizationNamespace与localizationPath的组合
	 * 
	 * @param namespacedId
	 * @return
	 */
	public static String stdLocalizationKey(String namespacedId) {
		return namespacedId.replace(ResourceLocation.NAMESPACE_SEPARATOR, Localizable.NAMESPACE_SEPARATOR);
	}
}
