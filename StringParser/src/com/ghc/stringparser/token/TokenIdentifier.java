package com.ghc.stringparser.token;

import com.ghc.stringparser.datatype.FloatingPoint;

public class TokenIdentifier extends Token {
	private String original;
	private FloatingPoint value;

	@Override
	public TokenType getType() {
		return TokenType.Identifier;
	}

	public String getOriginal() {
		return original;
	}

	public void setOriginal(String orginal) {
		this.original = orginal;
	}

	public FloatingPoint getValue() {
		return value;
	}

	public void setValue(FloatingPoint value) {
		this.value = value;
	}	
	
}
