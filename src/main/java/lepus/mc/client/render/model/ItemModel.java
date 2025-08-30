package lepus.mc.client.render.model;

import lepus.mc.core.registry.registries.bootstrap.BootstrapRegistries;
import lepus.mc.mixins.internal.ItemRendererInternal;
import lepus.mc.resources.ResourceLocations;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ItemModel {
	public ResourceLocation resourceLoc;
	public Item itemType;
	public int combinedLight;// 光照图的UV坐标1 VertexConsumer.setUv2(combinedLight & 0x0000FFFF, combinedLight >> 16 & 0x0000FFFF);
	public int combinedOverlay;// UV坐标2 VertexConsumer.setUv1(combinedOverlay & 0x0000FFFF, combinedOverlay >> 16 & 0x0000FFFF);

	public static final int NORMAL_LIGHT = 0x00F000F0;// 普通物品在inventory上下文渲染时的光照均为该值
	public static final int BOW_LIGHT = 0x00400000;

	public static final int NO_WHITE_U = 0;
	public static final int RED_OVERLAY_V = 3;
	public static final int WHITE_OVERLAY_V = 10;
	public static final int NO_OVERLAY = 0x000A0000;// GUI渲染中所有物品均为该值

	public ItemModel(ResourceLocation resourceLoc, Item itemType, int combinedLight, int combinedOverlay) {
		this.resourceLoc = resourceLoc;
		this.itemType = itemType;
		this.combinedLight = combinedLight;
		this.combinedOverlay = combinedOverlay;
	}

	public ItemModel(ResourceLocation resourceLoc, Item itemType) {
		this(resourceLoc, itemType, NORMAL_LIGHT, NO_OVERLAY);
	}

	public ItemModel(ResourceLocation resourceLoc) {
		this(resourceLoc, BootstrapRegistries.ITEM.get(resourceLoc));
	}

	// 如果显示给itemType指定为null，则取消目标模型的渲染
	public ItemModel(String resourceLoc, String itemType, int combinedLight, int combinedOverlay) {
		this.resourceLoc = ResourceLocations.build(resourceLoc);
		this.itemType = itemType == null ? null : BootstrapRegistries.ITEM.get(ResourceLocations.build(itemType));
		this.combinedLight = combinedLight;
		this.combinedOverlay = combinedOverlay;
	}

	public ItemModel(String resourceLoc, String itemType) {
		this(resourceLoc, itemType, NORMAL_LIGHT, NO_OVERLAY);
	}

	public ItemModel(String resourceLoc) {
		this(resourceLoc, resourceLoc);
	}

	public BakedModel inventoryModel() {
		ModelManager modelManager = ItemRendererInternal.Render.Args.this_.getItemModelShaper().getModelManager();
		if (resourceLoc == null)
			return ItemRendererInternal.emptyItemModel();
		else
			return modelManager.getModel(new ModelResourceLocation(resourceLoc, "inventory"));
	}

	public BakedModel model(String tyoe) {
		ModelManager modelManager = ItemRendererInternal.Render.Args.this_.getItemModelShaper().getModelManager();
		if (resourceLoc == null)
			return modelManager.getModel(new ModelResourceLocation(ResourceLocations.build("minecraft:air"), tyoe));
		else
			return modelManager.getModel(new ModelResourceLocation(resourceLoc, tyoe));
	}

	@Override
	public String toString() {
		return "{resourceLoc=" + resourceLoc + ", itemType=" + itemType + ", combinedLight=" + Integer.toHexString(combinedLight) + ", combinedOverlay=" + Integer.toHexString(combinedOverlay) + '}';
	}
}