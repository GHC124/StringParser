package com.ghc.stringparser.token;

public class TokenUnary extends Token {
	private char original;
	
	@Override
	public TokenType getType() {
		return TokenType.Unary;
	}

	public char getOriginal() {
		return original;
	}

	public void setOriginal(char orginal) {
		this.original = orginal;
	}	
}
