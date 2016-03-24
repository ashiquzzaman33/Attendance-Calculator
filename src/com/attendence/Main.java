package com.attendence;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRenderedImage;
import java.io.File;
import java.io.FileWriter;
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

import com.exports.PdfMaker;
import com.exports.TxtMaker;
import com.lowagie.text.DocumentException;
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
	public final static int OUTPUT_IMAGE_WIDTH = 2000;
	public final static int OUTPUT_IMAGE_HEIGHT = 3200;
	private int windowHeight = 600;

	private Rectangle sheetHeaderRectangle = new Rectangle(100, 90, 1820, 340);

	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem openMenuItem;
	private JMenuItem closeMenuItem;
	private JMenuItem mntmExit;
	private JMenu exportMenu;
	private JMenuItem exportAsPdf;
	private JMenuItem exportAsPlainText;
	private BufferedImage bufferedImage;
	private JMenu mnOperations;
	private JMenuItem mntmProcess;
	private ImagePanel imagePanel;
	public static final int FIRST_Y_COORDINATE = 127;
	public static final int HEIGHT_OF_EACH_ROW = 107;

	public BufferedImage sheetHeader;

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

						// sheetHeader = ImageUtility.cropImage(
						// ImageUtility.getScaledImage(outputImageWidth,
						// outputImageHeight, ImageIO.read(file)),
						// sheetHeaderRectangle);

						// ImageIO.write(ImageUtility.cropImage(
						// ImageUtility.getScaledImage(outputImageWidth,
						// outputImageHeight, ImageIO.read(file)),
						// IMAGE_RECTANGLE), "jpg", new File("Scaled.jpg"));

						// bufferedImage =
						// ImageUtility.getScaledImage(outputImageWidth,
						// outputImageHeight,
						// ImageUtility.cropImage(ImageIO.read(file),
						// IMAGE_RECTANGLE));
						bufferedImage = ImageIO.read(file);

					} catch (IOException e1) {
						e1.printStackTrace();
					}

					imagePanel.setImage(bufferedImage);

				} else {
					JOptionPane.showMessageDialog(Main.this, "Please Select JPG or PNG image.");
				}
			}
		});
		closeMenuItem = new JMenuItem("Close");
		closeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imagePanel.setImage(null);
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
					System.out.println(("Elaps Time: " + (stopTime - startTime) / 1000000000L + " Seconds"));

				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});
		mnOperations.add(mntmProcess);

		// Export Menu
		exportMenu = new JMenu("Export");
		exportAsPdf = new JMenuItem("As PDF");
		exportAsPdf.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (studentInfo.size() == 0) {
					JOptionPane.showMessageDialog(Main.this, "Please Process First.");

				} else {
					JFileChooser chooser = new JFileChooser();
					chooser.showSaveDialog(Main.this);
					File file = chooser.getSelectedFile();
					if (file != null) {

						try {
							PdfMaker maker = new PdfMaker(studentInfo, sheetHeader, file.getAbsolutePath() + ".pdf");
							maker.makePdf();
						} catch (DocumentException es) {
							es.printStackTrace();
						} catch (IOException e1) {
							e1.printStackTrace();
						}

					} else {
						JOptionPane.showMessageDialog(Main.this, "Please Select A File.");
					}

				}
			}
		});

		exportAsPlainText = new JMenuItem("As Plain Text");
		exportAsPlainText.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (studentInfo.size() == 0) {
					JOptionPane.showMessageDialog(Main.this, "Please Process First.");

				} else {
					JFileChooser chooser = new JFileChooser();
					chooser.showSaveDialog(Main.this);
					File file = chooser.getSelectedFile();

					if (file != null) {
						TxtMaker maker = new TxtMaker(studentInfo, file.getAbsolutePath() + ".txt");
						maker.makeTxtFile();

					} else {
						JOptionPane.showMessageDialog(Main.this, "Please Select A File.");
					}
				}
			}
		});

		exportMenu.add(exportAsPdf);
		exportMenu.addSeparator();
		exportMenu.add(exportAsPlainText);

		menuBar.add(exportMenu);

	}

	public void processImage() throws IOException {

		if (bufferedImage == null) {
			JOptionPane.showMessageDialog(Main.this, "Please Add Attendance Sheet");
		} else {
			bufferedImage = imagePanel.getCropedImage();
			if (bufferedImage == null) {
				JOptionPane.showMessageDialog(Main.this, "Please Select Four Coorner Point.");

			} else {
				
				ImageIO.write(bufferedImage, "jpg", new File("Fixed.jpg"));
				Rectangle c = new Rectangle(0, FIRST_Y_COORDINATE, bufferedImage.getWidth(),
						bufferedImage.getHeight() - FIRST_Y_COORDINATE);

				bufferedImage = ImageUtility.getScaledImage(OUTPUT_IMAGE_WIDTH, OUTPUT_IMAGE_HEIGHT,
						ImageUtility.cropImage(bufferedImage, c));

				splitStudentInfo();
				imagePanel.setImage(bufferedImage);
				imagePanel.setProcessed();

			}

		}

	}

	private void splitStudentInfo() {
		int pos = 0;
		for (int i = 0; i < 30; i++) {
			BufferedImage bf = new BufferedImage(bufferedImage.getWidth(), HEIGHT_OF_EACH_ROW, bufferedImage.getType());
			Rectangle r = new Rectangle(0, pos, bufferedImage.getWidth(),
					Math.min(HEIGHT_OF_EACH_ROW, bufferedImage.getHeight() - pos));

			bf = ImageUtility.cropImage(bufferedImage, r);

			studentInfo.add(new StudentInfo(bf, i));
			pos += HEIGHT_OF_EACH_ROW;
		}
	}

	public static void main(String[] args) throws IOException {
		new Main();
	}

}
