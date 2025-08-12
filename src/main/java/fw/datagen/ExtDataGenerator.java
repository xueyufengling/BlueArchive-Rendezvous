package fw.datagen;

import java.util.concurrent.CompletableFuture;

import fw.core.Core;
import fw.datagen.annotation.ItemDatagen;
import fw.datagen.annotation.LangDatagen;
import fw.datagen.annotation.RegistryEntry;
import fw.datagen.internal.ExtTagsProvider;
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
	public static void onGatherData(GatherDataEvent event) {
		Core.logInfo("ExtDataGenerator starting to datagen.");
		DataGenerator generator = event.getGenerator();
		PackOutput output = generator.getPackOutput();
		ExistingFileHelper helper = event.getExistingFileHelper();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
		// 注册表内容生成
		RegistryEntry.RegistriesProvider registriesProvider = new RegistryEntry.RegistriesProvider(output, lookupProvider);
		generator.addProvider(event.includeServer(), registriesProvider);
		// Tag及其成员生成
		ExtTagsProvider.addProvider(generator, event.includeServer(), output, registriesProvider, helper);
		// 物品数据生成
		ItemDatagen.ModelProvider.addProvider(generator, event.includeClient(), output, helper);
		// 语言文件生成
		LangDatagen.LangProvider.addProvider(generator, event.includeClient(), output);
	}
}
