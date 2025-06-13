package fw.core;

import org.slf4j.Logger;

import fw.Config;
import fw.resources.ResourceLocationBuilder;
import lyra.klass.JarKlassLoader;
import lyra.object.ObjectManipulator;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.javafmlmod.FMLModContainer;

public class Core {
	/**
	 * 为Core设置ModId，该ID将在其他所有类中使用
	 */
	public static final String ModId = Config.ModId;

	public static final IEventBus ModBus = null;

	public static final Logger Logger = Config.Logger;

	private static void loadLibrary() {
		JarKlassLoader.parentClassLoaderField = "fallbackClassLoader";// FML类加载器的默认父类加载器
		JarKlassLoader.loadKlass(Config.LyraPath, Config.AlphaLyrPath);
	}

	public static final void init(FMLModContainer container, IEventBus modBus) {
		if (Config.loadLibBySelf)
			loadLibrary();
		// 初始化赋值ModBus
		ObjectManipulator.setObject(Core.class, "ModBus", modBus);
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
}
