package com.lixinwan;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class GongZhuView extends JComponent implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static JTextArea STATUS = null;

	public static String formatException(Throwable e) {
		return "发生异常: " + e.getMessage();
	}

	public static String formatException2(Throwable e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}

	public static void appendStatus(String status) {
		STATUS.append(status + "\n");
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame("拱猪大赛 v0.1");
				frame.setDefaultCloseOperation(3);
				frame.setSize(660, 525);
				frame.setLocationRelativeTo(null);

				STATUS = new JTextArea();
				STATUS.setEditable(false);

				frame.add(new JScrollPane(STATUS), "South");

				try {
					frame.add(new GongZhuView(), "Center");
				} catch (Throwable ex) {
					appendStatus(formatException(ex));
				}

				frame.setVisible(true);
			}
		});
	}

	private GongZhuDoc gongZhuDoc = new GongZhuDoc();
	private Image backgroundImage;
	private Image backImage;
	private Image[] cardImages;

	public GongZhuView() throws Exception {
		backgroundImage = loadImage("/res/bmp1/bjta2.bmp");
		backImage = loadImage("/res/bmp1/dpta2.bmp");
		cardImages = new Image[] { loadImage("/res/bmp2/12.bmp"), loadImage("/res/bmp2/13.bmp"),
				loadImage("/res/bmp2/14.bmp"), loadImage("/res/bmp2/15.bmp"), loadImage("/res/bmp2/16.bmp"),
				loadImage("/res/bmp2/17.bmp"), loadImage("/res/bmp2/18.bmp"), loadImage("/res/bmp2/19.bmp"),
				loadImage("/res/bmp2/1a.bmp"), loadImage("/res/bmp2/1b.bmp"), loadImage("/res/bmp2/1c.bmp"),
				loadImage("/res/bmp2/1d.bmp"), loadImage("/res/bmp2/11.bmp"), loadImage("/res/bmp2/22.bmp"),
				loadImage("/res/bmp2/23.bmp"), loadImage("/res/bmp2/24.bmp"), loadImage("/res/bmp2/25.bmp"),
				loadImage("/res/bmp2/26.bmp"), loadImage("/res/bmp2/27.bmp"), loadImage("/res/bmp2/28.bmp"),
				loadImage("/res/bmp2/29.bmp"), loadImage("/res/bmp2/2a.bmp"), loadImage("/res/bmp2/2b.bmp"),
				loadImage("/res/bmp2/2c.bmp"), loadImage("/res/bmp2/2d.bmp"), loadImage("/res/bmp2/21.bmp"),
				loadImage("/res/bmp2/32.bmp"), loadImage("/res/bmp2/33.bmp"), loadImage("/res/bmp2/34.bmp"),
				loadImage("/res/bmp2/35.bmp"), loadImage("/res/bmp2/36.bmp"), loadImage("/res/bmp2/37.bmp"),
				loadImage("/res/bmp2/38.bmp"), loadImage("/res/bmp2/39.bmp"), loadImage("/res/bmp2/3a.bmp"),
				loadImage("/res/bmp2/3b.bmp"), loadImage("/res/bmp2/3c.bmp"), loadImage("/res/bmp2/3d.bmp"),
				loadImage("/res/bmp2/31.bmp"), loadImage("/res/bmp2/42.bmp"), loadImage("/res/bmp2/43.bmp"),
				loadImage("/res/bmp2/44.bmp"), loadImage("/res/bmp2/45.bmp"), loadImage("/res/bmp2/46.bmp"),
				loadImage("/res/bmp2/47.bmp"), loadImage("/res/bmp2/48.bmp"), loadImage("/res/bmp2/49.bmp"),
				loadImage("/res/bmp2/4a.bmp"), loadImage("/res/bmp2/4b.bmp"), loadImage("/res/bmp2/4c.bmp"),
				loadImage("/res/bmp2/4d.bmp"), loadImage("/res/bmp2/41.bmp") };
		Timer timer = new Timer(500, this);
		timer.start();
	}

	private Image loadImage(String path) throws IOException {
		return ImageIO.read(this.getClass().getResourceAsStream(path));
	}

	private void drawCards(int[] cards, Graphics g, int x, int y, boolean vertical, boolean add, boolean showBack) {
		int count = 0;
		int[] cards2 = new int[52];
		for (int card : cards) {
			if (card >= 0 && card < 52) {
				cards2[count++] = card;
			}
		}

		int step = vertical ? (add ? 13 : -13) : (add ? 10 : -10);

		if (vertical) {
			y -= count * step;
		} else {
			x -= count * step;
		}

		for (int i = 0; i < count; i++) {
			g.drawImage(showBack ? backImage : cardImages[cards2[i]], x, y, this);

			if (vertical) {
				y += step * 2;
			} else {
				x += step * 2;
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(backgroundImage, 0, 0, this);

		drawCards(gongZhuDoc.mCards[0], g, 300, 360, false, true, false);
		drawCards(gongZhuDoc.mCards[1], g, 560, 180, true, true, false);
		drawCards(gongZhuDoc.mCards[2], g, 290, 10, false, false, false);
		drawCards(gongZhuDoc.mCards[3], g, 10, 200, true, false, false);

		drawCards(gongZhuDoc.mGotCards[0], g, 300, 420, false, true, false);
		drawCards(gongZhuDoc.mGotCards[1], g, 600, 180, true, true, false);
		drawCards(gongZhuDoc.mGotCards[2], g, 290, -50, false, false, false);
		drawCards(gongZhuDoc.mGotCards[3], g, -40, 200, true, false, false);

		int usedCardsIndex = gongZhuDoc.mUsedCardsIndex;
		if (gongZhuDoc.mUsedCards[0][usedCardsIndex] != -1) {
			g.drawImage(cardImages[gongZhuDoc.mUsedCards[0][usedCardsIndex]], 290, 250, this);
		}
		if (gongZhuDoc.mUsedCards[1][usedCardsIndex] != -1) {
			g.drawImage(cardImages[gongZhuDoc.mUsedCards[1][usedCardsIndex]], 380, 180, this);
		}
		if (gongZhuDoc.mUsedCards[2][usedCardsIndex] != -1) {
			g.drawImage(cardImages[gongZhuDoc.mUsedCards[2][usedCardsIndex]], 290, 120, this);
		}
		if (gongZhuDoc.mUsedCards[3][usedCardsIndex] != -1) {
			g.drawImage(cardImages[gongZhuDoc.mUsedCards[3][usedCardsIndex]], 200, 180, this);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			gongZhuDoc.tick();
			repaint();
		} catch (Throwable ex) {
			appendStatus(formatException(ex));
		}
	}
}
