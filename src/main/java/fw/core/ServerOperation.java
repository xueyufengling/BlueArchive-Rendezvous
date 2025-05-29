package fw.core;

import net.minecraft.server.MinecraftServer;

@FunctionalInterface
public interface ServerOperation {
	public void operate(MinecraftServer server);
}
