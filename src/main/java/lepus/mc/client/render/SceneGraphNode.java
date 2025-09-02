package lepus.mc.client.render;

import org.joml.Matrix4f;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import lepus.graphics.ColorRGBA;
import lepus.mc.client.render.renderable.Renderable;
import lepus.mc.mixins.internal.LevelRendererInternal;
import lyra.alpha.struct.Node;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * 场景图节点，树结构组织RenderableRenderableObject.Instance
 */
@OnlyIn(Dist.CLIENT)
public class SceneGraphNode extends Node<String, VanillaRenderable.Instance> implements Renderable {

	private UpdateOperation preRenderOperation;

	/**
	 * 只有变换的组节点
	 * 
	 * @param name
	 */
	private SceneGraphNode(String name) {
		super(name);
	}

	private SceneGraphNode(String name, VanillaRenderable.Instance renderable) {
		super(name, renderable);
	}

	private SceneGraphNode(String name, Node<String, VanillaRenderable.Instance> parent) {
		super(name, parent);
	}

	/**
	 * 根节点
	 */
	private SceneGraphNode() {
		this("root");
		this.setValue(VanillaRenderable.Instance.empty(true));
	}

	@Override
	protected Node<String, VanillaRenderable.Instance> newNode(String name, Node<String, VanillaRenderable.Instance> parent) {
		return new SceneGraphNode(name, parent).setValue(VanillaRenderable.Instance.empty(true));// 默认节点可渲染对象为null
	}

	private static String[] parsePath(String path) {
		return path.split("/");
	}

	/**
	 * 创建场景图
	 * 
	 * @return
	 */
	public static final SceneGraphNode createSceneGraph() {
		return (SceneGraphNode) new SceneGraphNode();
	}

	/**
	 * 创建空节点或返回已有节点
	 * 
	 * @param path 节点路径，以"/"作为分隔符
	 * @return
	 */
	public final SceneGraphNode createNode(String path) {
		return (SceneGraphNode) this.findChildNode(parsePath(path), true);
	}

	public final SceneGraphNode createNode(String path, Matrix4f transform) {
		SceneGraphNode group = createNode(path);
		group.value.setTransform(transform);
		return group;
	}

	public final SceneGraphNode createRenderableNode(String path, VanillaRenderable.Instance renderable) {
		SceneGraphNode node = createNode(path);
		node.value = renderable;
		return node;
	}

	public final SceneGraphNode createRenderableNode(String path, VanillaRenderable renderable) {
		return createRenderableNode(path, renderable.newInstance());
	}

	/**
	 * 渲染前的更新操作，通常用于修改变换
	 */
	@FunctionalInterface
	public static interface UpdateOperation {
		/**
		 * 实时计算轨道
		 * 
		 * @param time tick为单位
		 * @return
		 */
		public abstract void update(SceneGraphNode node, float cam_x, float cam_y, float cam_z, float time);
	}

	/**
	 * 设置渲染前执行的操作
	 * 
	 * @param preRenderOperation
	 * @return
	 */
	public final SceneGraphNode setUpdate(UpdateOperation preRenderOperation) {
		this.preRenderOperation = preRenderOperation;
		return this;
	}

	/**
	 * 渲染本节点
	 * 
	 * @param poseStack
	 * @param projectionMatrix
	 */
	private void doRender(PoseStack poseStack, Matrix4f frustumMatrix, Matrix4f projectionMatrix) {
		if (preRenderOperation != null) {
			Vec3 pos = LevelRendererInternal.RenderLevel.LocalVars.camPos;
			preRenderOperation.update(this, (float) pos.x, (float) pos.y, (float) pos.z, LevelRendererInternal.RenderLevel.LocalVars.worldTime);
		}
		this.value.render(poseStack, projectionMatrix);// 渲染本节点
	}

	/**
	 * 设置顶点的纹理颜色各个通道的贡献比例。<br>
	 * 实际颜色为纹理颜色各个通道值乘以该比例，如果要可直观控制颜色，则纹理应当三色通道值相等。
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 * @return
	 */
	public SceneGraphNode setTexColorChannelContribution(float r, float g, float b, float a) {
		this.value.setShaderUniformColor(ColorRGBA.of(r, g, b, a));
		return this;
	}

	public SceneGraphNode setTexColorChannelContribution(ColorRGBA shaderColor) {
		this.value.setShaderUniformColor(shaderColor);
		return this;
	}

	public SceneGraphNode setTransform(Matrix4f transform) {
		this.value.setTransform(transform);
		return this;
	}

	public Matrix4f getTransform() {
		return this.value.getTransform();
	}

	public SceneGraphNode setVisible(boolean visible) {
		this.value.setVisible(visible);
		return this;
	}

	public boolean isVisible() {
		return this.value.isVisible();
	}

	/**
	 * 自顶向下创建渲染任务
	 * 
	 * @param frustumMatrix
	 * @param projectionMatrix
	 */
	public void render(Matrix4f frustumMatrix, Matrix4f projectionMatrix) {
		PoseStack poseStack = new PoseStack();
		poseStack.mulPose(frustumMatrix);
		RenderSystem.enableBlend();
		this.traverseChildrenFromTop((SceneGraphNode node) -> {
			node.doRender(poseStack, frustumMatrix, projectionMatrix);// 迭代子节点的子节点
		});
	}

	public void render(PoseStack poseStack, Matrix4f projectionMatrix) {
		render(poseStack.last().pose(), projectionMatrix);
	}

	public void render(PoseStack poseStack) {
		render(poseStack.last().pose(), RenderSystem.getProjectionMatrix());
	}

	public void render() {
		render(RenderSystem.getModelViewMatrix(), RenderSystem.getProjectionMatrix());
	}
}
