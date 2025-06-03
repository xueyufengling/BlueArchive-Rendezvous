package fw.core;

import fw.Config;
import fw.core.registry.RegistryFactory;
import fw.resources.ResourceLocationBuilder;
import lyra.filesystem.jar.JarKlassLoader;
import lyra.klass.ObjectManipulator;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.javafmlmod.FMLModContainer;

public class Core {
	/**
	 * 为Core设置ModId，该ID将在其他所有类中使用
	 */
	public static final String ModId = Config.ModId;

	public static final IEventBus ModBus = null;

	private static void loadLibrary() {
		JarKlassLoader.parentClassLoaderField = "fallbackClassLoader";// FML类加载器的默认父类加载器
		JarKlassLoader.loadKlass(Config.LyraPath, Config.AlphaLyrPath);
	}

	public static final void init(FMLModContainer container, IEventBus modBus) {
		loadLibrary();
		// 初始化赋值ModBus
		ObjectManipulator.setObject(Core.class, "ModBus", modBus);
		RegistryFactory.registerAll();
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
}
