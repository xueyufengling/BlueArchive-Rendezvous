package lepus.mc.resources;

import lyra.lang.InternalUnsafe;
import lyra.object.ObjectManipulator;
import net.minecraft.resources.ResourceLocation;

public class ResourceLocations {
	public String namespace;
	public String path;

	public ResourceLocations(String namespacedLoc) {
		String[] namespace_id = parse(namespacedLoc);
		this.namespace = namespace_id[0];
		this.path = namespace_id[1];
	}

	public ResourceLocations(String namespace, String path) {
		this.namespace = namespace;
		this.path = path;
	}

	@Override
	public String toString() {
		return namespace + ResourceLocation.NAMESPACE_SEPARATOR + path;
	}

	/**
	 * 解析带命名空间的id，返回数组[0]为命名空间（没有则是默认的minecraft）[1]为id
	 * 
	 * @param namespacedLoc 带命名空间的id
	 * @return 命名空间和id
	 */
	public static String[] parse(String namespacedLoc) {
		int delim_idx = namespacedLoc.indexOf(ResourceLocation.NAMESPACE_SEPARATOR);
		String result[] = new String[] { ResourceLocation.DEFAULT_NAMESPACE, null };
		if (delim_idx != -1)// 如果没有命名空间，则默认为ResourceLocation.DEFAULT_NAMESPACE空间
			result[0] = namespacedLoc.substring(0, delim_idx);
		result[1] = namespacedLoc.substring(delim_idx + 1);
		return result;
	}

	/**
	 * 通过带命名空间的id获取ResourceLocation，不检查命名空间和路径是否合法
	 * 
	 * @param namespacedLoc
	 * @return
	 */
	public static ResourceLocation build(String namespacedLoc) {
		if (namespacedLoc == null)
			return null;
		String[] namespace_path = parse(namespacedLoc);
		return build(namespace_path[0], namespace_path[1]);
	}

	public static ResourceLocation setNamespace(ResourceLocation resource_location, String namespace) {
		ObjectManipulator.setDeclaredMemberObject(resource_location, "namespace", namespace);
		return resource_location;
	}

	public static ResourceLocation setPath(ResourceLocation resource_location, String path) {
		ObjectManipulator.setDeclaredMemberObject(resource_location, "path", path);
		return resource_location;
	}

	public static ResourceLocation set(ResourceLocation resource_location, String namespace, String path) {
		return setNamespace(setPath(resource_location, path), namespace);
	}

	public static ResourceLocation set(ResourceLocation resource_location, String namespacedLoc) {
		String[] namespace_path = parse(namespacedLoc);
		return set(resource_location, namespace_path[0], namespace_path[1]);
	}

	public static ResourceLocation build(String namespace, String path) {
		ResourceLocation resource_location = InternalUnsafe.allocateInstance(ResourceLocation.class);
		return set(resource_location, namespace, path);
	}

	public ResourceLocation build() {
		return build(namespace, path);
	}

	public static String toString(ResourceLocation resloc) {
		return resloc == null ? null : resloc.toString();
	}
}
