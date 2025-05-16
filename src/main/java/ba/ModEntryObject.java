package ba;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import jvm.jar.JarFiles;
import jvm.jar.JarKlassLoader;
import jvm.klass.ObjectManipulator;
import jvm.vm.VmManipulator;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLModContainer;

//@EventBusSubscriber(modid = ModEntryObject.ModId)
@Mod(value = ModEntryObject.ModId)
public class ModEntryObject {
	public static final String ModName = "BlueArchive: Rendezvous";
	public static final String ModId = "ba";

	public static final IEventBus ModBus = null;

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
		Logger.info("Mod located at " + JarFiles.getJarFilePath());
		// 初始化赋值ModBus
		ObjectManipulator.setObject(this.getClass(), "ModBus", modBus);

		rmVanillaFeatures();
	}

	public static void rmVanillaFeatures() {
		// RegistryFactory.DIMENSION_TYPE;
	}
}
