
package inputOutput;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;


/**
 *
 * @author Adrian
 */
public class ReadDataFile {
	private InputStream in;
	private Scanner readFile;
	private String fileName;
	private ArrayList<String> data = new ArrayList<String>();
	
	/**
	 * 
	 * @param fileN
	 * 		String fileN contains the name of the file to be read in.
	 */
	public ReadDataFile(String fileN) {
		this.fileName = fileN;
	}
	
	/**
	 * 	Reads the file referred to in fileName and populates a list
	 */
	public void populateData() {
		try {
			//System.out.println(new File(fileName).getAbsolutePath());
			in = getClass().getResourceAsStream(fileName);
			readFile = new Scanner(in);
			
			while(readFile.hasNext()) {
				data.add(readFile.next());
			}
		} 
		catch(Exception ex) {
			System.out.println(ex.toString());
			ex.printStackTrace();
			System.out.println(fileName);
		}
		finally {
			readFile.close();
		}
	}
	
	public ArrayList<String> getData() {
		return data;
	}
}
