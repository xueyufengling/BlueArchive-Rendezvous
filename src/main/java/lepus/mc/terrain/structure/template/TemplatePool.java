package lepus.mc.terrain.structure.template;

import java.util.ArrayList;
import java.util.function.Function;

import com.mojang.datafixers.util.Pair;

import lepus.mc.datagen.EntryHolder;
import lepus.mc.resources.ResourceKeys;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

/**
 * 模板池,亦称之为结构池StructurePool。<br>
 * 用于储存同一个结构类型的多个拼图
 */
public class TemplatePool {
	public static final Holder<StructureTemplatePool> empty(BootstrapContext<?> context) {
		return context.lookup(Registries.TEMPLATE_POOL).getOrThrow(Pools.EMPTY);
	}

	public static final ResourceKey<StructureTemplatePool> key(String name) {
		return ResourceKeys.build(Registries.TEMPLATE_POOL, name);
	}

	public static final EntryHolder<StructureTemplatePool> register(String name, EntryHolder.BootstrapValue<StructureTemplatePool> pool) {
		return EntryHolder.of(Registries.TEMPLATE_POOL, name, pool);
	}

	private BootstrapContext<StructureTemplatePool> context;
	private Holder<StructureTemplatePool> fallback;
	private StructureTemplatePool.Projection proj;

	private ArrayList<Pair<StructurePoolElement, Integer>> templates = new ArrayList<>();

	public TemplatePool(BootstrapContext<StructureTemplatePool> context, String fallback, StructureTemplatePool.Projection proj) {
		this.context = context;
		ResourceKey<StructureTemplatePool> fallbackKey = fallback == null ? Pools.EMPTY : key(fallback);
		this.fallback = context.lookup(Registries.TEMPLATE_POOL).getOrThrow(fallbackKey);
		this.proj = proj;
	}

	public TemplatePool(BootstrapContext<StructureTemplatePool> context, String fallback) {
		this.context = context;
		ResourceKey<StructureTemplatePool> fallbackKey = fallback == null ? Pools.EMPTY : key(fallback);
		this.fallback = context.lookup(Registries.TEMPLATE_POOL).getOrThrow(fallbackKey);
	}

	public TemplatePool(BootstrapContext<StructureTemplatePool> context, StructureTemplatePool.Projection proj) {
		this(context, null, proj);
	}

	public TemplatePool(BootstrapContext<StructureTemplatePool> context) {
		this(context, null, null);
	}

	public StructureTemplatePool build() {
		return new StructureTemplatePool(fallback, templates);
	}

	public final TemplatePool projection(StructureTemplatePool.Projection proj) {
		this.proj = proj;
		return this;
	}

	public final TemplatePool entry(StructurePoolElement element, int weight) {
		templates.add(Pair.of(element, weight));
		return this;
	}

	/**
	 * @param elementFunc
	 * @param proj
	 * @param weight      重复次数
	 * @return
	 */
	public final TemplatePool entry(Function<StructureTemplatePool.Projection, ? extends StructurePoolElement> elementFunc, StructureTemplatePool.Projection proj, int weight) {
		return entry(elementFunc.apply(proj), weight);
	}

	public final TemplatePool singleEntry(String id, Holder<StructureProcessorList> processors, LiquidSettings liquidSettings, StructureTemplatePool.Projection proj, int weight) {
		return entry(StructurePoolElement.single(id, processors, liquidSettings), proj, weight);
	}

	public final TemplatePool singleEntry(String id, Holder<StructureProcessorList> processors, StructureTemplatePool.Projection proj, int weight) {
		return entry(StructurePoolElement.single(id, processors), proj, weight);
	}

	public final TemplatePool singleEntry(String id, LiquidSettings liquidSettings, StructureTemplatePool.Projection proj, int weight) {
		return entry(StructurePoolElement.single(id, liquidSettings), proj, weight);
	}

	public final TemplatePool singleEntry(String id, StructureTemplatePool.Projection proj, int weight) {
		return entry(StructurePoolElement.single(id), proj, weight);
	}

	public final TemplatePool singleEntry(String id, String processors, LiquidSettings liquidSettings, StructureTemplatePool.Projection proj, int weight) {
		return singleEntry(id, ExtStructureProcessor.list(context, processors), liquidSettings, proj, weight);
	}

	public final TemplatePool singleEntry(String id, String processors, StructureTemplatePool.Projection proj, int weight) {
		return singleEntry(id, ExtStructureProcessor.list(context, processors), proj, weight);
	}

	/**
	 * 使用本对象的proj计算
	 * 
	 * @param elementFunc
	 * @param weight      权重
	 * @return
	 */
	public final TemplatePool entry(Function<StructureTemplatePool.Projection, ? extends StructurePoolElement> elementFunc, int weight) {
		return entry(elementFunc, proj, weight);
	}

	public final TemplatePool singleEntry(String id, Holder<StructureProcessorList> processors, LiquidSettings liquidSettings, int weight) {
		return singleEntry(id, processors, liquidSettings, proj, weight);
	}

	public final TemplatePool singleEntry(String id, Holder<StructureProcessorList> processors, int weight) {
		return singleEntry(id, processors, proj, weight);
	}

	public final TemplatePool singleEntry(String id, LiquidSettings liquidSettings, int weight) {
		return singleEntry(id, liquidSettings, proj, weight);
	}

	public final TemplatePool singleEntry(String id, int weight) {
		return singleEntry(id, proj, weight);
	}

	public final TemplatePool singleEntry(String id, String processors, LiquidSettings liquidSettings, int weight) {
		return singleEntry(id, processors, liquidSettings, proj, weight);
	}

	public final TemplatePool singleEntry(String id, String processors, int weight) {
		return singleEntry(id, processors, proj, weight);
	}

	/**
	 * 只加载一次模板
	 * 
	 * @param id
	 * @param liquidSettings
	 * @param proj
	 * @return
	 */
	public final TemplatePool singleEntry(String id, LiquidSettings liquidSettings, StructureTemplatePool.Projection proj) {
		return singleEntry(id, liquidSettings, proj, 1);
	}

	public final TemplatePool singleEntry(String id, StructureTemplatePool.Projection proj) {
		return singleEntry(id, proj, 1);
	}

	public final TemplatePool singleEntry(String id, String processors, LiquidSettings liquidSettings, StructureTemplatePool.Projection proj) {
		return singleEntry(id, processors, liquidSettings, proj, 1);
	}

	public final TemplatePool singleEntry(String id, String processors, StructureTemplatePool.Projection proj) {
		return singleEntry(id, processors, proj, 1);
	}

	/**
	 * 使用本对象的proj计算
	 * 
	 * @param elementFunc
	 * @param weight
	 * @return
	 */
	public final TemplatePool singleEntry(String id, LiquidSettings liquidSettings) {
		return singleEntry(id, liquidSettings, proj);
	}

	public final TemplatePool singleEntry(String id) {
		return singleEntry(id, proj);
	}

	public final TemplatePool singleEntry(String id, String processors, LiquidSettings liquidSettings) {
		return singleEntry(id, processors, liquidSettings, proj);
	}

	public final TemplatePool singleEntry(String id, String processors) {
		return singleEntry(id, processors, proj);
	}
}