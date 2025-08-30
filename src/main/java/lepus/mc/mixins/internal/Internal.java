package lepus.mc.mixins.internal;

import java.util.ArrayList;

public class Internal {
	@FunctionalInterface
	public static interface Callback {
		public abstract void execute();
	}

	public static class Callbacks {
		public static <C extends Callback> void invoke(ArrayList<C> callbacks) {
			for (C func : callbacks)
				func.execute();
		}
	}
}
