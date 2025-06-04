package fw.datagen;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public interface Localizable {

	public abstract String localizationKey();

	public default Component localizedComponent() {
		return Component.translatable(localizationKey());
	}

	/**
	 * 根据注册表名获取本地化key，即类型registryKey.命名空间namespace.路径path
	 * 
	 * @param key
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String localizationKey(ResourceKey key) {
		ResourceLocation loc = key.location();
		return key.registryKey().location().getPath() + '.' + loc.getNamespace() + '.' + loc.getPath().replace('/', '.');
	}

	@SuppressWarnings("rawtypes")
	public static String localizationKey(Holder holder) {
		return localizationKey(holder.getKey());
	}
}
