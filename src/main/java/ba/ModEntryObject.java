package ba;

import java.util.Map;
import java.util.function.Supplier;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import ba.entries.dimension.kivotos.Kivotos;
import ba.entries.dimension.kivotos.KivotosDensityFunctions;
import fw.core.Core;
import fw.core.ServerEntry;
import fw.core.registry.MappedRegistries;
import fw.core.registry.MutableMappedRegistry;
import fw.core.registry.RegistryFactory;
import fw.datagen.ExtDataGenerator;
import fw.datagen.annotation.Translation;
import lyra.alpha.reference.FieldReference;
import lyra.filesystem.KlassPath;
import lyra.internal.oops.markWord;
import lyra.klass.KlassLoader;
import lyra.vm.Vm;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.neoforge.registries.DeferredHolder;

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
		Logger.info("JVM is " + Vm.NATIVE_JVM_BIT_VERSION + "-bit with flag UseCompressedOops=" + Vm.UseCompressedOops);
		Logger.info("KlassWord offset is " + markWord.KLASS_WORD_OFFSET + ", lenght is " + markWord.KLASS_WORD_LENGTH);
		Logger.info("Running on PID " + Vm.getProcessId());
		Logger.info("Mod located at " + KlassPath.getKlassPath());
		registerEntries();

		System.err.println(Kivotos.DIMENSION_TYPE.getClass().getCanonicalName());
		Map<DeferredHolder<DensityFunction, ? extends DensityFunction>, Supplier<? extends DensityFunction>> entries = RegistryFactory.deferredRegisterEntries(RegistryFactory.DENSITY_FUNCTION);
		for (Supplier<? extends DensityFunction> e : entries.values())
			System.err.println(e.get());

		ServerEntry.setServerStartCallback(ModEntryObject::rmVanillaFeatures);
	}

	public static void rmVanillaFeatures(MinecraftServer server) {
		System.err.println("into server " + Kivotos.DIMENSION_TYPE);
		System.err.println("into server " + KivotosDensityFunctions.KIVOTOS_BASE_3D_NOISE);
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
