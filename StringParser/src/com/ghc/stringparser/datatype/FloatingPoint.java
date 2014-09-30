package com.ghc.stringparser.datatype;

public class FloatingPoint {
	//
	// 2.3 => value = 2, fraction = 3, devide = 10 => 2 + 3/10
	// -2.3 => minus, value = -2, fraction = 3, devide = 10 => -(2 + 3/10)
	// -0.3 => minus, value = 0, fraction = -3, devide = 10 => 0 - 3/10
	//

	private int value;
	private int fraction;
	private int devide;

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getFraction() {
		return fraction;
	}

	public void setFraction(int fraction) {
		this.fraction = fraction;
	}

	public int getDevide() {
		return devide;
	}

	public void setDevide(int devide) {
		this.devide = devide;
	}

}
