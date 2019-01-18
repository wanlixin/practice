package com.liuying;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.liuying.Utils.CardType;

public class Utils {

	private static Card sheepCard = new Card(CardType.FANGKUAI.toString(), 11);
	private static Card pigCard = new Card(CardType.HEITAO.toString(), 12);
	private static Card doubleCard = new Card(CardType.MEIHUA.toString(), 10);

	public static ArrayList<Card> heitaoTableArray = new ArrayList<>();
	public static ArrayList<Card> hongtaoTableArray = new ArrayList<>();
	public static ArrayList<Card> meihuaTableArray = new ArrayList<>();
	public static ArrayList<Card> fangkuaiTableArray = new ArrayList<>();

	public static ArrayList<Card> heitaoHandArray = new ArrayList<>();
	public static ArrayList<Card> hongtaoHandArray = new ArrayList<>();
	public static ArrayList<Card> meihuaHandArray = new ArrayList<>();
	public static ArrayList<Card> fangkuaiHandArray = new ArrayList<>();

	public static ArrayList<Card> smallCards = new ArrayList<>();

	public enum CardType {
		HEITAO, HONGTAO, MEIHUA, FANGKUAI
	}

	static Comparator<Card> comparatorCardLargeToSmall = new Comparator<Card>() {
		@Override
		public int compare(Card c1, Card c2) {
			if (c1.getPoint() >= c2.getPoint()) {
				return -1;
			} else {
				return 1;
			}
		}
	};

	static Comparator<Card> comparatorCardSmallToLarge = new Comparator<Card>() {
		@Override
		public int compare(Card c1, Card c2) {
			if (c1.getPoint() < c2.getPoint()) {
				return -1;
			} else {
				return 1;
			}
		}
	};

	static Comparator<Card> comparatorSelectCardToSend = new Comparator<Card>() {
		@Override
		public int compare(Card c1, Card c2) {
			return (c1.Percentage == c2.Percentage) ? (c1.count - c2.count) : (int) (c2.Percentage - c1.Percentage);

//			if (c1.Percentage > c2.Percentage)
//			{
//				return -1;
//			} 
//			if(c1.Percentage == c2.Percentage)
//			{
//				if(c1.count < c2.count)
//				{
//					return -1;
//				}
//				else
//				{
//					return 1;
//				}
//			}
//			
//			return 1;			
		}
	};

	private static ArrayList<Card> findCardInOther(CardType cardType, ArrayList<Card> cardOnTable,
			ArrayList<Card> cardInHand) {

		ArrayList<Card> cardInOther = new ArrayList<>();
		for (int i = 0; i < 13; i++) {
			Card c = new Card(cardType.toString(), (i + 2));
			if (!cardOnTable.contains(c) && !cardInHand.contains(c)) {
				cardInOther.add(c);
			}

		}
//		boolean exist = false;
//		for (int i = 0; i < 13; i++) {
//			
//			exist = false;
//			
//			for(Card c1 : cardOnTable)
//			{
//				if(c1.getPoint() == (i+2))
//				{
//					exist = true;
//					break;
//				}
//			}
//			
//			if(!exist)
//			{
//				for(Card c2 : cardInHand)
//				{
//					if(c2.getPoint() == (i+2))
//					{
//						exist = true;
//						break;
//					}
//				}
//			}
//			
//			if(exist == false)
//			{
//				Card c = new Card(cardType.toString(), i + 2);
//				cardInOther.add(c);
//				
//			}		
//		}

		cardInOther.sort(comparatorCardLargeToSmall);
		return cardInOther;
	}

