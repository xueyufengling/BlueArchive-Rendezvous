package fw.blocks;

import fw.core.Core;
import fw.core.registry.RegistryMap;
import fw.core.registry.RegistryMap.BlockMap;
import fw.datagen.Localizable;
import fw.items.ExtCreativeTab;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;

/**
 * 没有任何功能的简单方块，通常是装饰性方块
 */
public class ExtBlock extends Block implements Localizable {
	public static final RegistryMap.BlockMap BLOCKS = (BlockMap) RegistryMap.of(Registries.BLOCK);

	public ExtBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	public static BlockBehaviour.Properties defaultBlockBehaviourProperties() {
		return BlockBehaviour.Properties.of();
	}

	public static BlockBehaviour.Properties blockBehaviourPropertiesOf(BlockBehaviour block) {
		return BlockBehaviour.Properties.ofFullCopy(block);
	}

	/**
	 * 从Blocks类中的指定方块获取属性并创建新方块
	 * 
	 * @param block
	 */
	public ExtBlock(BlockBehaviour block) {
		this(blockBehaviourPropertiesOf(block));
	}

	public ExtBlock() {
		this(defaultBlockBehaviourProperties());
	}

	@Override
	public String localizationKey() {
		return Localizable.stdLocalizationKey(this.getDescriptionId());
	}

	public static final DeferredBlock<ExtBlock> register(String name, BlockBehaviour.Properties blockProperties, ExtCreativeTab creativeTab) {
		return BLOCKS.registerBlock(name, () -> new ExtBlock(blockProperties), creativeTab);
	}

	public static final DeferredBlock<ExtBlock> registerMod(String name, BlockBehaviour.Properties blockProperties, ExtCreativeTab creativeTab) {
		return register(Core.modNamespacedId(name), blockProperties, creativeTab);
	}

	public static final DeferredBlock<ExtBlock> register(String name, BlockBehaviour.Properties blockProperties) {
		return register(name, blockProperties, null);
	}

	public static final DeferredBlock<ExtBlock> registerMod(String name, BlockBehaviour.Properties blockProperties) {
		return registerMod(name, blockProperties, null);
	}

	public static final DeferredBlock<ExtBlock> register(String name, ExtCreativeTab creativeTab) {
		return register(name, defaultBlockBehaviourProperties(), creativeTab);
	}

	public static final DeferredBlock<ExtBlock> registerMod(String name, ExtCreativeTab creativeTab) {
		return registerMod(name, defaultBlockBehaviourProperties(), creativeTab);
	}

	public static final DeferredBlock<ExtBlock> register(String name) {
		return register(name, (ExtCreativeTab) null);
	}

	public static final DeferredBlock<ExtBlock> registerMod(String name) {
		return registerMod(name, (ExtCreativeTab) null);
	}

	/**
	 * 以指定方块行为创建一个方块，可用Blocks类中的静态Block字段作为参数传入<br>
	 * 创建完成后会加入创造物品栏
	 * 
	 * @param name
	 * @param block
	 * @param creativeTab
	 * @return
	 */
	public static final DeferredBlock<ExtBlock> register(String name, BlockBehaviour block, ExtCreativeTab creativeTab) {
		return register(name, blockBehaviourPropertiesOf(block), creativeTab);
	}

	public static final DeferredBlock<ExtBlock> registerMod(String name, BlockBehaviour block, ExtCreativeTab creativeTab) {
		return registerMod(name, blockBehaviourPropertiesOf(block), creativeTab);
	}

	/**
	 * 以指定方块行为创建一个方块，可用Blocks类中的静态Block字段作为参数传入<br>
	 * 创建完成后不会加入创造物品栏，例如各种流体方块就可以不注册到创造物品栏
	 * 
	 * @param name
	 * @param block
	 * @return
	 */
	public static final DeferredBlock<ExtBlock> register(String name, BlockBehaviour block) {
		return register(name, block, null);
	}

	public static final DeferredBlock<ExtBlock> registerMod(String name, BlockBehaviour block) {
		return registerMod(name, block, null);
	}
}