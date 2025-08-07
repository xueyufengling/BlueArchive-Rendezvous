package fw.core;

import java.util.ArrayList;

import org.slf4j.Logger;

import fw.Config;
import fw.codec.annotation.CodecAutogen;
import fw.core.registry.RegistryFactory;
import fw.resources.ResourceLocationBuilder;
import lyra.klass.JarKlassLoader;
import lyra.klass.KlassLoader;
import lyra.object.ObjectManipulator;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.fml.event.lifecycle.ModLifecycleEvent;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.fml.loading.FMLLoader;

@EventBusSubscriber(modid = Core.ModId, bus = Bus.MOD)
public class Core {
	/**
	 * 为Core设置ModId，该ID将在其他所有类中使用
	 */
	public static final String ModId = Config.ModId;

	public static final ModContainer Mod = null;
	public static final IEventBus ModBus = null;
	public static final Dist Env = FMLLoader.getDist();

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
	 * @param c mod容器
	 * @return
	 */
	public static final IEventBus getModEventBus(ModContainer c) {
		if (c instanceof FMLModContainer fmlc)
			return fmlc.getEventBus();
		else
			throw new RuntimeException("CANNOT get mod event bus in container " + c);
	}

	/**
	 * 获取mod的事件总线
	 * 
	 * @param event 事件
	 * @return
	 */
	public static final IEventBus getModEventBus(ModLifecycleEvent event) {
		return getModEventBus(getModContainer(event));
	}

	private static void loadLibrary() {
		JarKlassLoader.parentClassLoaderField = "fallbackClassLoader";// FML类加载器的默认父类加载器
		JarKlassLoader.loadKlass(Config.LyraPath, Config.AlphaLyrPath);
	}

	/**
	 * Mod构造函数调用后，ModInit注解方法执行前调用
	 * 
	 * @param event
	 */
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	private static final void preinit(FMLConstructModEvent event) {
		if (Config.loadLibBySelf)
			loadLibrary();
		ObjectManipulator.setObject(Core.class, "Mod", getModContainer(event));
		ObjectManipulator.setObject(Core.class, "ModBus", getModEventBus(Mod));// 初始化赋值ModBus
		loadPackage("fw.core.registry.registries");// 加载并初始化注册表的字段初始化器
	}

	private static ArrayList<Runnable> postinitFuncs = new ArrayList<>();

	public static final void addPostinit(Runnable func) {
		postinitFuncs.add(func);
	}

	/**
	 * ModInit注解方法执行后调用
	 * 
	 * @param event
	 */
	@SubscribeEvent(priority = EventPriority.LOWEST)
	private static final void postinit(FMLConstructModEvent event) {
		CodecAutogen.CodecGenerator.autoGenerateCodecs();// 生成CODEC
		RegistryFactory.registerAll();// 注册所有新添加的注册表及其条目
		for (Runnable postinit : postinitFuncs)
			postinit.run();
	}

	/**
	 * 获取模组命名空间下的ResourceLocation
	 * 
	 * @param loc
	 * @return
	 */
	public static ResourceLocation modResourceLocation(String loc) {
		return ResourceLocationBuilder.build(Core.ModId, loc);
	}

	public static String modNamespacedId(String id) {
		return ModId + ResourceLocation.NAMESPACE_SEPARATOR + id;
	}

	public static void loadPackage(String pkg) {
		KlassLoader.loadKlass(pkg, true);// 强制加载并初始化未使用的类
	}

	public static void loadClientPackage(String pkg) {
		ExecuteIn.Client(() -> {
			KlassLoader.loadKlass(pkg, true);
		});
	}

	public static final void logDebug(String msg) {
		if (Config.logInfo)
			Logger.debug(msg);
	}

	public static final void logDebug(String msg, Throwable e) {
		if (Config.logInfo)
			Logger.debug(msg, e);
	}

	public static final void logInfo(String msg) {
		if (Config.logInfo)
			Logger.info(msg);
	}

	public static final void logInfo(String msg, Throwable e) {
		if (Config.logInfo)
			Logger.info(msg, e);
	}

	public static final void logWarn(String msg) {
		if (Config.logInfo)
			Logger.warn(msg);
	}

	public static final void logWarn(String msg, Throwable e) {
		if (Config.logInfo)
			Logger.warn(msg, e);
	}

	public static final void logError(String msg) {
		if (Config.logInfo)
			Logger.error(msg);
	}

	public static final void logError(String msg, Throwable e) {
		if (Config.logInfo)
			Logger.error(msg, e);
	}
}
