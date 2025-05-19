package ba.core.datagen;

import ba.core.Core;
import ba.core.ExtItem;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = Core.ModId, bus = EventBusSubscriber.Bus.MOD)
public class ExtDataGenerator {

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput output = generator.getPackOutput();
		ExistingFileHelper helper = event.getExistingFileHelper();
		// 物品数据生成
		generator.addProvider(event.includeClient(), new ExtItem.ModelProvider(output, helper));
	}
}
