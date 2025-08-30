package lepus.mc.core;

import java.lang.reflect.Field;
import java.util.ArrayList;

import lepus.mc.core.registry.MappedRegistryAccess;
import lepus.mc.core.registry.RegistryWalker;
import lepus.mc.event.ClientLifecycleTrigger;
import lyra.klass.KlassWalker;
import lyra.object.ObjectManipulator;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistryAccess;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

/**
 * 为MappedRegistriesClassFileGenerator生成的文件中的全部注册表字段在正确时机进行初始化操作
 */
public class RegistryFieldsInitializer {
	private static ArrayList<Class<?>> BootstrapRegistriesClasses = new ArrayList<>();
	private static ArrayList<Class<?>> ServerDynamicRegistriesClasses = new ArrayList<>();
	private static ArrayList<Class<?>> ClientDynamicRegistriesClasses = new ArrayList<>();

	/**
	 * 游戏启动阶段加载的注册表，这些注册表依然是MappedRegistry，但在启动时加载
	 */
	@EventBusSubscriber(bus = Bus.MOD)
	class Bootstrap {
		@SuppressWarnings("rawtypes")
		@SubscribeEvent(priority = EventPriority.HIGHEST) // 最高优先级以获取注册表
		private static void onRegister(RegisterEvent event) {
			MappedRegistry reg = (MappedRegistry) event.getRegistry();
			Class<?> type = RegistryWalker.getRegistryType(reg);// 获取Registry key对应的值类型
			for (Class<?> BootstrapRegistriesClass : BootstrapRegistriesClasses)
				KlassWalker.walkTypeFields(BootstrapRegistriesClass, MappedRegistry.class, (Field f, boolean isStatic, MappedRegistry registry) -> {
					if (isStatic && RegistryWalker.getRegistryKeyType(f) == type) {
						ObjectManipulator.setObject(BootstrapRegistriesClass, f.getName(), reg);
					}
					return true;
				});
		}
	}

	/**
	 * 设置启动注册表所在类
	 * 
	 * @param bootstrapRegCls
	 */
	public static final void forBootstrap(Class<?> bootstrapRegCls) {
		BootstrapRegistriesClasses.add(bootstrapRegCls);
	}

	class DynamicServer {
		public static void initializeFields() {
			Core.logInfo("Initializing server DynamicRegistry");
			for (Class<?> DynamicRegistriesClass : ServerDynamicRegistriesClasses) {
				MappedRegistryAccess.initializeServerRegistryFields(DynamicRegistriesClass);
			}
		}

		public static void freeze() {
			Core.logInfo("Freezing server DynamicRegistry");
			for (Class<?> DynamicRegistriesClass : ServerDynamicRegistriesClasses)
				MappedRegistryAccess.freezeRegistries(DynamicRegistriesClass);
		}
	}

	/**
	 * 设置运行时动态注册表所在类
	 * 
	 * @param dynamicRegCls
	 */
	public static final void forDynamicServer(Class<?> dynamicRegCls) {
		ServerDynamicRegistriesClasses.add(dynamicRegCls);
	}

	@OnlyIn(Dist.CLIENT)
	@EventBusSubscriber(bus = Bus.MOD)
	class DynamicClient {
		@SubscribeEvent(priority = EventPriority.HIGHEST) // 最高优先级以获取注册表
		private static void onRegister(FMLConstructModEvent event) {
			ClientLifecycleTrigger.CLIENT_CONNECT.addCallback(EventPriority.HIGHEST, (ClientLevel level, RegistryAccess.Frozen registryAccess) -> {
				Core.logInfo("Initializing client DynamicRegistry");
				for (Class<?> DynamicRegistriesClass : ClientDynamicRegistriesClasses) {
					MappedRegistryAccess.initializeClientRegistryFields(DynamicRegistriesClass);
					MappedRegistryAccess.freezeRegistries(DynamicRegistriesClass);
				}
			});
		}
	}

	public static final void forDynamicClient(Class<?> dynamicRegCls) {
		ClientDynamicRegistriesClasses.add(dynamicRegCls);
	}
}
