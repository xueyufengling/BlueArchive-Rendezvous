package fw.client.render.gl;

import java.util.function.Consumer;

import org.joml.Matrix4f;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import fw.client.render.renderable.Renderable;
import fw.common.ColorRGBA;
import lyra.alpha.struct.Node;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * 场景图节点，树结构组织RenderableRenderableObject.Instance
 */
@OnlyIn(Dist.CLIENT)
public class SceneGraphNode extends Node<String, RenderableObject.Instance> implements Renderable {

	private Consumer<SceneGraphNode> preRenderOperation;

	/**
	 * 只有变换的组节点
	 * 
	 * @param name
	 */
	private SceneGraphNode(String name) {
		super(name);
	}

	private SceneGraphNode(String name, RenderableObject.Instance renderable) {
		super(name, renderable);
	}

	private SceneGraphNode(String name, Node<String, RenderableObject.Instance> parent) {
		super(name, parent);
	}

	/**
	 * 根节点
	 */
	private SceneGraphNode() {
		this("root");
		this.setValue(RenderableObject.Instance.empty(true));
	}

	@Override
	protected Node<String, RenderableObject.Instance> newNode(String name, Node<String, RenderableObject.Instance> parent) {
		return new SceneGraphNode(name, parent).setValue(RenderableObject.Instance.empty(true));// 默认节点可渲染对象为null
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

	public final SceneGraphNode createRenderableNode(String path, RenderableObject.Instance renderable) {
		SceneGraphNode node = createNode(path);
		node.value = renderable;
		return node;
	}

	public final SceneGraphNode createRenderableNode(String path, RenderableObject renderable) {
		return createRenderableNode(path, renderable.newInstance());
	}

	/**
	 * 设置渲染前执行的操作
	 * 
	 * @param preRenderOperation
	 * @return
	 */
	public final SceneGraphNode setPreRenderOperation(Consumer<SceneGraphNode> preRenderOperation) {
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
		if (preRenderOperation != null)
			preRenderOperation.accept(this);
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
