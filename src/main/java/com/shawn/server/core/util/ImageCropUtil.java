package com.shawn.server.core.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class ImageCropUtil {

	/**
	 * crop image with certain parameters
	 *
	 * @param sourcePath
	 * @param descPath
	 * @param cropX
	 * @param cropY
	 * @param width
	 * @param height
	 */
	public static void ImageCrop(String sourcePath, String descPath, int cropX, int cropY, int width, int height) {
		BufferedImage bufferedImage = readImageFile(sourcePath);
		bufferedImage = doCrop(bufferedImage, cropX, cropY, width, height);
		saveImageFile(bufferedImage, getImageSuffix(sourcePath), descPath);
	}

	/**
	 * crop image center square
	 *
	 * @param sourcePath
	 * @param descPath
	 */
	public static void ImageCropCenterSquare(String sourcePath, String descPath, int resizeWidth, int resizeHeight) {
		BufferedImage bufferedImage = readImageFile(sourcePath);

		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		int scaleLength = Math.min(width, height);
		int cropX = 0;
		int cropY = 0;
		if (width > height) {
			cropX = (width - height) / 2;
		} else {
			cropY = (height - width) / 2;
		}

		bufferedImage = doCrop(bufferedImage, cropX, cropY, scaleLength, scaleLength);
		bufferedImage = zoom(bufferedImage, resizeWidth, resizeHeight);
		saveImageFile(bufferedImage, getImageSuffix(sourcePath), descPath);
	}

	private static BufferedImage readImageFile(String path) {
		File imageFile = new File(path);
		try {
			return ImageIO.read(imageFile);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 压缩图片
	 * 
	 * @param sourceImage
	 *            待压缩图片
	 * @param width
	 *            压缩图片高度
	 * @param heigt
	 *            压缩图片宽度
	 */
	public static BufferedImage zoom(BufferedImage sourceImage, int width, int height) {
		int sourceHeight = sourceImage.getHeight();
		int sourceWidth = sourceImage.getWidth();

		if (sourceHeight < height || sourceWidth < width) {
			return sourceImage;
		}

		BufferedImage zoomImage = new BufferedImage(width, height, sourceImage.getType());
		Image image = sourceImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		Graphics gc = zoomImage.getGraphics();
		gc.setColor(Color.WHITE);
		gc.drawImage(image, 0, 0, null);
		return zoomImage;
	}

	private static BufferedImage doCrop(BufferedImage bufferedImage, int cropX, int cropY, int width, int height) {
		return bufferedImage.getSubimage(cropX, cropY, width, height);
	}

	private static String getImageSuffix(String path) {
		return path.substring(path.lastIndexOf(".") + 1);
	}

	public static String getImageFormatName(File file) throws IOException {
		String formatName = null;

		ImageInputStream iis = ImageIO.createImageInputStream(file);
		Iterator<ImageReader> imageReader = ImageIO.getImageReaders(iis);
		if (imageReader.hasNext()) {
			ImageReader reader = imageReader.next();
			formatName = reader.getFormatName();
		}

		return formatName;
	}

	private static void saveImageFile(BufferedImage bufferedImage, String suffix, String path) {
		File imageFile = new File(path);
		try {
			ImageIO.write(bufferedImage, suffix, imageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String pathIphone6p = "/Users/shawn/Pictures/iphone6p.jpg";

		File file = new File(pathIphone6p);
		System.out.println(file.getName());
		
	}

}
