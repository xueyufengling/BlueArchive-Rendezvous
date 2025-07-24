package fw.core.event;

import java.util.ArrayList;
import java.util.Iterator;

import fw.core.ServerInstance;
import fw.core.ServerInstance.Operation;
import lyra.alpha.reference.Recoverable;
import lyra.klass.special.BaseClass;

public interface EventTrigger extends BaseClass<EventTrigger.Definition> {

	class Definition extends BaseClass.Definition<EventTrigger> {
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
}
