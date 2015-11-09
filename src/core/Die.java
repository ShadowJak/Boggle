
package core;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Adrian
 */
public class Die {
	private final int NUMBER_OF_SIDES = 6;
	
	// stores data for the sides of a Die
	private ArrayList<String> diceSides = new ArrayList<String>();
	
	// stores the current face of the Die
	private String letter;
	
	/**
	 * 	Sets letter to a random side from diceSides.
	 */
	public void randomLetter() {
		Random temp = new Random();
		letter = diceSides.get(temp.nextInt(NUMBER_OF_SIDES));
	}
	
	/**
	 * 
	 * @return
	 * 		Returns a randomized letter from the Die object.
	 */
	public String getLetter() {
		this.randomLetter();
		return letter;
	}
	
	// adds a letter to diceSides
	/**
	 * 
	 * @param ltr
	 * 		String ltr contains a letter to be added to the list of dice sides.
	 */
	public void addLetter(String ltr) {
		diceSides.add(ltr);
	}
	
}
