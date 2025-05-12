package ba.core;

import ba.ModEntryObject;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
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
		return ResourceLocation.fromNamespaceAndPath(ModEntryObject.ModId, loc);
	}

	public static final DeferredRegister<Block> BLOCK = deferredRegister(BuiltInRegistries.BLOCK);
	public static final DeferredRegister<Item> ITEM = deferredRegister(BuiltInRegistries.ITEM);

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
