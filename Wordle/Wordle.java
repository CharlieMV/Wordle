import java.util.List;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Font;
import java.util.*;
import java.io.*;
import java.nio.*;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import javax.swing.JDialog;

/**
 *	Wordle.java
 *
 *	Provide a description here.
 *
 *	@author	Scott DeRuiter and David Greenstein and Charles Chang
 *	@version	1.0
 *	@since	October 12, 2023
 */ 
public class Wordle
{ 
	/**	This is a complete list of fields for the game */
	
	/**	A String to store the word that the player is trying to find. */
	private String word;
	
	/**	An array of String to store the guesses that have been made. */
	private String [] wordGuess;
	
	/**	A String to store the letters in the current guess.  Can have from 0 to 5 chars*/
	private String letters;
	
	/**	File that contains 5-letter words to find. */
	private final String WORDS5 = "words5.txt";
	
	/**	File that contains 5-letter words allowed for user guesses. (bigger file) */
	private final String WORDS5_ALLOWED = "words5allowed.txt";
	
	/**	A variety of boolean variables to turn things on and off.  These include:
	 *	show				-	when true, will print the current word to the terminal
	 *	readyForKeyInput    -	when true, will accept keyboard input, when false,
	 *							will not accept keyboard input.
	 *	readyForMouseInput  -	when true, will accept mouse input, when false,+
	 *							will not accept mouse input.
	 *	activeGame          -	when false, will only accept action on the RESET button.
	 */
	private boolean show, readyForKeyInput, readyForMouseInput, activeGame;
	
	/**  An array to determine how to color the keyboard at the bottom of the gameboard.
	 *   0 for not checked yet, 1 for no match, 2 for partial, 3 for exact
	 */
	private int [] keyBoardColors;						
	
	/** 
	 *	Creates a Wordle object.  A constructor.  Initializes all of the variables by 
	 *	calling the method initAll.
	 *	@param testWord		if this String is found in words5allowed.txt, it will
	 *						be used to set word.
	 *	This method is complete.
	 */
	public Wordle(String showIt, String testWord)
	{
		show = false;
		if (showIt.equalsIgnoreCase("show"))
			show = true;
		
		initAll(testWord);
	}
	
	/** 
	 *	Initializes all fields.  Calls openFileAndChooseWord to choose the word.
	 *	Sets all of the keyboard colors to light gray to start.
	 *	@param testWord		if this String is found in words5allowed.txt, it will
	 *						be used to set word.
	 *	This method is complete.
	 */
	public void initAll(String testWord)
	{
		wordGuess = new String[6];
		for(int i = 0; i < wordGuess.length; i++)
		{
			wordGuess[i] = new String("");
		}
		letters = "";
		readyForKeyInput = activeGame = true;
		readyForMouseInput = false;
		keyBoardColors = new int[29];
		word = openFileAndChooseWord(WORDS5, testWord);		
	}

	/**
	 *	The main method, to run the program.  The constructor is called, so that
	 *	all of the fields are initialized.  The canvas is set up, and the GUI
	 *	(the game of Wordle) runs.
	 *	THIS METHOD IS COMPLETE.
	 */
	public static void main(String[] args)
	{
		// Determines if args[0] and args[1] are set
		// args[0] is "show" which means to show the word chosen
		// args[1] is a word which is used as the chosen word
		String testWord = new String("");
		String showIt = new String("");
		switch (args.length) {
			case 2:
			testWord = args[1];
			case 1:
			showIt = args[0];
			default:
			break;
		}
		

		Wordle run = new Wordle(showIt, testWord);
		if (showIt.equals("show")) run.show = true;
		run.setUpCanvas();
		run.playGame();

	}

