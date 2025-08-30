package lepus.mc.terrain.structure.template;

import java.util.ArrayList;
import java.util.List;

import lepus.mc.core.registry.RegistryMap;
import lepus.mc.datagen.EntryHolder;
import lepus.mc.resources.ResourceKeys;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.JigsawReplacementProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.PosRuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.ProcessorRule;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.RuleBlockEntityModifier;

public class ExtStructureProcessor {
	public static final StructureProcessorList listOf(List<StructureProcessor> list) {
		return new StructureProcessorList(list);
	}

	public static final StructureProcessorList listOf(StructureProcessor... processors) {
		return listOf(List.of(processors));
	}

	public static final RegistryMap<StructureProcessorList> PROCESSOR_LISTS = RegistryMap.of(Registries.PROCESSOR_LIST);

	public static final ResourceKey<StructureProcessorList> listKey(String name) {
		return ResourceKeys.build(Registries.PROCESSOR_LIST, name);
	}

	public static final Holder.Reference<StructureProcessorList> list(BootstrapContext<?> context, String name) {
		return context.lookup(Registries.PROCESSOR_LIST).getOrThrow(listKey(name));
	}

	/**
	 * 注册一个StructureProcessorList
	 * 
	 * @param name
	 * @param list
	 * @return
	 */
	public static final EntryHolder<StructureProcessorList> registerList(String name, EntryHolder.BootstrapValue<StructureProcessorList> list) {
		return EntryHolder.of(Registries.PROCESSOR_LIST, name, list);
	}

	public static final JigsawReplacementProcessor JigsawReplacementProcessor() {
		return JigsawReplacementProcessor.INSTANCE;
	}

	public static class ExtRuleProcessor {
		private ArrayList<ProcessorRule> rules = new ArrayList<>();

		public static final ExtRuleProcessor of() {
			return new ExtRuleProcessor();
		}

		public final ExtRuleProcessor rule(RuleTest inputPredicate, RuleTest locPredicate, PosRuleTest posPredicate, BlockState outputState, RuleBlockEntityModifier blockEntityModifier) {
			rules.add(new ProcessorRule(inputPredicate, locPredicate, posPredicate, outputState, blockEntityModifier));
			return this;
		}

		public final ExtRuleProcessor rule(RuleTest inputPredicate, RuleTest locPredicate, PosRuleTest posPredicate, BlockState outputState) {
			rules.add(new ProcessorRule(inputPredicate, locPredicate, posPredicate, outputState));
			return this;
		}

		public final ExtRuleProcessor rule(RuleTest inputPredicate, RuleTest locPredicate, BlockState outputState) {
			rules.add(new ProcessorRule(inputPredicate, locPredicate, outputState));
			return this;
		}

		public final RuleProcessor build() {
			return new RuleProcessor(rules);
		}
	}
}
