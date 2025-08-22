package ba.client.render.item;

import fw.client.render.item.ItemBackground;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MaterialBackground {
	private static final ItemBackground T0 = ItemBackground.of("ba:textures/bg/item/item_t0.png");
	private static final ItemBackground T1 = ItemBackground.of("ba:textures/bg/item/item_t1.png");
	private static final ItemBackground T2 = ItemBackground.of("ba:textures/bg/item/item_t2.png");
	private static final ItemBackground T3 = ItemBackground.of("ba:textures/bg/item/item_t3.png");

	static {
		ItemBackground.registerResolver(ItemBackground.Resolver.ItemIdResolver.equals()
				.register("ba:shittim_chest", T3));
		ItemBackground.registerResolver(ItemBackground.Resolver.ItemIdResolver.endsWith()
				.register("_0", T0)
				.register("_1", T1)
				.register("_2", T2)
				.register("_3", T3)
				.registerElse(T0));
	}
}
