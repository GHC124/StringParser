package com.ghc.stringparser;

import java.util.List;
import java.util.Stack;

import com.ghc.stringparser.datatype.FloatingPoint;
import com.ghc.stringparser.token.Token;
import com.ghc.stringparser.token.TokenConstant;
import com.ghc.stringparser.token.TokenFunction;
import com.ghc.stringparser.token.TokenIdentifier;
import com.ghc.stringparser.token.TokenOperator;
import com.ghc.stringparser.token.TokenUnary;

public class Evaluator {
	public TokenIdentifier evaluate(List<Token> input) {
		Stack<FloatingPoint> constantStack = new Stack<FloatingPoint>();
		TokenIdentifier tokenIdentifier = null;

		int argCount = 0;
		FloatingPoint[] values;
		FloatingPoint value;

		for (int i = 0; i < input.size(); i++) {
			Token token = input.get(i);
			switch (token.getType()) {
			case Constant:
				constantStack.push(((TokenConstant) token).getValue());
				break;
			case Operator:
				TokenOperator tokenOperator = (TokenOperator) token;
				argCount = getArgumentCount(tokenOperator);
				values = new FloatingPoint[argCount];
				for (int j = argCount - 1; j >= 0; j--) {
					if (!constantStack.empty()) {
						values[j] = constantStack.pop();
					} else {
						break;
					}
				}
				value = getOperatorValue(tokenOperator, values);
				constantStack.push(value);
				break;
			case Unary:
				values = new FloatingPoint[1];
				if (!constantStack.empty()) {
					values[0] = constantStack.pop();
				}
				value = getUnaryValue((TokenUnary) token, values);
				constantStack.push(value);
				break;
			case Function:
				TokenFunction tokenFunction = (TokenFunction) token;
				argCount = tokenFunction.getArgCount();
				values = new FloatingPoint[argCount];
				for (int j = argCount - 1; j >= 0; j--) {
					if (!constantStack.empty()) {
						values[j] = constantStack.pop();
					} else {
						break;
					}
				}
				value = getFunctionValue(tokenFunction, values);
				constantStack.push(value);
				break;
			case Assignment:
				if (tokenIdentifier != null) {
					tokenIdentifier.setValue(constantStack.pop());
				}
				break;
			case Identifier:
				tokenIdentifier = ((TokenIdentifier) token);
				break;
			default:
				break;
			}
		}

		if (tokenIdentifier == null) {
			tokenIdentifier = new TokenIdentifier();
			if (!constantStack.empty()) {
				tokenIdentifier.setValue(constantStack.pop());
			}
		}

		return tokenIdentifier;
	}

	private int getArgumentCount(TokenOperator token) {
		int argCount = 0;

		String original = token.getOriginal();
		if (original.equals("*")) {
			argCount = 2;
		} else if (original.equals("/")) {
			argCount = 2;
		} else if (original.equals("+")) {
			argCount = 2;
		} else if (original.equals("-")) {
			argCount = 2;
		}

		return argCount;
	}

	private FloatingPoint getOperatorValue(TokenOperator token,
			FloatingPoint[] values) {
		FloatingPoint floatingPoint = null;

		// TODO Implement new way to caculate floating point number without
		// using Float

		String original = token.getOriginal();
		float value = 0.0f;
		if (original.equals("*")) {
			value = getFloatNumber(values[0]) * getFloatNumber(values[1]);
		} else if (original.equals("/")) {
			if (values[1].getValue() != 0) {
				value = getFloatNumber(values[0]) / getFloatNumber(values[1]);
			}
		} else if (original.equals("+")) {
			value = getFloatNumber(values[0]) + getFloatNumber(values[1]);
		} else if (original.equals("-")) {
			value = getFloatNumber(values[0]) - getFloatNumber(values[1]);
		}

		floatingPoint = getFloatingPoint(value);

		return floatingPoint;
	}

	public float getFloatNumber(FloatingPoint floatingPoint) {
		float value = 0.0f;

		if (floatingPoint.getFraction() > 0 && floatingPoint.getDevide() > 0) {
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

	public FloatingPoint getFloatingPoint(float fValue) {
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

	private FloatingPoint getUnaryValue(TokenUnary token, FloatingPoint[] values) {
		FloatingPoint value = values[0];

		value.setValue(-value.getValue());

		return value;
	}

	private FloatingPoint getFunctionValue(TokenFunction token,
			FloatingPoint[] values) {
		FloatingPoint value = new FloatingPoint();

		String original = token.getOriginal();
		if (original.equals("min")) {
			value = getMin(values);
		} else if (original.equals("max")) {
			value = getMax(values);
		}

		return value;
	}

	private FloatingPoint getMin(FloatingPoint[] values) {
		FloatingPoint value = values[0];
		for (int i = 1; i < values.length; i++) {
			float value1 = getFloatNumber(value);
			float value2 = getFloatNumber(values[i]);
			if (value1 > value2) {
				value = values[i];
			}
		}
		return value;
	}

	private FloatingPoint getMax(FloatingPoint[] values) {
		FloatingPoint value = values[0];
		for (int i = 1; i < values.length; i++) {
			float value1 = getFloatNumber(value);
			float value2 = getFloatNumber(values[i]);
			if (value1 < value2) {
				value = values[i];
			}
		}
		return value;
	}
}
