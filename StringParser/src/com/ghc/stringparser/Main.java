package com.ghc.stringparser;

import java.io.Console;
import java.util.List;

import com.ghc.stringparser.token.Token;
import com.ghc.stringparser.util.Log;

public class Main {
	public static void main(String[] args) {
		Log.enableDebug();

		Console console = System.console();		
		if (console == null) {
			Log.d("No Console!!!");
		} else {
			String input = "";
			while (true) {
				input = console.readLine("Input:");
				if (input != null && input.length() > 0) {
					if(input.equals("exit")){
						break;
					}else{
						doTask(input);
					}
				}
			}
		}
	}
	
	private static void doTask(String input){
		Lexer lexer = new Lexer();
		List<Token> tokens = lexer.parse(input);
		lexer.printTokens(tokens);

		Log.d("\nParser\n");

		Parser parser = new Parser();
		List<Token> parserTokens = parser.rpnParser(tokens);
		parser.printTokens(parserTokens);

		Log.d("\nEvaluator\n");

		Evaluator evaluator = new Evaluator();
		int result = evaluator.evaluate(parserTokens);
		Log.d("Result: %d", result);
	}
}
