package fw.core.registry;

import java.lang.reflect.Field;

import fw.core.Core;
import lyra.klass.KlassWalker;
import lyra.klass.ObjectManipulator;
import lyra.lang.Reflection;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.dimension.DimensionType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RegistryFactory {
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
				if (!(boolean) ObjectManipulator.access(register, "registeredEventBus"))
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

	public static void registerAll() {
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
}
