package com.utility;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class ImageUtility {

	public static BufferedImage getScaledImage(int width, int height, BufferedImage src) {
		BufferedImage dst = new BufferedImage(width, height, src.getType());
		Graphics2D g = dst.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.drawImage(src, 0, 0, width, height, null);
		g.dispose();
		return dst;
	}

	public static BufferedImage cropImage(BufferedImage src, Rectangle coords) {

		BufferedImage dest = src.getSubimage(coords.x, coords.y, coords.width, coords.height);
		return dest;
	}
}
