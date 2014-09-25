package com.ghc.stringparser.token;

public class TokenGroupEnd extends Token {
	private char original;
	
	@Override
	public TokenType getType() {
		return TokenType.GroupEnd;
	}

	public char getOriginal() {
		return original;
	}

	public void setOriginal(char orginal) {
		this.original = orginal;
	}	
}
