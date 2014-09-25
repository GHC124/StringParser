package com.ghc.stringparser.token;

public class TokenGroupStart extends Token {
	private char original;
	
	@Override
	public TokenType getType() {
		return TokenType.GroupStart;
	}

	public char getOriginal() {
		return original;
	}

	public void setOriginal(char orginal) {
		this.original = orginal;
	}	
}
