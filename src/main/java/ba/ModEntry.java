package ba;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import ba.entries.dimension.shittim_chest.ShittimChest;
import fw.core.Core;
import fw.core.ModInit;
import fw.datagen.annotation.LangDatagen;
import fw.datagen.annotation.Translation;
import fw.dimension.Dimensions;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = ModEntry.ModId)
public class ModEntry {
	public static final String ModName = "BlueArchive: Rendezvous";
	public static final String ModId = "ba";

	public static final Logger Logger = LogUtils.getLogger();

	static {
		LangDatagen.LangProvider.genLangs(Translation.EN_US, Translation.ZH_CN);
		ModInit.Initializer.forInit();
	}

	@ModInit
	public static final void init(Dist env) {
		Core.loadPackage("ba.entries");
		Core.loadClientPackage("ba.client.render");
		Dimensions.removeTheNether(true);
		Dimensions.removeTheEnd(true);
		Dimensions.redirectOverworld(ShittimChest.ID);
	}
}
