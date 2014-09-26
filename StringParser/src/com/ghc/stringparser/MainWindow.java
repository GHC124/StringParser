package com.ghc.stringparser;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.ghc.stringparser.token.Token;
import com.ghc.stringparser.util.Log;

public class MainWindow {
	private JFrame frame;
	private JTextField textField;
	private JTextArea textArea;
	
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
		frame.setBounds(100, 100, 526, 385);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Input:");
		lblNewLabel.setBounds(10, 10, 46, 14);
		frame.getContentPane().add(lblNewLabel);

		textField = new JTextField();
		textField.setBounds(10, 30, 499, 32);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		JButton btnNewButton = new JButton("Evaluate");
		btnNewButton.addActionListener(evaluateActionListener);
		btnNewButton.setBounds(10, 73, 89, 23);
		frame.getContentPane().add(btnNewButton);

		JLabel lblNewLabel_1 = new JLabel("Log:");
		lblNewLabel_1.setBounds(10, 107, 46, 14);
		frame.getContentPane().add(lblNewLabel_1);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 130, 499, 181);
		frame.getContentPane().add(scrollPane);

		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setEditable(false);

		JButton btnClear = new JButton("Clear");
		btnClear.setBounds(10, 322, 89, 23);
		btnClear.addActionListener(clearActionListener);
		
		frame.getContentPane().add(btnClear);
	}

	private ActionListener evaluateActionListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			textArea.setText("");
			
			String input = textField.getText();

			StringBuilder output = new StringBuilder();
			
			Lexer lexer = new Lexer();
			List<Token> tokens = lexer.parse(input);
			lexer.printTokens(tokens, output);

			output.append("\nParser\n");

			Parser parser = new Parser();
			List<Token> parserTokens = parser.rpnParser(tokens);
			parser.printTokens(parserTokens, output);

			output.append("\nEvaluator\n");

			Evaluator evaluator = new Evaluator();
			int result = evaluator.evaluate(parserTokens);
			output.append(String.format("\nResult: %d", result));
			
			textArea.setText(output.toString());
		}
	};
	
	private ActionListener clearActionListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			textArea.setText("");
		}
	};	
}
