// Adrian Melendez
// Assignment
// Written on a Win7 computer

package boggle;

import core.Board;
import inputOutput.ReadDataFile;
import java.util.ArrayList;
import userInterface.BoggleUi;

/**
 *
 * @author Adrian Melendez
 */
public class Boggle {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		// Contains data for each side of each die.
		ArrayList<String> diceList = new ArrayList<String>();
		
		// Contains all the words in the dictionary for scoring.
		ArrayList<String> dictionaryList = new ArrayList<String>();
		
		// File name for the file with the dice information.
		String diceFileName = "BoggleData.txt";//"src\\inputOutput\\BoggleData.txt";
		
		// File name for the file with all the words in the scoring dictionary.
		String dictionaryFileName = "AllWordsFixed.txt";//"src\\inputOutput\\allWords.txt";
		
		// Create objects to read in data from the files.
		ReadDataFile diceData = new ReadDataFile(diceFileName);
		ReadDataFile dictionaryData = new ReadDataFile(dictionaryFileName);
		
		// Populates the objects with the data in the files.
		diceData.populateData();
		dictionaryData.populateData();
		
		// Retrieves the data from the objects and puts the data into ArrayLists
		diceList = diceData.getData();
		dictionaryList = dictionaryData.getData();
		
		// Passes the list of die faces to the board object where the die
		//   objects and list of randomized faces will be generated
		Board board = new Board(diceList);
		
		// Creates the user interface.
		@SuppressWarnings("unused")
		BoggleUi boggle = new BoggleUi(board, dictionaryList);
	}
}
