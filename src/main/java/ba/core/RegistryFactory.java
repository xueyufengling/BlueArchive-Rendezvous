package ba.core;

import java.util.IdentityHashMap;

import ba.ModEntryObject;
import jvm.klass.ObjectManipulator;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess.Frozen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.dimension.DimensionType;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = ModEntryObject.ModId)
public class RegistryFactory {
	public static final Frozen registryAccess = null;

	/**
	 * 获取模组命名空间下的ResourceLocation
	 * 
	 * @param loc
	 * @return
	 */
	public static ResourceLocation resourceLocation(String loc) {
		return ResourceLocation.fromNamespaceAndPath(ModEntryObject.ModId, loc);
	}

	public static final DeferredRegister<Block> BLOCK = deferredRegister(BuiltInRegistries.BLOCK);
	public static final DeferredRegister<Item> ITEM = deferredRegister(BuiltInRegistries.ITEM);
	//public static final DeferredRegister<DimensionType> DIMENSION_TYPE = deferredRegister(Registries.DIMENSION_TYPE);

	/**
	 * 获取注册表，该表里的条目会自动注册到ModBus
	 * 
	 * @param <T>
	 * @param registry
	 * @return
	 */
	public static <T> DeferredRegister<T> deferredRegister(Registry<T> registry) {
		DeferredRegister<T> deferredRegister = DeferredRegister.create(registry, ModEntryObject.ModId);
		deferredRegister.register(ModEntryObject.ModBus);
		return deferredRegister;
	}

	public static <T> DeferredRegister<T> deferredRegister(ResourceKey<? extends Registry<T>> resource_key) {
		return deferredRegister(getRegistry(resource_key));
	}

	public static <R, T extends R> DeferredHolder<R, T> register(DeferredRegister<R> registry, String registerName, T obj) {
		return registry.register(registerName, () -> obj);
	}

	/**
	 * 调用class的无参构造方法作为注册返回的结果，如果需要调用带参数的构造方法请使用register(DeferredRegister<R> registry, String registerName, T obj)，自行实例化完成以后再将对象传入
	 * 
	 * @param <R>
	 * @param <T>
	 * @param registry
	 * @param registerName
	 * @param blockCls
	 * @return
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	public static <R, T extends R> DeferredHolder<R, T> register(DeferredRegister<R> registry, String registerName, Class<?> blockCls) {
		return registry.register(registerName, () -> {
			try {
				return (T) blockCls.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
			return null;
		});
	}

	/**
	 * @param <T>
	 * @param resource_key
	 * @return
	 */
	public static <T> Registry<T> getRegistry(ResourceKey<? extends Registry<T>> resource_key) {
		return registryAccess.registryOrThrow(resource_key);
	}

	/**
	 * 解冻注册表冻结以注册，不记录入is_registrise_frozen，因此尽量不要使用
	 * 
	 * @return 操作是否成功
	 */
	public static <T> boolean unfreezeRegistry(Registry<T> registry) {
		return ObjectManipulator.setBoolean(registry, "net.minecraft.core.MappedRegistry.frozen", false) && ObjectManipulator.setObject(registry, "net.minecraft.core.MappedRegistry.unregisteredIntrusiveHolders", new IdentityHashMap<>());
	}

	/**
	 * 解冻注册表冻结以注册，并记录入is_registrise_frozen，应当总是使用该方法解冻注册表
	 * 
	 * @return 操作是否成功
	 */
	public static <T> boolean unfreezeRegistry(ResourceKey<? extends Registry<T>> resource_key) {
		return unfreezeRegistry(getRegistry(resource_key));
	}

	/**
	 * 冻结注册表，并记录入is_registrise_frozen
	 * 
	 * @return
	 */
	public static <T> void freezeRegistry(Registry<T> registry) {
		registry.freeze();
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST) // 最高优先级以获取注册表
	public static void onServerStartup(ServerStartingEvent event) {
		ObjectManipulator.setObject(RegistryFactory.class, "registryAccess", event.getServer().registryAccess());
	}
}
