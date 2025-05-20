package fw.core;

import ba.ModEntryObject;
import jvm.klass.ObjectManipulator;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.javafmlmod.FMLModContainer;

public class Core {
	/**
	 * 为Core设置ModId，该ID将在其他所有类中使用
	 */
	public static final String ModId = ModEntryObject.ModId;

	public static final IEventBus ModBus = null;

	public static final void init(FMLModContainer container, IEventBus modBus) {
		// 初始化赋值ModBus
		ObjectManipulator.setObject(Core.class, "ModBus", modBus);
		RegistryFactory.registerAll();
	}
}
