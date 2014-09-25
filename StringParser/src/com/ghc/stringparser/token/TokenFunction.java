package com.ghc.stringparser.token;

public class TokenFunction extends Token {
	private String original;
	private int argCount;
	
	@Override
	public TokenType getType() {
		return TokenType.Function;
	}

	public String getOriginal() {
		return original;
	}

	public void setOriginal(String orginal) {
		this.original = orginal;
	}

	public int getArgCount() {
		return argCount;
	}

	public void setArgCount(int argCount) {
		this.argCount = argCount;
	}	
	
	
}
