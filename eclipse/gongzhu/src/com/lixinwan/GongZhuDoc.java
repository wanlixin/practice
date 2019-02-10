package com.lixinwan;

import java.util.Arrays;
import java.util.Random;

public class GongZhuDoc {
	int[][] mCards = new int[][] { new int[13], new int[13], new int[13], new int[13] };
	int[][] mUsedCards = new int[][] { new int[13], new int[13], new int[13], new int[13], new int[13] };
	int mUsedCardsIndex;
	int[][] mGotCards = new int[][] { new int[17], new int[17], new int[17], new int[17] };
	int[] mScores = new int[4];

	private static final Random sRandom = new Random();
	private Player[] mPlayers;
	private int mPlayerIndex;

	public GongZhuDoc(Player player0, Player player1, Player player2, Player player3) {
		mPlayers = new Player[] { player0, player1, player2, player3 };
		startGame();
	}

	public int[] getScores() {
		return mScores.clone();
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
							if (i == 39) {
								mUsedCards[4][0] = j;
							}
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

	private void useCard2(int player, int card) throws Exception {
		int firstPlayer = mUsedCards[4][mUsedCardsIndex];

		Arrays.sort(mCards[player]);
		int currentCardIndex = Arrays.binarySearch(mCards[player], card);
		if (currentCardIndex < 0) {
			throw new Exception("card not found");
		}
		if (mPlayerIndex != 0) {
			int firstCardType = mUsedCards[firstPlayer][mUsedCardsIndex] / 13;

			if (card / 13 != firstCardType) {
				for (int c : mCards[player]) {
					if (c != -1 && c / 13 == firstCardType) {
						throw new Exception("you cann't throw this card");
					}
				}
			}
		}

		mCards[player][currentCardIndex] = -1;
		mUsedCards[player][mUsedCardsIndex] = card;
		mPlayerIndex++;
	}

	public boolean tick() throws Exception {
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
				boolean[] slots = new boolean[52];
				for (int i = 0; i < 4; i++) {
					Arrays.fill(slots, false);
					for (int card : mGotCards[i]) {
						if (card != -1) {
							slots[card] = true;
						}
					}
					int score = 0;
					boolean gotBlood = false;
					boolean gotAllBlood = true;
					for (int j = 26; j < 39; j++) {
						if (slots[j]) {
							if (j > 33) {
								score -= (j - 33) * 10;
							} else if (j > 28) {
								score -= 10;
							}
							gotBlood = true;
						} else {
							gotAllBlood = false;
						}
					}
					if (gotAllBlood) {
						score = -score;
					}
					boolean gotAllCard = gotAllBlood && slots[9] && slots[23] && slots[47];
					if (slots[9]) {
						score += 100;
					}
					if (slots[23]) {
						score += (gotAllCard ? 100 : -100);
					}
					if (slots[47]) {
						if (gotBlood || slots[9] || slots[23]) {
							score *= 2;
						} else {
							score += 50;
						}
					}
					mScores[i] += score;
				}

				startGame();
				return false;
			}

			mUsedCards[4][mUsedCardsIndex] = maxCardPlayer;
			mPlayerIndex = 0;

			return true;
		}

		int player = firstPlayer + mPlayerIndex;
		if (player > 3) {
			player -= 4;
		}

		if (mPlayers[player] == null) {
			return true;
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
		int currentCard = mPlayers[player].tick(cards, usedCards, gotCards);

		useCard2(player, currentCard);

		return true;
	}

	void useCard(int player, int card) throws Exception {
		if (mPlayers[player] != null) {
			throw new Exception("this is a ai player");
		}

		int firstPlayer = mUsedCards[4][mUsedCardsIndex];
		int currentPlayer = firstPlayer + mPlayerIndex;
		if (currentPlayer > 3) {
			currentPlayer -= 4;
		}

		if (currentPlayer != player) {
			throw new Exception("it is not your turn");
		}

		useCard2(player, card);
	}

	public interface Player {
		int tick(int[] cards, int[][] usedCards, int[][] gotCards);
	}

	public static class AiPlayer implements Player {
		private static boolean haveCard(int theCard, int[] cards) {
			for (int card : cards) {
				if (card == theCard) {
					return true;
				}
			}
			return false;
		}

		private static int getMaxCard(int type, int[] cards, int max) {
			int maxCard = -1;
			for (int card : cards) {
				if (card / 13 == type && card < max && maxCard < card) {
					maxCard = card;
				}
			}
			if (maxCard == -1) {
				for (int card : cards) {
					if (card / 13 == type && maxCard < card) {
						maxCard = card;
					}
				}
			}
			return maxCard;
		}

		private static int getMinCard(int type, int[] cards) {
			int minCard = 100;
			for (int card : cards) {
				if (card / 13 == type && card < minCard) {
					minCard = card;
				}
			}
			return (minCard == 100) ? -1 : minCard;
		}

		@Override
		public int tick(int[] cards, int[][] usedCards, int[][] gotCards) {
			int usedCardsIndex = usedCards[4].length - 1;
			int firstPlayer = usedCards[4][usedCardsIndex];
			if (firstPlayer != 0) {
				int firstCard = usedCards[firstPlayer][usedCardsIndex];
				int firstCardType = firstCard / 13;
				if (firstCardType == 0) {
					int maxCard = getMaxCard(firstCardType, cards, 100);
					if (maxCard != -1) {
						return maxCard;
					}
				} else if (firstCardType == 1) {
					int maxCard = getMaxCard(firstCardType, cards, 23);
					if (maxCard != -1) {
						return maxCard;
					}
				} else if (firstCardType == 2) {
					int minCard = getMinCard(firstCardType, cards);
					if (minCard != -1) {
						return minCard;
					}
				} else if (firstCardType == 3) {
					int maxCard = getMaxCard(firstCardType, cards, 47);
					if (maxCard != -1) {
						return maxCard;
					}
				}
				if (haveCard(23, cards)) {
					return 23;
				}
				if (haveCard(47, cards)) {
					return 47;
				}
				int maxCard = getMaxCard(2, cards, 100);
				if (maxCard != -1) {
					return maxCard;
				}
			}
			return cards[0];
		}
	}
}
