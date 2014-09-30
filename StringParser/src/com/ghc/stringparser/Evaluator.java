package com.ghc.stringparser;

import static com.ghc.stringparser.datatype.util.FloatingPointUtil.getFloatNumber;
import static com.ghc.stringparser.datatype.util.FloatingPointUtil.getFloatingPoint;

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

	

	private FloatingPoint getUnaryValue(TokenUnary token, FloatingPoint[] values) {
		FloatingPoint value = values[0];
		
		if(value == null){
			return value;
		}

		// -0 == 0, so change fraction to negative
		if(value.getValue() == 0){
			value.setFraction(-value.getFraction());
		}else{		
			value.setValue(-value.getValue());
		}

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
