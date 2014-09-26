package com.ghc.stringparser.token;

public class TokenAssignment extends Token {
	private char original;

	@Override
	public TokenType getType() {
		return TokenType.Assignment;
	}

	public char getOriginal() {
		return original;
	}

	public void setOriginal(char orginal) {
		this.original = orginal;
	}

}