	public static void cardArrange(Card[] cardOnTable, Card[] cardInHand) {

		heitaoTableArray.clear();
		hongtaoTableArray.clear();
		meihuaTableArray.clear();
		fangkuaiTableArray.clear();
		heitaoHandArray.clear();
		hongtaoHandArray.clear();
		meihuaHandArray.clear();
		fangkuaiHandArray.clear();

		smallCards.clear();

		// classify and range the card
		for (Card c : cardOnTable) {
			if (c.getColor() == CardType.HEITAO.toString()) {
				heitaoTableArray.add(c);
			}
			if (c.getColor() == CardType.HONGTAO.toString()) {
				hongtaoTableArray.add(c);
			}
			if (c.getColor() == CardType.MEIHUA.toString()) {
				meihuaTableArray.add(c);
			}
			if (c.getColor() == CardType.FANGKUAI.toString()) {
				fangkuaiTableArray.add(c);
			}
		}

		for (Card c : cardInHand) {
			if (c.getColor() == CardType.HEITAO.toString()) {
				heitaoHandArray.add(c);
			}
			if (c.getColor() == CardType.HONGTAO.toString()) {
				hongtaoHandArray.add(c);
			}
			if (c.getColor() == CardType.MEIHUA.toString()) {
				meihuaHandArray.add(c);
			}
			if (c.getColor() == CardType.FANGKUAI.toString()) {
				fangkuaiHandArray.add(c);
			}
		}

		heitaoTableArray.sort(comparatorCardLargeToSmall);
		hongtaoTableArray.sort(comparatorCardLargeToSmall);
		meihuaTableArray.sort(comparatorCardLargeToSmall);
		fangkuaiTableArray.sort(comparatorCardLargeToSmall);

		heitaoHandArray.sort(comparatorCardLargeToSmall);
		hongtaoHandArray.sort(comparatorCardLargeToSmall);
		meihuaHandArray.sort(comparatorCardLargeToSmall);
		fangkuaiHandArray.sort(comparatorCardLargeToSmall);

		if (!heitaoHandArray.isEmpty()) {
			smallCards.add(heitaoHandArray.get(heitaoHandArray.size() - 1));
			smallCards.get(smallCards.size() - 1).count = heitaoHandArray.size();
			smallCards.get(smallCards.size() - 1).Percentage = compareCardWithOther(CardType.HEITAO);
		}

		if (!hongtaoHandArray.isEmpty()) {
			smallCards.add(hongtaoHandArray.get(hongtaoHandArray.size() - 1));
			smallCards.get(smallCards.size() - 1).count = hongtaoHandArray.size();
			smallCards.get(smallCards.size() - 1).Percentage = compareCardWithOther(CardType.HONGTAO);
		}

		if (!meihuaHandArray.isEmpty()) {
			smallCards.add(meihuaHandArray.get(meihuaHandArray.size() - 1));
			smallCards.get(smallCards.size() - 1).count = meihuaHandArray.size();
			smallCards.get(smallCards.size() - 1).Percentage = compareCardWithOther(CardType.MEIHUA);
		}

		if (!fangkuaiHandArray.isEmpty()) {
			smallCards.add(fangkuaiHandArray.get(fangkuaiHandArray.size() - 1));
			smallCards.get(smallCards.size() - 1).count = fangkuaiHandArray.size();
			smallCards.get(smallCards.size() - 1).Percentage = compareCardWithOther(CardType.FANGKUAI);
		}

	}

