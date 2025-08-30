package lepus.mc.client.render.model;

import java.util.ArrayList;
import java.util.HashMap;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import lepus.mc.items.Items;
import lepus.mc.mixins.internal.ItemRendererInternal;
import lepus.mc.resources.ResourceLocations;
import lyra.object.ObjectManipulator;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ItemModels {
	// 储存了所有ItemDisplayContext各自对应的模型，以ItemDisplayContext的id为索引
	private static final ArrayList<HashMap<String, ItemModel>> itemModels = new ArrayList<>();

	public static Item currentOriginalRenderItem = null;

	// 渲染普通物品和方块前
	private static final ItemRendererInternal.Render.Callback modelReplaceFunc = (
			ItemRenderer this_,
			ItemStack itemStack,
			ItemDisplayContext displayContext,
			boolean leftHand,
			PoseStack poseStack,
			MultiBufferSource bufferSource,
			int combinedLight,
			int combinedOverlay,
			BakedModel p_model,
			CallbackInfo ci) -> {
		String item_id = Items.getID(itemStack.getItem());
		HashMap<String, ItemModel> map = itemModels.get(displayContext.getId());
		if (map.containsKey(item_id)) {
			ItemModel item_model = map.get(item_id);
			ItemRendererInternal.Render.Args.p_model = item_model.inventoryModel();
			// 如果指定渲染物品类型，则需要更改ItemStack的item成员变量，因为该参数和model同样关乎渲染
			if (item_model.itemType != null) {
				poseStack.popPose();// 先撤销poseStack的model.getTransforms().getTransform().apply()的变换
				poseStack.pushPose();// 推入原本的变换
				currentOriginalRenderItem = (Item) ObjectManipulator.getDeclaredMemberObject(itemStack, "item");// 储存当前的ItemStack.item
				ObjectManipulator.setDeclaredMemberObject(itemStack, "item", item_model.itemType);// 修改目标item成员
			}
		}
	};

	@SuppressWarnings("deprecation")
	private static final ItemRendererInternal.Render.Callback modelTransformFunc = (
			ItemRenderer this_,
			ItemStack itemStack,
			ItemDisplayContext displayContext,
			boolean leftHand,
			PoseStack poseStack,
			MultiBufferSource bufferSource,
			int combinedLight,
			int combinedOverlay,
			BakedModel p_model,
			CallbackInfo ci) -> {
		if (currentOriginalRenderItem != null)
			p_model.getTransforms().getTransform(displayContext).apply(leftHand, poseStack);
	};

	private static final ItemRendererInternal.Render.Callback itemTypeRecoveryFunc = (
			ItemRenderer this_,
			ItemStack itemStack,
			ItemDisplayContext displayContext,
			boolean leftHand,
			PoseStack poseStack,
			MultiBufferSource bufferSource,
			int combinedLight,
			int combinedOverlay,
			BakedModel p_model,
			CallbackInfo ci) -> { // 如果修改了渲染的item，则需要在渲染结束后改回去
		if (currentOriginalRenderItem != null) {
			ObjectManipulator.setDeclaredMemberObject(itemStack, "item", currentOriginalRenderItem);// ItemStack.item
			currentOriginalRenderItem = null;
		}
	};

	// 如果JVM没有加载此类，那么modelReplaceFunc注入方法将不会被执行
	static {
		int context_num = ItemDisplayContext.values().length;
		for (int i = 0; i < context_num; ++i)
			itemModels.add(new HashMap<>());
		ItemRendererInternal.Render.Callbacks.addBeforePoseStackTranslateCallback(modelReplaceFunc);
		ItemRendererInternal.Render.Callbacks.addBeforePoseStackTranslateCallback(modelTransformFunc);
		ItemRendererInternal.Render.Callbacks.addBeforeReturnCallback(itemTypeRecoveryFunc);
	}

	public static void setItemModel(String item_id, ItemDisplayContext context, ItemModel info) {
		itemModels.get(context.getId()).put(item_id, info);
	}

	public static void setItemModel(String item_id, String context, String model_resloc, String item) {
		setItemModel(item_id, Items.parseItemDisplayContext(context), new ItemModel(model_resloc, item));
	}

	public static void setItemModel(String item_id, String context, String model_resloc) {
		setItemModel(item_id, Items.parseItemDisplayContext(context), new ItemModel(model_resloc, model_resloc));
	}

	public static void setInventoryModel(String item_id, ItemModel info) {
		setItemModel(item_id, ItemDisplayContext.GUI, info);
		setItemModel(item_id, ItemDisplayContext.GROUND, info);
		setItemModel(item_id, ItemDisplayContext.FIXED, info);
	}

	public static void setInventoryModel(String item_id, ResourceLocation model_resloc, Item item) {
		setInventoryModel(item_id, new ItemModel(model_resloc, item));
	}

	public static void setInventoryModel(String item_id, ResourceLocation model_resloc) {
		setInventoryModel(item_id, new ItemModel(model_resloc));
	}

	public static void setInventoryModel(String item_id, String model_resloc, Item item) {
		setInventoryModel(item_id, ResourceLocations.build(model_resloc), item);
	}

	public static void setInventoryModel(String item_id, String model_resloc, String item) {
		setInventoryModel(item_id, new ItemModel(model_resloc, item));
	}

	public static void setInventoryModel(String item_id, String model_resloc) {
		setInventoryModel(item_id, new ItemModel(model_resloc, null));
	}

	// 物品渲染模型设置
	public static void setInHandModel(String item_id, ItemModel info) {
		setItemModel(item_id, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, info);
		setItemModel(item_id, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, info);
		setItemModel(item_id, ItemDisplayContext.FIRST_PERSON_LEFT_HAND, info);
		setItemModel(item_id, ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, info);
	}

	public static void setInHandModel(String item_id, ResourceLocation model_resloc, Item item) {
		setInHandModel(item_id, new ItemModel(model_resloc, item));
	}

	public static void setInHandModel(String item_id, ResourceLocation model_resloc) {
		setInHandModel(item_id, new ItemModel(model_resloc));
	}

	public static void setInHandModel(String item_id, String model_resloc, Item item) {
		setInHandModel(item_id, ResourceLocations.build(model_resloc), item);
	}

	public static void setInHandModel(String item_id, String model_resloc, String item) {
		setInHandModel(item_id, new ItemModel(model_resloc, item));
	}

	public static void setInHandModel(String item_id, String model_resloc) {
		setInHandModel(item_id, new ItemModel(model_resloc, null));
	}

	public static ItemModel getItemModel(String item_id, ItemDisplayContext context) {
		HashMap<String, ItemModel> map = itemModels.get(context.getId());
		return map.containsKey(item_id) ? map.get(item_id) : null;
	}

	private static final ArrayList<ItemModelTransform> itemModelTransforms = new ArrayList<>();

	private static final ItemRendererInternal.Render.Callback itemModelTransformer = (
			ItemRenderer this_,
			ItemStack itemStack,
			ItemDisplayContext displayContext,
			boolean leftHand,
			PoseStack poseStack,
			MultiBufferSource bufferSource,
			int combinedLight,
			int combinedOverlay,
			BakedModel p_model,
			CallbackInfo ci) -> {
		for (ItemModelTransform transform_func : itemModelTransforms) {
			if (transform_func.condition(itemStack, Items.getID(itemStack.getItem()), displayContext, leftHand))
				transform_func.transform(itemStack, displayContext, leftHand, poseStack, p_model);
		}
	};

	static {
		ItemRendererInternal.Render.Callbacks.addBeforePoseStackTranslateCallback(itemModelTransformer);
	}

	public static void addItemModelTransform(ItemModelTransform func) {
		itemModelTransforms.add(func);
	}
}
