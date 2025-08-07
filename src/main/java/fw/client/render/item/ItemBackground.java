package fw.client.render.item;

import java.util.ArrayList;
import java.util.HashMap;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import fw.client.render.gui.GuiGraphicsContext;
import fw.client.render.renderable.ConditionalRenderable2D;
import fw.client.render.renderable.Renderable2D;
import fw.client.render.renderable.Texture;
import fw.items.Items;
import fw.mixins.internal.GuiGraphicsInternal;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemBackground extends ConditionalRenderable2D {
	public static abstract class Resolver<K> {
		private ItemBackground elseValue;
		protected final HashMap<K, ItemBackground> backgrounds = new HashMap<>();

		protected abstract K key(LivingEntity entity, Level level, ItemStack stack, String itemId, int seed);

		public final Resolver<K> registerElse(ItemBackground bg) {
			this.elseValue = bg;
			return this;
		}

		public final ItemBackground resolve(LivingEntity entity, Level level, ItemStack stack, String itemId, int seed) {
			ItemBackground bg = backgrounds.get(this.key(entity, level, stack, itemId, seed));
			return bg == null ? elseValue : bg;
		}

		public final Resolver<K> register(K key, ItemBackground bg) {
			backgrounds.put(key, bg);
			return this;
		}

		public final ItemBackground get(K key) {
			return backgrounds.get(key);
		}

		public final boolean contains(K key) {
			return backgrounds.containsKey(key);
		}

		public final static Resolver<String> ID_START_WITH = new Resolver<String>() {
			@Override
			protected String key(LivingEntity entity, Level level, ItemStack stack, String itemId, int seed) {
				for (String str : backgrounds.keySet()) {
					if (itemId.startsWith(str))
						return str;
				}
				return null;
			}
		};

		public final static Resolver<String> ID_END_WITH = new Resolver<String>() {
			@Override
			protected String key(LivingEntity entity, Level level, ItemStack stack, String itemId, int seed) {
				for (String str : backgrounds.keySet()) {
					if (itemId.endsWith(str))
						return str;
				}
				return null;
			}
		};

		public final static Resolver<String> ID_EQUALS = new Resolver<String>() {
			@Override
			protected String key(LivingEntity entity, Level level, ItemStack stack, String itemId, int seed) {
				for (String str : backgrounds.keySet()) {
					if (itemId.equals(str))
						return str;
				}
				return null;
			}
		};
	}

	private static final ArrayList<Resolver<?>> resolvers = new ArrayList<>();

	public static final void registerResolver(Resolver<?> resolver) {
		if (!resolvers.contains(resolver))
			resolvers.add(resolver);
	}

	static {
		GuiGraphicsInternal.RenderItem.Callbacks.addBeforePosePushPoseCallback((GuiGraphics this_, LivingEntity entity, Level level, ItemStack stack, int x, int y, int seed, int guiOffset, CallbackInfo ci) -> {
			String id = Items.getID(stack.getItem());
			for (Resolver<?> resolver : resolvers) {
				ItemBackground bg = resolver.resolve(entity, level, stack, id, seed);
				if (bg != null)
					bg.render(this_.pose(), x, y);
			}
		});
	}

	public ItemBackground add(int priority, Renderable2D.Instance... renderables) {
		for (Renderable2D.Instance renderable : renderables)
			this.renderables.add(new Entry(renderable.setRenderingSize(GuiGraphicsContext.SLOT_SIZE, GuiGraphicsContext.SLOT_SIZE), priority));
		sort();
		return this;
	}

	public ItemBackground add(Renderable2D.Instance... renderables) {
		return this.add(Entry.DEFAULT_PRIORITY, renderables);
	}

	public ItemBackground add(Renderable2D.Instance renderable, int priority) {
		this.renderables.add(new Entry(renderable.setRenderingSize(GuiGraphicsContext.SLOT_SIZE, GuiGraphicsContext.SLOT_SIZE), priority));
		sort();
		return this;
	}

	public static ItemBackground of(ResourceLocation loc) {
		return new ItemBackground().add(Texture.of(loc).areaOf());
	}

	public static ItemBackground of(String loc) {
		return new ItemBackground().add(Texture.of(loc).areaOf());
	}
}
