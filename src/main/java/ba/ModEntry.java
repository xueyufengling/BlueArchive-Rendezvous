package ba;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import ba.entries.dimension.shittim_chest.ShittimChest;
import fw.core.ModInit;
import fw.datagen.annotation.LangDatagen;
import fw.datagen.annotation.Translation;
import fw.dimension.Dimensions;
import lyra.filesystem.KlassPath;
import lyra.internal.oops.markWord;
import lyra.klass.KlassLoader;
import lyra.vm.Vm;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = ModEntry.ModId)
public class ModEntry {
	public static final String ModName = "BlueArchive: Rendezvous";
	public static final String ModId = "ba";

	public static final Logger Logger = LogUtils.getLogger();

	static {
		LangDatagen.LangProvider.genLangs(Translation.EN_US, Translation.ZH_CN);
		ModInit.Initializer.forInit(ModEntry.class);
	}

	public static void registerEntries() {
		KlassLoader.loadKlass("ba.entries", true);// 强制加载并初始化未使用的类
		Logger.info("Loaded entries class");
	}

	@ModInit
	public static final void init(Dist env) {
		// 打印调试信息
		Logger.info("JVM is " + Vm.NATIVE_JVM_BIT_VERSION + "-bit with flag UseCompressedOops=" + Vm.UseCompressedOops);
		Logger.info("KlassWord offset is " + markWord.KLASS_WORD_OFFSET + ", lenght is " + markWord.KLASS_WORD_LENGTH);
		Logger.info("Running on " + env + " environment with PID " + Vm.getProcessId());
		Logger.info("Mod located at " + KlassPath.getKlassPath());
		registerEntries();
		Dimensions.removeTheNether(true);
		Dimensions.removeTheEnd(true);
		Dimensions.redirectOverworldMod(ShittimChest.ID);
	}
}
