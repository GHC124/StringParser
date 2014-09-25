package com.ghc.stringparser;

import java.awt.EventQueue;
import java.util.List;

import javax.swing.JFrame;

import com.ghc.stringparser.token.Token;
import com.ghc.stringparser.util.Log;

public class MainWindow {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
					
					Log.enableDebug();
					
					String input = "1+20*(140-3)/10+2";
					
					Log.d("Input: %s\n\n", input);
					
					Lexer lexer = new Lexer();
					List<Token> tokens = lexer.parse(input);
					lexer.printTokens(tokens);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
