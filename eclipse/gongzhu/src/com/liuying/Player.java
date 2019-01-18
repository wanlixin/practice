package com.liuying;

import java.util.Arrays;

import com.liuying.Utils.CardType;
import com.lixinwan.GongZhuDoc;

public class Player implements GongZhuDoc.Player {

	private static final String CardColor[] = new String[] { CardType.FANGKUAI.toString(), CardType.HEITAO.toString(),
			CardType.HONGTAO.toString(), CardType.MEIHUA.toString() };

	private static final Card toCard(int card) {
		return new Card(CardColor[card / 13], (card % 13 + 2));
	}

	private static final int fromCard(Card card) {
		for (int i = 0; i < 4; i++) {
			if (CardColor[i].equals(card.getColor())) {
				return i * 13 + card.getPoint() - 2;
			}
		}
		return -1;
	}

	@Override
	public int tick(int[] cards, int[][] usedCards, int[][] gotCards) {
		int usedCardsIndex = usedCards[4].length - 1;

		Card[] cardOnTable = new Card[usedCardsIndex * 4];
		for (int i = 0, k = 0; i < usedCardsIndex; i++) {
			for (int j = 0; j < 4; j++, k++) {
				cardOnTable[k] = toCard(usedCards[j][i]);
			}
		}

		Card[] cardInHand = new Card[cards.length];
		for (int i = 0; i < cards.length; i++) {
			cardInHand[i] = toCard(cards[i]);
		}

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

		Card card = Utils.SendOutCard(cardOnTable, cardInHand, 0, 0, 0, 0);

		return fromCard(card);
	}

	public static void main(String[] args) throws Exception {
		GongZhuDoc gongZhuDoc = new GongZhuDoc(new Player(), new GongZhuDoc.AiPlayer(), new GongZhuDoc.AiPlayer(),
				new GongZhuDoc.AiPlayer());
		for (int i = 0; i < 10000; i++) {
			while (gongZhuDoc.tick()) {
			}
		}
		System.out.println("scores: " + Arrays.toString(gongZhuDoc.getScores()));
	}
}
