import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import com.utility.ImageUtility;

public class Main extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Margin form Four side

	public static Point[] COORNER_COORDINATES = { new Point(106, 425), new Point(1715, 425), new Point(1715, 2638),
			new Point(106, 2638) };
	public static Rectangle IMAGE_RECTANGLE = new Rectangle(COORNER_COORDINATES[0].x, COORNER_COORDINATES[0].y,
			COORNER_COORDINATES[1].x - COORNER_COORDINATES[0].x, COORNER_COORDINATES[3].y - COORNER_COORDINATES[0].y);
	private int imageWidth = 900;
	private int imageHeight = 1400;
	private int outputImageWidth = 2000;
	private int outputImageHeight = 3200;
	private int windowHeight = 600;
	public static int MARKER_RADIUS = 20;
	private double scaledFactorX;
	private double scaledFactorY;

	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem openMenuItem;
	private JMenuItem closeMenuItem;
	private JMenuItem mntmExit;
	private BufferedImage bufferedImage;
	private JMenu mnOperations;
	private JMenuItem mntmProcess;
	private ImagePanel imagePanel;
	public static final int FIRST_Y_COORDINATE = 127;
	public static final int HEIGHT_OF_EACH_ROW = 107;
	public Dimension screenSize = new Dimension(900, 1400);

	private ArrayList<StudentInfo> studentInfo = new ArrayList<>();

	public Main() throws IOException {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(imageWidth + 20, windowHeight);
		setResizable(false);
		setLocationRelativeTo(null);

		setJMenu();

		imagePanel = new ImagePanel(this, null, imageWidth, imageHeight);
		JScrollPane scPane = new JScrollPane(imagePanel);

		getContentPane().add(scPane);

		setVisible(true);
	}

	public void setJMenu() {
		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");

		openMenuItem = new JMenuItem("Open");
		openMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser("Choose Roll Sheet");
				chooser.showOpenDialog(Main.this);
				File file = chooser.getSelectedFile();
				if (file != null && file.getName().endsWith(".jpg") || file.getName().endsWith(".png")) {
					try {

						bufferedImage = ImageUtility.getScaledImage(outputImageWidth, outputImageHeight,
								ImageUtility.cropImage(ImageIO.read(file), IMAGE_RECTANGLE));

					} catch (IOException e1) {
						e1.printStackTrace();
					}

					imagePanel.setImage(screenSize.width, screenSize.height, bufferedImage);

				} else {
					JOptionPane.showMessageDialog(Main.this, "Please Select JPG or PNG image.");
				}
			}
		});
		closeMenuItem = new JMenuItem("Close");
		closeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imagePanel.setImage(0, 0, null);
			}
		});

		fileMenu.add(openMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(closeMenuItem);

		menuBar.add(fileMenu);

		mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		fileMenu.add(mntmExit);

		setJMenuBar(menuBar);

		mnOperations = new JMenu("Operations");
		menuBar.add(mnOperations);

		mntmProcess = new JMenuItem("Process");
		mntmProcess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					long startTime = System.nanoTime();
					processImage();

					long stopTime = System.nanoTime();
					System.out.println(("Elaps Time: "+(stopTime - startTime)/1000000000L+" Seconds"));

				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});
		mnOperations.add(mntmProcess);

	}

	public void processImage() throws IOException {

		Rectangle c = new Rectangle(0, FIRST_Y_COORDINATE, bufferedImage.getWidth(),
				bufferedImage.getHeight() - FIRST_Y_COORDINATE);
		bufferedImage = ImageUtility.getScaledImage(outputImageWidth, outputImageHeight,
				ImageUtility.cropImage(bufferedImage, c));

		int pos = 0;
		for (int i = 0; i < 30; i++) {
			BufferedImage bf = new BufferedImage(bufferedImage.getWidth(), HEIGHT_OF_EACH_ROW, bufferedImage.getType());
			Rectangle r = new Rectangle(0, pos, bufferedImage.getWidth(),
					Math.min(HEIGHT_OF_EACH_ROW, bufferedImage.getHeight() - pos));

			bf = ImageUtility.cropImage(bufferedImage, r);

			studentInfo.add(new StudentInfo(bf, i));
			pos += HEIGHT_OF_EACH_ROW;
		}

		imagePanel.setImage(screenSize.width, screenSize.height, bufferedImage);

		for (int i = 0; i < studentInfo.size(); i++) {

			String fileP = (i + 1) + "";
			System.out.print(studentInfo.get(i).getName() + "  ");
			System.out.println(studentInfo.get(i).getRoll());

			// ImageIO.write(studentInfo.get(i).getNameImage(), "jpg", new
			// File(fileP + "_name.jpg"));
			// ImageIO.write(studentInfo.get(i).getRoll(), "jpg", new File(fileP
			// + "_roll.jpg"));

		}

		System.out.println("Complete");
	}

	public static void main(String[] args) throws IOException {
		long startTime = System.nanoTime();
		new Main();
		long stopTime = System.nanoTime();
		System.out.println(stopTime - startTime);
		

	}

	public void setScaledFactor(double x, double y) {
		scaledFactorX = x;
		scaledFactorY = y;
	}

}
