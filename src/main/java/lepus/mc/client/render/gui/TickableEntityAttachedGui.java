package lepus.mc.client.render.gui;

import lepus.mc.core.Tickable;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface TickableEntityAttachedGui extends Tickable {
	@Override
	public default Object tick(Object... args) {
		return this.tick((EntityAttachedGuiRenderDispatcher.EntityTask) args[0]);
	}

	public default boolean tick(EntityAttachedGuiRenderDispatcher.EntityTask task) {
		return true;
	}
}
