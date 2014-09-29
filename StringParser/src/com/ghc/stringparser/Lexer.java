package com.ghc.stringparser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public class Lexer {
	// Maximum length of operator, ex: <=
	private static final int MAX_OPERATOR_LENGTH = 2;
	// Maximum length of constant, ex: 20140
	private static final int MAX_CONSTANT_LENGTH = 5;
	// Maximum length of function's name, ex: min
	private static final int MAX_FUNCTION_LENGTH = 5;
	// Maximum length of identifier's name, ex: x
	private static final int MAX_IDENTIFIER_LENGTH = 1;

	private int identifierCount = 0;

	private Pattern getPattern(String regex) {
		return Pattern.compile(regex);
	}

	public int getIdentifierCount() {
		return identifierCount;
	}

	public List<Token> parse(String input) {
		List<Token> tokens = new ArrayList<Token>();

		if (input == null || input.length() == 0) {
			return tokens;
		}

		char[] array = getArray(input);
		int length = array.length;
		for (int i = 0; i < length; i++) {
			Token token = getToken(array, i, tokens);
			if (token != null) {
				tokens.add(token);
				i = token.getEnd();
			}
		}

		return tokens;
	}

	private char[] getArray(String input) {
		return input.toCharArray();
	}

	private Token getToken(char[] input, int start, List<Token> tokens) {
		Token token = null;
		for (TokenType type : TokenType.values()) {
			switch (type) {
			case GroupStart:
				token = getTokenGroupStart(input, start);
				break;
			case GroupEnd:
				token = getTokenGroupEnd(input, start);
				break;
			case Constant:
				token = getTokenConstant(input, start);
				break;
			case Argument:
				token = getTokenArgument(input, start);
				break;
			case Assignment:
				token = getTokenAssignment(input, start);
				break;
			default:
				break;
			}
			if (token != null) {
				break;
			}
		}
		// Check unary & operator
		if (token == null) {
			if (isTokenUnary(input, start)) {
				// Check previous token
				if (tokens.isEmpty()) {
					token = createTokenUnary(start);
				} else {
					Token previous = tokens.get(tokens.size() - 1);
					if (previous.getType() == TokenType.Operator
							|| previous.getType() == TokenType.GroupStart
							|| previous.getType() == TokenType.Unary
							|| previous.getType() == TokenType.Assignment
							|| previous.getType() == TokenType.Argument) {
						token = createTokenUnary(start);
					}
				}
			}
			if (token == null) {
				token = getTokenOperator(input, start);
			}
		}
		// Check function & identifier
		if (token == null) {
			token = getTokenFunction(input, start);
			if (token == null) {
				token = getTokenIdentifier(input, start);
				if (token != null) {
					identifierCount++;
				}
			}
		}

		return token;
	}

	private Token getTokenGroupStart(char[] input, int start) {
		TokenGroupStart token = null;
		if (input[start] == '(') {
			token = new TokenGroupStart();
			token.setOriginal(input[start]);
			token.setStart(start);
			token.setEnd(start);
		}
		return token;
	}

	private Token getTokenGroupEnd(char[] input, int start) {
		TokenGroupEnd token = null;
		if (input[start] == ')') {
			token = new TokenGroupEnd();
			token.setOriginal(input[start]);
			token.setStart(start);
			token.setEnd(start);
		}
		return token;
	}

	private Token getTokenOperator(char[] input, int start) {
		TokenOperator token = null;
		Pattern pattern = getPattern("^[\\+\\*\\-\\/]");
		Matcher matcher = pattern.matcher(String.valueOf(input[start]));
		if (matcher.find()) {
			String subString = getSubString(input, start, MAX_OPERATOR_LENGTH);
			matcher = pattern.matcher(subString);
			if (matcher.find()) {
				String original = matcher.group();
				token = new TokenOperator();
				token.setOriginal(original);
				token.setStart(start);
				token.setEnd(start + original.length() - 1);
			}
		}
		return token;
	}

	private Token getTokenConstant(char[] input, int start) {
		TokenConstant token = null;
		Pattern pattern = getPattern("^[0-9]+");
		Matcher matcher = pattern.matcher(String.valueOf(input[start]));
		if (matcher.find()) {
			String subString = getSubString(input, start, MAX_CONSTANT_LENGTH);
			matcher = pattern.matcher(subString);
			if (matcher.find()) {
				String original = matcher.group();
				int value = Integer.parseInt(original);
				token = new TokenConstant();
				token.setOriginal(original);
				token.setStart(start);
				token.setEnd(start + original.length() - 1);
				token.setValue(value);
			}
		}
		return token;
	}

	private Token getTokenFunction(char[] input, int start) {
		TokenFunction token = null;
		Pattern pattern = getPattern("^[a-zA-Z]");
		Matcher matcher = pattern.matcher(String.valueOf(input[start]));
		if (matcher.find()) {
			pattern = getPattern("^(min|max)");
			String subString = getSubString(input, start, MAX_FUNCTION_LENGTH);
			matcher = pattern.matcher(subString);
			if (matcher.find()) {
				String original = matcher.group();
				token = new TokenFunction();
				token.setOriginal(original);
				token.setStart(start);
				token.setEnd(start + original.length() - 1);
			}
		}
		return token;
	}

	private Token getTokenArgument(char[] input, int start) {
		TokenArgument token = null;
		if (input[start] == ',') {
			token = new TokenArgument();
			token.setOriginal(input[start]);
			token.setStart(start);
			token.setEnd(start);
		}
		return token;
	}

	private Token getTokenIdentifier(char[] input, int start) {
		TokenIdentifier token = null;
		Pattern pattern = getPattern("^[a-zA-Z]");
		Matcher matcher = pattern.matcher(String.valueOf(input[start]));
		if (matcher.find()) {
			pattern = getPattern("^[a-zA-Z]+");
			String subString = getSubString(input, start, MAX_IDENTIFIER_LENGTH);
			matcher = pattern.matcher(subString);
			if (matcher.find()) {
				String original = matcher.group();
				token = new TokenIdentifier();
				token.setOriginal(original);
				token.setStart(start);
				token.setEnd(start + original.length() - 1);
			}
		}
		return token;
	}

	private Token getTokenAssignment(char[] input, int start) {
		TokenAssignment token = null;
		if (input[start] == '=') {
			token = new TokenAssignment();
			token.setOriginal(input[start]);
			token.setStart(start);
			token.setEnd(start);
		}
		return token;
	}

	private boolean isTokenUnary(char[] input, int start) {
		if (input[start] == '-') {
			return true;
		}
		return false;
	}

	private Token createTokenUnary(int start) {
		TokenUnary token = new TokenUnary();
		token.setOriginal('-');
		token.setStart(start);
		token.setEnd(start);
		return token;
	}

	private String getSubString(char[] input, int start, int maxLength) {
		StringBuilder sb = new StringBuilder();
		int end = start + maxLength;
		for (int i = start; i < input.length && i < end; i++) {
			sb.append(input[i]);
		}
		return sb.toString();
	}

	public void printTokens(List<Token> tokens, StringBuilder sb) {
		for (Token token : tokens) {
			String original = "";
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
				break;
			case Argument:
				original = String
						.valueOf(((TokenArgument) token).getOriginal());
				break;
			case Identifier:
				original = ((TokenIdentifier) token).getOriginal();
				break;
			case Assignment:
				original = String.valueOf(((TokenAssignment) token)
						.getOriginal());
				break;
			case Unary:
				original = String.valueOf(((TokenUnary) token)
						.getOriginal());
				break;
			default:
				break;
			}

			sb.append(String.format(
					"Original %s, Type %s, start %d, end %d \n", original,
					token.getType().name(), token.getStart(), token.getEnd()));
		}
	}
}
