package com.liuying;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

			return (c2.getPoint() - c1.getPoint());
		}

	};

	static Comparator<Card> comparatorCardSmallToLarge = new Comparator<Card>() {
		@Override
		public int compare(Card c1, Card c2) {

			return (c1.getPoint() - c2.getPoint());
		}

	};

	static Comparator<Card> comparatorSelectCardToSend = new Comparator<Card>() {
		@Override
		public int compare(Card c1, Card c2) {

			return (c1.Percentage == c2.Percentage) ? (c1.count - c2.count)
					: (int) (c2.Percentage * 10000 - c1.Percentage * 10000);

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
			smallCards.get(smallCards.size() - 1).Percentage = compareCardWithOther(
					heitaoHandArray.get(heitaoHandArray.size() - 1), CardType.HEITAO);
		}

		if (!hongtaoHandArray.isEmpty()) {
			smallCards.add(hongtaoHandArray.get(hongtaoHandArray.size() - 1));
			smallCards.get(smallCards.size() - 1).count = hongtaoHandArray.size();
			smallCards.get(smallCards.size() - 1).Percentage = compareCardWithOther(
					hongtaoHandArray.get(hongtaoHandArray.size() - 1), CardType.HONGTAO);
		}

		if (!meihuaHandArray.isEmpty()) {
			smallCards.add(meihuaHandArray.get(meihuaHandArray.size() - 1));
			smallCards.get(smallCards.size() - 1).count = meihuaHandArray.size();
			smallCards.get(smallCards.size() - 1).Percentage = compareCardWithOther(
					meihuaHandArray.get(meihuaHandArray.size() - 1), CardType.MEIHUA);
		}

		if (!fangkuaiHandArray.isEmpty()) {
			smallCards.add(fangkuaiHandArray.get(fangkuaiHandArray.size() - 1));
			smallCards.get(smallCards.size() - 1).count = fangkuaiHandArray.size();
			smallCards.get(smallCards.size() - 1).Percentage = compareCardWithOther(
					fangkuaiHandArray.get(fangkuaiHandArray.size() - 1), CardType.FANGKUAI);
		}

	}

	public static Card FirstScan(Card[] cardOnTable, Card[] cardInHand, int salePig, int saleSheep, int saleRed,
			int saleDouble) {

		cardArrange(cardOnTable, cardInHand);

		// check heitao ����б�Q ����ƣ�Q ���������ˣ����������֣���ô��������
		if (heitaoHandArray.isEmpty() == false && salePig == 1 && heitaoTableArray.isEmpty()
				&& heitaoHandArray.get(0).getPoint() > pigCard.getPoint()) {
			return heitaoHandArray.get(0);
		}

		// check meihua �����Ʊ�10 �󣬲��� 10 ���ˣ��������֣���ô��������
		if (meihuaHandArray.isEmpty() == false && saleDouble != 0 && meihuaTableArray.isEmpty()
				&& meihuaHandArray.get(0).getPoint() > doubleCard.getPoint()) {
			return meihuaHandArray.get(0);
		}

		// check fangkuai in otherone's hand
		ArrayList<Card> fangkuaiInOtherOne = new ArrayList<>();
		fangkuaiInOtherOne = findCardInOther(CardType.FANGKUAI, fangkuaiTableArray, fangkuaiHandArray);

		// ��������J����J �Ƿ������������������ƣ�����ǣ�����
		if (fangkuaiHandArray.contains(sheepCard)) {

			if (fangkuaiInOtherOne.isEmpty() || fangkuaiInOtherOne.get(0).getPoint() < sheepCard.getPoint()) {
				if (saleSheep != 0 && fangkuaiTableArray.isEmpty()) {

				} else {
					return sheepCard;
				}
			}

		}

		// �������б�J ����ƣ�����J�ڱ������û������//�����������7�ţ���ô��������
		if (!fangkuaiInOtherOne.isEmpty() && !fangkuaiHandArray.isEmpty()
				&& fangkuaiHandArray.get(0).getPoint() > sheepCard.getPoint() && fangkuaiInOtherOne.contains(sheepCard)
				&& saleSheep == 0) {
			return fangkuaiHandArray.get(0);
		}

		// �������б�J ����ƣ�J�ڱ������J ���ˣ�������ƴ���1�� С��7�ţ���ô��������
		if (!fangkuaiInOtherOne.isEmpty() && !fangkuaiHandArray.isEmpty()
				&& fangkuaiHandArray.get(0).getPoint() > sheepCard.getPoint() && fangkuaiInOtherOne.contains(sheepCard)
				&& saleSheep != 0 && fangkuaiTableArray.size() > 0 && fangkuaiTableArray.size() < 10) {
			return fangkuaiHandArray.get(0);
		}

		// TODO hongtao��ȫѪ�����

		return null;

	}

	// ��������Ļ�ɫ���ƣ����˵��Ʊ����Ĵ�ٷֱȡ�
	private static Double compareCardWithOther(Card current, CardType cardType) {
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
			if (c.getPoint() > current.getPoint()) {
				count++;
			}
		}

		double percentiage = (double) count / (double) cardsInOtherOne.size();
		BigDecimal bd = new BigDecimal(percentiage);
		percentiage = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return percentiage;

	}

	// �ڶ���ɨ�裬����ÿ����ɫ��С���ƣ������м����Ʊ����Ĵ�ѡ��������ĳ��ƣ����ͬ�������ѡ�����������ٵĳ���
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

	// sale: 0 stand for didn't sale, sale: 1 stand for sale other, sale: 2 stand
	// for sale by yourself
	public static Card FollowCard(Card[] cardOnTable, Card[] cardInHand, Card[] CardThisTurn, int salePig,
			int saleSheep, int saleRed, int saleDouble) {

		Card returnCard = null;
		Card currentCard;
		currentCard = CardThisTurn[0];
		cardArrange(cardOnTable, cardInHand);

		// if only one card in the hand, then send out the card
		if (cardInHand.length == 1) {
			return cardInHand[0];
		}

		// if only one card in the type, then send out the card
		ArrayList<Card> currentCardArray = new ArrayList<>();
		if (currentCard.getColor() == CardType.HEITAO.toString()) {
			currentCardArray = heitaoHandArray;
		}
		if (currentCard.getColor() == CardType.HONGTAO.toString()) {
			currentCardArray = hongtaoHandArray;
		}
		if (currentCard.getColor() == CardType.MEIHUA.toString()) {
			currentCardArray = meihuaHandArray;
		}
		if (currentCard.getColor() == CardType.FANGKUAI.toString()) {
			currentCardArray = fangkuaiHandArray;
		}

		if (currentCardArray.size() == 1) {
			return currentCardArray.get(0);
		}

		// first scan,������ƻ�ɫ����û��
		if (currentCardArray.size() == 0) {
			return FirstScanByFollow(cardOnTable, cardInHand, salePig, saleSheep, saleRed, saleDouble);
		}

		// second scan ������ƻ�ɫ������
		returnCard = secondScanByFollow(cardOnTable, cardInHand, CardThisTurn, currentCardArray, salePig, saleSheep,
				saleRed, saleDouble);

		return returnCard;

	}

	// ������ƻ�ɫ�����е����
	private static Card secondScanByFollow(Card[] cardOnTable, Card[] cardInHand, Card[] CardThisTurn,
			ArrayList<Card> currentCardArray, int salePig, int saleSheep, int saleRed, int saleDouble) {

		CardType cardType = CardType.HEITAO;
		Card smallCardThisTurn = CardThisTurn[0];
		for (int i = 0; i < CardThisTurn.length; i++) {
			if (CardThisTurn[i].getPoint() < smallCardThisTurn.getPoint()
					&& CardThisTurn[i].getColor() == smallCardThisTurn.getColor()) {
				smallCardThisTurn = CardThisTurn[i];
			}

		}

		Card bigCardThisTurn = CardThisTurn[0];
		for (int i = 0; i < CardThisTurn.length; i++) {
			if (CardThisTurn[i].getPoint() > smallCardThisTurn.getPoint()
					&& CardThisTurn[i].getColor() == smallCardThisTurn.getColor()) {
				smallCardThisTurn = CardThisTurn[i];
			}

		}

		// ������ƻ�ɫ�Ǻ��ҵ����
		if (CardThisTurn[0].getColor() == CardType.HEITAO.toString()) {

			// ��һ�� ,�����ˣ�������֮������
			if (salePig != 0 && heitaoTableArray.size() == 0) {
				if (heitaoHandArray.get(0).equals(pigCard)) {
					return heitaoHandArray.get(1);
				} else {
					return heitaoHandArray.get(0);
				}

			}

			// ��һ�� ,��û�����������б������� �������� 
			if (salePig == 0 && heitaoTableArray.size() == 0 && heitaoHandArray.contains(pigCard) && bigCardThisTurn.getPoint() > 12 ) {
				return pigCard;
			}

			// ���ǵ�һ�֣����������������б������ƣ���ô����

			if (heitaoTableArray.size() != 0 && heitaoHandArray.contains(pigCard) && bigCardThisTurn.getPoint() > 12) {
				return pigCard;
			}

			// ���ǵ�һ�֣������������������Ʊ���С�� //ͨ���߼�

			// ���ǵ�һ�֣�����������ô����������С���ƣ����ߵ�ǰ��С���� ͨ���߼�

			// ����ͨ���߼�

		}

		// ������ƻ�ɫ�Ǻ��ҵ����
		if (CardThisTurn[0].getColor() == CardType.HONGTAO.toString()) {

			cardType = CardType.HONGTAO;

			// ����ͨ���߼�

		}

		// ������ƻ�ɫ��÷�������
		if (CardThisTurn[0].getColor() == CardType.MEIHUA.toString()) {

			cardType = CardType.MEIHUA;

			// ��һ�� ,÷�����ˣ�����÷��֮������
			if (saleSheep != 0 && meihuaTableArray.size() == 0) {
				if (meihuaHandArray.get(0).equals(doubleCard)) {
					return meihuaHandArray.get(1);
				} else {
					return meihuaHandArray.get(0);
				}

			}

		}

		// ������ƻ�ɫ�Ƿ�������
		if (CardThisTurn[0].getColor() == CardType.FANGKUAI.toString()) {

			cardType = CardType.FANGKUAI;

			// ��һ�� ,�������ˣ����Ƿ���֮������
			if (saleSheep != 0 && fangkuaiTableArray.size() == 0) {
				for (int i = 0; i < currentCardArray.size(); i++) {
					if (sheepCard.getPoint() > currentCardArray.get(i).getPoint()) {
						return currentCardArray.get(i);
					}
				}

			}

			// ǰ���� ,������
			if (fangkuaiTableArray.size() <= 5) {
				if(!currentCardArray.get(0).equals(sheepCard))
				{
					return currentCardArray.get(0);
				}
			}
			
			
			// ����������򣬲������ϵ��Ʊ���� �������
			if (bigCardThisTurn.getPoint() > sheepCard.getPoint() && currentCardArray.contains(sheepCard)) {
				if(!currentCardArray.get(0).equals(sheepCard))
				{
					return currentCardArray.get(0);
				}
				return currentCardArray.get(1);
			}

		}

		// ����ͨ���߼�
		for (int i = 0; i < currentCardArray.size(); i++) {
			if (bigCardThisTurn.getPoint() > currentCardArray.get(i).getPoint()) {
				return currentCardArray.get(i);
			}
		}
		for (int i = 0; i < currentCardArray.size(); i++) {
			if (compareCardWithOther(currentCardArray.get(i), cardType) == 1.0) {
				System.out.println("suanpai");
				return currentCardArray.get(i);
			}
		}
		return currentCardArray.get(currentCardArray.size() - 1);

	}

	// ������ƻ�ɫ����û�У���ô�������������������б�ѹ��������ѹ����������Ҵ���10���������ҡ���󣬳������ٵ����滨ɫ���ģ�
	public static Card FirstScanByFollow(Card[] cardOnTable, Card[] cardInHand, int salePig, int saleSheep, int saleRed,
			int saleDouble) {

		// ���������������
		if (!heitaoHandArray.isEmpty() && heitaoHandArray.contains(pigCard)) {
			return pigCard;
		}

		// ����б�ѹ��������ѹ��
		if (!meihuaHandArray.isEmpty() && meihuaHandArray.contains(doubleCard)) {
			return doubleCard;
		}

		// ������Ҵ���10����������
		if (!hongtaoHandArray.isEmpty() && hongtaoHandArray.get(0).getPoint() > 10) {
			return hongtaoHandArray.get(0);
		}

		for (CardType c : CardType.values()) {

		}

		ArrayList<Card> smallList = heitaoHandArray;
		if (smallList.isEmpty()) {
			smallList = meihuaHandArray;
		}
		if (smallList.isEmpty()) {
			smallList = hongtaoHandArray;
		}
		if (smallList.isEmpty()) {
			smallList = fangkuaiHandArray;
		}

		if (smallList.size() > meihuaHandArray.size() && !meihuaHandArray.isEmpty()) {
			smallList = meihuaHandArray;
		}

		if (smallList.size() > hongtaoHandArray.size() && !hongtaoHandArray.isEmpty()) {
			smallList = hongtaoHandArray;
		}

		if (smallList.size() > fangkuaiHandArray.size() && !fangkuaiHandArray.isEmpty()) {
			smallList = fangkuaiHandArray;
		}

		return smallList.get(0);

	}

	enum Day {
		MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ArrayList<Card> smallList = new ArrayList<Card>();
		System.out.println("size of empty arraylist : " + smallList.size());
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

		cardArrange(cardOnTable, cardInHand);

		if (cardOnTable[0].equals(new Card(CardType.FANGKUAI.toString(), 8))) {
			System.out.println("�������������equal �Ƚ�");
		}

		if (compareCardWithOther(new Card(CardType.HONGTAO.toString(), 2), CardType.HONGTAO) == 1.0) {
			System.out.println("���ԱȽ�");
		}

		System.out.println(
				"..............." + compareCardWithOther(new Card(CardType.HONGTAO.toString(), 2), CardType.HONGTAO));

		Card c = SendOutCard(cardOnTable, cardInHand, 1, 1, 1, 1);

		Double d = (double) -1;
		if (d == -1) {
			System.out.println("d.toString().equals(\"-1\")");
		}

		System.out.println(Day.FRIDAY.toString());

		Card c1 = new Card("11", 3);
		c1.Percentage = 0.2;
		c1.count = 3;
		Card c2 = new Card("22", 5);
		c2.Percentage = 0.8;
		c2.count = 4;
		Card c3 = new Card("33", 1);
		c3.Percentage = 0.2;
		c3.count = 2;

		ArrayList<Card> testArray = new ArrayList<>();
		testArray.add(c1);
		testArray.add(c2);
		testArray.add(c3);

		Card[] cardArray = testArray.toArray(new Card[3]);

		Card cc = FirstScan(cardArray, cardArray, 0, 0, 0, 0);

		for (int i = 0; i < testArray.size(); i++) {
			System.out.println(testArray.get(i).getPoint());
		}

		testArray.sort(comparatorCardSmallToLarge);
		System.out.println("Small To Large");
		for (int i = 0; i < testArray.size(); i++) {
			System.out.println(testArray.get(i).getPoint());
		}

		testArray.sort(comparatorCardLargeToSmall);
		System.out.println("Large To Small");
		for (int i = 0; i < testArray.size(); i++) {
			System.out.println(testArray.get(i).getPoint());
		}

		testArray.sort(comparatorSelectCardToSend);
		System.out.println("comparatorSelectCardToSend");
		for (int i = 0; i < testArray.size(); i++) {
			System.out.println(testArray.get(i).getPoint());
		}

	}
}
