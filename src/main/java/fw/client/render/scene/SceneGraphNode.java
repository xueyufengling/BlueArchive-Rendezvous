package fw.client.render.scene;

import java.util.function.Consumer;

import org.joml.Matrix4f;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import fw.client.render.renderable.Renderable;
import lyra.alpha.struct.Node;

/**
 * 场景图节点，树结构组织RenderableObject
 */
public class SceneGraphNode extends Node<String, RenderableObject> implements Renderable {
	/**
	 * 对象可见性，不可见则不渲染
	 */
	public boolean visiable = true;

	/**
	 * 该节点的变换矩阵，此变换矩阵将应用在所有子节点及其嵌套节点上<br>
	 * 默认恒等矩阵，不进行任何变换
	 */
	public Matrix4f transform = new Matrix4f();

	private Consumer<SceneGraphNode> preRenderOperation;

	/**
	 * 只有变换的组节点
	 * 
	 * @param name
	 */
	private SceneGraphNode(String name) {
		super(name);
		this.visiable = false;
	}

	private SceneGraphNode(String name, RenderableObject renderable) {
		super(name, renderable);
	}

	private SceneGraphNode(String name, Node<String, RenderableObject> parent) {
		super(name, parent);
	}

	/**
	 * 根节点
	 */
	private SceneGraphNode() {
		this("root");
	}

	@Override
	protected Node<String, RenderableObject> newNode(String name, Node<String, RenderableObject> parent) {
		return new SceneGraphNode(name, parent);
	}

	/**
	 * 创建场景图
	 * 
	 * @return
	 */
	public static final SceneGraphNode createSceneGraph() {
		return new SceneGraphNode();
	}

	public static final SceneGraphNode createGroupNode(String name) {
		return new SceneGraphNode(name);
	}

	public static final SceneGraphNode createGroupNode(String name, Matrix4f transform) {
		SceneGraphNode group = createGroupNode(name);
		group.transform = transform;
		return group;
	}

	public static final SceneGraphNode createRenderableNode(String name, RenderableObject renderable) {
		return new SceneGraphNode(name, renderable);
	}

	public final SceneGraphNode attachRenderableNode(String name, RenderableObject renderable) {
		SceneGraphNode node = createRenderableNode(name, renderable);
		this.attachChildNode(node);
		return node;
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
		poseStack.pushPose();
		if (preRenderOperation != null)
			preRenderOperation.accept(this);
		poseStack.mulPose(this.transform);// 应用节点变换
		if (this.visiable && this.value != null) {
			float[] prevColor = RenderSystem.getShaderColor();
			RenderSystem.setShaderColor(shaderColor[0], shaderColor[1], shaderColor[2], shaderColor[3]);// 设置其顶点颜色
			this.value.render(poseStack, projectionMatrix);// 渲染本节点
			RenderSystem.setShaderColor(prevColor[0], prevColor[1], prevColor[2], prevColor[3]);// 恢复顶点颜色
		}
		poseStack.popPose();
	}

	private float[] shaderColor = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };

	/**
	 * 设置顶点颜色
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 * @return
	 */
	public SceneGraphNode setShaderColor(float r, float g, float b, float a) {
		shaderColor[0] = r;
		shaderColor[1] = g;
		shaderColor[2] = b;
		shaderColor[3] = a;
		return this;
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
		RenderSystem.disableBlend();
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
