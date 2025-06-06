package fw.datagen;

import java.util.ArrayList;

import fw.core.Core;
import fw.datagen.annotation.ItemDatagen;
import fw.datagen.annotation.LangDatagen;
import fw.datagen.annotation.RegistryDatagen;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = Core.ModId, bus = EventBusSubscriber.Bus.MOD)
public class ExtDataGenerator {
	private static final ArrayList<String> genLangs = new ArrayList<>();

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput output = generator.getPackOutput();
		ExistingFileHelper helper = event.getExistingFileHelper();
		// 注册表内容生成
		RegistryDatagen.RegistriesProvider registriesProvider = new RegistryDatagen.RegistriesProvider(output, event.getLookupProvider());
		generator.addProvider(true, registriesProvider);
		// 物品数据生成
		generator.addProvider(event.includeClient(), new ItemDatagen.ModelProvider(output, helper));
		for (String lang : genLangs)
			generator.addProvider(event.includeClient(), new LangDatagen.LangProvider(output, lang));
	}

	public static final void genLang(String lang) {
		String formatLang = lang.toLowerCase();
		if (!genLangs.contains(formatLang))
			genLangs.add(formatLang);
	}

	public static final void genLangs(String... langs) {
		for (String lang : langs)
			genLang(lang);
	}
}
