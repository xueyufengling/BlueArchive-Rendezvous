package ba;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import ba.entries.dimension.kivotos.Kivotos;
import fw.core.Core;
import fw.core.ServerEntry;
import fw.core.registry.MappedRegistries;
import fw.core.registry.MutableMappedRegistry;
import fw.datagen.ExtDataGenerator;
import fw.datagen.annotation.Translation;
import lyra.alpha.reference.FieldReference;
import lyra.filesystem.KlassPath;
import lyra.klass.KlassLoader;
import lyra.klass.ObjectHeader;
import lyra.vm.Vm;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLModContainer;

@Mod(value = ModEntryObject.ModId)
public class ModEntryObject {
	public static final String ModName = "BlueArchive: Rendezvous";
	public static final String ModId = "ba";

	public static final Logger Logger = LogUtils.getLogger();

	public static boolean unregisterNether = true;
	public static boolean unregisterEnd = true;

	static {
		ExtDataGenerator.genLangs(Translation.EN_US, Translation.ZH_CN);
	}

	public static void registerEntries() {
		KlassLoader.loadKlass("ba.entries", true);// 强制加载并初始化未使用的类
	}

	public ModEntryObject(FMLModContainer container, IEventBus modBus) {
		Core.init(container, modBus);
		// 打印调试信息
		Logger.info("JVM is " + Vm.NATIVE_JVM_BIT_VERSION + "-bit with flag UseCompressedOops=" + Vm.NATIVE_JVM_COMPRESSED_OOPS);
		Logger.info("KlassWord offset is " + ObjectHeader.KLASS_WORD_OFFSET + ", lenght is " + ObjectHeader.KLASS_WORD_LENGTH);
		Logger.info("Running on PID " + Vm.getProcessId());
		Logger.info("Mod located at " + KlassPath.getKlassPath());
		registerEntries();
		// ServerEntry.setServerStartCallback(ModEntryObject::rmVanillaFeatures);
	}

	public static void rmVanillaFeatures(MinecraftServer server) {
		MutableMappedRegistry<DimensionType> mutableDimensionTypeRegistry = MutableMappedRegistry.from(MappedRegistries.DIMENSION_TYPE);
		MutableMappedRegistry<Level> mutableDimensionRegistry = MutableMappedRegistry.from(MappedRegistries.DIMENSION);
		MutableMappedRegistry<LevelStem> mutableLevelStemRegistry = MutableMappedRegistry.from(MappedRegistries.LEVEL_STEM);
		ServerEntry.delegateRecoverableRedirectors(
				mutableDimensionTypeRegistry,
				mutableDimensionRegistry,
				mutableLevelStemRegistry,
				// FieldReference.of(BuiltinDimensionTypes.class, "OVERWORLD", Kivotos.DIMENSION_TYPE.resourceKey),
				FieldReference.of(Level.class, "OVERWORLD", Kivotos.DIMENSION_TYPE.resourceKey(Registries.DIMENSION)) // 重定向主世界
		);
		if (unregisterNether) {// 删除下界
			mutableDimensionTypeRegistry.unregister(BuiltinDimensionTypes.NETHER);
			mutableDimensionRegistry.unregister(Level.NETHER);
			mutableLevelStemRegistry.unregister(LevelStem.NETHER);
			ServerEntry.delegateRecoverableRedirectors(
					FieldReference.of(BuiltinDimensionTypes.class, "NETHER"),
					FieldReference.of(Level.class, "NETHER"),
					FieldReference.of(LevelStem.class, "NETHER"));
		}
		if (unregisterEnd) { // 删除末地
			mutableDimensionTypeRegistry.unregister(BuiltinDimensionTypes.END);
			mutableDimensionRegistry.unregister(Level.END);
			mutableLevelStemRegistry.unregister(LevelStem.END);
			ServerEntry.delegateRecoverableRedirectors(
					FieldReference.of(BuiltinDimensionTypes.class, "END"),
					FieldReference.of(Level.class, "END"),
					FieldReference.of(LevelStem.class, "END"));
		}
	}

}
