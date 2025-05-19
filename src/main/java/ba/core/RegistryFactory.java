package ba.core;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.IdentityHashMap;

import jvm.klass.KlassWalker;
import jvm.klass.ObjectManipulator;
import jvm.lang.Reflection;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess.Frozen;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.dimension.DimensionType;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = Core.ModId)
public class RegistryFactory {
	public static final Frozen registryAccess = null;

	/**
	 * 获取模组命名空间下的ResourceLocation
	 * 
	 * @param loc
	 * @return
	 */
	public static ResourceLocation resourceLocation(String loc) {
		return ResourceLocation.fromNamespaceAndPath(Core.ModId, loc);
	}

	public static final DeferredRegister.Blocks BLOCK = deferredBlocksRegister();
	public static final DeferredRegister.Items ITEM = deferredItemsRegister();
	public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = deferredRegister(Registries.CREATIVE_MODE_TAB);
	public static final DeferredRegister<DimensionType> DIMENSION_TYPE = deferredRegister(Registries.DIMENSION_TYPE);

	/**
	 * 获取注册表，该表里的条目会自动注册到ModBus
	 * 
	 * @param <T>
	 * @param registry
	 * @return
	 */
	public static <T> DeferredRegister<T> deferredRegister(Registry<T> registry) {
		DeferredRegister<T> deferredRegister = DeferredRegister.create(registry, Core.ModId);
		return deferredRegister;
	}

	public static <T> DeferredRegister<T> deferredRegister(ResourceKey<? extends Registry<T>> resource_key) {
		DeferredRegister<T> deferredRegister = DeferredRegister.create(resource_key, Core.ModId);
		return deferredRegister;
	}

	public static DeferredRegister.Items deferredItemsRegister() {
		DeferredRegister.Items deferredRegister = DeferredRegister.createItems(Core.ModId);
		return deferredRegister;
	}

	public static DeferredRegister.Blocks deferredBlocksRegister() {
		DeferredRegister.Blocks deferredRegister = DeferredRegister.createBlocks(Core.ModId);
		return deferredRegister;
	}

	private static Field DeferredRegister_f_registeredEventBus = Reflection.getField(DeferredRegister.class, "registeredEventBus");

	/**
	 * 注册目标类中的所有静态DeferredRegister成员
	 * 
	 * @param registryFactory 目标类
	 * @param modBus
	 */
	@SuppressWarnings("rawtypes")
	public static void registerAll(Class<?> registryFactory, IEventBus modBus) {
		KlassWalker.walkFields(registryFactory, (Field f, boolean isStatic, Object value) -> {
			// 判断目标是否是静态字段，以及目标字段是否可以赋值给DeferredRegister（即目标字段是否是DeferredRegister类或其子类）
			if (value != null && DeferredRegister.class.isAssignableFrom(f.getType())) {
				DeferredRegister register = (DeferredRegister) value;
				if (!(boolean) ObjectManipulator.access(register, DeferredRegister_f_registeredEventBus))
					register.register(modBus);
			}
		});
	}

	/**
	 * 注册本类中的所有DeferredRegister
	 * 
	 * @param modBus
	 */
	public static void registerAll(IEventBus modBus) {
		registerAll(RegistryFactory.class, modBus);
	}

	static void registerAll() {
		registerAll(Core.ModBus);
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