	public static Card FirstScan(Card[] cardOnTable, Card[] cardInHand, int salePig, int saleSheep, int saleRed,
			int saleDouble) {

		cardArrange(cardOnTable, cardInHand);

		// check heitao 如果有比Q 大的牌，Q 被别人卖了，并且是首轮，那么出最大的牌
		if (heitaoHandArray.isEmpty() == false && salePig == 1 && heitaoTableArray.isEmpty()
				&& heitaoHandArray.get(0).getPoint() > pigCard.getPoint()) {
			return heitaoHandArray.get(0);
		}

		// check meihua 最大的牌比10 大，并且 10 卖了，且是首轮，那么出最大的牌
		if (meihuaHandArray.isEmpty() == false && saleDouble != 0 && meihuaTableArray.isEmpty()
				&& meihuaHandArray.get(0).getPoint() > doubleCard.getPoint()) {
			return meihuaHandArray.get(0);
		}

		// check fangkuai in otherone's hand
		ArrayList<Card> fangkuaiInOtherOne = new ArrayList<>();
		fangkuaiInOtherOne = findCardInOther(CardType.FANGKUAI, fangkuaiTableArray, fangkuaiHandArray);

		// 牌里面有J，算J 是否是所有人手里最大的牌，如果是，出牌
		if (fangkuaiHandArray.contains(sheepCard)) {

			if (fangkuaiInOtherOne.isEmpty() || fangkuaiInOtherOne.get(0).getPoint() < sheepCard.getPoint()) {
				if (saleSheep != 0 && fangkuaiTableArray.isEmpty()) {

				} else {
					return sheepCard;
				}
			}

		}

		// 牌里面有比J 大的牌，并且J在别人手里，没有卖，下面的牌少于7张，那么出最大的牌
		if (!fangkuaiInOtherOne.isEmpty() && !fangkuaiHandArray.isEmpty()
				&& fangkuaiHandArray.get(0).getPoint() > sheepCard.getPoint() && fangkuaiInOtherOne.contains(sheepCard)
				&& saleSheep == 0 && fangkuaiTableArray.size() < 7) {
			return fangkuaiHandArray.get(0);
		}

		// 牌里面有比J 大的牌，J在别人手里，J 卖了，下面的牌大于1张 小于7张，那么出最大的牌
		if (!fangkuaiInOtherOne.isEmpty() && !fangkuaiHandArray.isEmpty()
				&& fangkuaiHandArray.get(0).getPoint() > sheepCard.getPoint() && fangkuaiInOtherOne.contains(sheepCard)
				&& saleSheep != 0 && fangkuaiTableArray.size() > 0 && fangkuaiTableArray.size() < 7) {
			return fangkuaiHandArray.get(0);
		}

		// TODO hongtao收全血的情况

		return null;

	}

	// 计算给定的花色最小的牌，别人的牌比它的大百分比。
	private static Double compareCardWithOther(CardType cardType) {
		int count = 0;
		ArrayList<Card> cardsInOtherOne = new ArrayList<>();
		ArrayList<Card> cardsInHand = new ArrayList<>();

		switch (cardType) {
		case FANGKUAI:
			cardsInOtherOne = findCardInOther(CardType.FANGKUAI, fangkuaiTableArray, fangkuaiHandArray);
			cardsInHand = fangkuaiHandArray;
			break;
		case HONGTAO:
			cardsInOtherOne = findCardInOther(CardType.HONGTAO, hongtaoTableArray, hongtaoHandArray);
			cardsInHand = hongtaoHandArray;
			break;
		case MEIHUA:
			cardsInOtherOne = findCardInOther(CardType.MEIHUA, meihuaTableArray, meihuaHandArray);
			cardsInHand = meihuaHandArray;
			break;
		case HEITAO:
			cardsInOtherOne = findCardInOther(CardType.HEITAO, heitaoTableArray, heitaoHandArray);
			cardsInHand = heitaoHandArray;
			break;
		}

		if (cardsInOtherOne.isEmpty()) {
			return Double.MIN_NORMAL;
		}

		if (cardsInHand.isEmpty()) {
			return Double.NaN;
		}

		for (Card c : cardsInOtherOne) {
			if (c.getPoint() > cardsInHand.get(cardsInHand.size() - 1).getPoint()) {
				count++;
			}
		}

		double percentiage = (double) count / (double) cardsInOtherOne.size();
		BigDecimal bd = new BigDecimal(percentiage);
		percentiage = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return percentiage;

	}

	// 第二轮扫描，计算每个花色最小的牌，别人有几张牌比它的大。选择概率最大的出牌，如果同样情况下选择本身牌数量少的出牌
	private static Card secondScan(Card[] cardOnTable, Card[] cardInHand, int salePig, int saleSheep, int saleRed,
			int saleDouble) {

		cardArrange(cardOnTable, cardInHand);
		smallCards.sort(comparatorSelectCardToSend);
		return smallCards.get(0);
	}

