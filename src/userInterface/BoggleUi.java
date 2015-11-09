
package userInterface;

import core.Board;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

/**
 *
 * @author Adrian Melendez
 */
public class BoggleUi extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	// Menu Components
	private static JMenuBar menuBar;
	private static JMenu gameMenu;
	private static JMenuItem newGame;
	private static JMenuItem exitGame;
	
	// Lay out the UI
	private static JPanel dicePanel;
	private static DieButton[][] dieGrid;
	private static final int GRID = 4;
	private static Board dieBoard;
	private static ArrayList<String> gridList;
	private static DieListener dieListener;
	
	private static JPanel inputPanel;
	private static JEditorPane inputArea;
	private static JScrollPane inputScroll;
	private static HTMLEditorKit kit;
	private static HTMLDocument doc;
	private static JLabel timerLabel;
	private static JButton shakeButton;
	
	private static JPanel infoPanel;
	private static JLabel wordLabel;
	private static String wordString;
	private static JButton submitButton;
	private static JLabel scoreLabel;
	private static int scoreInt;
	
	// Dictionary List
	private static ArrayList<String> dictionaryList;
	
	// Used Word List to prevent double scoring
	private static ArrayList<String> usedWordList;
	
	// Random Number List for Computer Scoring
	//private static ArrayList<Integer> computerIndexList;
	
	// Timer Thread Stopping Variable
	private static boolean isTimerRunning;
	
	// Time in seconds
	private static int totalTime;
	
	/**
	 * 
	 * @param board, dictionary
	 * 		Board board contains the information needed to generate the game board.
	 * 		ArrayList dictionary contains the list to be used to score the game.
	 */
	public BoggleUi(Board board, ArrayList<String> dictionary) {
		dieBoard = board;
		gridList = dieBoard.shakeDice();
		dictionaryList = dictionary;
		setResizable(false);
		this.initComponents();
	}
	
	/**
	 * 	Initializes the UI components.
	 */
	private void initComponents() {
		this.setTitle("Boggle");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(600, 575));
		
		initMenuBar();
		initDiceGridPanel();
		initInputPanel();
		initInfoPanel();
		addPanels();
		
		this.setVisible(true);
	}
	
	/**
	 * 	Initializes the Menu Bar components.
	 */
	private void initMenuBar() {
		// Menu Bar
		menuBar = new JMenuBar();
		gameMenu = new JMenu("Boggle");
		gameMenu.setMnemonic('B');
		menuBar.add(gameMenu);
		this.setJMenuBar(menuBar);
		
		// New Game
		newGame = new JMenuItem("New Game");
		newGame.setMnemonic('N');
		newGame.addActionListener(new NewGameListener());
		gameMenu.add(newGame);
		
		// Exit Game
		exitGame = new JMenuItem("Exit Game");
		exitGame.setMnemonic('E');
		exitGame.addActionListener(new ExitListener());
		gameMenu.add(exitGame);
	}
	
	/**
	 * 	Initializes the dice grid.
	 */
	private void initDiceGridPanel() {
		dicePanel = new JPanel(new GridLayout(GRID, GRID));
		dicePanel.setPreferredSize(new Dimension(400, 400));
		dicePanel.setBorder(BorderFactory.createTitledBorder("Boggle Board"));
		dieListener = new DieListener();
		int x, y;
		//int dieCounter = 0;
		dieGrid = new DieButton[GRID][GRID];
		for (y = 0; y < GRID; y++) {
			for (x = 0; x < GRID; x++) {
				dieGrid[y][x] = new DieButton("");
				dieGrid[y][x].setFont(new Font("Arial", Font.BOLD, 40));
				dieGrid[y][x].setFocusable(false);
				dieGrid[y][x].setRow(y);
				dieGrid[y][x].setCol(x);
				dieGrid[y][x].addActionListener(dieListener);
				dicePanel.add(dieGrid[y][x]);
			}
		}
	}
	
	/**
	 * 	Initializes the input panel components.
	 */
	private void initInputPanel() {
		inputPanel = new JPanel();
		inputPanel.setBorder(BorderFactory.createTitledBorder("Enter Words Found"));
		
		// User Text Input Area
		//   Shows the words the user has entered.
		usedWordList = new ArrayList<String>();
		inputArea = new JEditorPane("text/html", "");
		kit = new HTMLEditorKit();
		doc = new HTMLDocument();
		inputArea.setEditorKit(kit);
		inputArea.setDocument(doc);
		inputArea.setText("");
		inputArea.setEditable(false);
		inputScroll = new JScrollPane(inputArea);
		inputScroll.setPreferredSize(new Dimension(180, 250));
		inputScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		inputScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		inputPanel.add(inputScroll);
		
		// Timer Label
		timerLabel = new JLabel("3:00", SwingConstants.CENTER);
		timerLabel.setBorder(BorderFactory.createTitledBorder("Timer"));
		timerLabel.setPreferredSize(new Dimension(180, 80));
		timerLabel.setFont(new Font("Arial", Font.BOLD, 50));
		inputPanel.add(timerLabel);
		
		// Shake Button
		shakeButton = new JButton("Shake Dice");
		shakeButton.setPreferredSize(new Dimension(170, 50));
		shakeButton.setFont(new Font("Arial", Font.BOLD, 24));
		shakeButton.setFocusable(false);
		shakeButton.addActionListener(new ShakeListener());
		inputPanel.add(shakeButton);
	}
	
	/**
	 * 	Initializes the info pane components.
	 */
	private void initInfoPanel() {
		// Info Panel
		infoPanel = new JPanel();
		infoPanel.setBorder(BorderFactory.createTitledBorder(""));
		
		// Current Word Label
		wordString = "";
		wordLabel = new JLabel("   " + wordString, SwingConstants.LEFT);
		wordLabel.setBorder(BorderFactory.createTitledBorder("Current Word"));
		wordLabel.setPreferredSize(new Dimension(280, 65));
		wordLabel.setFont(new Font("Arial", Font.BOLD, 16));
		infoPanel.add(wordLabel);
		
		// Submit Button
		submitButton = new JButton("Submit Word");
		submitButton.setPreferredSize(new Dimension(160, 65));
		submitButton.setFont(new Font("Arial", Font.BOLD, 18));
		submitButton.setFocusable(false);
		submitButton.addActionListener(new SubmitListener());
		infoPanel.add(submitButton);
		
		// Score Label
		scoreInt = 0;
		scoreLabel = new JLabel("", SwingConstants.LEFT);
		scoreLabel.setBorder(BorderFactory.createTitledBorder("Score"));
		scoreLabel.setPreferredSize(new Dimension(110, 65));
		scoreLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		infoPanel.add(scoreLabel);
	}
	
	/**
	 * 	Adds all panels to the main JFrame
	 */
	private void addPanels() {
		this.add(dicePanel, BorderLayout.WEST);
		this.add(inputPanel, BorderLayout.CENTER);
		this.add(infoPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * 
	 * @param len
	 * 		Int len contains the length of the word.
	 * @return
	 * 		Returns the score for the word.
	 */
	private int wordScore(int len) {
		int score = 0;
		if (len == 3 || len == 4){
			score = 1;
			} else if (len == 5) {
				score = 2;
			} else if (len == 6) {
				score = 3;
			} else if (len == 7) {
				score = 5;
			} else if (len > 7) {
				score = 11;
			}
		
		return score;
	}
	
	// Extension of JButton to store extra information
	/**
	 * 
	 * 	Extension of JButton to store extra information
	 *
	 */
	private class DieButton extends JButton {
		private static final long serialVersionUID = 1L;

		// Letter is available for use. 
		private boolean available = true;
		
		// Position in the grid
		private int row;
		private int col;
		
		public DieButton (String str) {
			this.setText(str);
		}

		public boolean isAvailable() {
			return available;
		}

		public void setAvailable(boolean available) {
			this.available = available;
		}

		public int getRow() {
			return row;
		}

		public void setRow(int row) {
			this.row = row;
		}

		public int getCol() {
			return col;
		}

		public void setCol(int col) {
			this.col = col;
		}
	}

	/**
	 * 
	 * 	ActionListener attached to the Exit option in the Boggle menu.
	 * 	Exits the game.
	 *
	 */
	private class ExitListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			int response = JOptionPane.showConfirmDialog(null, "Do you want to exit Boggle?",
					"Exit?", JOptionPane.YES_NO_OPTION);
			
			if (response == JOptionPane.YES_OPTION)
				System.exit(0);
		}
	}
	
	/**
	 * 
	 * 	ActionListener attached to the New Game option in the Boggle menu.
	 * 	Starts a new game.
	 *
	 */
	private class NewGameListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			inputArea.setText(null);
			timerLabel.setText("3:00");
			isTimerRunning = false;
			shakeButton.setEnabled(true);
			wordString = "";
			wordLabel.setText("");
			submitButton.setEnabled(true);
			//submitButton.setForeground(null);
			//submitButton.setText("Submit Word");
			scoreInt = 0;
			scoreLabel.setText("");
			usedWordList = new ArrayList<String>();
			for (Component x : dicePanel.getComponents()) {
				((DieButton)x).setEnabled(true);
				((DieButton)x).setAvailable(true);
				((DieButton)x).setText("");
				((DieButton)x).setBackground(null);
			}
		}
	}
	
	/**
	 * 
	 * 	ActionListener attached to the Shake Button. Generates and randomizes the game board.
	 *
	 */
	private class ShakeListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			int counter = 0;
			gridList = dieBoard.shakeDice();
			for (Component x : dicePanel.getComponents()) {
				((DieButton)x).setText(gridList.get(counter++).toString());
			}
			scoreLabel.setText(null);
			wordLabel.setText("   " + wordString);
			timerLabel.setText("3:00");
			submitButton.setEnabled(true);
			shakeButton.setEnabled(false);
			
			GameTimer gameTimer = new GameTimer();
			new Thread(gameTimer).start();
		}
	}
	
	/**
	 * 
	 * 	ActionListener attached to the Submit Button. 
	 * 	Submits current word to be checked for correctness and uniqueness then
	 * 	scores the word accordingly and adds it to the text area if valid.
	 *
	 */
	private class SubmitListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!(usedWordList.contains(wordString))) {
				for (String x : dictionaryList) {
					if (x.equalsIgnoreCase(wordString)) {
						int len = x.length();
						if (len > 2) {
							try {
								usedWordList.add(wordString);
								kit.insertHTML(doc, doc.getLength(),wordString, 0, 0, null);
							} catch (BadLocationException | IOException exc) {
							}
						}
						
						scoreInt += wordScore(len);
						
						break;
					}
				}
			} else {
				// The code below enables an annoying pop up. I recommend not using it.
				/*
				if (!wordString.equalsIgnoreCase("")){
					JOptionPane.showMessageDialog(null, "DUPLICATE",
					"DUPLICATE", JOptionPane.ERROR_MESSAGE);
				}
				*/
			}
			wordString = "";
			wordLabel.setText("   " + wordString);
			if (scoreInt != 0) {
				scoreLabel.setText("   " + scoreInt);
			}
			if (isTimerRunning) {
				for (Component x : dicePanel.getComponents()) {
					((DieButton)x).setEnabled(true);
					((DieButton)x).setAvailable(true);
					((DieButton)x).setBackground(null);
				}
			} else {
				// It is possible to allow the submit button to be pressed after the timer
				//   has run out. Doing so requires removing the code in the Timer thread 
				//   that disables the button and also uncommenting the line of code below.
				//submitButton.setEnabled(false);
			}
		}
	}
	
	/**
	 * 
	 * 	ActionListener attached to each Die Button. 
	 * 	Adds the die letter to the current word and disables all buttons that 
	 * 	aren't selectable according to the rules of the game.
	 *
	 */
	private class DieListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (isTimerRunning && e.getSource() instanceof DieButton) {
				DieButton button = (DieButton)e.getSource();
				wordString = wordString + button.getText().toUpperCase();
				wordLabel.setText("   " + wordString);
				button.setAvailable(false);
				button.setBackground(Color.pink);
				for (Component x : dicePanel.getComponents()) {
					((DieButton)x).setEnabled(false);
				}
				int row = button.getRow();
				int col = button.getCol();
				int x, y;
				for (y = row - 1; y <= row + 1; y++) {
					if (y >= 0 && y < 4) {
						for (x = col - 1; x <= col + 1; x++) {
							if (x >= 0 && x < 4) {
								if (dieGrid[y][x].isAvailable()) {
									dieGrid[y][x].setEnabled(true);
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * 	Thread for the timer. When the timer thread is active the game can be  
	 * 	played. When time expires, most of the game disables. 
	 *
	 */
	private class GameTimer implements Runnable{
		
		@Override
		public void run() {
			this.countdown();
		}
		
		public void countdown() {
			isTimerRunning = true;
			totalTime = 180; // seconds
			while (totalTime >= 0) {
				timerLabel.setText(totalTime/60 +":"+ String.format("%02d", (totalTime%60)));
				try {
					totalTime--;
					Thread.sleep(1000L);
					if (!isTimerRunning) {
						break;
					}
				}
				catch (InterruptedException e) {
				}
			}
			isTimerRunning = false;
			if (totalTime < 0) {
				submitButton.setEnabled(false);
				for (Component x : dicePanel.getComponents()) {
					((DieButton)x).setEnabled(false);
					((DieButton)x).setBackground(null);
				}
				
				// The code below enables a crappy AI. I recommend not using it.
				/*
				JOptionPane.showMessageDialog(null, "Comparing words with what the computer found...",
				"GAME OVER", JOptionPane.INFORMATION_MESSAGE);
				
				// List to be used to make a list of indexes for the computer to find
				computerIndexList = new ArrayList<Integer>();
				
				int listSize = usedWordList.size();
				
				// Loop to fill the list with all the possible indexes
				for (int counter = 0; counter < listSize; counter++) {
					computerIndexList.add(counter);
				}
				
				// Randomizing the list so part of it can be used
				Collections.shuffle(computerIndexList);
				
				// Finding a random number of words for the computer to claim
				Random rnd = new Random();
				int computerWordCount = rnd.nextInt(listSize + 1);
				listSize--;
				
				// Starting from the end of the computerIndexList, removing items until
				//   the size of the list equals the total words the computer will claim
				while (listSize >= computerWordCount) {
					computerIndexList.remove(listSize--);
				}
				
				// Clearing the inputArea of text.
				inputArea.setText("");
				
				// String to store the words the computer claimed. Will be space separated.
				String compFound = "";
				
				// Iterating over the player's list. If the index of a word exists in
				//   computerIndexList, that word is claimed by the computer and will be
				//   struck out and removed from the player's score.
				int x;
				int len;
				for (x = 0; x < usedWordList.size(); x++) {
					len = usedWordList.get(x).length();
					if (computerIndexList.contains(x)) {
						try {
							scoreInt -= wordScore(len);
							kit.insertHTML(doc, doc.getLength(), 
								"<font style=\"text-decoration:line-through\">"+usedWordList.get(x)+"</font>", 0, 0, null);
							compFound = compFound + "\n" + usedWordList.get(x);
						} catch (BadLocationException | IOException exc) {
						}
					} else {
						try {
							kit.insertHTML(doc, doc.getLength(), usedWordList.get(x), 0, 0, null);
						} catch (BadLocationException | IOException exc) {
						}
					}
				}
				scoreLabel.setText("   " + scoreInt);
				JOptionPane.showMessageDialog(null, "The computer found these words:"+compFound,
				"GAME OVER", JOptionPane.INFORMATION_MESSAGE);
				*/
				
			}
		}
	}
}