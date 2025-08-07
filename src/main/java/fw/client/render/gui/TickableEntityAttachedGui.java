package fw.client.render.gui;

import fw.core.Tickable;

public interface TickableEntityAttachedGui extends Tickable {
	@Override
	public default Object tick(Object... args) {
		return this.tick((EntityAttachedGuiRenderDispatcher.EntityTask) args[0]);
	}

	public default boolean tick(EntityAttachedGuiRenderDispatcher.EntityTask task) {
		return true;
	}
}
