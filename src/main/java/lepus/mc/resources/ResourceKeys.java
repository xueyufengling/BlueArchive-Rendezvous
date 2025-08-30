package lepus.mc.resources;

import lyra.lang.Reflection;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

/**
 * 创建ResourceKey，用于注册表条目命名
 */
public class ResourceKeys {
	/**
	 * 从一个resource_key生成相同路径的其他registry_key下的值
	 * 
	 * @param <T>
	 * @param registry_key
	 * @param resource_key
	 * @return
	 */
	public static <T> ResourceKey<T> build(ResourceKey<? extends Registry<T>> registry_key, ResourceKey<?> resource_key) {
		return build(registry_key, resource_key.location());
	}

	public static <T> ResourceKey<T> build(ResourceKey<? extends Registry<T>> registry_key, ResourceLocation resource_location) {
		return ResourceKey.create(registry_key, resource_location);
	}

	/**
	 * 获取ResourceKey
	 * 
	 * @param <T>                 注册表类型
	 * @param registry_key        ResourceKey参数定义于net.minecraft.core.registries.Registries
	 * @param namespacedLoc ResourceLocation字符串
	 * @return 返回name对应的T类型注册表的ResourceKey
	 */
	public static <T> ResourceKey<T> build(ResourceKey<? extends Registry<T>> registry_key, String namespacedLoc) {
		return ResourceKey.create(registry_key, ResourceLocations.build(namespacedLoc));
	}

	/**
	 * @param <T>          注册表类型
	 * @param resource_key ResourceKey参数定义于net.minecraft.core.registries.Registries
	 * @param namespace    命名空间
	 * @param path         命名空间path
	 * @return 返回name对应的T类型注册表的ResourceKey
	 */
	public static <T> ResourceKey<T> build(ResourceKey<? extends Registry<T>> registry_key, String namespace, String path) {
		return ResourceKey.create(registry_key, ResourceLocations.build(namespace, path));
	}

	/**
	 * 由于获取TagKey时要传入ResourceKey（注册类型），因此该函数用于从TagKey中获取私有属性ResourceKey registry
	 * 
	 * @param <T>
	 * @param tag_key 要获取ResourceKey的tag
	 * @return 返回tag_key的ResourceKey（注册类型）
	 */
	@SuppressWarnings("unchecked")
	public static <T> ResourceKey<? extends Registry<T>> build(TagKey<T> tag_key) {
		return (ResourceKey<? extends Registry<T>>) Reflection.getValue(tag_key, "registry");
	}

	public static String toString(ResourceKey<?> resourceKey) {
		if (resourceKey == null)
			return null;
		else
			return resourceKey.location().toString();
	}
}
