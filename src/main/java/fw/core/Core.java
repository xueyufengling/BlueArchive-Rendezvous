package fw.core;

import org.slf4j.Logger;

import fw.Config;
import fw.resources.ResourceLocationBuilder;
import lyra.klass.JarKlassLoader;
import lyra.object.ObjectManipulator;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.fml.event.lifecycle.ModLifecycleEvent;
import net.neoforged.fml.javafmlmod.FMLModContainer;

@EventBusSubscriber(modid = Core.ModId, bus = Bus.MOD)
public class Core {
	/**
	 * 为Core设置ModId，该ID将在其他所有类中使用
	 */
	public static final String ModId = Config.ModId;

	public static final IEventBus ModBus = null;

	public static final Logger Logger = Config.Logger;

	/**
	 * 获取指定事件包装的ModContainer
	 * 
	 * @param event
	 * @return
	 */
	public static final ModContainer getModContainer(ModLifecycleEvent event) {
		return (ModContainer) ObjectManipulator.access(event, "container");
	}

	/**
	 * 获取mod的事件总线
	 * 
	 * @param event
	 * @return
	 */
	public static final IEventBus getModEventBus(ModLifecycleEvent event) {
		ModContainer c = getModContainer(event);
		if (c instanceof FMLModContainer fmlc)
			return fmlc.getEventBus();
		else
			throw new RuntimeException("CANNOT get mod event bus in container " + c);
	}

	private static void loadLibrary() {
		JarKlassLoader.parentClassLoaderField = "fallbackClassLoader";// FML类加载器的默认父类加载器
		JarKlassLoader.loadKlass(Config.LyraPath, Config.AlphaLyrPath);
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static final void init(FMLConstructModEvent event) {
		if (Config.loadLibBySelf)
			loadLibrary();
		// 初始化赋值ModBus
		ObjectManipulator.setObject(Core.class, "ModBus", getModEventBus(event));
	}

	/**
	 * 获取模组命名空间下的ResourceLocation
	 * 
	 * @param loc
	 * @return
	 */
	public static ResourceLocation resourceLocation(String loc) {
		return ResourceLocationBuilder.build(Core.ModId, loc);
	}

	public static final void logInfo(String msg) {
		if (Config.logInfo)
			Logger.info(msg);
	}

	public static final void logError(String msg) {
		if (Config.logInfo)
			Logger.error(msg);
	}

	static Class<?> coreInitClass;

	/**
	 * 设置初始化函数所在类
	 * 
	 * @param coreInitCls
	 */
	public static final void init(Class<?> coreInitCls) {
		coreInitClass = coreInitCls;
	}
}
