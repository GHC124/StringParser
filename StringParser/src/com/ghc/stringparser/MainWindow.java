package com.ghc.stringparser;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;

import com.ghc.stringparser.util.Log;

public class MainWindow {
	private JFrame frame;
	private JTextArea ouputArea;
	private JTextArea inputArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
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
		frame.setBounds(100, 100, 527, 467);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Input:");
		lblNewLabel.setBounds(10, 10, 46, 14);
		frame.getContentPane().add(lblNewLabel);

		JButton btnNewButton = new JButton("Evaluate");
		btnNewButton.addActionListener(evaluateActionListener);
		btnNewButton.setBounds(10, 120, 89, 23);
		frame.getContentPane().add(btnNewButton);
		
		JLabel lblNewLabel_1 = new JLabel("Log:");
		lblNewLabel_1.setBounds(10, 154, 46, 14);
		frame.getContentPane().add(lblNewLabel_1);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 187, 499, 206);
		frame.getContentPane().add(scrollPane);

		ouputArea = new JTextArea();
		scrollPane.setViewportView(ouputArea);
		ouputArea.setEditable(false);

		JButton btnClear = new JButton("Clear");
		btnClear.setBounds(10, 404, 89, 23);
		btnClear.addActionListener(clearActionListener);

		frame.getContentPane().add(btnClear);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 30, 499, 79);
		frame.getContentPane().add(scrollPane_1);

		inputArea = new JTextArea();
		scrollPane_1.setViewportView(inputArea);
		
		JButton btnReadMe = new JButton("Read Me");
		btnReadMe.addActionListener(readMeActionListener);
		btnReadMe.setBounds(422, 404, 89, 23);
		frame.getContentPane().add(btnReadMe);
	}

	private ActionListener evaluateActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			ouputArea.setText("");

			Log.enableDebug();
			
			StringBuilder output = new StringBuilder();
			List<String> inputs = new ArrayList<String>();
			Element paragraph = inputArea.getDocument().getDefaultRootElement();
			int contentCount = paragraph.getElementCount();
			for (int i = 0; i < contentCount; i++) {
				Element el = paragraph.getElement(i);
				int rangeStart = el.getStartOffset();
				int rangeEnd = el.getEndOffset();
				String line;
				try {
					line = inputArea.getText(rangeStart, rangeEnd - rangeStart);
					if (line != null && line.length() > 0) {
						inputs.add(line);
					}
				} catch (BadLocationException ex) {
					ex.printStackTrace();
				}
			}

			StringParser stringParser = new StringParser();
			stringParser.execute(inputs, output);

			ouputArea.setText(output.toString());
		}
	};
	
	private ActionListener readMeActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			StringBuilder message = new StringBuilder();
			message.append("Support:\n");
			message.append("+ functions: min(), max() \n");
			message.append("+ operators: +, -, *, / \n");
			message.append("+ negative number: - (-1+2) \n");
			message.append("+ floating point number: m.n (1.2) \n     m < 5 characters \n     n < 3 characters \n");
			message.append("+ group: (, ) \n");
			message.append("+ assigment: one character length (x=1+2) \n");
			message.append("+ mutil line: \n    x=1+2 \n    y=x+min(x,max(2,3)) \n");
			
			JOptionPane.showMessageDialog(frame, message.toString());
		}
	};

	private ActionListener clearActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			ouputArea.setText("");
		}
	};
}
