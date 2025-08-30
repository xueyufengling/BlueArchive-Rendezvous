package lepus.mc.core;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import lepus.mc.core.registry.RegistryWalker;
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
			Core.logInfo("Analyzing registry " + regInfo.toString());
			lines.add("	public static final MappedRegistry<" + regInfo.regType.getSimpleName() + "> " + regInfo.fieldName + " = null;\r\n");
		}
		lines.add("}");
		try {
			IoOperations.writeToFile(generatedClassPath(output_dir, classPackage, className), lines);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static final String generatedClassPath(String output_dir, String classPackage, String className) {
		return output_dir + File.separatorChar + classPackage.replace('.', File.separatorChar) + File.separatorChar + className + ".java";
	}

	public static final String bootstrapClassPath() {
		return generatedClassPath(outputDir, registriesPkg, bootstrapClsName);
	}

	public static final String dynamicClassPath() {
		return generatedClassPath(outputDir, registriesPkg, dynamicClsName);
	}

	public static boolean gen_file = false;

	/**
	 * 是否覆写现有启动注册表
	 */
	public static boolean override_bootstrap = false;

	/**
	 * 是否覆写现有动态注册表
	 */
	public static boolean override_dynamic = false;
	public static final String outputDir = "D:\\JavaProjects\\BlueArchive-Rendezvous\\src\\main\\java";
	public static final String registriesPkg = "fw.core.registry.registries";
	public static final String bootstrapClsName = "BootstrapRegistries";
	public static final String dynamicClsName = "DynamicRegistries";

	@EventBusSubscriber(bus = Bus.MOD)
	private static class BootstrapGenerator {
		static ArrayList<ResourceKey<? extends Registry<?>>> bootstrapRegistries = new ArrayList<>();

		@SubscribeEvent(priority = EventPriority.HIGHEST)
		private static void onRegister(RegisterEvent event) {
			bootstrapRegistries.add(event.getRegistry().key());
		}

		@SubscribeEvent(priority = EventPriority.LOWEST)
		private static void onRegister(GatherDataEvent event) {
			if (gen_file && !Files.exists(Paths.get(bootstrapClassPath())) || override_bootstrap)
				generate(outputDir, registriesPkg, bootstrapClsName, bootstrapRegistries, true, false);
		}
	}

	@EventBusSubscriber(bus = Bus.GAME)
	private static class DynamicGenerator {
		/**
		 * 服务器启动后再收集加载的注册表
		 */
		@SubscribeEvent(priority = EventPriority.LOWEST)
		private static void onServerStarted(ServerStartedEvent event) {
			if (gen_file && !Files.exists(Paths.get(dynamicClassPath())) || override_dynamic) {// 动态注册表java源文件存在则且不覆写则不再生成
				ArrayList<ResourceKey<? extends Registry<?>>> dynamicRegistries = new ArrayList<>();
				RegistryWalker.walkRegistries((Field f, ResourceKey<? extends Registry<?>> registryKey, Class<?> registryType) -> {
					if (!BootstrapGenerator.bootstrapRegistries.contains(registryKey))
						dynamicRegistries.add(registryKey);
					return true;
				});
				generate(outputDir, registriesPkg, dynamicClsName, dynamicRegistries, false, true);
			}
		}
	}
}
