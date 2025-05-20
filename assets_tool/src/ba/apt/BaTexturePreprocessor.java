package ba.apt;

import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

public class BaTexturePreprocessor {

	/**
	 * 去除提取出的源素材文件的前缀名并重命名为小写格式，符合MC的命名规则
	 * 
	 * @param start_path
	 * @param file_type
	 * @param identifier
	 */
	public static void discardPrefix(String start_path, String file_type, String identifier) {
		Path rootPath = Paths.get(start_path);
		try (Stream<Path> stream = Files.walk(rootPath)) {
			stream.filter(Files::isRegularFile)
					.filter(path -> path.toString().toLowerCase().endsWith(file_type.toLowerCase()))
					.forEach((Path original_path) -> {
						try {
							String file_name = original_path.getFileName().toString();
							String new_name = file_name;
							int idx = file_name.indexOf(identifier);
							if (idx != -1)// 如果不满足前缀规则就不截断
								new_name = file_name.substring(idx + identifier.length());
							new_name = new_name.toLowerCase();
							Path new_path = original_path.getParent().resolve(new_name);
							// 重命名文件
							Files.move(original_path, new_path, StandardCopyOption.REPLACE_EXISTING);
							System.out.println("Renamed " + original_path + " -> " + new_path);
						} catch (IOException e) {
							System.err.println("Rename " + original_path + " failed.");
							e.printStackTrace();
						}
					});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 裁剪素材，裁剪区域为居中的最大方形
	 * 
	 * @param start_path
	 * @param file_type
	 */
	public static void clipCenteredTexture(String start_path, String file_type) {
		Path rootPath = Paths.get(start_path);
		try (Stream<Path> stream = Files.walk(rootPath)) {
			stream.filter(Files::isRegularFile)
					.filter(path -> path.toString().toLowerCase().endsWith(file_type.toLowerCase()))
					.forEach((Path image_path) -> {
						try {
							BufferedImage img = ImageIO.read(image_path.toFile());
							int width = img.getWidth();
							int height = img.getHeight();
							if (width == height)// 已经是方形则不再裁剪
								return;
							int size = Math.min(width, height);
							int x = (width - size) / 2;
							int y = (height - size) / 2;
							Path temp_centered_img = Files.createTempFile(image_path.getParent(), "temp_", file_type);
							ImageIO.write(img.getSubimage(x, y, size, size), file_type.replace(".", "").toLowerCase(), temp_centered_img.toFile());
							Files.move(temp_centered_img, image_path, StandardCopyOption.REPLACE_EXISTING);
							System.out.println("Clipped " + image_path);
						} catch (IOException | IllegalArgumentException | RasterFormatException e) {
							System.err.println("Clipped " + image_path + " failed.");
							e.printStackTrace();
						}
					});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 快速处理某个文件夹下的指定文件
	 * 
	 * @param start_path
	 * @param identifier
	 */
	public static void preprocess(String start_path, String identifier) {
		discardPrefix(start_path, ".png", identifier);
		clipCenteredTexture(start_path, ".png");
	}
}
