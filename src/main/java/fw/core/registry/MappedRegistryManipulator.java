package fw.core.registry;

import java.util.IdentityHashMap;

import lyra.klass.ObjectManipulator;
import net.minecraft.client.Minecraft;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;

public class MappedRegistryManipulator {
	public static final RegistryAccess.Frozen serverRegistryAccess = null;

	public static final RegistryAccess.Frozen clientRegistryAccess() {
		return Minecraft.getInstance().getConnection().registryAccess();
	}

	/**
	 * 获取注册表
	 * 
	 * @param <T>
	 * @param resource_key
	 * @return
	 * @since 1.21
	 */
	public static <T> Registry<T> getRegistry(RegistryAccess.Frozen registryAccess, ResourceKey<? extends Registry<T>> resource_key) {
		return registryAccess.registryOrThrow(resource_key);
	}

	/**
	 * 获取解冻的注册表
	 * 
	 * @param <T>
	 * @param resource_key
	 * @return
	 */
	public static <T> MappedRegistry<T> getUnfrozenRegistry(RegistryAccess.Frozen registryAccess, ResourceKey<? extends Registry<T>> resource_key) {
		return unfreezeRegistry(getRegistry(registryAccess, resource_key));
	}

	/**
	 * 操作注册表，自动解冻并在操作完成后冻结注册表
	 * 
	 * @param <T>
	 * @param resource_key
	 * @param op
	 */
	public static <T> void manipulate(RegistryAccess.Frozen registryAccess, ResourceKey<? extends Registry<T>> resource_key, MappedRegistryOperation<T> op) {
		MappedRegistry<T> registry = getUnfrozenRegistry(registryAccess, resource_key);
		op.operate(registry);
		freezeRegistry(registry);
	}

	public static <T> void manipulate(ResourceKey<? extends Registry<T>> resource_key, MappedRegistryOperation<T> op) {
		MappedRegistry<T> registry = getUnfrozenRegistry(serverRegistryAccess, resource_key);
		op.operate(registry);
		freezeRegistry(registry);
	}

	public static <T> boolean isFrozen(Registry<T> registry) {
		return (boolean) ObjectManipulator.access(registry, "frozen");
	}

	/**
	 * 解冻注册表冻结以注册
	 * 
	 * @since 1.21
	 */
	public static <T> MappedRegistry<T> unfreezeRegistry(Registry<T> registry) {
		ObjectManipulator.setBoolean(registry, "frozen", false);// 解冻注册表
		ObjectManipulator.setObject(registry, "unregisteredIntrusiveHolders", new IdentityHashMap<>());// 将尚未注册的Holder清空，防止抛出错误
		return (MappedRegistry<T>) registry;
	}

	/**
	 * 解冻注册表冻结以注册
	 * 
	 * @return 操作是否成功
	 * @since 1.21
	 */
	public static <T> MappedRegistry<T> unfreezeRegistry(RegistryAccess.Frozen registryAccess, ResourceKey<? extends Registry<T>> resource_key) {
		return unfreezeRegistry(getRegistry(registryAccess, resource_key));
	}

	/**
	 * 冻结注册表
	 * 
	 * @return
	 * @since 1.21
	 */
	public static <T> void freezeRegistry(Registry<T> registry) {
		registry.freeze();
	}
}
