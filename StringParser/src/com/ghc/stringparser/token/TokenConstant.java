package com.ghc.stringparser.token;

public class TokenConstant extends Token {
	private String original;
	private int value;
		
	@Override
	public TokenType getType() {
		return TokenType.Constant;
	}

	public String getOriginal() {
		return original;
	}

	public void setOriginal(String orginal) {
		this.original = orginal;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}		
}
