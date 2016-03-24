package com.attendence;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.JPanel;

import com.utility.Coordinate;
import com.utility.ImageUtility;

public class ImagePanel extends JPanel {

	private Image image;
	private Main main;

	public ImagePanel(Main main, Image image, int width, int height) {
		this.main = main;
		setLayout(new BorderLayout());
		this.image = image;
		setPreferredSize(new Dimension(width, height));

	}

	public void setImage(int width, int height, BufferedImage image) {

		this.image = ImageUtility.getScaledImage(width, height, image);
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		if (image != null) {
			g2.drawImage(image, 0, 0, null);
		}

	}
}
