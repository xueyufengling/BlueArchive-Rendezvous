package fw.datagen;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import fw.core.Core;
import fw.datagen.annotation.ItemDatagen;
import fw.datagen.annotation.LangDatagen;
import fw.datagen.annotation.RegistryDatagen;
import fw.datagen.internal.ExtTagsProvider;
import lyra.object.ObjectManipulator;
import net.minecraft.core.HolderLookup;
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
	public static void onGatherData(GatherDataEvent event) {
		Core.logInfo("ExtDataGenerator started datagen.");
		DataGenerator generator = event.getGenerator();
		PackOutput output = generator.getPackOutput();
		ExistingFileHelper helper = event.getExistingFileHelper();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
		// 注册表内容生成
		RegistryDatagen.RegistriesProvider registriesProvider = new RegistryDatagen.RegistriesProvider(output, lookupProvider);
		generator.addProvider(event.includeServer(), registriesProvider);
		ExtTagsProvider.addProvider(generator, event.includeServer(), output, registriesProvider, helper);
		// 物品数据生成
		generator.addProvider(event.includeClient(), new ItemDatagen.ModelProvider(output, helper));
		ArrayList<String> genLangs = (ArrayList<String>) ObjectManipulator.access(LangDatagen.LangProvider.class, "genLangs");
		for (String lang : genLangs)
			generator.addProvider(event.includeClient(), new LangDatagen.LangProvider(output, lang));
	}
}
