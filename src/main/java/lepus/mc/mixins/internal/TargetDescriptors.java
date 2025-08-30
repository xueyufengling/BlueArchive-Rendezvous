package lepus.mc.mixins.internal;

public class TargetDescriptors {
	public static class LClientLevel {
		public static final String getCloudColor = "Lnet/minecraft/client/multiplayer/ClientLevel;getCloudColor(F)Lnet/minecraft/world/phys/Vec3;";
	}

	public static class LLevelRenderer {
		public static final String checkPoseStack = "Lnet/minecraft/client/renderer/LevelRenderer;checkPoseStack(Lcom/mojang/blaze3d/vertex/PoseStack;)V";
		public static final String renderSky = "Lnet/minecraft/client/renderer/LevelRenderer;renderSky(Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;FLnet/minecraft/client/Camera;ZLjava/lang/Runnable;)V";
		public static final String renderDebug = "Lnet/minecraft/client/renderer/LevelRenderer;renderDebug(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/Camera;)V";

	}

	public static class LGuiGraphics {
		public static final String renderItem = "Lnet/minecraft/client/gui/GuiGraphics;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;IIII)V";
	}

	public static class LRenderSystem {
		public static final String disableBlend = "Lcom/mojang/blaze3d/systems/RenderSystem;disableBlend()V";
		public static final String setShaderColor = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V";
		public static final String clearColor = "Lcom/mojang/blaze3d/systems/RenderSystem;clearColor(FFFF)V";
		public static final String activeTexture = "Lcom/mojang/blaze3d/systems/RenderSystem;activeTexture(I)V";
	}

	public static class LRenderTarget {
		public static final String bindWrite = "Lcom/mojang/blaze3d/pipeline/RenderTarget;bindWrite(Z)V";
	}

	public static class LPoseStack {
		public static final String popPose = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V";
		public static final String pushPose = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V";
		public static final String translate = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V";
	}

	public static class LVertexBuffer {
		public static final String unbind = "Lcom/mojang/blaze3d/vertex/VertexBuffer;unbind()V";
	}

	public static class LBufferUploader {
		public static final String drawWithShader = "Lcom/mojang/blaze3d/vertex/BufferUploader;drawWithShader(Lcom/mojang/blaze3d/vertex/MeshData;)V";
	}

	public static class LItemStack {
		public static final String hasFoil = "Lnet/minecraft/world/item/ItemStack;hasFoil()Z";
	}

	public static class LBiome {
		public static final String getWaterFogColor = "Lnet/minecraft/world/level/biome/Biome;getWaterFogColor()I";
	}

	public static class LProfilerFiller {
		public static final String popPush = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V";
	}

	public static class LFogRenderer {
		public static final String levelFogColor = "Lnet/minecraft/client/renderer/FogRenderer;levelFogColor()V";
	}

	public static class Iris {

		public static class LIrisRenderingPipeline {
			public static final String beginLevelRendering = "Lnet/irisshaders/iris/pipeline/IrisRenderingPipeline;beginLevelRendering()V";
			public static final String setPhase = "Lnet/irisshaders/iris/pipeline/IrisRenderingPipeline;setPhase(Lnet/irisshaders/iris/pipeline/WorldRenderingPhase;)V";
		}

		public static class LCompositeRenderer {
			public static final String renderAll = "Lnet/irisshaders/iris/pipeline/CompositeRenderer;renderAll()V";
		}

		public static class LGlFramebuffer {
			public static final String bind = "Lnet/irisshaders/iris/gl/framebuffer/GlFramebuffer;bind()V";
		}

		public static class LHorizonRenderer {
			public static final String renderHorizon = "Lnet/irisshaders/iris/pathways/HorizonRenderer;renderHorizon(Lorg/joml/Matrix4fc;Lorg/joml/Matrix4fc;Lnet/minecraft/client/renderer/ShaderInstance;)V";
		}

		public static class LFullScreenQuadRenderer {
			public static final String renderQuad = "Lnet/irisshaders/iris/pathways/FullScreenQuadRenderer;renderQuad()V";
		}

		public static class LExtendedShader {
			/**
			 * Iris着色器更新uniform及绑定帧缓冲
			 */
			public static final String apply = "Lnet/irisshaders/iris/pipeline/programs/ExtendedShader;apply()V";
			/**
			 * Iris渲染结束并绑回MainRenderTarget
			 */
			public static final String clear = "Lnet/irisshaders/iris/pipeline/programs/ExtendedShader;clear()V";
		}
	}
}
