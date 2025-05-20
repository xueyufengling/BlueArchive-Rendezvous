package ba.apt;

public class Main {
	public static void main(String args[]) {
		// BaTexturePreprocessor.preprocess("D:\\JavaProjects\\BlueArchive-Rendezvous\\src\\main\\resources\\assets\\ba\\textures\\item\\materials", "Material_");
		ItemsClassFileGenerator.generateItemsClassFile("D:\\JavaProjects\\BlueArchive-Rendezvous\\src\\main\\resources\\assets\\ba\\textures\\item\\materials",
				"Materials",
				"D:\\JavaProjects\\BlueArchive-Rendezvous\\src\\main\\java\\ba\\entries\\items");
	}
}
