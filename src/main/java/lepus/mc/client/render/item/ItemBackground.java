package lepus.mc.client.render.item;

import java.util.ArrayList;
import java.util.HashMap;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import lepus.mc.client.render.gui.GuiGraphicsContext;
import lepus.mc.client.render.renderable.ConditionalRenderable2D;
import lepus.mc.client.render.renderable.Renderable2D;
import lepus.mc.client.render.renderable.Texture;
import lepus.mc.items.Items;
import lepus.mc.mixins.internal.GuiGraphicsInternal;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ItemBackground extends ConditionalRenderable2D {
	public static abstract class Resolver<K> {
		private ItemBackground elseValue = null;
		protected final HashMap<K, ItemBackground> backgrounds = new HashMap<>();

		/**
		 * 当有先注册的Resolver匹配到背景后，是否继续渲染此Resolver决议出的背景。<br>
		 * 如果设置为true，则将在先匹配到的背景上二次绘制本背景<br>
		 */
		private boolean reserved;

		public Resolver() {
			this(false);
		}

		public Resolver(boolean reserved) {
			this.reserved = reserved;
		}

		public Resolver<K> setReserved(boolean reserved) {
			this.reserved = reserved;
			return this;
		}

		public boolean getReserved() {
			return reserved;
		}

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

		@FunctionalInterface
		public static interface ItemIdValidation {
			/**
			 * 判断当前itemId是否是满足背景渲染条件<br>
			 * 根据itemId和遍历的background_key判断该itemId是否是有效的key
			 * 
			 * @param background_key 注册的key
			 * @param entity
			 * @param level
			 * @param stack
			 * @param itemId
			 * @param seed
			 * @return true则表示当前background_key满足匹配条件，false表示不满足，并且开始匹配下一个background_key
			 */
			public boolean validate(String background_key, LivingEntity entity, Level level, ItemStack stack, String itemId, int seed);

			public static final ItemIdValidation ALWAYS_TRUE = (String background_key, LivingEntity entity, Level level, ItemStack stack, String itemId, int seed) -> true;

			public static final ItemIdValidation STARTS_WITH = (String background_key, LivingEntity entity, Level level, ItemStack stack, String itemId, int seed) -> itemId.startsWith(background_key);

			public static final ItemIdValidation ENDS_WITH = (String background_key, LivingEntity entity, Level level, ItemStack stack, String itemId, int seed) -> itemId.endsWith(background_key);

			public static final ItemIdValidation MATCHES = (String background_key, LivingEntity entity, Level level, ItemStack stack, String itemId, int seed) -> itemId.matches(background_key);

			public static final ItemIdValidation EQUALS = (String background_key, LivingEntity entity, Level level, ItemStack stack, String itemId, int seed) -> itemId.equals(background_key);
		}

		public static class ItemIdResolver extends Resolver<String> {
			/**
			 * 从前往后依次匹配，返回第一次匹配成功的key
			 */
			protected ItemIdValidation validation;

			private ItemIdResolver(ItemIdValidation validation) {
				this.validation = validation;
			}

			@Override
			protected String key(LivingEntity entity, Level level, ItemStack stack, String itemId, int seed) {
				if (validation == null)
					return null;
				for (String background_key : backgrounds.keySet()) {
					if (validation.validate(background_key, entity, level, stack, itemId, seed))
						return background_key;
				}
				return null;
			}

			private static final HashMap<ItemIdValidation, ItemIdResolver> item_id_resolvers = new HashMap<>();

			/**
			 * 一种ItemIdValidation只有一个ItemIdResolver实例。<br>
			 * 如果对同一个key注册多次背景，则只取最后一次为有效值。
			 * 
			 * @param id_validation
			 * @return
			 */
			public static final ItemIdResolver of(ItemIdValidation id_validation) {
				return item_id_resolvers.computeIfAbsent(id_validation, (ItemIdValidation v) -> new ItemIdResolver(v));
			}

			public static final ItemIdResolver startsWith() {
				return of(ItemIdValidation.STARTS_WITH);
			}

			public static final ItemIdResolver endsWith() {
				return of(ItemIdValidation.ENDS_WITH);
			}

			public static final ItemIdResolver matches() {
				return of(ItemIdValidation.MATCHES);
			}

			public static final ItemIdResolver equals() {
				return of(ItemIdValidation.EQUALS);
			}
		}
	}

	private static final ArrayList<Resolver<?>> resolvers = new ArrayList<>();

	public static final void registerResolver(Resolver<?> resolver) {
		if (!resolvers.contains(resolver))
			resolvers.add(resolver);
	}

	/**
	 * 单一线程使用
	 */
	private static final ArrayList<ItemBackground> render_bgs = new ArrayList<>();

	static {
		GuiGraphicsInternal.RenderItem.Callbacks.addBeforePosePushPoseCallback((GuiGraphics this_, LivingEntity entity, Level level, ItemStack stack, int x, int y, int seed, int guiOffset, CallbackInfo ci) -> {
			String id = Items.getID(stack.getItem());
			for (Resolver<?> resolver : resolvers) {
				if (render_bgs.isEmpty() || resolver.getReserved()) {
					ItemBackground bg = resolver.resolve(entity, level, stack, id, seed);
					if (bg != null)
						render_bgs.add(bg);
				}
			}
			for (ItemBackground bg : render_bgs) {
				bg.render(this_.pose(), x, y);
			}
			render_bgs.clear();
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
