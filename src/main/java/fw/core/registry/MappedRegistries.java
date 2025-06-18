package fw.core.registry;

import java.lang.reflect.Field;
import java.util.IdentityHashMap;

import lyra.klass.KlassWalker;
import lyra.object.ObjectManipulator;
import net.minecraft.client.Minecraft;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;

/**
 * 主要用作运行时修改注册表
 */
public class MappedRegistries {
	@FunctionalInterface
	public interface Operation<T> {
		public void operate(MappedRegistry<T> registry);
	}

	public static final RegistryAccess.Frozen serverRegistryAccess = null;

	public static final RegistryAccess.Frozen clientRegistryAccess() {
		return Minecraft.getInstance().getConnection().registryAccess();
	}

	public static final MappedRegistry<DimensionType> DIMENSION_TYPE = null;
	public static final MappedRegistry<Level> DIMENSION = null;
	public static final MappedRegistry<LevelStem> LEVEL_STEM = null;
	public static final MappedRegistry<Biome> BIOME = null;

	/**
	 * 根据该类声明的静态MappedRegistry字段获取实际的注册表并赋值给这些静态字段
	 * 
	 * @param registryAccess
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static final void fetchRegistries(Class<?> target, RegistryAccess.Frozen registryAccess) {
		KlassWalker.walkTypeFields(target, MappedRegistry.class, (Field f, boolean isStatic, MappedRegistry value) -> {
			// 判断目标是否是静态字段，以及目标字段是否可以赋值给MappedRegistry（即目标字段是否是MappedRegistry类或其子类）
			if (isStatic) {
				ObjectManipulator.setObject(MappedRegistries.class, f.getName(), getUnfrozenRegistry(registryAccess, (ResourceKey) ObjectManipulator.access(Registries.class, f.getName())));
			}
			return true;
		});
	}

	public static final void fetchRegistries() {
		fetchRegistries(MappedRegistries.class, serverRegistryAccess);
	}

	@SuppressWarnings("rawtypes")
	public static final void freezeRegistries(Class<?> target) {
		KlassWalker.walkTypeFields(target, MappedRegistry.class, (Field f, boolean isStatic, MappedRegistry value) -> {
			// 判断目标是否是静态字段，以及目标字段是否可以赋值给MappedRegistry（即目标字段是否是MappedRegistry类或其子类）
			if (isStatic) {
				freezeRegistry((Registry<?>) value);
			}
			return true;
		});
	}

	public static final void freezeRegistries() {
		freezeRegistries(MappedRegistries.class);
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

	public static <T> Registry<T> getRegistry(ResourceKey<? extends Registry<T>> resource_key) {
		return getRegistry(serverRegistryAccess, resource_key);
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

	public static <T> void manipulate(ResourceKey<? extends Registry<T>> resource_key, Operation<T> op) {
		MappedRegistry<T> registry = getUnfrozenRegistry(serverRegistryAccess, resource_key);
		op.operate(registry);
		freezeRegistry(registry);
	}

	public static <T> void manipulate(RegistryAccess.Frozen registryAccess, ResourceKey<? extends Registry<T>> resource_key, Operation<T> op) {
		MappedRegistry<T> registry = getUnfrozenRegistry(registryAccess, resource_key);
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