	// sale: 0 stand for didn't sale, sale: 1 stand for sale other, sale: 2 stand
	// for sale by yourself
	public static Card SendOutCard(Card[] cardOnTable, Card[] cardInHand, int salePig, int saleSheep, int saleRed,
			int saleDouble) {

		Card returnCard = null;

		// if only one card in the hand, then send out the card
		if (cardInHand.length == 1) {
			return cardInHand[0];
		}

		// first scan
		returnCard = FirstScan(cardOnTable, cardInHand, salePig, saleSheep, saleRed, saleDouble);

		// second scan
		if (returnCard == null) {
			returnCard = secondScan(cardOnTable, cardInHand, salePig, saleSheep, saleRed, saleDouble);
		}

		return returnCard;

	}

	enum Day {
		MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Card[] cardOnTable = new Card[] { new Card(CardType.FANGKUAI.toString(), 8),
				new Card(CardType.HONGTAO.toString(), 12), new Card(CardType.MEIHUA.toString(), 2),
				new Card(CardType.HEITAO.toString(), 5), new Card(CardType.FANGKUAI.toString(), 7),
				new Card(CardType.HEITAO.toString(), 9), new Card(CardType.HONGTAO.toString(), 10),
				new Card(CardType.MEIHUA.toString(), 6), new Card(CardType.FANGKUAI.toString(), 9),
				new Card(CardType.HONGTAO.toString(), 11), new Card(CardType.MEIHUA.toString(), 7),
				new Card(CardType.HEITAO.toString(), 12), new Card(CardType.FANGKUAI.toString(), 2),
				new Card(CardType.HEITAO.toString(), 3), new Card(CardType.HONGTAO.toString(), 3),
				new Card(CardType.MEIHUA.toString(), 14), };

		Card[] cardInHand = new Card[] { new Card(CardType.FANGKUAI.toString(), 3),
				new Card(CardType.HONGTAO.toString(), 2), new Card(CardType.MEIHUA.toString(), 10),
				new Card(CardType.HEITAO.toString(), 13), new Card(CardType.FANGKUAI.toString(), 6),
				new Card(CardType.HEITAO.toString(), 4), new Card(CardType.HONGTAO.toString(), 9),
				new Card(CardType.MEIHUA.toString(), 13), new Card(CardType.FANGKUAI.toString(), 10),
				new Card(CardType.HONGTAO.toString(), 5), new Card(CardType.MEIHUA.toString(), 9),
				new Card(CardType.HEITAO.toString(), 6), new Card(CardType.FANGKUAI.toString(), 11),
				new Card(CardType.HEITAO.toString(), 10), new Card(CardType.HONGTAO.toString(), 8),
				new Card(CardType.MEIHUA.toString(), 5), };

		Card c = SendOutCard(cardOnTable, cardInHand, 1, 1, 1, 1);

		Double d = (double) -1;
		if (d == -1) {
			System.out.println("d.toString().equals(\"-1\")");
		}

		System.out.println(Day.FRIDAY.toString());

		Card c1 = new Card("11", 3);
		Card c2 = new Card("22", 5);
		Card c3 = new Card("33", 1);

		ArrayList<Card> testArray = new ArrayList<>();
		testArray.add(c1);
		testArray.add(c2);
		testArray.add(c3);

		Card[] cardArray = testArray.toArray(new Card[3]);

		Card cc = FirstScan(cardArray, cardArray, 0, 0, 0, 0);

		for (int i = 0; i < testArray.size(); i++) {
			System.out.println(testArray.get(i).getPoint());
		}

		Comparator<Card> comparatorCard = new Comparator<Card>() {
			@Override
			public int compare(Card c1, Card c2) {
				if (c1.getPoint() >= c2.getPoint()) {
					return -1;
				} else {
					return 1;
				}
			}
		};

		testArray.sort(comparatorCard);

		for (int i = 0; i < testArray.size(); i++) {
			System.out.println(testArray.get(i).getPoint());
		}

	}

}
