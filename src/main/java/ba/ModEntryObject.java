package ba;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import ba.entries.dimension.kivotos.Kivotos;
import fw.core.Core;
import fw.core.CoreInit;
import fw.datagen.annotation.LangDatagen;
import fw.datagen.annotation.Translation;
import fw.dimension.Dimensions;
import lyra.filesystem.KlassPath;
import lyra.internal.oops.markWord;
import lyra.klass.KlassLoader;
import lyra.vm.Vm;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLModContainer;

@Mod(value = ModEntryObject.ModId)
public class ModEntryObject {
	public static final String ModName = "BlueArchive: Rendezvous";
	public static final String ModId = "ba";

	public static final Logger Logger = LogUtils.getLogger();

	static {
		LangDatagen.LangProvider.genLangs(Translation.EN_US, Translation.ZH_CN);
		Core.init(ModEntryObject.class);
	}

	public ModEntryObject(FMLModContainer container, IEventBus modBus) {

	}

	public static void registerEntries() {
		KlassLoader.loadKlass("ba.entries", true);// 强制加载并初始化未使用的类
	}

	@CoreInit
	public static final void init() {
		System.err.println("@CoreInit");
		// 打印调试信息
		Logger.info("JVM is " + Vm.NATIVE_JVM_BIT_VERSION + "-bit with flag UseCompressedOops=" + Vm.UseCompressedOops);
		Logger.info("KlassWord offset is " + markWord.KLASS_WORD_OFFSET + ", lenght is " + markWord.KLASS_WORD_LENGTH);
		Logger.info("Running on PID " + Vm.getProcessId());
		Logger.info("Mod located at " + KlassPath.getKlassPath());
		registerEntries();

		Dimensions.removeTheNether(true);
		Dimensions.removeTheEnd(true);
		Dimensions.redirectOverworld(Kivotos.DIMENSION_TYPE, Kivotos.LEVEL_STEM);
	}
}
