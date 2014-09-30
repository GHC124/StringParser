package com.ghc.stringparser.datatype.util;

import com.ghc.stringparser.Lexer;
import com.ghc.stringparser.datatype.FloatingPoint;

public class FloatingPointUtil {
	public static float getFloatNumber(FloatingPoint floatingPoint) {
		if (floatingPoint == null) {
			return 0.0f;
		}

		float value = 0.0f;

		if (floatingPoint.getDevide() >= Lexer.FLOATING_POINT_DEVICE_OFFSET) {
			if (floatingPoint.getValue() < 0) {
				value = (-(float) floatingPoint.getValue())
						+ (float) floatingPoint.getFraction()
						/ (float) floatingPoint.getDevide();
				value = -value;
			} else {
				value = (float) floatingPoint.getValue()
						+ (float) floatingPoint.getFraction()
						/ (float) floatingPoint.getDevide();
			}
		} else {
			value = floatingPoint.getValue();
		}

		return value;
	}

	public static FloatingPoint getFloatingPoint(float fValue) {
		FloatingPoint floatingPoint = new FloatingPoint();

		String original = String.valueOf(fValue);

		int dot = original.indexOf(".");
		int value = 0;
		int fraction = 0;
		int devide = 1;
		if (dot == -1) {
			value = Integer.parseInt(original);
		} else {
			value = Integer.parseInt(original.substring(0, dot));
			if (dot < original.length() - 1) {
				String sFraction = original.substring(dot + 1);
				fraction = Integer.parseInt(sFraction);
				for (int i = 0; i < sFraction.length(); i++) {
					devide = devide * 10;
				}
			}
		}
		floatingPoint.setValue(value);
		floatingPoint.setFraction(fraction);
		floatingPoint.setDevide(devide);

		return floatingPoint;
	}
}
