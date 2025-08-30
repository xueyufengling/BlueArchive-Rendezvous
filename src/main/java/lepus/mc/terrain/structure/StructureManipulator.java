package lepus.mc.terrain.structure;

import java.io.InputStream;
import java.lang.invoke.MethodHandle;

import lepus.mc.core.Core;
import lepus.mc.event.ServerLifecycleTrigger;
import lyra.filesystem.jar.JarFiles;
import lyra.lang.Handles;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.neoforged.bus.api.EventPriority;

/**
 * 结构相关操作
 */
public class StructureManipulator {
	private static StructureTemplateManager structureTemplateManager;

	private static MethodHandle StructureTemplateManager_readStructure = null;

	static {
		ServerLifecycleTrigger.BEFORE_SERVER_START.addCallback(EventPriority.HIGHEST, (MinecraftServer server) -> {
			StructureManipulator.structureTemplateManager = server.getStructureManager();
		});
		StructureTemplateManager_readStructure = Handles.findMethodHandle(StructureTemplateManager.class, "readStructure", InputStream.class);
	}

	/**
	 * 从nbt结构文件流加载结构
	 * 
	 * @param stream
	 * @return
	 */
	public static StructureTemplate loadStructure(InputStream stream) {
		try {
			return (StructureTemplate) StructureTemplateManager_readStructure.invokeExact(structureTemplateManager, stream);
		} catch (Throwable ex) {
			Core.logError("Read structure from input stream failed.", ex);
		}
		return null;
	}

	/**
	 * 加载指定class所在jar文件内的nbt结构文件
	 * 
	 * @param targetJarCls
	 * @param inJarPath
	 * @return
	 */
	public static StructureTemplate loadStructureInJar(Class<?> targetJarCls, String inJarPath) {
		return loadStructure(JarFiles.getResourceAsStream(targetJarCls, inJarPath));
	}

	public static StructureTemplate loadStructureInJar(String inJarPath) {
		return loadStructure(JarFiles.getResourceAsStream(inJarPath));
	}
}
