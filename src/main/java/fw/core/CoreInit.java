package fw.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import lyra.klass.KlassWalker;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;

/**
 * 初始化函数注解、<br>
 * 在加载完依赖库、获取事件总线ModBus后调用。<br>
 * 所有mod初始化操作必须在具有该注解的方法中进行，不得在mod入口类构造函数中进行初始化操作。<br>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface CoreInit {
	@EventBusSubscriber(modid = Core.ModId, bus = Bus.MOD)
	static class Executor {
		@SubscribeEvent(priority = EventPriority.HIGH)
		public static final void executeAllInitFuncs(FMLConstructModEvent event) {
			KlassWalker.walkAnnotatedMethods(Core.coreInitClass, CoreInit.class, (Method m, boolean isStatic, Object obj, CoreInit annotation) -> {
				if (isStatic)
					try {
						m.invoke(obj);
					} catch (IllegalAccessException | InvocationTargetException e) {
						Core.logError("@CoreInit method " + m + " execute failed.");
					}
				return true;
			});
		}
	}
}
