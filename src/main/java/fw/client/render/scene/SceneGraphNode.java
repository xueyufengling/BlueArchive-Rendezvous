package fw.client.render.scene;

import java.util.function.Consumer;

import org.joml.Matrix4f;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import fw.client.render.renderable.Renderable;
import lyra.alpha.struct.Node;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * 场景图节点，树结构组织RenderableObject
 */
@OnlyIn(Dist.CLIENT)
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

	private static String[] parsePath(String path) {
		return path.split("/");
	}

	/**
	 * 创建场景图
	 * 
	 * @return
	 */
	public static final SceneGraphNode createSceneGraph() {
		return new SceneGraphNode();
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
		group.transform = transform;
		return group;
	}

	public final SceneGraphNode createRenderableNode(String path, RenderableObject renderable) {
		SceneGraphNode node = createNode(path);
		node.value = renderable;
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
	 * 设置顶点的纹理颜色各个通道的贡献比例。<br>
	 * 实际颜色为纹理颜色各个通道值乘以该比例，如果要可直观控制颜色，则纹理应当三色通道值相等。
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 * @return
	 */
	public SceneGraphNode setTexColorChannelRatio(float r, float g, float b, float a) {
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
