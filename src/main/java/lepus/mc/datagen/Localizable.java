package lepus.mc.datagen;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public interface Localizable {
	public static final char PATH_SEPARATOR = '.';

	public default String localizationType() {
		return "";
	}

	/**
	 * localizationPath()和localizationKey()至少需要重写其中一个。<br>
	 * 本地化key的localizationPath，需要有命名空间
	 * 
	 * @return
	 */
	public default String localizationPath() {
		throw new UnsupportedOperationException("No localizationPath() or localizationKey() overrided in Localizable " + this.getClass());
	}

	/**
	 * 默认localizationPath()以PATH_SEPARATOR分隔后第一个子串为命名空间
	 * 
	 * @return
	 */
	public default String namespace() {
		String path = localizationPath();
		int idx = path.indexOf(PATH_SEPARATOR);
		return path.substring(0, idx);
	}

	public static String joinKey(String path1, String path2) {
		if (path1 == null || path1.equals(""))
			return path2;
		if (path2 == null || path2.equals(""))
			return path1;
		else
			return path1 + PATH_SEPARATOR + path2;
	}

	/**
	 * 获取key实际上只基于该方法，覆写该方法则localizationType()、localizationPath()全部无效
	 * 
	 * @return
	 */
	public default String localizationKey() {
		return joinKey(localizationType(), localizationPath());
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
	 * 注册条目类型路径为localizationPath
	 * 
	 * @param key
	 * @return
	 */
	public static String localizationPath(ResourceKey<?> key) {
		ResourceLocation loc = key.location();
		return loc.getNamespace() + PATH_SEPARATOR + loc.getPath().replace('/', PATH_SEPARATOR);
	}

	@SuppressWarnings("rawtypes")
	public static String localizationKey(Holder holder) {
		ResourceKey<?> key = holder.getKey();
		return joinKey(localizationType(key), localizationPath(key));
	}

	/**
	 * 将ResourceLocation.toString()转换为本地化key
	 * 
	 * @param namespacedId
	 * @return
	 */
	public static String stdLocalizationKey(String namespacedId) {
		return namespacedId.replace(ResourceLocation.NAMESPACE_SEPARATOR, Localizable.PATH_SEPARATOR);
	}
}
