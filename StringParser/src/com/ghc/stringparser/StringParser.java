package com.ghc.stringparser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ghc.stringparser.token.Token;
import com.ghc.stringparser.token.TokenConstant;
import com.ghc.stringparser.token.TokenIdentifier;
import com.ghc.stringparser.token.TokenType;

public class StringParser {
	public void execute(List<String> inputs, StringBuilder output) {
		Map<String, TokenIdentifier> identifiers = new HashMap<String, TokenIdentifier>();
		for (String input : inputs) {
			output.append("Lexer\n");

			Lexer lexer = new Lexer();
			List<Token> tokens = lexer.parse(input);
			lexer.printTokens(tokens, output);

			// Check identifier
			int identifierCount = lexer.getIdentifierCount();
			if (identifierCount > 0 && !identifiers.isEmpty()) {
				int count = 0;
				// Replace exit identifier
				for (int i = 0; i < tokens.size(); i++) {
					if (tokens.get(i).getType() == TokenType.Identifier) {
						count++;
						TokenIdentifier item = (TokenIdentifier) tokens.get(i);
						TokenIdentifier exitIdentifier = identifiers.get(item
								.getOriginal());
						if (exitIdentifier != null) {

							output.append(String.format(
									"Replace identifier: %s=%d\n",
									exitIdentifier.getOriginal(),
									exitIdentifier.getValue()));

							TokenConstant tokenConstant = new TokenConstant();
							copyToken(tokenConstant, exitIdentifier);
							tokens.set(i, tokenConstant);
						}
						if (count == identifierCount) {
							break;
						}
					}
				}
			}

			output.append("\nParser\n");

			Parser parser = new Parser();
			List<Token> parserTokens = parser.rpnParser(tokens);
			parser.printTokens(parserTokens, output);

			output.append("\nEvaluator\n");

			Evaluator evaluator = new Evaluator();
			TokenIdentifier tokenIdentifier = evaluator.evaluate(parserTokens);
			if (tokenIdentifier.getOriginal() == null) {
				output.append(String.format("Result: %f",
						evaluator.getFloatNumber(tokenIdentifier.getValue())));
			} else {
				identifiers.put(tokenIdentifier.getOriginal(), tokenIdentifier);
				output.append(String.format("Identifier: %s = %f",
						tokenIdentifier.getOriginal(),
						evaluator.getFloatNumber(tokenIdentifier.getValue())));
			}
			output.append("\n\n");
		}
	}

	private static void copyToken(TokenConstant tokenConstant,
			TokenIdentifier tokenIdentifier) {
		tokenConstant.setOriginal(String.valueOf(tokenIdentifier.getValue()));
		tokenConstant.setStart(tokenIdentifier.getStart());
		tokenConstant.setEnd(tokenIdentifier.getEnd());
		tokenConstant.setValue(tokenIdentifier.getValue());
	}
}
