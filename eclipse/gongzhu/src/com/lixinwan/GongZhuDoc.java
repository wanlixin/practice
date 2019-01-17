package com.lixinwan;

import java.util.Arrays;
import java.util.Random;

public class GongZhuDoc {

	int[][] mCards = new int[][] { new int[13], new int[13], new int[13], new int[13] };
	int[][] mUsedCards = new int[][] { new int[13], new int[13], new int[13], new int[13], new int[13] };
	int mUsedCardsIndex;
	int[][] mGotCards = new int[][] { new int[17], new int[17], new int[17], new int[17] };

	private static final Random sRandom = new Random();
	private Player[] mPlayers = new Player[4];
	private int mPlayerIndex;

	GongZhuDoc() {
		mPlayers[0] = new AiPlayer();
		mPlayers[1] = new AiPlayer();
		mPlayers[2] = new AiPlayer();
		mPlayers[3] = new AiPlayer();

		startGame();
	}

	void startGame() {
		for (int i = 0; i < 4; i++) {
			Arrays.fill(mCards[i], -1);
			Arrays.fill(mUsedCards[i], -1);
			Arrays.fill(mGotCards[i], -1);
		}
		Arrays.fill(mUsedCards[4], -1);
		mUsedCardsIndex = 0;

		for (int i = 51; i >= 0; i--) {
			int card = sRandom.nextInt(i + 1);
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

		mUsedCardsIndex = 0;
		mPlayerIndex = 0;
		mUsedCards[4][0] = sRandom.nextInt(4);
	}

	private static final int[] trimArray(int[] in) {
		int count = 0;
		for (int i : in) {
			if (i != -1) {
				count++;
			}
		}
		int[] out = new int[count];
		int index = 0;
		for (int i : in) {
			if (i != -1) {
				out[index++] = i;
			}
		}
		return out;
	}

	void tick() throws Exception {
		int firstPlayer = mUsedCards[4][mUsedCardsIndex];

		if (mPlayerIndex > 3) {
			int firstCardType = mUsedCards[firstPlayer][mUsedCardsIndex] / 13;
			int maxCardPlayer = -1;
			int maxCardValue = -1;
			for (int i = 0; i < 4; i++) {
				int card = mUsedCards[i][mUsedCardsIndex];
				if (card / 13 == firstCardType && card % 13 > maxCardValue) {
					maxCardValue = card % 13;
					maxCardPlayer = i;
				}
			}

			for (int i = 0; i < 4; i++) {
				int card = mUsedCards[i][mUsedCardsIndex];
				if (card == 9 || card == 23 || card == 47 || (card >= 26 && card < 39)) {
					mGotCards[maxCardPlayer][i] = card;
				}
			}
			Arrays.sort(mGotCards[maxCardPlayer]);

			if (++mUsedCardsIndex >= 13) {
				startGame();
				return;
			}

			mUsedCards[4][mUsedCardsIndex] = maxCardPlayer;
			mPlayerIndex = 0;

			return;
		}

		int player = firstPlayer + mPlayerIndex;
		if (player > 3) {
			player -= 4;
		}

		int[] cards = trimArray(mCards[player]);
		int[][] usedCards = new int[5][];
		int[][] gotCards = new int[4][];
		for (int i = 0; i < 4; i++) {
			int k = player + i;
			if (k > 3) {
				k -= 4;
			}
			usedCards[i] = trimArray(mUsedCards[k]);
			gotCards[i] = trimArray(mGotCards[k]);
		}
		usedCards[4] = new int[mUsedCardsIndex + 1];
		for (int i = 0; i <= mUsedCardsIndex; i++) {
			int first = mUsedCards[4][i] - player;
			if (first < 0) {
				first += 4;
			}
			usedCards[4][i] = first;
		}
		int currentCard = mPlayers[player].tick(cards, mUsedCardsIndex, usedCards, gotCards);

		Arrays.sort(mCards[player]);
		int currentCardIndex = Arrays.binarySearch(mCards[player], currentCard);
		if (currentCardIndex < 0) {
			throw new Exception("card not found");
		}
		if (mPlayerIndex != 0) {
			int firstCardType = mUsedCards[firstPlayer][mUsedCardsIndex] / 13;

			if (currentCard / 13 != firstCardType) {
				for (int card : mCards[player]) {
					if (card != -1 && card / 13 == firstCardType) {
						throw new Exception("you cann't throw this card");
					}
				}
			}
		}

		mCards[player][currentCardIndex] = -1;
		mUsedCards[player][mUsedCardsIndex] = currentCard;
		mPlayerIndex++;
	}

	public interface Player {
		int tick(int[] cards, int usedCardsIndex, int[][] usedCards, int[][] gotCards);
	}

	class AiPlayer implements Player {
		@Override
		public int tick(int[] cards, int usedCardsIndex, int[][] usedCards, int[][] gotCards) {
			int firstPlayer = usedCards[4][usedCardsIndex];
			if (firstPlayer != 0) {
				int firstCard = usedCards[firstPlayer][usedCardsIndex];
				int firstCardType = firstCard / 13;
				for (int card : cards) {
					if (card / 13 == firstCardType) {
						return card;
					}
				}
			}
			return cards[0];
		}
	}
}
