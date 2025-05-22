package ba.apt;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ExtItemsClassFileGenerator {

	/**
	 * 材质拓展名，根据材质创建Java源文件中的字段
	 */
	public static final String texture_type = ".png";

	public static String texture_path_prefix = "BaCreativeTab.Id.BA_";
	public static String creative_tab_prefix = "BaCreativeTab.BA_";

	public static String generated_class_prefix = "Ba";
	public static String generated_class_package = "ba.entries.items";
	public static String[] generated_class_imports = new String[] { "ba.entries.BaCreativeTab" };

	/**
	 * 生成的物品类的前缀
	 */

	public static void generateItemsClassFile(String start_path, String class_name, String class_package, String[] imports, String tex_path, String creative_tab, String itemGetter, String output_dir) {
		try {
			List<String> textures = listAllTextureNames(start_path);
			String generated_class_name = generated_class_prefix + class_name;
			List<String> lines = generateJavaCode(class_package, imports, generated_class_name, tex_path, creative_tab, itemGetter, textures);
			writeToFile(output_dir + File.separatorChar + generated_class_name + ".java", lines);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void generateItemsClassFile(String start_path, String class_name, String class_package, String[] imports, String tex_path, String creative_tab, String output_dir) {
		generateItemsClassFile(start_path, class_name, class_package, imports, tex_path, creative_tab, null, output_dir);
	}

	public static void generateItemsClassFile(String start_path, String class_name, String tex_path, String creative_tab, String output_dir) {
		generateItemsClassFile(start_path, class_name, generated_class_package, generated_class_imports, tex_path, creative_tab, output_dir);
	}

	public static void generateItemsClassFile(String start_path, String class_name, String output_dir) {
		String enum_name = class_name.toUpperCase();
		generateItemsClassFile(start_path, class_name, texture_path_prefix + enum_name, creative_tab_prefix + enum_name, output_dir);
	}

	private static List<String> listAllTextureNames(String start_path) throws IOException {
		List<String> names = new ArrayList<>();
		try (Stream<Path> stream = Files.walk(Paths.get(start_path))) {
			stream.filter(Files::isRegularFile)
					.filter(path -> path.toString().toLowerCase().endsWith(texture_type))
					.map(path -> {
						String texture_name = path.getFileName().toString();
						return texture_name.substring(0, texture_name.length() - texture_type.length());
					})
					.forEach(names::add);
		}
		return names;
	}

	/**
	 * 将ID转化为英文单词，包括以" "代替"_"，单词首字母大写
	 * 
	 * @param str
	 * @return
	 */
	private static String toEnglishWord(String str) {
		if (str == null || str.isEmpty()) {
			return str;
		}
		String[] words = str.split("_");
		String result = "";
		for (String word : words)
			result += word.substring(0, 1).toUpperCase() + word.substring(1) + " ";
		return result.trim();
	}

	private static List<String> generateJavaCode(String class_package, String[] imports, String class_name, String tex_path, String creative_tab, String itemGetter, List<String> textures) {
		List<String> lines = new ArrayList<>();
		lines.add("package " + class_package + ";\r\n"
				+ "\r\n"
				+ "import fw.core.ExtItems;\r\n"
				+ "import fw.datagen.ExtLangProvider;\r\n"
				+ "import fw.datagen.ItemDatagen;\r\n"
				+ "import fw.datagen.LangDatagen;\r\n"
				+ "import net.minecraft.world.item.Item;\r\n"
				+ "import net.neoforged.neoforge.registries.DeferredItem;\r\n");
		for (String imp : imports)
			lines.add("import " + imp + ";\r\n");
		lines.add("public class " + class_name + " extends ExtItems implements ExtLangProvider {\r\n"
				+ "\r\n"
				+ "	static {\r\n"
				+ "		ExtItems.forDatagen(" + class_name + ".class);\r\n"
				+ "		ExtLangProvider.forDatagen(" + class_name + ".class);\r\n"
				+ "	}\r\n"
				+ "\r\n"
				+ "	public static final String resourcePath = " + tex_path + ";\r\n"
				+ "\r\n");
		for (String tex : textures)
			lines.add("	@LangDatagen(en_us = \"" + toEnglishWord(tex) + "\", zh_cn = \"\")\r\n"
					+ "	@ItemDatagen(name = \"" + tex + "\", path = resourcePath)\r\n"
					+ "	public static final DeferredItem<Item> " + tex + " = register(\"" + tex + "\"" + (creative_tab == null ? "" : ", " + creative_tab) + (itemGetter == null ? "" : ", " + itemGetter) + ");"
					+ "\r\n");
		lines.add("}");
		return lines;
	}

	private static void writeToFile(String output_file, List<String> content) throws IOException {
		Path output_path = Paths.get(output_file);
		Files.createDirectories(output_path.getParent());
		Files.write(output_path, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
	}
}
