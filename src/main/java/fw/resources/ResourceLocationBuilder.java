package fw.resources;

import lyra.lang.InternalUnsafe;
import lyra.object.ObjectManipulator;
import net.minecraft.resources.ResourceLocation;

public class ResourceLocationBuilder {
	public String namespace;
	public String path;

	public ResourceLocationBuilder(String namespaced_id) {
		String[] namespace_id = parse(namespaced_id);
		this.namespace = namespace_id[0];
		this.path = namespace_id[1];
	}

	public ResourceLocationBuilder(String namespace, String path) {
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
	 * @param reslocWithNamespace 带命名空间的id
	 * @return 命名空间和id
	 */
	public static String[] parse(String reslocWithNamespace) {
		int delim_idx = reslocWithNamespace.indexOf(ResourceLocation.NAMESPACE_SEPARATOR);
		String result[] = new String[] { ResourceLocation.DEFAULT_NAMESPACE, null };
		if (delim_idx != -1)// 如果没有命名空间，则默认为ResourceLocation.DEFAULT_NAMESPACE空间
			result[0] = reslocWithNamespace.substring(0, delim_idx);
		result[1] = reslocWithNamespace.substring(delim_idx + 1);
		return result;
	}

	/**
	 * 通过带命名空间的id获取ResourceLocation，不检查命名空间和路径是否合法
	 * 
	 * @param reslocWithNamespace
	 * @return
	 */
	public static ResourceLocation build(String reslocWithNamespace) {
		if (reslocWithNamespace == null)
			return null;
		String[] namespace_path = parse(reslocWithNamespace);
		return build(namespace_path[0], namespace_path[1]);
	}

	public static ResourceLocation build(String namespace, String path) {
		ResourceLocation resource_location = InternalUnsafe.allocateInstance(ResourceLocation.class);
		ObjectManipulator.setDeclaredMemberObject(resource_location, "namespace", namespace);
		ObjectManipulator.setDeclaredMemberObject(resource_location, "path", path);
		return resource_location;
	}

	public ResourceLocation build() {
		return build(namespace, path);
	}

	public static String toString(ResourceLocation resloc) {
		return resloc == null ? null : resloc.toString();
	}
}
