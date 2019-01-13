package com.lixinwan;

import java.util.Arrays;
import java.util.Random;

public class GongZhuDoc {

	protected Random random = new Random();

	protected int[][] mCards = new int[][] { new int[13], new int[13], new int[13], new int[13] };
	protected int[][] mUsedCards = new int[][] { new int[13], new int[13], new int[13], new int[13] };
	protected int[][] mGotCards = new int[][] { new int[17], new int[17], new int[17], new int[17] };
	protected int[] mCurrentCards = new int[4];

	public GongZhuDoc() {
		startGame();
	}

	protected void startGame() {
		for (int i = 0; i < 4; i++) {
			Arrays.fill(mCards[i], -1);
			Arrays.fill(mUsedCards[i], -1);
			Arrays.fill(mGotCards[i], -1);
		}

		Arrays.fill(mCurrentCards, -1);

		for (int i = 51; i >= 0; i--) {
			int card = random.nextInt(i + 1);
			for (int j = 0; j < 4; j++) {
				for (int k = 0; k < 13; k++) {
					if (card < 0) {
						break;
					}
					if (mCards[j][k] == -1) {
						if (card-- == 0) {
							mCards[j][k] = i;
						}
					}
				}
			}
		}

		for (int i = 0; i < 4; i++) {
			Arrays.sort(mCards[i]);
		}
	}

	int i = 0;

	protected void tick() {
		mCurrentCards[i] = -1;
		if (i++ == 3) {
			i = 0;
		}
		mCurrentCards[i] = 1;
	}
}
