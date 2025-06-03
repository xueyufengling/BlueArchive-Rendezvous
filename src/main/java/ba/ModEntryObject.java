package ba;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import ba.entries.dimension.kivotos.Kivotos;
import ba.entries.items.BaCurrency;
import fw.core.Core;
import fw.core.ServerEntry;
import fw.core.registry.MappedRegistries;
import fw.core.registry.MutableMappedRegistry;
import fw.datagen.ExtDataGenerator;
import fw.resources.ResourceKeyBuilder;
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

	static {
		ExtDataGenerator.genLangs("en_us", "zh_cn");
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
		redirectOverworld();
		ServerEntry.setServerStartCallback(ModEntryObject::rmVanillaFeatures);
	}

	// 重定向主世界
	public static void redirectOverworld() {
		ServerEntry.delegateRecoverableRedirectors(
				FieldReference.of(BuiltinDimensionTypes.class, "OVERWORLD", Kivotos.DIMENSION_TYPE.resourceKey),
				FieldReference.of(Level.class, "OVERWORLD", Kivotos.DIMENSION_TYPE.resourceKey(Registries.DIMENSION)),
				FieldReference.of(LevelStem.class, "OVERWORLD", Kivotos.LEVEL_STEM.resourceKey));
	}

	public static void rmVanillaFeatures(MinecraftServer server) {
		System.err.println("kivotos/land " + Kivotos.LAND_DENSITY_FUNCTION.value());
		System.err.println("gem " + BaCurrency.gem.value());
		MutableMappedRegistry<DimensionType> mutableDimensionTypeRegistry = MutableMappedRegistry.from(MappedRegistries.DIMENSION_TYPE);
		mutableDimensionTypeRegistry.deleteEntry(BuiltinDimensionTypes.NETHER, FieldReference.of(Level.class, "NETHER"));// 删除地狱
		mutableDimensionTypeRegistry.deleteEntry(BuiltinDimensionTypes.END, FieldReference.of(Level.class, "END"));// 删除末地
	}

}
