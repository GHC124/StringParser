package com.ghc.stringparser;

public class Token {
	public enum TokenType{
		Constant, Operator, GroupStart, GroupEnd, Function, Argument
	}
	
	private TokenType type;
	private String original;
	
	public TokenType getType() {
		return type;
	}
	public void setType(TokenType type) {
		this.type = type;
	}
	public String getOriginal() {
		return original;
	}
	public void setOriginal(String original) {
		this.original = original;
	}
	
	
}
