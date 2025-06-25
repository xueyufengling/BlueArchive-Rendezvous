package fw.items;

import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Supplier;

import fw.core.registry.RegistryFactory;
import fw.datagen.Localizable;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;

public class ExtBlocks {
	private static final HashMap<String, DeferredBlock<Block>> blocksMap = new HashMap<>();

	/**
	 * 没有任何功能的简单方块，通常是装饰性方块
	 */
	public static class SimpleBlock extends Block implements Localizable {

		public SimpleBlock(BlockBehaviour.Properties properties) {
			super(properties);
		}

		@Override
		public String localizationKey() {
			return this.getDescriptionId();
		}
	}

	public static final DeferredBlock<Block> register(String name, Supplier<? extends Block> blockGetter, Item.Properties blockItemProps) {
		DeferredBlock<Block> block = RegistryFactory.BLOCK.register(name, blockGetter);
		blocksMap.put(name, block);
		if (blockItemProps != null)
			ExtItems.registerBlockItem(name, block.get(), blockItemProps);// 添加物品进创造物品栏
		return block;
	}

	public static final DeferredBlock<Block> register(String name, ExtCreativeTab creativeTab, Supplier<? extends Block> blockGetter, Item.Properties blockItemProps) {
		DeferredBlock<Block> block = RegistryFactory.BLOCK.register(name, blockGetter);
		blocksMap.put(name, block);
		if (blockItemProps != null)
			ExtItems.registerBlockItem(name, creativeTab, block.get(), blockItemProps);// 添加物品进创造物品栏
		return block;
	}

	public static final DeferredBlock<Block> register(String name, ExtCreativeTab creativeTab, Supplier<? extends Block> blockGetter) {
		return register(name, creativeTab, blockGetter, new Item.Properties());
	}

	public static final DeferredBlock<Block> register(String name, Supplier<? extends Block> blockGetter) {
		return register(name, blockGetter, new Item.Properties());
	}

	// --------------------------------------
	/**
	 * 注册方块，blockItemProps非null则为其创建BlockItem并注册
	 * 
	 * @param name
	 * @param blockGetter
	 * @param blockProps
	 * @param blockItemProps
	 * @return
	 */
	public static final DeferredBlock<Block> register(String name, Function<BlockBehaviour.Properties, ? extends Block> blockGetter, BlockBehaviour.Properties blockProps, Item.Properties blockItemProps) {
		DeferredBlock<Block> block = RegistryFactory.BLOCK.registerBlock(name, blockGetter, blockProps);
		blocksMap.put(name, block);
		if (blockItemProps != null)
			ExtItems.registerBlockItem(name, block.get(), blockItemProps);// 添加物品进创造物品栏
		return block;
	}

	/**
	 * 注册方块，blockItemProps非null则为其创建BlockItem并注册和添加到对应的创造物品栏
	 * 
	 * @param name
	 * @param creativeTab
	 * @param blockGetter
	 * @param blockProps
	 * @param blockItemProps
	 * @return
	 */
	public static final DeferredBlock<Block> register(String name, ExtCreativeTab creativeTab, Function<BlockBehaviour.Properties, ? extends Block> blockGetter, BlockBehaviour.Properties blockProps, Item.Properties blockItemProps) {
		DeferredBlock<Block> block = RegistryFactory.BLOCK.registerBlock(name, blockGetter, blockProps);
		blocksMap.put(name, block);
		if (blockItemProps != null)
			ExtItems.registerBlockItem(name, creativeTab, block.get(), blockItemProps);// 添加物品进创造物品栏
		return block;
	}

	public static final DeferredBlock<Block> register(String name, ExtCreativeTab creativeTab, Function<BlockBehaviour.Properties, ? extends Block> blockGetter, BlockBehaviour.Properties blockProps) {
		return register(name, creativeTab, blockGetter, blockProps, new Item.Properties());
	}

	public static final DeferredBlock<Block> register(String name, Function<BlockBehaviour.Properties, ? extends Block> blockGetter, BlockBehaviour.Properties blockProps) {
		return register(name, blockGetter, blockProps, new Item.Properties());
	}

	// ---------------------------------------
	public static final DeferredBlock<Block> register(String name, ExtCreativeTab creativeTab, BlockBehaviour.Properties blockProps) {
		return register(name, creativeTab, () -> new SimpleBlock(blockProps));
	}

	public static final DeferredBlock<Block> register(String name, BlockBehaviour.Properties blockProps) {
		return register(name, () -> new SimpleBlock(blockProps));
	}

	/**
	 * 根据注册名获取物品
	 * 
	 * @param name
	 * @return
	 */
	public static final DeferredBlock<Block> get(String name) {
		return blocksMap.get(name);
	}

	/**
	 * 根据注册名判定是否已经注册物品
	 * 
	 * @param name
	 * @return
	 */
	public static final boolean contains(String name) {
		return blocksMap.containsKey(name);
	}
}