	/**
	 *	Sets up the canvas.  Enables double buffering so that the gameboard is drawn
	 *	offscreen first, then drawn to the gameboard when everything is ready (with
	 *	the show method).
	 *	This method is complete.
	 */
	public void setUpCanvas ( )
	{
		StdDraw.setCanvasSize(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
		StdDraw.setXscale(0, Constants.SCREEN_WIDTH);
		StdDraw.setYscale(0, Constants.SCREEN_HEIGHT);

		StdDraw.enableDoubleBuffering();
	}
	
	/**
	 *	Runs the game.  An endless loop is created, constantly cycling and looking
	 *	for user input.
	 *	This method is complete.
	 */
	public void playGame ( )
	{
		boolean keepGoing = true;
		while(keepGoing)
		{
			if(activeGame)
			{
				drawPanel();
			}
			update();
		}
	}

	/** 
	 *	If the testWord is valid, it is used as the "goal word".  If it is not, then
	 *	the text file is opened, and a word is chosen at random from the list to be
	 *	the "goal word".  If the field variable show is true, it will print the
	 *	chosen word to the terminal window. Be sure to close file when you are done.
	 *	@param inFileName		this file is to be opened, and a random word is to be
	 *							chosen from it.
	 *	@param testWord			if this String is found in words5allowed.txt, it
	 *							will be used to set word.
	 *	@return					the word chosen as the "goal word".
	 *	THIS METHOD IS COMPLETE 10/17/23.
	 */
	public String openFileAndChooseWord(String inFileName, String testWord)
	{
		int numOfLines = 0;
		/* Open files */
		try {
			File allowedWords = new File(inFileName);
			Scanner reader = new Scanner(allowedWords);
			/* Check next line */
			while (reader.hasNextLine()) {
				/* See if next line is same as testWord*/
				String tempWord = reader.nextLine();
				if (tempWord.equals(testWord)) {
					/* Print the word if showIt is true */
					if (show)
						System.out.println(testWord);
					return testWord;
				}
				numOfLines ++;
			}
			reader.close();
			/* The method should break at the return if the testWord matches
			 * one in the file. Get a random # from 1 to numOfLines, and 
			 * return the word at that line */
			int randomLineNum = (int)(Math.random() * numOfLines) + 1;
			/* New scanner */
			Scanner reader2 = new Scanner(allowedWords);
			/* Scanner gets the (randomLineNum - 1)th line from the file */
			for (int i = 1; i < randomLineNum; i++) {
				String temp = reader2.nextLine();
			}
			String newWord = reader2.nextLine();
			/* Print the word if showIt is true */
			if (show)
				System.out.println(newWord);
			/* Returns the word at line randomLineNum */
			return newWord;
		}
		catch (IOException e) {
			System.out.print(e);
		}
		return "";
	}

	/** 
	 *	Checks to see if the word in the parameter list is found in the text file
	 *	words5allowed.txt
	 *	Returns true if the word is in the file, false otherwise.
	 *	@param possibleWord       the word to looked for in words5allowed.txt
	 *	@return                   true if the word is in the text file, false otherwise
	 *	THIS METHOD IS COMPLETE 10/24/23.
	 */
	public boolean inAllowedWordFile(String possibleWord)
	{
		Scanner input = FileUtils.openToRead(WORDS5_ALLOWED);;
		while(input.hasNextLine()) {
			if (input.nextLine().toUpperCase().equals(possibleWord.toUpperCase())) {
				input.close();
				return true;
			}
		}
		input.close();
		return false;
	}
	
	/** 
	 *	Processes the guess made by the user.  This method will only be called if
	 *	the field variable letters has length 5.  The guess in letters will need
	 *	to be checked against the words in words5allowed.txt. The method
	 *	inAllowedWordFile will be called for this task.  If the guess in letters
	 *	does not exist in the text file, a message is displayed to the user in the
	 *	form of a JOptionPane with JDialog.
	 *	THIS METHOD IS COMPLETE. 10/24/23
	 */
	public void processGuess ( )
	{
		letters = letters.toUpperCase();
		
		// if guess is in words5allowed.txt then put into guess list
		int guessNumber = 0;
		for(int i = 0; i < wordGuess.length; i++)
		{
			if(wordGuess[i].length() == 5 && inAllowedWordFile(wordGuess[i]))
			{
				guessNumber = i + 1;
			} 
		}
		
		wordGuess[guessNumber] = letters.toUpperCase();
		letters = "";
		
		// if guess is not in words5allowed.txt then print dialog box
		
		if (wordGuess[guessNumber].length() == 5 && 
							!inAllowedWordFile(wordGuess[guessNumber])) {
			JOptionPane pane = new JOptionPane(wordGuess[guessNumber] + 
										" is not a valid word");
			JDialog d = pane.createDialog(null, "INVALID INPUT");
			d.setLocation(365,250);
			d.setVisible(true);
			wordGuess[guessNumber] = "";
		}
	}
	
	/** 
	 *	Draws the entire game panel.  This includes the guessed words, the current
	 *	word being guessed, and all of the letters in the "keyboard" at the bottom
	 *	of the gameboard.  The correct colors will need to be chosen for every letter.
	 *	THIS METHOD IS COMPLETE. 10/24/23
	 */
	public void drawPanel ( )
	{
		// Keep track of keyboard colors before arranging to layout
		int[] tmpKeyColors = new int[29];
		StdDraw.clear(StdDraw.WHITE);
		int[][] colors = new int[6][5];
		// Determine color of guessed letters and draw backgrounds
	 	// 0 for not checked yet, 1 for no match, 2 for partial, 3 for exact
	 	for(int row = 0; row < 6; row++)
		{
			for(int col = 0; col < 5; col++)
			{
				// If letter is not ""
				if(wordGuess[row].length() != 0) {
					// Default to grey
					colors[row][col] = 1;
					// Check all characters of the guess for yellow
					for(int i = 0; i < word.length(); i++) 
						if(word.toUpperCase().charAt(i) == 
								wordGuess[row].toUpperCase().charAt(col))
							colors[row][col] = 2;
					// Green will override yellow
					if(wordGuess[row].toUpperCase().charAt(col) == 
										word.toUpperCase().charAt(col)) 
						colors[row][col] = 3;
				// If letter is ""
				} else colors[row][col] = 0;
			}
		}
	 	
		// draw guessed letter backgrounds
		
		for(int row = 0; row < 6; row++)
		{
			for(int col = 0; col < 5; col++)
			{
				if (wordGuess[row].length() != 0)
					switch(colors[row][col]) {
						case 1: 
						tmpKeyColors[wordGuess[row].toUpperCase().charAt(col) 
																- 'A'] = 1;
						break; case 2:
						tmpKeyColors[wordGuess[row].toUpperCase().charAt(col) 
																- 'A'] = 2;
						break; case 3:
						tmpKeyColors[wordGuess[row].toUpperCase().charAt(col) 
																- 'A'] = 3;
						break; default:
						tmpKeyColors[wordGuess[row].toUpperCase().charAt(col) 
																- 'A'] = 0;
					}
				switch(colors[row][col]) {
						case 1: 
						StdDraw.picture(209 + col * 68, 650 - row * 68, 
												"letterFrameDarkGray.png");
						break; case 2:
						StdDraw.picture(209 + col * 68, 650 - row * 68, 
												"letterFrameYellow.png");

						break; case 3:
						StdDraw.picture(209 + col * 68, 650 - row * 68, 
												"letterFrameGreen.png");

						break; default:
						StdDraw.picture(209 + col * 68, 650 - row * 68, 
												"letterFrame.png");
					}
			}
		}
		
		// Transpose a = 0; b = 1; ... z = 25 to QWERTY Q = 0; W = 1, etc.
		for(int i = 0; i < 27; i++) {
			char letter = 'a';
			switch (i) {
				case 0: letter = 'Q'; break;
				case 1: letter = 'W'; break;
				case 2: letter = 'E'; break;
				case 3: letter = 'R'; break;
				case 4: letter = 'T'; break;
				case 5: letter = 'Y'; break;
				case 6: letter = 'U'; break;
				case 7: letter = 'I'; break;
				case 8: letter = 'O'; break;
				case 9: letter = 'P'; break;
				case 10: letter = 'A'; break;
				case 11: letter = 'S'; break;
				case 12: letter = 'D'; break;
				case 13: letter = 'F'; break;
				case 14: letter = 'G'; break;
				case 15: letter = 'H'; break;
				case 16: letter = 'J'; break;
				case 17: letter = 'K'; break;
				case 18: letter = 'L'; break;
				case 20: letter = 'Z'; break;
				case 21: letter = 'X'; break;
				case 22: letter = 'C'; break;
				case 23: letter = 'V'; break;
				case 24: letter = 'B'; break;
				case 25: letter = 'N'; break;
				case 26: letter = 'M'; break;
			}
			if (letter != 'a')
				keyBoardColors[i] = tmpKeyColors[letter - 'A'];
		}
		
		// draw Wordle board
		Font font = new Font("Arial", Font.BOLD, 12);
		StdDraw.setFont(font);
		StdDraw.picture(Constants.SCREEN_WIDTH / 2, Constants.SCREEN_HEIGHT - 30, "wordle.png");
		
		// draw keyboard with appropriate colors
		StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
		int place = 0;
		String tempWord = "";
		for(int [] pair : Constants.KEYPLACEMENT)
		{
			if(place == 19 || place == 27 || place == 28)
			{
				StdDraw.picture(pair[0], pair[1], "keyBackgroundBig.png");		
			}						
			else
			{
				switch (keyBoardColors[place]) {
					case 1:
					StdDraw.picture(pair[0], pair[1], "keyBackgroundDarkGray.png");
					break; case 2: 
					StdDraw.picture(pair[0], pair[1], "keyBackgroundYellow.png");
					break; case 3:
					StdDraw.picture(pair[0], pair[1], "keyBackgroundGreen.png");
					break; default: 
					StdDraw.picture(pair[0], pair[1], "keyBackground.png");
					break;
				}
			}
			
			StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.text(pair[0], pair[1], Constants.KEYBOARD[place]);
			place++;
		}
		
		// draw guesses
		drawAllLettersGuessed();
		
		StdDraw.show();
		StdDraw.pause(Constants.DRAW_DELAY);
		
		// check if won or lost
		checkIfWonOrLost();
	}
	
	/** 
	 *	This method is called by drawPanel, and draws all of the letters in the
	 *	guesses made by the user.
	 *	This method is complete.
	 */
	public void drawAllLettersGuessed ( )
	{	
		// Draw guessed letters
		Font font = new Font("Arial", Font.BOLD, 34);
		StdDraw.setFont(font);
		int guessNumber = 0;
		for(int i = 0; i < wordGuess.length; i++)
		{
			if(wordGuess[i].length() > 0)
			{
				for(int j = 0; j < wordGuess[i].length(); j++)
				{
					StdDraw.text(209 + j * 68, 644 - i * 68, "" + wordGuess[i].charAt(j));
				}
			}
			if(wordGuess[i].length() == 5)
			{
				guessNumber = i + 1;
			}
		}
		for(int i = 0; i < letters.length(); i++)
		{
			StdDraw.text(209 + i * 68, 644 - guessNumber * 68, "" + letters.substring(i, i+1));
		}
	}

	/** 
	 *	Checks to see if the game has been won or lost.  The game is won if the user
	 *	enters the correct word with a guess.  The game is lost when the user does
	 *	not enter the correct word with the last (6th) guess.  An appropriate message
	 *	is displayed to the user in the form of a JOptionPane with JDialog for a win or a loss.
	 *	THIS METHOD IS COMPLETE 10/24/23.
	 */
	public void checkIfWonOrLost ( )
	{
		String lastWord = "";
		for(int i = 0; i < wordGuess.length; i++)
		{
			if(wordGuess[i].length() == 5)
			{
				lastWord = wordGuess[i];
			}
		}
		
		// declare the winner by matching the word
		if(lastWord.toUpperCase().equals(word.toUpperCase()))
		{
			activeGame = false;
			JOptionPane pane = new JOptionPane(lastWord + " is the word!  Press RESET to begin again");
			JDialog d = pane.createDialog(null, "CONGRATULATIONS!");
			d.setLocation(365,250);
			d.setVisible(true);
		}
		
		// else if all guesses are filled then declare loser
		else if(!wordGuess[5].equals("")) {
			activeGame = false;
			JOptionPane pane = new JOptionPane(word + " was the word!  Press RESET to begin again");
			JDialog d = pane.createDialog(null, "Sorry!");
			d.setLocation(365,250);
			d.setVisible(true);
		}
		
	}
	
	/** 
	 *	This method is constantly looking for keyboard or mouse input from the user,
	 *	and reacting to this input.
	 *	This method is complete.
	 */
	public void update ( )
	{
		if(activeGame)
		{
			respondToKeys();
		}
		respondToMouse();
	}
	
	/** 
	 *	Responds to input from the keyboard.  Will call the method processGuess
	 *	when the user has entered a word to guess.
	 *	This method is complete.
	 */
	public void respondToKeys ( )
	{
		if(readyForKeyInput && StdDraw.hasNextKeyTyped() && 
			StdDraw.isKeyPressed(KeyEvent.VK_BACK_SPACE) && letters.length() > 0)
		{
			letters = letters.substring(0, letters.length() - 1);
			readyForKeyInput = false;
		}
		else if(readyForKeyInput && StdDraw.hasNextKeyTyped() && 
				StdDraw.isKeyPressed(KeyEvent.VK_ENTER) && letters.length() == 5)
		{
			processGuess();
			readyForKeyInput = false;
		}
		else if(readyForKeyInput && StdDraw.hasNextKeyTyped() && letters.length() < 5)
		{
			String letter = "" + StdDraw.nextKeyTyped();
			letter = letter.toUpperCase();
			if(letter.charAt(0) >= 'A' && letter.charAt(0) <= 'Z')
			{
				letters += letter;
			}
			readyForKeyInput = false;
		}
		else
		{
			while(StdDraw.hasNextKeyTyped())
			{	
				StdDraw.nextKeyTyped();
			}
			if(!StdDraw.hasNextKeyTyped())
			{
				readyForKeyInput = true;
			}
		}
	}
	
	/** 
	 *	Responds to input from the mouse, simulating the typing of keys on the
	 *	"keyboard" at the bottom of the game panel. Will call the method processGuess
	 *	when the user has entered a word to guess.
	 *	This method is complete.
	 */
	public void respondToMouse ( )
	{
		if(readyForMouseInput && StdDraw.isMousePressed())
		{
			for(int i = 0; i < Constants.KEYPLACEMENT.length; i++)
			{
				if(StdDraw.mouseX() > Constants.KEYPLACEMENT[i][0] - 22 && 
					StdDraw.mouseX() < Constants.KEYPLACEMENT[i][0] + 22 && 
					StdDraw.mouseY() > Constants.KEYPLACEMENT[i][1] - 29 && 
					StdDraw.mouseY() < Constants.KEYPLACEMENT[i][1] + 29)
				{
					if(i == 28)
					{
						initAll("");
						activeGame = true;
					}
					else if(activeGame && i == 27 && letters.length() > 0)
					{
						letters = letters.substring(0, letters.length() - 1);
					}
					else if(activeGame && i == 19 && letters.length() == 5)
					{
						processGuess();
					}
					else if(activeGame && i != 19 && i != 27 && i != 28 && letters.length() < 5)
					{
						String letter = Constants.KEYBOARD[i].toUpperCase();
						letters += letter;
					}
				}
			}
			readyForMouseInput = false;
		}
		else if(!StdDraw.isMousePressed())
		{
			readyForMouseInput = true;
		}
	}
}
