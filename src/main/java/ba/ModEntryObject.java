package ba;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import fw.core.Core;
import fw.core.ServerEntry;
import fw.core.registry.MappedRegistryManipulator;
import fw.core.registry.MutableMappedRegistry;
import fw.datagen.ExtDataGenerator;
import lyra.filesystem.KlassPath;
import lyra.filesystem.jar.JarKlassLoader;
import lyra.klass.KlassLoader;
import lyra.klass.ObjectHeader;
import lyra.vm.Vm;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
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
		ExtDataGenerator.genLangs("en_us", "zh_cn");
	}

	public static void loadLibrary() {
		JarKlassLoader.parentClassLoaderField = "fallbackClassLoader";// FML类加载器的默认父类加载器
		JarKlassLoader.loadKlass("/libs/CommonUtils.jar");
	}

	public static void registerEntries() {
		KlassLoader.loadKlass("ba.entries", true);// 强制加载并初始化未使用的类
	}

	public ModEntryObject(FMLModContainer container, IEventBus modBus) {
		// 打印调试信息
		Logger.info("JVM is " + Vm.NATIVE_JVM_BIT_VERSION + "-bit with flag UseCompressedOops=" + Vm.NATIVE_JVM_COMPRESSED_OOPS);
		Logger.info("KlassWord offset is " + ObjectHeader.KLASS_WORD_OFFSET + ", lenght is " + ObjectHeader.KLASS_WORD_LENGTH);
		Logger.info("Running on PID " + Vm.getProcessId());
		Logger.info("Mod located at " + KlassPath.getKlassPath());
		Core.init(container, modBus);
		registerEntries();

		ServerEntry.setServerCallback(ModEntryObject::rmVanillaFeatures);
	}

	public static void rmVanillaFeatures(MinecraftServer server) {
		MappedRegistryManipulator.manipulate(Registries.DIMENSION_TYPE, (MappedRegistry<DimensionType> registry) -> {
			MutableMappedRegistry<DimensionType> mutableRegistry = MutableMappedRegistry.from(registry);
			mutableRegistry.deleteEntry(BuiltinDimensionTypes.NETHER, Level.class, "NETHER");// 删除地狱
			mutableRegistry.deleteEntry(BuiltinDimensionTypes.END, Level.class, "END");// 删除末地
		});
	}

}
