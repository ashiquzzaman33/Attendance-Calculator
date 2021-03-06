package com.attendence;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.utility.ImageToText;
import com.utility.ImageUtility;

public class StudentInfo {
	private String name;
	private String roll;
	private ArrayList<BufferedImage> attendance = new ArrayList<>();
	private Rectangle rollRect = new Rectangle(106, 0, 178, 107);
	private Rectangle nameRect = new Rectangle(283, 0, 573, 107);

	private static int NAME_WIDTH = 574;
	private static int ROLL_WIDTH = 178;
	private static int ATTENDENCE_GRID_WIDTH = 115;
	private static int GRID_HEIGHT = Main.HEIGHT_OF_EACH_ROW;
	private Point attendenceGridULCorner = new Point(853, 0);
	private int outputImageHeight = 800;

	public StudentInfo(BufferedImage imageContainingStdInfo, int idx) {

		GRID_HEIGHT = imageContainingStdInfo.getHeight();
		rollRect = new Rectangle(103, 0, ROLL_WIDTH, imageContainingStdInfo.getHeight());
		nameRect = new Rectangle(280, 0, NAME_WIDTH, imageContainingStdInfo.getHeight());

		double rollRatio = (double) ROLL_WIDTH / imageContainingStdInfo.getHeight();
		double nameRatio = (double) NAME_WIDTH / imageContainingStdInfo.getHeight();

		roll = ImageToText.getText(ImageUtility.getScaledImage((int) (rollRatio * outputImageHeight), outputImageHeight,
				ImageUtility.cropImage(imageContainingStdInfo, rollRect)), true);

		name = ImageToText.getText(ImageUtility.getScaledImage((int) (nameRatio * outputImageHeight), outputImageHeight,
				ImageUtility.cropImage(imageContainingStdInfo, nameRect)), false);

		try {
			ImageIO.write(
					ImageUtility.getScaledImage((int) (rollRatio * outputImageHeight), outputImageHeight,
							ImageUtility.cropImage(imageContainingStdInfo, rollRect)),
					"jpg", new File(idx + "_roll.jpg"));
			ImageIO.write(ImageUtility.getScaledImage((int) (nameRatio * outputImageHeight), outputImageHeight,
					ImageUtility.cropImage(imageContainingStdInfo, nameRect)), "jpg", new File(idx+"_name.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int p = attendenceGridULCorner.x;
		for (int i = 0; i < 10; i++) {
			BufferedImage bf = new BufferedImage(ATTENDENCE_GRID_WIDTH, GRID_HEIGHT, imageContainingStdInfo.getType());
			Rectangle r = new Rectangle(p, attendenceGridULCorner.y,
					Math.min(ATTENDENCE_GRID_WIDTH, imageContainingStdInfo.getWidth() - p), GRID_HEIGHT);

			bf = ImageUtility.cropImage(imageContainingStdInfo, r);
			p += ATTENDENCE_GRID_WIDTH;
			attendance.add(bf);
		}
	}

	public String getName() {
		return name;
	}

	public String getRoll() {
		return roll;
	}

	public ArrayList<BufferedImage> getAttendance() {
		return attendance;
	}

}
