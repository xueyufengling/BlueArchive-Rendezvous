package fw.core.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import fw.core.ServerInstance;
import fw.core.ServerInstance.Operation;
import lyra.alpha.reference.Recoverable;
import lyra.klass.special.BaseClass;
import net.neoforged.bus.api.EventPriority;

/**
 * 事件触发和执行器封装
 */
public interface EventTrigger extends BaseClass<EventTrigger.Definition> {
	/**
	 * 事件触发动作定义
	 */
	class Definition extends BaseClass.Definition<EventTrigger> {
		public static class Executor {
			private final ArrayList<Operation> callbacks = new ArrayList<>();
			/**
			 * 执行完成后就会移除的回调函数
			 */
			private final ArrayList<Operation> temp_callbacks = new ArrayList<>();
			private final ArrayList<Recoverable<?>> redirect_recoverables = new ArrayList<>();
			private final ArrayList<Recoverable<?>> recovery_recoverables = new ArrayList<>();

			public final void addCallbacks(Operation... ops) {
				for (Operation op : ops)
					callbacks.add(op);
			}

			public final void addTempCallbacks(Operation... ops) {
				for (Operation op : ops)
					temp_callbacks.add(op);
			}

			public final void addRedirectRecoverables(Recoverable<?>... refs) {
				for (Recoverable<?> ref : refs)
					if (!redirect_recoverables.contains(ref))
						redirect_recoverables.add(ref);
			}

			public final void addRecoveryRecoverables(Recoverable<?>... refs) {
				for (Recoverable<?> ref : refs)
					if (!redirect_recoverables.contains(ref))
						recovery_recoverables.add(ref);
			}

			public final void addCallback(Operation op) {
				callbacks.add(op);
			}

			public final void addTempCallback(Operation op) {
				temp_callbacks.add(op);
			}

			public final void addRedirectRecoverable(Recoverable<?> ref) {
				if (!redirect_recoverables.contains(ref))
					redirect_recoverables.add(ref);
			}

			public final void addRecoveryRecoverable(Recoverable<?> ref) {
				if (!redirect_recoverables.contains(ref))
					recovery_recoverables.add(ref);
			}

			public final void execute() {
				for (Operation callback : callbacks)
					callback.operate(ServerInstance.server);
				Iterator<Operation> iter = temp_callbacks.iterator();
				while (iter.hasNext()) {
					iter.next().operate(ServerInstance.server);
					iter.remove();
				}
				for (Recoverable<?> ref : redirect_recoverables)
					ref.redirect();
				for (Recoverable<?> ref : recovery_recoverables)
					ref.recovery();
			}
		}

		private EventPriority defaultPriority;
		private final HashMap<EventPriority, Executor> executors = new HashMap<>();

		public Definition(EventPriority defaultPriority) {
			this.defaultPriority = defaultPriority;
		}

		public Definition() {
			this(EventPriority.LOWEST);
		}

		public final Executor priority(EventPriority p) {
			return executors.computeIfAbsent(p, (EventPriority) -> new Executor());
		}

		public final Executor priority() {
			return priority(defaultPriority);
		}

		public EventPriority getDefaultPriority() {
			return defaultPriority;
		}

		public Definition setDefaultPriority(EventPriority defaultPriority) {
			this.defaultPriority = defaultPriority;
			return this;
		}
	}

	public default void addCallbacks(EventPriority p, Operation... ops) {
		definition().priority(p).addCallbacks(ops);
	}

	public default void addTempCallbacks(EventPriority p, Operation... ops) {
		definition().priority(p).addTempCallbacks(ops);
	}

	public default void addRedirectRecoverables(EventPriority p, Recoverable<?>... refs) {
		definition().priority(p).addRedirectRecoverables(refs);
	}

	public default void addRecoveryRecoverables(EventPriority p, Recoverable<?>... refs) {
		definition().priority(p).addRecoveryRecoverables(refs);
	}

	public default void addCallback(EventPriority p, Operation op) {
		definition().priority(p).addCallback(op);
	}

	public default void addTempCallback(EventPriority p, Operation op) {
		definition().priority(p).addTempCallback(op);
	}

	public default void addRedirectRecoverable(EventPriority p, Recoverable<?> ref) {
		definition().priority(p).addRedirectRecoverable(ref);
	}

	public default void addRecoveryRecoverable(EventPriority p, Recoverable<?> ref) {
		definition().priority(p).addRecoveryRecoverable(ref);
	}

	// 默认优先级
	public default void addCallbacks(Operation... ops) {
		definition().priority().addCallbacks(ops);
	}

	public default void addTempCallbacks(Operation... ops) {
		definition().priority().addTempCallbacks(ops);
	}

	public default void addRedirectRecoverables(Recoverable<?>... refs) {
		definition().priority().addRedirectRecoverables(refs);
	}

	public default void addRecoveryRecoverables(Recoverable<?>... refs) {
		definition().priority().addRecoveryRecoverables(refs);
	}

	public default void addCallback(Operation op) {
		definition().priority().addCallback(op);
	}

	public default void addTempCallback(Operation op) {
		definition().priority().addTempCallback(op);
	}

	public default void addRedirectRecoverable(Recoverable<?> ref) {
		definition().priority().addRedirectRecoverable(ref);
	}

	public default void addRecoveryRecoverable(Recoverable<?> ref) {
		definition().priority().addRecoveryRecoverable(ref);
	}

	// 子类使用
	default void define(EventPriority defaultPriority) {
		this.construct(EventTrigger.Definition.class, new Class<?>[] { EventPriority.class }, defaultPriority);
	}

	default void define() {
		this.construct(EventTrigger.Definition.class, new Class<?>[] {});
	}
}
