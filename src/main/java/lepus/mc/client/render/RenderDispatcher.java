package lepus.mc.client.render;

import java.lang.invoke.MethodHandle;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.mojang.blaze3d.vertex.PoseStack;

import lepus.mc.client.render.renderable.Renderable;
import lepus.mc.core.Core;
import lyra.lang.Arrays;
import lyra.lang.Handles;
import net.minecraft.client.Minecraft;
import net.minecraft.util.profiling.ProfilerFiller;

public abstract class RenderDispatcher<R extends Renderable> {
	protected RenderDispatcher() {
		this.registerDispatcher();
	}

	private static final ArrayList<RenderDispatcher<?>> dispatchers = new ArrayList<>();

	@SuppressWarnings("deprecation")
	public static final <R extends Renderable, D extends RenderDispatcher<R>, T extends RenderDispatcher<R>.Task> D register(Class<D> dispatcherClass, Class<T> taskType, Class<?>... argTypes) {
		D dispatcher = null;
		try {
			dispatcher = dispatcherClass.newInstance();
			dispatcher.bindTaskType(taskType, argTypes);
			dispatchers.add(dispatcher);
		} catch (InstantiationException | IllegalAccessException e) {
			Core.logError("Create RenderDispatcher " + dispatcherClass + " failed", e);
			e.printStackTrace();
		}
		return dispatcher;
	}

	public abstract class Task {
		private Object key;
		protected final PoseStack poseStack;
		protected final PoseStack.Pose pose;
		private final ArrayList<R> toBeRemoved = new ArrayList<>();

		public Task(PoseStack poseStack) {
			this.poseStack = poseStack;
			this.pose = poseStack.last().copy();
		}

		private final Task setKey(Object key) {
			this.key = key;
			return this;
		}

		public final void render() {
			CopyOnWriteArrayList<R> renderableList = RenderDispatcher.this.getTaskTypeRenderables(key);
			PoseStacks.pushPose(poseStack, pose);// 手动推入创建该渲染任务时的Pose
			for (R r : renderableList) {
				poseStack.pushPose();
				this.render(r, poseStack);
				poseStack.popPose();
			}
			renderableList.removeAll(toBeRemoved);
			toBeRemoved.clear();
			poseStack.popPose();// 弹出手动推入的Pose
		}

		public final void remove(R r) {
			toBeRemoved.add(r);
		}

		/**
		 * 渲染逻辑
		 * 
		 * @param renderable
		 * @param poseStack
		 */
		public abstract void render(R renderable, PoseStack poseStack);
	}

	private MethodHandle taskTypeConstructor;
	private final ConcurrentHashMap<Object, CopyOnWriteArrayList<R>> taskRenderables = new ConcurrentHashMap<>();
	private final ArrayList<Task> createdTasks = new ArrayList<>();

	/**
	 * 获取绑定某个key的可渲染对象
	 * 
	 * @param key
	 * @return
	 */
	public final CopyOnWriteArrayList<R> getTaskTypeRenderables(Object key) {
		return taskRenderables.computeIfAbsent(key, (Object) -> new CopyOnWriteArrayList<>());
	}

	public final <T extends Task> void bindTaskType(Class<T> type, Class<?>... argTypes) {
		taskTypeConstructor = Handles.findConstructor(type, Arrays.cat(this.getClass(), argTypes));
	}

	/**
	 * 创建渲染任务
	 * 
	 * @param task
	 */
	@SuppressWarnings("unchecked")
	public final void createTask(Object key, Object... taskArgs) {
		CopyOnWriteArrayList<R> renderables = taskRenderables.get(key);
		if (renderables == null || renderables.isEmpty())// 如果指定类型没有可渲染对象，则不创建任务
			return;
		try {
			createdTasks.add(((Task) taskTypeConstructor.invokeWithArguments(Arrays.cat(this, taskArgs))).setKey(key));
		} catch (Throwable e) {
			Core.logError("Initialization new task failed, bound constructor is " + taskTypeConstructor, e);
		}
	}

	/**
	 * 为指定的渲染任务类型添加可渲染对象
	 * 
	 * @param key
	 * @param rs
	 */
	@SuppressWarnings("unchecked")
	public void addTaskTypeRenderables(Object key, R... rs) {
		CopyOnWriteArrayList<R> renderables = getTaskTypeRenderables(key);
		renderables.addAll(List.of(rs));
	}

	/**
	 * 执行渲染任务
	 * 
	 * @param profilerfiller_info
	 */
	@SuppressWarnings("resource")
	public final void executeTasks(String profilerfiller_info) {
		ProfilerFiller profilerfiller = Minecraft.getInstance().level.getProfiler();
		profilerfiller.push(profilerfiller_info);// 当前渲染状态
		for (Task task : createdTasks)
			task.render();
		profilerfiller.pop();
		createdTasks.clear();// 渲染完成后清除实体列表
	}

	public abstract void registerDispatcher();

}
