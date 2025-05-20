package ba.apt;

public class Main {
	public static void main(String args[]) {
		String texture_path = "D:\\JavaProjects\\BlueArchive-Rendezvous\\src\\main\\resources\\assets\\ba\\textures\\item\\skillbooks";
		BaTexturePreprocessor.preprocess(texture_path, "Item_Icon_");
		ItemsClassFileGenerator.generateItemsClassFile(texture_path,
				"Skillbooks",
				"D:\\JavaProjects\\BlueArchive-Rendezvous\\src\\main\\java\\ba\\entries\\items");
	}
}
