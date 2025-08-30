package lepus.mc.core;

import java.util.function.Supplier;
import net.neoforged.api.distmarker.Dist;

/**
 * 在特定运行环境下执行代码
 */
public abstract class ExecuteIn {
	private ExecuteIn() {

	}

	public static final Object Client(Supplier<?> exec) {
		if (Core.Env == Dist.CLIENT)
			return exec.get();
		return null;
	}

	public static final void Client(Runnable exec) {
		if (Core.Env == Dist.CLIENT)
			exec.run();
	}

	public static final Object Server(Supplier<?> exec) {
		if (Core.Env == Dist.DEDICATED_SERVER)
			return exec.get();
		return null;
	}

	public static final void Server(Runnable exec) {
		if (Core.Env == Dist.DEDICATED_SERVER)
			exec.run();
	}
}