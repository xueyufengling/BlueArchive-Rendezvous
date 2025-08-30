package lepus.mc.core.registry;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Supplier;

import lepus.mc.core.Core;
import lyra.alpha.struct.K2HashMap;
import lyra.klass.KlassWalker;
import lyra.lang.Reflection;
import lyra.object.ObjectManipulator;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * 启动阶段的注册表操作
 */
public class RegistryFactory {
	private static final K2HashMap<ResourceKey<?>, String, DeferredRegister<?>> registries = new K2HashMap<>();

	public static <T> DeferredRegister<T> deferredRegister(Registry<T> registry, String modId) {
		return deferredRegister(registry.key(), modId);
	}

	/**
	 * 获取注册表，所有通过该方法获取的注册表将缓存至Map，并且都可以通过registerAll()一次性全部自动注册到ModBus。<br>
	 * 如果有注册表已经被注册到ModBus，那么将忽略该注册表。
	 * 
	 * @param <T>
	 * @param registryKey
	 * @param modId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> DeferredRegister<T> deferredRegister(ResourceKey<? extends Registry<T>> registryKey, String modId) {
		DeferredRegister<T> deferredRegister = (DeferredRegister<T>) registries.computeIfPresent(registryKey, modId, (ResourceKey<?> resKey, String mod_id) -> {
			if (resKey == Registries.ITEM)
				return DeferredRegister.createItems(mod_id);
			else if (resKey == Registries.BLOCK)
				return DeferredRegister.createBlocks(mod_id);
			else
				return DeferredRegister.create((ResourceKey<? extends Registry<T>>) resKey, mod_id);
		});
		return deferredRegister;
	}

	public static void register(DeferredRegister<?> register, IEventBus modBus) {
		if (!(boolean) ObjectManipulator.access(register, "registeredEventBus"))
			register.register(modBus);
	}

	/**
	 * 注册目标类中的所有静态DeferredRegister成员
	 * 
	 * @param registryFactory 目标类
	 * @param modBus
	 */
	@SuppressWarnings("rawtypes")
	public static void registerStatic(Class<?> registryFactory, IEventBus modBus) {
		KlassWalker.walkFields(registryFactory, (Field f, boolean isStatic, Object value) -> {
			// 判断目标是否是静态字段，以及目标字段是否可以赋值给DeferredRegister（即目标字段是否是DeferredRegister类或其子类）
			if (value != null && Reflection.is(f, DeferredRegister.class)) {
				register((DeferredRegister) value, modBus);
			}
			return true;
		});
	}

	/**
	 * 注册本类中的所有DeferredRegister
	 * 
	 * @param modBus
	 */
	public static void registerAll(IEventBus modBus) {
		for (DeferredRegister<?> register : registries.values()) {
			Core.logInfo("Registered registry " + register.getRegistryKey().location());
			register(register, modBus);
		}
	}

	public static void registerAll() {
		Core.logInfo("RegistryFactory start to auto register.");
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

	@SuppressWarnings("unchecked")
	public static <R> Map<DeferredHolder<R, ? extends R>, Supplier<? extends R>> deferredRegisterEntries(DeferredRegister<R> register) {
		return (Map<DeferredHolder<R, ? extends R>, Supplier<? extends R>>) ObjectManipulator.access(register, "entries");
	}
}
