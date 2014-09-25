package com.ghc.stringparser.token;

public class TokenArgument extends Token {
	private char original;
	
	@Override
	public TokenType getType() {
		return TokenType.Argument;
	}

	public char getOriginal() {
		return original;
	}

	public void setOriginal(char orginal) {
		this.original = orginal;
	}	
}
