package lepus.mc.datagen.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import lepus.mc.resources.ResourceKeys;
import lepus.mc.resources.ResourceLocations;
import lepus.mc.resources.TagKeys;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/**
 * 数据生成阶段注册tag
 * 
 * @param <T>
 */
public final class ExtTagsProvider<T> extends TagsProvider<T> {
	private static final HashMap<String, HashMap<ResourceKey<? extends Registry<?>>, HashMap<TagKey<?>, List<ResourceKey<?>>>>> datagenTags = new HashMap<>();

	private HashMap<TagKey<T>, List<ResourceKey<T>>> tagsMap;

	ExtTagsProvider(ResourceKey<? extends Registry<T>> registryKey, PackOutput output, DatapackBuiltinEntriesProvider registriesProvider, String modId, ExistingFileHelper helper) {
		super(output, registryKey, registriesProvider.getRegistryProvider(), modId, helper);
	}

	/**
	 * 添加tag及其成员
	 * 
	 * @param <T>
	 * @param structureSetTagName
	 * @param members
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static final <T> void setTags(TagKey<T> structureSetTagName, List<ResourceKey<T>> tagMembers) {
		String modId = structureSetTagName.location().getNamespace();
		HashMap<ResourceKey<? extends Registry<?>>, HashMap<TagKey<?>, List<ResourceKey<?>>>> modAllTags = datagenTags.computeIfAbsent(modId, (String id) -> new HashMap<>());
		HashMap<TagKey<?>, List<ResourceKey<?>>> tagsMap = modAllTags.computeIfAbsent(structureSetTagName.registry(), (ResourceKey<? extends Registry<?>> key) -> new HashMap<>());
		tagsMap.put(structureSetTagName, (List) tagMembers);
	}

	public static final <T> void setTags(ResourceKey<? extends Registry<T>> registryKey, String structureSetTagName, String... tagMembers) {
		ResourceLocation loc = ResourceLocations.build(structureSetTagName);
		String modId = loc.getNamespace();
		HashMap<ResourceKey<? extends Registry<?>>, HashMap<TagKey<?>, List<ResourceKey<?>>>> modAllTags = datagenTags.computeIfAbsent(modId, (String id) -> new HashMap<>());
		HashMap<TagKey<?>, List<ResourceKey<?>>> tagsMap = modAllTags.computeIfAbsent(registryKey, (ResourceKey<? extends Registry<?>> key) -> new HashMap<>());
		ArrayList<ResourceKey<?>> members = new ArrayList<>();
		for (String member : tagMembers)
			members.add(ResourceKeys.build(registryKey, member));
		TagKey<T> tagKey = TagKeys.build(registryKey, loc);
		tagsMap.put(tagKey, members);
	}

	private final ExtTagsProvider<T> setTagsMap(HashMap<TagKey<T>, List<ResourceKey<T>>> tags) {
		this.tagsMap = tags;
		return this;
	}

	/**
	 * 向数据生成器添加本类记录的所有TagsProvider
	 * 
	 * @param generator
	 * @param run
	 * @param output
	 * @param registriesProvider
	 * @param helper
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static final void addProvider(DataGenerator generator, boolean run, PackOutput output, DatapackBuiltinEntriesProvider registriesProvider, ExistingFileHelper helper) {
		for (Entry<String, HashMap<ResourceKey<? extends Registry<?>>, HashMap<TagKey<?>, List<ResourceKey<?>>>>> modEntry : datagenTags.entrySet()) {
			String modId = modEntry.getKey();
			HashMap<ResourceKey<? extends Registry<?>>, HashMap<TagKey<?>, List<ResourceKey<?>>>> modAllTags = modEntry.getValue();
			for (Entry<ResourceKey<? extends Registry<?>>, HashMap<TagKey<?>, List<ResourceKey<?>>>> tagTypeEntry : modAllTags.entrySet()) {
				ResourceKey<? extends Registry<?>> tagRegistry = tagTypeEntry.getKey();
				HashMap<TagKey<?>, List<ResourceKey<?>>> tagsMap = tagTypeEntry.getValue();
				ExtTagsProvider<?> tagsProvider = new ExtTagsProvider<>(tagRegistry.registryKey(), output, registriesProvider, modId, helper);
				generator.addProvider(run, tagsProvider.setTagsMap((HashMap) tagsMap));
			}
		}
	}

	/**
	 * 每个ExtTagsProvider只对应一个mod、一个注册类型Tag的数据生成，如果有多个不同注册表类型的tag，则分别实例化各自的ExtTagsProvider，<br>
	 */
	@Override
	public void addTags(HolderLookup.Provider provider) {
		for (Entry<TagKey<T>, List<ResourceKey<T>>> tagEntry : tagsMap.entrySet())
			this.tag(tagEntry.getKey()).addAll(tagEntry.getValue());
	}
}