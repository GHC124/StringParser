package com.ghc.stringparser.token;

public class TokenOperator extends Token {
	private String original;
	
	@Override
	public TokenType getType() {
		return TokenType.Operator;
	}

	public String getOriginal() {
		return original;
	}

	public void setOriginal(String orginal) {
		this.original = orginal;
	}	
}
