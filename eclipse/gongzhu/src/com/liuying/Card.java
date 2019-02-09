package com.liuying;

public class Card {

	private String Color;
	private int Point;
	public int count;
	public double Percentage;

	public Card(String color, int point) {
		Color = color;
		Point = point;
		count = 0;
		Percentage = 0;
	}

	public String getColor() {
		return Color;
	}

	public int getPoint() {
		return Point;
	}

	public void setColor(String color) {
		Color = color;
	}

	public void setPoint(int point) {
		Point = point;
	}

	public boolean equals(Object obj) {
		if (obj instanceof Card) {
			Card c = (Card) obj;
			return this.getColor().equals(c.getColor()) && this.getPoint() == c.getPoint();
		}
		return super.equals(obj);
	}



}
