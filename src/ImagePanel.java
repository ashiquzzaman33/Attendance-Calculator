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
	private ArrayList<Point> markerPosition = new ArrayList<>();
	private Main main;

	public ImagePanel(Main main, Image image, int width, int height) {
		this.main = main;
		setLayout(new BorderLayout());
		this.image = image;
		setPreferredSize(new Dimension(width, height));
		setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));

	}

	public void setImage(int width, int height, BufferedImage image) {

		markerPosition = new ArrayList<>();
		main.setScaledFactor(image.getWidth() / main.screenSize.getWidth(),
				image.getHeight() / main.screenSize.getHeight());
		this.image = ImageUtility.getScaledImage(width, height, image);
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		if (image != null) {
			g2.drawImage(image, 0, 0, null);

			for (int i = 0; i < markerPosition.size() && i < 4; i++) {
				if (markerPosition.get(i).getX() != -1 && markerPosition.get(i).getY() != -1) {

					g2.setColor(Color.RED);
					int X = (int) (markerPosition.get(i).getX() - Main.MARKER_RADIUS / 2);
					int Y = (int) markerPosition.get(i).getY() - Main.MARKER_RADIUS / 2;

					g2.fillOval(X, Y, Main.MARKER_RADIUS, Main.MARKER_RADIUS);
				}

			}
		}

	}
}
