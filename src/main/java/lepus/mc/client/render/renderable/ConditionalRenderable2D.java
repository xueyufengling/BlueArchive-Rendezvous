package lepus.mc.client.render.renderable;

import java.util.ArrayList;
import java.util.Iterator;

import com.mojang.blaze3d.vertex.PoseStack;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * 根据优先级选择性地绘制复合2D对象，比CompositeRenderable2D更灵活。<br>
 * 支持动态添加和删除待渲染对象，但性能次于CompositeRenderable2D。<br>
 */
@OnlyIn(Dist.CLIENT)
public class ConditionalRenderable2D implements Cloneable, Renderable2D {
	/**
	 * 绘制策略
	 */
	public static interface DrawStrategy {
		public int next(int this_idx, ArrayList<Entry> renderables);

		public static final DrawStrategy THIS = (int this_idx, ArrayList<Entry> renderables) -> this_idx;
		public static final DrawStrategy BY_PRIORITY = (int this_idx, ArrayList<Entry> renderables) -> this_idx + 1;
		public static final DrawStrategy PRIORITY_HIGHEST = (int this_idx, ArrayList<Entry> renderables) -> -1;
		public static final DrawStrategy DEFAULT = DrawStrategy.PRIORITY_HIGHEST;
	}

	public class Entry implements Cloneable {
		Renderable2D renderable;

		public static final int DEFAULT_PRIORITY = 0;

		public int drawPriority = DEFAULT_PRIORITY;// 该图像的整体优先级

		@Override
		public Entry clone() {
			try {
				return (Entry) super.clone();
			} catch (CloneNotSupportedException ex) {
				ex.printStackTrace();
			}
			return null;
		}

		public Entry(Renderable2D renderable, int drawPriority) {
			this.renderable = renderable;
			this.drawPriority = drawPriority;
		}

		public Entry(Renderable2D renderable) {
			this(renderable, DEFAULT_PRIORITY);
		}

		public Entry setDrawPriority(int drawPriority) {
			this.drawPriority = drawPriority;
			return this;
		}
	}

	/**
	 * 可渲染的所有对象，按照drawPriority从大到小降序排列
	 */
	protected ArrayList<Entry> renderables = new ArrayList<>();

	private DrawStrategy drawStrategy;// 存在多个可渲染对象时的选择策略

	public ConditionalRenderable2D setDrawStrategy(DrawStrategy drawStrategy) {
		this.drawStrategy = drawStrategy;
		return this;
	}

	public DrawStrategy getDrawStrategy() {
		return this.drawStrategy;
	}

	public ConditionalRenderable2D(DrawStrategy drawStrategy) {
		this.drawStrategy = drawStrategy;
	}

	public ConditionalRenderable2D() {
		this(DrawStrategy.DEFAULT);
	}

	// 深拷贝data，里面的元素浅拷贝
	@Override
	@SuppressWarnings("unchecked")
	public ConditionalRenderable2D clone() {
		try {
			ConditionalRenderable2D obj = (ConditionalRenderable2D) super.clone();
			obj.renderables = (ArrayList<Entry>) obj.renderables.clone();
			for (int i = 0; i < obj.renderables.size(); ++i) {
				Entry entry = obj.renderables.get(i);
				obj.renderables.set(i, entry.clone());
			}
			return obj;
		} catch (CloneNotSupportedException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public void render(PoseStack poseStack, float x1, float y1) {
		poseStack.pushPose();
		int next_idx = 0;// 渲染开始时先获取优先级最高的可渲染对象
		while (next_idx >= 0 && next_idx < renderables.size()) {
			Entry next = renderables.get(next_idx);
			if (next != null)
				next.renderable.render(poseStack, x1, y1);
			next_idx = drawStrategy.next(next_idx, renderables);// 决定下一个要渲染的对象
		}
		poseStack.popPose();
	}

	/**
	 * 按照优先级进行降序排列，优先级最高的在最前面
	 * 
	 * @return
	 */
	protected final void sort() {
		this.renderables.sort((Entry e1, Entry e2) -> e2.drawPriority - e1.drawPriority);
	}

	public ConditionalRenderable2D add(int priority, Renderable2D... renderables) {
		for (Renderable2D renderable : renderables)
			this.renderables.add(new Entry(renderable, priority));
		sort();
		return this;
	}

	public ConditionalRenderable2D add(Renderable2D... renderables) {
		return this.add(Entry.DEFAULT_PRIORITY, renderables);
	}

	public ConditionalRenderable2D add(Renderable2D renderable, int priority) {
		this.renderables.add(new Entry(renderable, priority));
		sort();
		return this;
	}

	public ConditionalRenderable2D remove(Renderable2D... renderables) {
		Iterator<Entry> iter = this.renderables.iterator();
		while (iter.hasNext()) {
			Renderable2D r = iter.next().renderable;
			for (Renderable2D d : renderables)
				if (r == d) {
					iter.remove();
					break;
				}
		}
		sort();
		return this;
	}

	public Entry remove(int source_idx) {
		return renderables.remove(source_idx);
	}

	public int size() {
		return renderables.size();
	}

	public ConditionalRenderable2D clear() {
		renderables.clear();
		return this;
	}
}
