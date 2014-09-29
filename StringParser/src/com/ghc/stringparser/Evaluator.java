package com.ghc.stringparser;

import java.util.List;
import java.util.Stack;

import com.ghc.stringparser.token.Token;
import com.ghc.stringparser.token.TokenConstant;
import com.ghc.stringparser.token.TokenFunction;
import com.ghc.stringparser.token.TokenIdentifier;
import com.ghc.stringparser.token.TokenOperator;
import com.ghc.stringparser.token.TokenUnary;

public class Evaluator {
	public TokenIdentifier evaluate(List<Token> input) {
		Stack<Integer> constantStack = new Stack<Integer>();
		TokenIdentifier tokenIdentifier = null;

		int argCount = 0;
		int[] values;
		int value;

		for (int i = 0; i < input.size(); i++) {
			Token token = input.get(i);
			switch (token.getType()) {
			case Constant:
				constantStack.push(((TokenConstant) token).getValue());
				break;
			case Operator:
				TokenOperator tokenOperator = (TokenOperator) token;
				argCount = getArgumentCount(tokenOperator);
				values = new int[argCount];
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
				values = new int[1];
				if (!constantStack.empty()) {
					values[0] = constantStack.pop();
				}
				value = getUnaryValue((TokenUnary) token, values);
				constantStack.push(value);
				break;
			case Function:
				TokenFunction tokenFunction = (TokenFunction) token;
				argCount = tokenFunction.getArgCount();
				values = new int[argCount];
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

	private int getOperatorValue(TokenOperator token, int[] values) {
		int value = 0;

		String original = token.getOriginal();
		if (original.equals("*")) {
			value = values[0] * values[1];
		} else if (original.equals("/")) {
			if (values[1] != 0) {
				value = values[0] / values[1];
			}
		} else if (original.equals("+")) {
			value = values[0] + values[1];
		} else if (original.equals("-")) {
			value = values[0] - values[1];
		}

		return value;
	}

	private int getUnaryValue(TokenUnary token, int[] values) {
		int value = 0;

		value = -values[0];

		return value;
	}

	private int getFunctionValue(TokenFunction token, int[] values) {
		int value = 0;

		String original = token.getOriginal();
		if (original.equals("min")) {
			value = getMin(values);
		} else if (original.equals("max")) {
			value = getMax(values);
		}

		return value;
	}

	private int getMin(int[] values) {
		int value = values[0];
		for (int i = 1; i < values.length; i++) {
			if (value > values[i]) {
				value = values[i];
			}
		}
		return value;
	}

	private int getMax(int[] values) {
		int value = values[0];
		for (int i = 1; i < values.length; i++) {
			if (value < values[i]) {
				value = values[i];
			}
		}
		return value;
	}
}
