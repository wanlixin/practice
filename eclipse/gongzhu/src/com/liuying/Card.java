package com.liuying;

public class Card {

	private String Color;
	private int Point;

	Card(String color, int point) {
		Color = color;
		Point = point;
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

}
