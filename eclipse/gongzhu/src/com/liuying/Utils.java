package com.liuying;

import java.util.ArrayList;
import java.util.Comparator;

public class Utils {

	private static final String HongTao = "hongtao";
	private static final String MeiHua = "meihua";
	private static Card sheepCard = new Card(myCards.FANGKUAI.toString(), 11);
	private static Card pigCard = new Card(myCards.HEITAO.toString(), 12);
	private static Card doubleCard = new Card(MeiHua, 10);

	private static ArrayList<Card> heitaoTableArray = new ArrayList<>();
	private static ArrayList<Card> hongtaoTableArray = new ArrayList<>();
	private static ArrayList<Card> meihuaTableArray = new ArrayList<>();
	private static ArrayList<Card> fangkuaiTableArray = new ArrayList<>();

	private static ArrayList<Card> heitaoHandArray = new ArrayList<>();
	private static ArrayList<Card> hongtaoHandArray = new ArrayList<>();
	private static ArrayList<Card> meihuaHandArray = new ArrayList<>();
	private static ArrayList<Card> fangkuaiHandArray = new ArrayList<>();
	
	enum myCards {
		HEITAO, HONGTAO, MEIHUA,FANGKUAI
	}
	
	
	private static ArrayList<Card> findCardInOther(myCards cardType, ArrayList<Card> cardOnTable, ArrayList<Card> cardInHand)
	{

		ArrayList<Card> cardInOther = new ArrayList<>();
		for (int i = 0; i < 13; i++) {
			Card c = new Card(cardType.toString(), i + 2);
			if (!cardOnTable.contains(c) && !cardInHand.contains(c)) {
				cardInOther.add(c);
			}
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
		
		cardInOther.sort(comparatorCard);	
		return cardInOther;
	}
	
	public static void cardArrange(Card[] cardOnTable, Card[] cardInHand)
	{

		// classify and range the card
		for (Card c : cardOnTable) {
			if (c.getColor() == myCards.HEITAO.toString()) {
				heitaoTableArray.add(c);
			}
			if (c.getColor() == myCards.HONGTAO.toString()) {
				hongtaoTableArray.add(c);
			}
			if (c.getColor() == MeiHua) {
				meihuaTableArray.add(c);
			}
			if (c.getColor() == myCards.FANGKUAI.toString()) {
				fangkuaiTableArray.add(c);
			}
		}

		for (Card c : cardInHand) {
			if (c.getColor() == myCards.HEITAO.toString()) {
				heitaoHandArray.add(c);
			}
			if (c.getColor() == HongTao) {
				hongtaoHandArray.add(c);
			}
			if (c.getColor() == MeiHua) {
				meihuaHandArray.add(c);
			}
			if (c.getColor() == myCards.FANGKUAI.toString()) {
				fangkuaiHandArray.add(c);
			}
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

		heitaoTableArray.sort(comparatorCard);
		hongtaoTableArray.sort(comparatorCard);
		meihuaTableArray.sort(comparatorCard);
		fangkuaiTableArray.sort(comparatorCard);

		heitaoHandArray.sort(comparatorCard);
		hongtaoHandArray.sort(comparatorCard);
		meihuaHandArray.sort(comparatorCard);
		fangkuaiHandArray.sort(comparatorCard);
		
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
		fangkuaiInOtherOne = findCardInOther(myCards.FANGKUAI,fangkuaiTableArray,fangkuaiHandArray);

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

	private Card secondScan(Card[] cardOnTable, Card[] cardInHand, int salePig, int saleSheep, int saleRed,
			int saleDouble) {

		ArrayList<Card> heitaoTableArray = new ArrayList<>();
		ArrayList<Card> hongtaoTableArray = new ArrayList<>();
		ArrayList<Card> meihuaTableArray = new ArrayList<>();
		ArrayList<Card> fangkuaiTableArray = new ArrayList<>();

		ArrayList<Card> heitaoHandArray = new ArrayList<>();
		ArrayList<Card> hongtaoHandArray = new ArrayList<>();
		ArrayList<Card> meihuaHandArray = new ArrayList<>();
		ArrayList<Card> fangkuaiHandArray = new ArrayList<>();
		return null;
	}

	// sale: 0 stand for didn't sale, sale: 1 stand for sale other, sale: 2 stand
	// for sale by yourself
	public Card SendOutCard(Card[] cardOnTable, Card[] cardInHand, int salePig, int saleSheep, int saleRed,
			int saleDouble) {

		Card returnCard = null;

		// if only one card in the hand, then send out the card
		if (cardInHand.length == 1) {
			return cardInHand[0];
		}

		// first scan
		returnCard = FirstScan(cardOnTable, cardInHand, salePig, saleSheep, saleRed, saleDouble);

		// second scan
		returnCard = secondScan(cardOnTable, cardInHand, salePig, saleSheep, saleRed, saleDouble);

		return returnCard;

	}

	enum Day {
	    MONDAY, TUESDAY, WEDNESDAY,
	    THURSDAY, FRIDAY, SATURDAY, SUNDAY
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		

		System.out.println(Day.FRIDAY.toString());

		Card c1 = new Card("11", 3);
		Card c2 = new Card("22", 5);
		Card c3 = new Card("33", 1);

		ArrayList<Card> testArray = new ArrayList<>();
		testArray.add(c1);
		testArray.add(c2);
		testArray.add(c3);

		Card[] cardArray = testArray.toArray(new Card[3]);

		Card c = FirstScan(cardArray, cardArray, 0, 0, 0, 0);

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
