package ba;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import ba.entries.dimension.kivotos.Kivotos;
import ba.entries.dimension.shittim_chest.ShittimChest;
import lepus.mc.core.Core;
import lepus.mc.core.ExecuteIn;
import lepus.mc.core.ModInit;
import lepus.mc.datagen.annotation.LangDatagen;
import lepus.mc.datagen.annotation.Translation;
import lepus.mc.dimension.Dimensions;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = ModEntry.ModId)
public class ModEntry {
	public static final String ModName = "BlueArchive: Rendezvous";

	public static final String ModId = "ba";

	public static final String ModIdPrefix = ModId + ":";

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
		ExecuteIn.Server(() -> {
			Dimensions.redirectOverworld(Kivotos.ID);
			Logger.info("Running on server-side, overworld redirected to Kivotos");
		});
		ExecuteIn.Client(() -> {
			Dimensions.redirectOverworld(ShittimChest.ID);
			Logger.info("Running on client-side, overworld redirected to Shittim Chest");
		});
		Dimensions.redirectOverworld(Kivotos.ID);
	}
}
