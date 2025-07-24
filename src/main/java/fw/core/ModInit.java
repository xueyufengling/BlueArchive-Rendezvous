package fw.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import lyra.klass.KlassWalker;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.fml.javafmlmod.FMLModContainer;

/**
 * 初始化函数注解、<br>
 * 在加载完依赖库、获取事件总线ModBus后调用。<br>
 * 所有mod初始化操作必须在具有该注解的方法中进行，不得在mod入口类构造函数中进行初始化操作。<br>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface ModInit {
	/**
	 * Mod的运行环境，客户端或服务端
	 * 
	 * @return
	 */
	Dist[] env() default { Dist.CLIENT, Dist.DEDICATED_SERVER };

	@EventBusSubscriber(modid = Core.ModId, bus = Bus.MOD)
	public static class Initializer {
		private static ArrayList<Class<?>> modInitClasses = new ArrayList<>();

		@SubscribeEvent(priority = EventPriority.HIGH)
		static final void executeAllInitFuncs(FMLConstructModEvent event) {
			ModContainer mod = Core.Mod;
			IEventBus bus = Core.ModBus;
			Dist dist = Core.Env;
			for (Class<?> modInitClass : modInitClasses)
				KlassWalker.walkAnnotatedMethods(modInitClass, ModInit.class, (Method m, boolean isStatic, Object obj, ModInit annotation) -> {
					if (isStatic) {
						Dist[] env = annotation.env();
						for (Dist d : env) {
							if (d == dist) {
								Class<?>[] paramTypes = m.getParameterTypes();
								Object[] args = new Object[paramTypes.length];
								for (int i = 0; i < paramTypes.length; ++i) {
									if (paramTypes[i] == ModContainer.class || paramTypes[i] == FMLModContainer.class)
										args[i] = mod;
									else if (paramTypes[i] == IEventBus.class)
										args[i] = bus;
									else if (paramTypes[i] == Dist.class)
										args[i] = dist;
									else if (paramTypes[i] == FMLConstructModEvent.class)
										args[i] = event;
								}
								try {
									m.invoke(obj, args);
								} catch (IllegalAccessException | InvocationTargetException e) {
									Core.logError("@CoreInit method " + m + " execute failed.", e);
								}
								break;// 只要匹配任意一个env指定的运行环境，则执行该静态方法并退出循环转而判定下一个静态方法
							}
						}
					}
					return true;
				});
		}

		/**
		 * 设置初始化函数所在类
		 * 
		 * @param modInitCls
		 */
		public static final void forInit(Class<?> modInitCls) {
			modInitClasses.add(modInitCls);
		}
	}
}
