package fw.core;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import fw.core.registry.RegistryWalker;
import lyra.alpha.io.IoOperations;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

/**
 * 注册表文件生成器
 */
public class MappedRegistriesClassFileGenerator {
	public static void generate(String output_dir, String classPackage, String className, List<ResourceKey<? extends Registry<?>>> registries, boolean initBootstrap, boolean initDynamic) {
		ArrayList<String> lines = new ArrayList<>();
		lines.add("package " + classPackage + ";\r\n"
				+ "\r\n"
				+ "import fw.core.RegistryFieldsInitializer;\r\n"
				+ "import net.minecraft.core.MappedRegistry;");
		ArrayList<Class<?>> importClasses = new ArrayList<>();
		ArrayList<RegistryWalker.RegistryInfo> regInfoList = RegistryWalker.collectRegistryInfo(registries);
		for (RegistryWalker.RegistryInfo regInfo : regInfoList) {
			Class<?> type = regInfo.regType;
			if (!importClasses.contains(type))
				importClasses.add(type);
		}
		for (Class<?> cls : importClasses)
			lines.add("import " + cls.getName().replace('$', '.') + ";");
		lines.add("\r\n"
				+ "@SuppressWarnings(\"rawtypes\")\r\n"
				+ "public class " + className + " {\r\n");
		if (initBootstrap || initDynamic) {
			lines.add("	static {");
			if (initBootstrap)
				lines.add("		RegistryFieldsInitializer.forBootstrap(" + className + ".class);");
			if (initDynamic)
				lines.add("		RegistryFieldsInitializer.forDynamic(" + className + ".class);");
			lines.add("	}\r\n");
		}
		for (RegistryWalker.RegistryInfo regInfo : regInfoList) {
			Core.logInfo("Analyzing Registry " + regInfo.toString());
			lines.add("	public static final MappedRegistry<" + regInfo.regType.getSimpleName() + "> " + regInfo.fieldName + " = null;\r\n"
					+ "\r\n");
		}
		lines.add("}");
		try {
			IoOperations.writeToFile(output_dir + File.separatorChar + classPackage.replace('.', File.separatorChar) + File.separatorChar + className + ".java", lines);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static final String outputDir = "D:\\JavaProjects\\BlueArchive-Rendezvous\\src\\main\\java";
	public static final String registriesPkg = "fw.core.registry";
	public static final String bootstrapClsName = "BootstrapRegistries";
	public static final String dynamicClsName = "DynamicRegistries";

	@EventBusSubscriber(modid = Core.ModId, bus = Bus.MOD)
	private static class BootstrapGenerator {
		static ArrayList<ResourceKey<? extends Registry<?>>> bootstrapRegistries = new ArrayList<>();

		@SubscribeEvent(priority = EventPriority.HIGHEST)
		private static void onRegister(RegisterEvent event) {
			bootstrapRegistries.add(event.getRegistry().key());
		}

		@SubscribeEvent(priority = EventPriority.LOWEST)
		private static void onRegister(GatherDataEvent event) {
			generate(outputDir, registriesPkg, bootstrapClsName, bootstrapRegistries, true, false);
		}
	}

	@EventBusSubscriber(modid = Core.ModId, bus = Bus.GAME)
	private static class DynamicGenerator {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		@SubscribeEvent(priority = EventPriority.LOWEST) // 服务器启动后再收集加载的注册表
		private static void onServerStarted(ServerStartedEvent event) {
			ArrayList<ResourceKey<? extends Registry<?>>> dynamicRegistries = new ArrayList<>();
			RegistryWalker.walkRegistries((Field f, ResourceKey registryKey, Class<?> registryType) -> {
				if (!BootstrapGenerator.bootstrapRegistries.contains(registryKey))
					dynamicRegistries.add(registryKey);
				return true;
			});
			generate(outputDir, registriesPkg, dynamicClsName, dynamicRegistries, false, true);
		}
	}
}
