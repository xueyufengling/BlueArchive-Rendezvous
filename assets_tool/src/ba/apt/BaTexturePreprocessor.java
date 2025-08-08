package ba.apt;

import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
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

	public static final double gray(int r, int g, int b) {
		return 0.299 * r + 0.587 * g + 0.114 * b;
	}

	public static final void toAlphaMask(String src, String dest) {
		try {
			BufferedImage image = ImageIO.read(new File(src));
			int width = image.getWidth();
			int height = image.getHeight();
			int origMaxA = 0;
			int procMaxA = 0;
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int pixel = image.getRGB(x, y);
					int a = (pixel >> 24) & 0xff;
					int r = (pixel >> 16) & 0xff;
					int g = (pixel >> 8) & 0xff;
					int b = pixel & 0xff;
					if (a > origMaxA)
						origMaxA = a;
					double gray = gray(r, g, b);
					int newAlpha = (int) (a * (1 - gray / 255));
					if (newAlpha > procMaxA)
						procMaxA = newAlpha;
					image.setRGB(x, y, (a << 24) | ((int) newAlpha << 16) | ((int) newAlpha << 8) | (int) newAlpha);
				}
			}
			double aScale = ((double) origMaxA) / procMaxA;// 对转换后的alpha因子进行补偿
			System.out.println("Original image maxAlpha=" + origMaxA + ", mask image maxAlpha=" + procMaxA + ", alpha scale factor is set to " + aScale);
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int pixel = image.getRGB(x, y);
					int a = (pixel >> 24) & 0xff;
					int r = (pixel >> 16) & 0xff;
					int g = (pixel >> 8) & 0xff;
					int b = pixel & 0xff;
					image.setRGB(x, y, ((int) (a) << 24) | ((int) (r * aScale) << 16) | ((int) (g * aScale) << 8) | (int) (b * aScale));
				}
			}
			ImageIO.write(image, "PNG", new File(dest));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void applyAlphaMask(String mask, String src, String dest) {
		try {
			BufferedImage maskImage = ImageIO.read(new File(mask));
			BufferedImage colorImage = ImageIO.read(new File(src));
			int width = colorImage.getWidth();
			int height = colorImage.getHeight();
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int pixel = colorImage.getRGB(x, y);
					int r = (pixel >> 16) & 0xff;
					int g = (pixel >> 8) & 0xff;
					int b = pixel & 0xff;
					// mask
					int mpixel = maskImage.getRGB(x, y);
					int msa = (mpixel >> 24) & 0xff;// mask原始图像的alpha通道值
					int ma = (mpixel >> 16) & 0xff;
					double f = ma / 255.0;
					colorImage.setRGB(x, y, (msa << 24) | ((int) (255 * (1 - f) + r * f) << 16) | ((int) (255 * (1 - f) + g * f) << 8) | (int) (255 * (1 - f) + b * f));
				}
			}
			ImageIO.write(colorImage, "PNG", new File(dest));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void applyAlphaChannel(String alpha, String src, String dest) {
		try {
			BufferedImage alphaImage = ImageIO.read(new File(alpha));
			BufferedImage colorImage = ImageIO.read(new File(src));
			int width = colorImage.getWidth();
			int height = colorImage.getHeight();
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int pixel = colorImage.getRGB(x, y);
					int r = (pixel >> 16) & 0xff;
					int g = (pixel >> 8) & 0xff;
					int b = pixel & 0xff;
					int destAlpha = (alphaImage.getRGB(x, y) >> 24) & 0xff;
					colorImage.setRGB(x, y, (destAlpha << 24) | (r << 16) | (g << 8) | b);
				}
			}
			ImageIO.write(colorImage, "PNG", new File(dest));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
