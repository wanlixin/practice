package com.lixinwan;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

public class GongZhuView extends JComponent implements ActionListener, MouseListener {
	private static final long serialVersionUID = 1L;
	private static JTextArea STATUS = null;

	private Point[] mCardsLocation;
	private Point[] mGotCardsOffset = new Point[] { new Point(0, 50), new Point(40, 0), new Point(0, -50),
			new Point(-40, 0) };
	private Point[] mCardsOffset = new Point[] { new Point(20, 0), new Point(0, 26), new Point(-20, 0),
			new Point(0, -26) };

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

		int backgroundWidth = backgroundImage.getWidth(null);
		int backgroundHeight = backgroundImage.getHeight(null);
		int backHalfWidth = backImage.getWidth(null) / 2;
		int backHalfHeight = backImage.getHeight(null) / 2;

		mCardsLocation = new Point[] { new Point(backgroundWidth / 2, backgroundHeight - backHalfHeight - 10),
				new Point(backgroundWidth - backHalfWidth - 10, backgroundHeight / 2),
				new Point(backgroundWidth / 2, backHalfHeight + 10),
				new Point(backHalfWidth + 10, backgroundHeight / 2) };

		addMouseListener(this);
		Timer timer = new Timer(500, this);
		timer.start();
	}

	private Image loadImage(String path) throws IOException {
		return ImageIO.read(this.getClass().getResourceAsStream(path));
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(backgroundImage, 0, 0, this);

		for (int i = 0; i < 4; i++) {
			for (int k = 0; k < 2; k++) {
				int[] cards = (k == 0) ? gongZhuDoc.mCards[i] : gongZhuDoc.mGotCards[i];

				int count = 0;
				for (int card : cards) {
					if (card >= 0 && card < 52) {
						count++;
					}
				}

				if (count == 0) {
					continue;
				}

				int locationX = mCardsLocation[i].x;
				int locationY = mCardsLocation[i].y;
				if (k == 1) {
					locationX += mGotCardsOffset[i].x;
					locationY += mGotCardsOffset[i].y;
				}

				Point cardsOffset = mCardsOffset[i];
				int width = backImage.getWidth(null) + (count - 1) * Math.abs(cardsOffset.x);
				int height = backImage.getHeight(null) + (count - 1) * Math.abs(cardsOffset.y);

				int x = (cardsOffset.x < 0) ? (locationX + width / 2 - backImage.getWidth(null))
						: ((locationX - width / 2));
				int y = (cardsOffset.y < 0) ? (locationY + height / 2 - backImage.getHeight(null))
						: ((locationY - height / 2));

				for (int card : cards) {
					if (card < 0 || card > 51) {
						continue;
					}
					g.drawImage(cardImages[card], x, y, this);
					x += cardsOffset.x;
					y += cardsOffset.y;
				}
			}
		}

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

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (arg0.getClickCount() != 2 || arg0.getButton() != MouseEvent.BUTTON1) {
			return;
		}
		int clickedPlayer = -1;
		int clickedCard = -1;
		for (int i = 0; i < 4; i++) {
			for (int k = 0; k < 2; k++) {
				int[] cards = (k == 0) ? gongZhuDoc.mCards[i] : gongZhuDoc.mGotCards[i];

				int count = 0;
				for (int card : cards) {
					if (card >= 0 && card < 52) {
						count++;
					}
				}

				if (count == 0) {
					continue;
				}

				int locationX = mCardsLocation[i].x;
				int locationY = mCardsLocation[i].y;
				if (k == 1) {
					locationX += mGotCardsOffset[i].x;
					locationY += mGotCardsOffset[i].y;
				}

				Point cardsOffset = mCardsOffset[i];
				int width = backImage.getWidth(null) + (count - 1) * Math.abs(cardsOffset.x);
				int height = backImage.getHeight(null) + (count - 1) * Math.abs(cardsOffset.y);

				int x = (cardsOffset.x < 0) ? (locationX + width / 2 - backImage.getWidth(null))
						: ((locationX - width / 2));
				int y = (cardsOffset.y < 0) ? (locationY + height / 2 - backImage.getHeight(null))
						: ((locationY - height / 2));

				for (int card : cards) {
					if (card < 0 || card > 51) {
						continue;
					}
					if (arg0.getX() >= x && arg0.getX() < x + backImage.getWidth(null) && arg0.getY() >= y
							&& arg0.getY() < y + backImage.getHeight(null)) {
						clickedPlayer = i;
						clickedCard = card;
					}
					x += cardsOffset.x;
					y += cardsOffset.y;
				}
			}
		}
		if (clickedCard != -1) {
			try {
				gongZhuDoc.useCard(clickedPlayer, clickedCard);
				repaint();
			} catch (Exception e) {
				Toolkit.getDefaultToolkit().beep();
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
}
