package com.ghc.stringparser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ghc.stringparser.token.Token;
import com.ghc.stringparser.token.TokenConstant;
import com.ghc.stringparser.token.TokenGroupEnd;
import com.ghc.stringparser.token.TokenGroupStart;
import com.ghc.stringparser.token.TokenOperator;
import com.ghc.stringparser.token.TokenType;

public class Lexer {
	 // Maximum length of operator, ex: <=
	private static final int MAX_OPERATOR_LENGTH = 2;
	 // Maximum length of constant, ex: 20140
	private static final int MAX_CONSTANT_LENGTH = 5;
	
	private Pattern getPattern(String regex) {
		return Pattern.compile(regex);
	}

	public List<Token> parse(String input) {
		List<Token> tokens = new ArrayList<Token>();

		if (input == null || input.length() == 0) {
			return tokens;
		}

		char[] array = getArray(input);
		int length = array.length;
		for (int i = 0; i < length; i++) {
			Token token = getToken(array, i);
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

	private Token getToken(char[] input, int start) {
		Token token = null;
		for (TokenType type : TokenType.values()) {
			switch (type) {
			case GroupStart:
				token = getTokenGroupStart(input, start);
				break;
			case GroupEnd:
				token = getTokenGroupEnd(input, start);
				break;
			case Operator:
				token = getTokenOperator(input, start);
				break;
			case Constant:
				token = getTokenConstant(input, start);
				break;
			default:
				break;
			}
			if (token != null) {
				break;
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
		String subString = getSubString(input, start, MAX_OPERATOR_LENGTH);

		Pattern pattern = getPattern("^[\\+\\*-/]");
		Matcher matcher = pattern.matcher(subString);
		TokenOperator token = null;
		if (matcher.find()) {
			String original = matcher.group();
			token = new TokenOperator();
			token.setOriginal(original);
			token.setStart(start);
			token.setEnd(start + original.length() - 1);
		}
		return token;
	}
	
	private Token getTokenConstant(char[] input, int start) {
		String subString = getSubString(input, start, MAX_CONSTANT_LENGTH);

		Pattern pattern = getPattern("^[0-9]+");
		Matcher matcher = pattern.matcher(subString);
		TokenConstant token = null;
		if (matcher.find()) {
			String original = matcher.group();
			int value = Integer.parseInt(original);
			token = new TokenConstant();
			token.setOriginal(original);
			token.setStart(start);
			token.setEnd(start + original.length() - 1);
			token.setValue(value);
		}
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

	public void printTokens(List<Token> tokens) {
		for (Token token : tokens) {
			String original = "";
			switch(token.getType()){
			case GroupStart:
				original = String.valueOf(((TokenGroupStart)token).getOriginal());
				break;
			case GroupEnd:
				original = String.valueOf(((TokenGroupEnd)token).getOriginal());
				break;
			case Operator:
				original = ((TokenOperator)token).getOriginal();
				break;
			case Constant:
				original = ((TokenConstant)token).getOriginal();
				break;
			default:
				break;
			}
			
			System.out.printf("Original %s, Type %s, start %d, end %d \n", original,
					token.getType().name(), token.getStart(), token.getEnd());
		}
	}
}
