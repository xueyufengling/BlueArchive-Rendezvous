package ba;

import jvm.klass.ObjectManipulator;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLModContainer;

@EventBusSubscriber(modid = ModEntryObject.ModId)
@Mod(value = ModEntryObject.ModId)
public class ModEntryObject {
	public static final String ModName = "BlueArchive: Rendezvous";
	public static final String ModId = "ba";

	public static final IEventBus ModBus = null;

	public ModEntryObject(FMLModContainer container, IEventBus modBus) {
		ObjectManipulator.setObject(this.getClass(), "ModBus", modBus);// 设置ModBus
	}

}
