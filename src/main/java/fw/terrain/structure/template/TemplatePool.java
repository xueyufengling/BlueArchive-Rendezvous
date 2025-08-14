package fw.terrain.structure.template;

import java.util.ArrayList;
import java.util.function.Function;

import com.mojang.datafixers.util.Pair;

import fw.core.registry.RegistryMap;
import fw.datagen.EntryHolder;
import fw.resources.ResourceKeyBuilder;
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
	public static final RegistryMap<StructureTemplatePool> TEMPLATE_POOLS = RegistryMap.of(Registries.TEMPLATE_POOL);

	public static final Holder<StructureTemplatePool> empty(BootstrapContext<?> context) {
		return context.lookup(Registries.TEMPLATE_POOL).getOrThrow(Pools.EMPTY);
	}

	public static final ResourceKey<StructureTemplatePool> key(String name) {
		return ResourceKeyBuilder.build(Registries.TEMPLATE_POOL, name);
	}

	public static final EntryHolder<StructureTemplatePool> register(String name, EntryHolder.BootstrapValue<StructureTemplatePool> pool) {
		return EntryHolder.of(Registries.TEMPLATE_POOL, name, pool);
	}

	private BootstrapContext<?> context;
	private Holder<StructureTemplatePool> fallback;

	private ArrayList<Pair<StructurePoolElement, Integer>> templates = new ArrayList<>();

	public TemplatePool(BootstrapContext<?> context, String fallback) {
		this.context = context;
		ResourceKey<StructureTemplatePool> fallbackKey = fallback == null ? Pools.EMPTY : key(fallback);
		this.fallback = context.lookup(Registries.TEMPLATE_POOL).getOrThrow(fallbackKey);
	}

	public TemplatePool(BootstrapContext<?> context) {
		this(context, null);
	}

	public StructureTemplatePool build() {
		return new StructureTemplatePool(fallback, templates);
	}

	public final TemplatePool entry(StructurePoolElement element, int repeatCount) {
		templates.add(Pair.of(element, repeatCount));
		return this;
	}

	/**
	 * @param elementFunc
	 * @param proj
	 * @param repeatCount 重复次数
	 * @return
	 */
	public final TemplatePool entry(Function<StructureTemplatePool.Projection, ? extends StructurePoolElement> elementFunc, StructureTemplatePool.Projection proj, int repeatCount) {
		return entry(elementFunc.apply(proj), repeatCount);
	}

	public final TemplatePool singleEntry(String id, Holder<StructureProcessorList> processors, LiquidSettings liquidSettings, StructureTemplatePool.Projection proj, int repeatCount) {
		return entry(StructurePoolElement.single(id, processors, liquidSettings), proj, repeatCount);
	}

	public final TemplatePool singleEntry(String id, Holder<StructureProcessorList> processors, StructureTemplatePool.Projection proj, int repeatCount) {
		return entry(StructurePoolElement.single(id, processors), proj, repeatCount);
	}

	public final TemplatePool singleEntry(String id, LiquidSettings liquidSettings, StructureTemplatePool.Projection proj, int repeatCount) {
		return entry(StructurePoolElement.single(id, liquidSettings), proj, repeatCount);
	}

	public final TemplatePool singleEntry(String id, StructureTemplatePool.Projection proj, int repeatCount) {
		return entry(StructurePoolElement.single(id), proj, repeatCount);
	}

	public final TemplatePool singleEntry(String id, String processors, LiquidSettings liquidSettings, StructureTemplatePool.Projection proj, int repeatCount) {
		return singleEntry(id, ExtStructureProcessor.list(context, processors), liquidSettings, proj, repeatCount);
	}

	public final TemplatePool singleEntry(String id, String processors, StructureTemplatePool.Projection proj, int repeatCount) {
		return singleEntry(id, ExtStructureProcessor.list(context, processors), proj, repeatCount);
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
}