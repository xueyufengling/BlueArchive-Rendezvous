package lepus.mc.core;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import lepus.mc.codec.annotation.CodecAutogen;
import lepus.mc.core.registry.RegistryFactory;
import lepus.mc.datagen.annotation.RegistryEntry;
import lepus.mc.event.ClientLifecycleTrigger;
import lyra.filesystem.KlassPath;
import lyra.internal.oops.markWord;
import lyra.klass.JarKlassLoader;
import lyra.klass.KlassLoader;
import lyra.object.ObjectManipulator;
import lyra.vm.Vm;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.RegistryAccess;
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

@EventBusSubscriber(bus = Bus.MOD)
public class Core {
	public static final ModContainer Mod = null;
	public static final IEventBus ModBus = null;
	public static final Dist Env = FMLLoader.getDist();

	public static final Logger Logger = LogUtils.getLogger();

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

	private static final ArrayList<String> preloadLibs = new ArrayList<>();

	private static boolean loadedPreloadLibs = false;

	/**
	 * 添加预加载的库
	 * 
	 * @param libs
	 */
	public static final void preloadLibs(String... libs) {
		if (loadedPreloadLibs)
			throw new IllegalStateException("Preload stage has passed, call this method in mod entry class's <cinit> or <init>.");
		else
			preloadLibs.addAll(List.of(libs));
	}

	private static void loadLibrary() {
		JarKlassLoader.parentClassLoaderField = "fallbackClassLoader";// FML类加载器的默认父类加载器
		for (String lib : preloadLibs) {
			Core.logInfo("Preloading library " + lib);
			JarKlassLoader.loadKlass(lib);
		}
		loadedPreloadLibs = true;
	}

	/**
	 * 本库需要预先强制加载初始化的包
	 */
	private static final List<String> internalPackages = List.of(
			"lepus.mc.core.registry.registries", // 加载并初始化注册表的字段初始化器
			"lepus.mc.terrain.algorithm"// 加载地形生成算法包以生成对应的CODEC
	);

	/**
	 * Mod构造函数调用后，ModInit注解方法执行前调用
	 * 
	 * @param event
	 */
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	private static final void preinit(FMLConstructModEvent event) {
		loadLibrary();
		// 打印调试信息
		Logger.info("JVM bit-version=" + Vm.NATIVE_JVM_BIT_VERSION + ", flag UseCompressedOops=" + Vm.UseCompressedOops);
		Logger.info("KlassWord offset=" + markWord.KLASS_WORD_OFFSET + ", lenght=" + markWord.KLASS_WORD_LENGTH);
		Logger.info("Running on " + Core.Env + " environment with PID=" + Vm.getProcessId());
		Logger.info("Mod classpath=\"" + KlassPath.getKlassPath() + '\"');
		ObjectManipulator.setObject(Core.class, "Mod", getModContainer(event));
		ObjectManipulator.setObject(Core.class, "ModBus", getModEventBus(Mod));// 初始化赋值ModBus
		loadPackage(internalPackages);
		ClientLifecycleTrigger.CLIENT_CONNECT.addCallback(EventPriority.HIGHEST, (ClientLevel level, RegistryAccess.Frozen registryAccess) -> {
			ModInit.Initializer.executeAllInitFuncs(null, ModInit.Stage.CLIENT_CONNECT);
		});
		ModInit.Initializer.executeAllInitFuncs(event, ModInit.Stage.PRE_INIT);
	}

	/**
	 * ModInit注解方法执行后调用
	 * 
	 * @param event
	 */
	@SubscribeEvent(priority = EventPriority.LOWEST)
	private static final void postinit(FMLConstructModEvent event) {
		CodecAutogen.CodecGenerator.generateCodecs();// 生成CODEC
		RegistryEntry.DeferredEntryHolderRegister.registerAll();
		RegistryFactory.registerAll();// 注册所有新添加的注册表及其条目
		ModInit.Initializer.executeAllInitFuncs(event, ModInit.Stage.POST_INIT);
	}

	public static void loadPackage(String pkg) {
		KlassLoader.loadKlass(pkg, true);// 强制加载并初始化未使用的类
		Logger.info("Loaded package " + pkg);
	}

	public static void loadPackage(List<String> pkgs) {
		for (String pkg : pkgs)
			loadPackage(pkg);
	}

	public static void loadClientPackage(String pkg) {
		ExecuteIn.Client(() -> {
			KlassLoader.loadKlass(pkg, true);
		});
		Logger.info("Loaded client-side package " + pkg);
	}

	public static void loadClientPackage(List<String> pkgs) {
		for (String pkg : pkgs)
			loadClientPackage(pkg);
	}

	private static boolean printLog = true;

	public static final void setPrintLog(boolean print) {
		printLog = print;
	}

	public static final void logDebug(String msg) {
		if (printLog)
			Logger.debug(msg);
	}

	public static final void logDebug(String msg, Throwable e) {
		if (printLog)
			Logger.debug(msg, e);
	}

	public static final void logInfo(String msg) {
		if (printLog)
			Logger.info(msg);
	}

	public static final void logInfo(String msg, Throwable e) {
		if (printLog)
			Logger.info(msg, e);
	}

	public static final void logWarn(String msg) {
		if (printLog)
			Logger.warn(msg);
	}

	public static final void logWarn(String msg, Throwable e) {
		if (printLog)
			Logger.warn(msg, e);
	}

	public static final void logError(String msg) {
		if (printLog)
			Logger.error(msg);
	}

	public static final void logError(String msg, Throwable e) {
		if (printLog)
			Logger.error(msg, e);
	}
}
