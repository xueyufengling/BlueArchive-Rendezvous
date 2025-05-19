package ba;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import ba.core.Core;
import jvm.filesystem.KlassPath;
import jvm.filesystem.jar.JarKlassLoader;
import jvm.klass.KlassLoader;
import jvm.vm.VmManipulator;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLModContainer;

@Mod(value = ModEntryObject.ModId)
public class ModEntryObject {
	public static final String ModName = "BlueArchive: Rendezvous";
	public static final String ModId = "ba";

	public static final Logger Logger = LogUtils.getLogger();

	static {
		loadLibrary();
	}

	public static void loadLibrary() {
		JarKlassLoader.parentClassLoaderField = "fallbackClassLoader";// FML类加载器的默认父类加载器
		JarKlassLoader.loadKlass("/libs/CommonUtils.jar");
	}

	public ModEntryObject(FMLModContainer container, IEventBus modBus) {
		// 打印调试信息
		Logger.info("Running on PID " + VmManipulator.getProcessId());
		Logger.info("Mod located at " + KlassPath.getKlassPath());
		Core.init(container, modBus);
		KlassLoader.loadKlass("ba.entries", true);// 强制加载并初始化未使用的类

		rmVanillaFeatures();
	}

	public static void rmVanillaFeatures() {
		// RegistryFactory.DIMENSION_TYPE;
	}

}
