package com.ghc.stringparser;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
	public List<Token> parse(String input) {
		List<Token> tokens = new ArrayList<Token>();

		if (input == null || input.length() == 0) {
			return tokens;
		}

		char[] array = getArray(input);

		return tokens;
	}

	private char[] getArray(String input) {
		return input.toCharArray();
	}

	private int isGroupStart(char[] input, int start) {
		if (input[start] == '(') {
			start = start + 1;
		}
		return start;
	}

	private int isGroupEnd(char[] input, int start) {
		if (input[start] == ')') {
			start = start + 1;
		}
		return start;
	}

	private int isFunction(char[] input, int start) {
		// Maximum length of function's name
		int maxLength = 5;

		for (int i = start; i < input.length && i < maxLength; i++) {
			
		}

		return start;
	}
}
