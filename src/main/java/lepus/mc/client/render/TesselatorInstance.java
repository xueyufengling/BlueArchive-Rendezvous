package lepus.mc.client.render;

import java.lang.invoke.MethodHandle;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;

import lyra.lang.Handles;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TesselatorInstance {
	public static Tesselator instance;
	private static MethodHandle Tesselator_begin = null;
	private static MethodHandle VertexConsumer_addvertex$FFF = null;
	private static MethodHandle VertexConsumer_addvertex$Matrix4f$FFF = null;
	private static MethodHandle BufferBuilder_setUv = null;
	private static MethodHandle BufferBuilder_buildOrThrow = null;

	private static MethodHandle VertexConsumer_setColor$FFFF = null;
	private static MethodHandle BufferUploader_drawWithShader = null;

	static {
		instance = Tesselator.getInstance();
		Tesselator_begin = Handles.findMethodHandle(Tesselator.class, "begin", VertexFormat.Mode.class, VertexFormat.class);
		VertexConsumer_addvertex$FFF = Handles.findMethodHandle(VertexConsumer.class, "addVertex", float.class, float.class, float.class);
		VertexConsumer_addvertex$Matrix4f$FFF = Handles.findMethodHandle(VertexConsumer.class, "addVertex", Matrix4f.class, float.class, float.class, float.class);
		BufferBuilder_setUv = Handles.findMethodHandle(BufferBuilder.class, "setUv", float.class, float.class);

		VertexConsumer_setColor$FFFF = Handles.findMethodHandle(VertexConsumer.class, "setColor", float.class, float.class, float.class, float.class);
		BufferUploader_drawWithShader = Handles.findMethodHandle(BufferUploader.class, "drawWithShader", MeshData.class);
		BufferBuilder_buildOrThrow = Handles.findMethodHandle(BufferBuilder.class, "buildOrThrow");
	}

	public static BufferBuilder begin(VertexFormat.Mode mode, VertexFormat format) {
		BufferBuilder builder = null;
		try {
			builder = (BufferBuilder) Tesselator_begin.invokeExact(instance, mode, format);
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		return builder;
	}

	public static VertexConsumer addVertex(VertexConsumer vertexConsumer, float x, float y, float z) {
		try {
			return (VertexConsumer) VertexConsumer_addvertex$FFF.invokeExact((VertexConsumer) vertexConsumer, x, y, z);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return vertexConsumer;
	}

	public static VertexConsumer addVertex(VertexConsumer vertexConsumer, Matrix4f pose, float x, float y, float z) {
		try {
			return (VertexConsumer) VertexConsumer_addvertex$Matrix4f$FFF.invokeExact(vertexConsumer, pose, x, y, z);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return vertexConsumer;
	}

	public static BufferBuilder setUv(BufferBuilder bufferBuilder, float u, float v) {
		try {
			return (BufferBuilder) (VertexConsumer) BufferBuilder_setUv.invokeExact(bufferBuilder, u, v);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return bufferBuilder;
	}

	/**
	 * 添加一个含位置和UV坐标的顶点
	 * 
	 * @param bufferBuilder
	 * @param pose
	 * @param x
	 * @param y
	 * @param z
	 * @param u
	 * @param v
	 */
	public static void posUvVertex(BufferBuilder bufferBuilder, Matrix4f pose, float x, float y, float z, float u, float v) {
		setUv((BufferBuilder) addVertex(bufferBuilder, pose, x, y, z), u, v);
	}

	public static VertexConsumer setColor(VertexConsumer vertexConsumer, float r, float g, float b, float a) {
		try {
			return (VertexConsumer) VertexConsumer_setColor$FFFF.invokeExact(vertexConsumer, r, g, b, a);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return vertexConsumer;
	}

	public static void posUvColorVertex(BufferBuilder bufferBuilder, float x, float y, float z, float u, float v, float r, float g, float b, float a) {
		setColor(setUv((BufferBuilder) addVertex(bufferBuilder, x, y, z), u, v), r, g, b, a);
	}

	public static void posColorVertex(BufferBuilder bufferBuilder, float x, float y, float z, float r, float g, float b, float a) {
		setColor(addVertex(bufferBuilder, x, y, z), r, g, b, a);
	}

	public static void posUvVertex(BufferBuilder bufferBuilder, float x, float y, float z, float u, float v) {
		setUv((BufferBuilder) addVertex(bufferBuilder, x, y, z), u, v);
	}

	public static void posUvColorVertex(BufferBuilder bufferBuilder, Matrix4f pose, float x, float y, float z, float u, float v, float r, float g, float b, float a) {
		setColor(setUv((BufferBuilder) addVertex(bufferBuilder, pose, x, y, z), u, v), r, g, b, a);
	}

	public static void posUvColorVertex(BufferBuilder bufferBuilder, Matrix4f pose, float x, float y, float z, float u, float v) {
		posUvColorVertex(bufferBuilder, pose, x, y, z, u, v, 1.0f, 1.0f, 1.0f, 1.0f);
	}

	public static MeshData buildOrThrow(BufferBuilder bufferBuilder) {
		MeshData data = null;
		try {
			data = (MeshData) BufferBuilder_buildOrThrow.invokeExact(bufferBuilder);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return data;
	}

	public static void drawBufferBuilder(BufferBuilder bufferBuilder) {
		drawWithShader(buildOrThrow(bufferBuilder));
	}

	public static void drawWithShader(MeshData bufferBuilder) {
		try {
			BufferUploader_drawWithShader.invokeExact(bufferBuilder);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
