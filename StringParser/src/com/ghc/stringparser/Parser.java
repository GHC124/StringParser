package com.ghc.stringparser;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.ghc.stringparser.token.Token;
import com.ghc.stringparser.token.TokenArgument;
import com.ghc.stringparser.token.TokenAssignment;
import com.ghc.stringparser.token.TokenConstant;
import com.ghc.stringparser.token.TokenFunction;
import com.ghc.stringparser.token.TokenGroupEnd;
import com.ghc.stringparser.token.TokenGroupStart;
import com.ghc.stringparser.token.TokenIdentifier;
import com.ghc.stringparser.token.TokenOperator;
import com.ghc.stringparser.token.TokenType;
import com.ghc.stringparser.token.TokenUnary;

public class Parser {
	public List<Token> rpnParser(List<Token> input) {
		List<Token> output = new ArrayList<Token>();

		Stack<Token> operatorStack = new Stack<Token>();
		Stack<Boolean> wereValues = new Stack<Boolean>();
		Stack<Integer> argCount = new Stack<Integer>();

		for (int i = 0; i < input.size(); i++) {
			Token token = input.get(i);
			switch (token.getType()) {
			case Constant:
				output.add(token);
				if (!wereValues.empty()) {
					wereValues.pop();
					wereValues.push(true);
				}
				break;
			case Operator:
				int precedence1 = getPrecedence((TokenOperator) token);
				if (!operatorStack.empty()) {
					Token token2 = operatorStack.peek();
					while (token2.getType() == TokenType.Operator) {
						int precedence2 = getPrecedence((TokenOperator) token2);
						if (precedence1 >= precedence2) {
							output.add(operatorStack.pop());
							if (!operatorStack.empty()) {
								token2 = operatorStack.peek();
							} else {
								break;
							}
						} else {
							break;
						}
					}
					if(token2.getType() == TokenType.Unary){
						output.add(operatorStack.pop());
					}
				}
				operatorStack.push(token);
				break;
			case Unary:
				operatorStack.push(token);
				break;
			case GroupStart:
				operatorStack.push(token);
				break;
			case GroupEnd:
				if (!operatorStack.empty()) {
					token = operatorStack.peek();
					while (token.getType() != TokenType.GroupStart) {
						output.add(operatorStack.pop());
						if (!operatorStack.empty()) {
							token = operatorStack.peek();
						} else {
							break;
						}
					}
				}
				if (!operatorStack.empty()
						&& operatorStack.peek().getType() == TokenType.GroupStart) {
					operatorStack.pop();
				}
				if (!operatorStack.empty()) {
					token = operatorStack.peek();
					if (token.getType() == TokenType.Function) {
						Token f = operatorStack.pop();
						Integer a = argCount.pop();
						Boolean w = wereValues.pop();
						if (w) {
							a = a + 1;
						}
						((TokenFunction) f).setArgCount(a);
						output.add(f);
					}
				}
				break;
			case Function:
				operatorStack.push(token);
				argCount.push(0);
				if (!wereValues.empty()) {
					wereValues.pop();
					wereValues.push(true);
				}
				wereValues.push(false);
				break;
			case Argument:
				if (!operatorStack.empty()) {
					token = operatorStack.peek();
					while (token.getType() != TokenType.GroupStart) {
						output.add(operatorStack.pop());
						if (!operatorStack.empty()) {
							token = operatorStack.peek();
						} else {
							break;
						}
					}
				}
				if (!wereValues.empty()) {
					Boolean w = wereValues.pop();
					if (w) {
						Integer a = argCount.pop();
						argCount.push(a + 1);
					}
				}
				wereValues.push(false);
				break;
			case Identifier:
				output.add(token);
				break;
			case Assignment:
				operatorStack.push(token);
				break;
			default:
				break;
			}
		}
		while (!operatorStack.empty()) {
			output.add(operatorStack.pop());
		}

		return output;
	}

	/**
	 * Get precedence, smaller is bigger
	 */
	private int getPrecedence(TokenOperator token) {
		int precedence = 0;

		String original = token.getOriginal();
		if (original.equals("*")) {
			precedence = 1;
		} else if (original.equals("/")) {
			precedence = 1;
		} else if (original.equals("+")) {
			precedence = 2;
		} else if (original.equals("-")) {
			precedence = 2;
		}

		return precedence;
	}

	public void printTokens(List<Token> tokens, StringBuilder sb) {
		for (Token token : tokens) {
			String original = "";
			int argCount = -1;
			switch (token.getType()) {
			case GroupStart:
				original = String.valueOf(((TokenGroupStart) token)
						.getOriginal());
				break;
			case GroupEnd:
				original = String
						.valueOf(((TokenGroupEnd) token).getOriginal());
				break;
			case Operator:
				original = ((TokenOperator) token).getOriginal();
				break;
			case Constant:
				original = ((TokenConstant) token).getOriginal();
				break;
			case Function:
				original = ((TokenFunction) token).getOriginal();
				argCount = ((TokenFunction) token).getArgCount();
				break;
			case Argument:
				original = String
						.valueOf(((TokenArgument) token).getOriginal());
				break;
			case Identifier:
				original = ((TokenIdentifier) token).getOriginal();
				break;
			case Assignment:
				original = String
						.valueOf(((TokenAssignment) token).getOriginal());
				break;
			case Unary:
				original = String
						.valueOf(((TokenUnary) token).getOriginal());
				break;
			default:
				break;
			}
			if (argCount >= 0) {
				sb.append(String.format("Original %s, Type %s, Arg %d\n",
						original, token.getType().name(), argCount));
			} else {
				sb.append(String.format("Original %s, Type %s\n", original,
						token.getType().name()));
			}
		}
	}
}
