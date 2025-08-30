package lepus.mc.resources;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

/**
 * 创建TagKey，用于给HolderSet命名
 */
public class TagKeys {
	public static <T> TagKey<T> build(ResourceKey<T> resource_key) {
		return build(resource_key.registryKey(), resource_key.location());
	}

	/**
	 * 从一个resource_key生成相同路径的其他registry_key下的值
	 * 
	 * @param <T>
	 * @param registry_key
	 * @param resource_key
	 * @return
	 */
	public static <T> TagKey<T> build(ResourceKey<? extends Registry<T>> registry_key, ResourceKey<?> resource_key) {
		return build(registry_key, resource_key.location());
	}

	public static <T> TagKey<T> build(ResourceKey<? extends Registry<T>> registry_key, ResourceLocation resource_location) {
		return TagKey.create(registry_key, resource_location);
	}

	/**
	 * 获取TagKey
	 * 
	 * @param <T>           注册表类型
	 * @param registry_key  ResourceKey参数定义于net.minecraft.core.registries.Registries
	 * @param namespacedLoc ResourceLocation字符串
	 * @return 返回name对应的T类型注册表的ResourceKey
	 */
	public static <T> TagKey<T> build(ResourceKey<? extends Registry<T>> registry_key, String namespacedLoc) {
		return build(registry_key, ResourceLocations.build(namespacedLoc));
	}

	/**
	 * @param <T>          注册表类型
	 * @param resource_key ResourceKey参数定义于net.minecraft.core.registries.Registries
	 * @param namespace    命名空间
	 * @param path         命名空间path
	 * @return 返回name对应的T类型注册表的ResourceKey
	 */
	public static <T> TagKey<T> build(ResourceKey<? extends Registry<T>> registry_key, String namespace, String path) {
		return build(registry_key, ResourceLocations.build(namespace, path));
	}

	public static String toString(TagKey<?> tagKey) {
		if (tagKey == null)
			return null;
		else
			return tagKey.toString();
	}
}
