package fw.datagen;

import java.util.ArrayList;

import fw.core.Core;
import fw.datagen.annotation.ItemDatagen;
import fw.datagen.annotation.LangDatagen;
import fw.datagen.annotation.RegistryDatagen;
import lyra.object.ObjectManipulator;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = Core.ModId, bus = EventBusSubscriber.Bus.MOD)
public class ExtDataGenerator {
	@SubscribeEvent
	@SuppressWarnings("unchecked")
	public static void gatherData(GatherDataEvent event) {
		Core.logInfo("ExtDataGenerator started datagen.");
		DataGenerator generator = event.getGenerator();
		PackOutput output = generator.getPackOutput();
		ExistingFileHelper helper = event.getExistingFileHelper();
		// 注册表内容生成
		RegistryDatagen.RegistriesProvider registriesProvider = new RegistryDatagen.RegistriesProvider(output, event.getLookupProvider());
		generator.addProvider(true, registriesProvider);
		// 物品数据生成
		generator.addProvider(event.includeClient(), new ItemDatagen.ModelProvider(output, helper));
		ArrayList<String> genLangs = (ArrayList<String>) ObjectManipulator.access(LangDatagen.LangProvider.class, "genLangs");
		for (String lang : genLangs)
			generator.addProvider(event.includeClient(), new LangDatagen.LangProvider(output, lang));
	}
}
