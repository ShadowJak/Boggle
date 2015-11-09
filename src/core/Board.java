
package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author Adrian
 */
public class Board {
	private final int NUMBER_OF_DICE = 16;
	private final int NUMBER_OF_SIDES = 6;
	
	// contains data for each side of each die
	private ArrayList<String> diceData = new ArrayList<String>();
	
	// contains each Die object
	private ArrayList<Die> dice = new ArrayList<Die>();
	
	/**
	 * 
	 * @param list
	 * 		ArrayList list contains the die faces to be turned into Die Objects
	 */
	public Board (ArrayList<String> list) {
		diceData = list;
		this.populateDice();
	}
	
	/**
	 * 	Creates Die objects from the data in diceData and adds them to the dice field
	 */
	public void populateDice() {
		Die temp;
		int x, y, counter;
		counter = 0;
		for (x = 0; x < NUMBER_OF_DICE; x++) {
			temp = new Die();
			for (y = 0; y < NUMBER_OF_SIDES; y++) {
				temp.addLetter(diceData.get(counter).toString());
				counter++;
			}
			dice.add(temp);
		}
	}
	
	/**
	 * 
	 * @return
	 * 		Returns an ArrayList containing a random letter from each die in random order
	 */
	public ArrayList<String> shakeDice() {
		int x;
		ArrayList<String> temp = new ArrayList<String>();
		
		for (x = 0; x < NUMBER_OF_DICE; x++) {
			temp.add(dice.get(x).getLetter());
		}
		
		long seed = System.nanoTime();
		Collections.shuffle(temp, new Random(seed));
		
		return temp;
	}
}
