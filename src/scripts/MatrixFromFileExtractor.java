package scripts;


import java.util.*;
import java.io.*;

/**
 * The {@code MatrixFromFileExtractor} class is responsible for reading a file 
 * containing a matrix and converting it into a 2D string array. It also includes 
 * methods for debugging and converting data structures for matrix manipulation.
 * 
 * @author Davide Di Stefano
 * @version 1.2.0
 * @since 1.0.0
 */

public class MatrixFromFileExtractor {
	private static String[][] gameMapCopy;
	
	
	/**
     * Extracts a matrix from a specified file and converts it into a 2D string array.
     * Each row of the file represents a row in the matrix, with values separated by spaces.
     *
     * @param filepath the file path of the matrix data source(Strings separated by spaces)
     * @return a 2D string array representing the extracted matrix.
     */
	public static String[][] MatrixExtractor(String filepath){
		
		// ArrayList is used in beginning for simpler assembling of the game board
		ArrayList<String[]> gameMap = new ArrayList<String[]>();
		
		// FIle Reading
		try (InputStream is = MatrixFromFileExtractor.class.getResourceAsStream(filepath);
			    BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
		        String line;
		        while ((line = reader.readLine()) != null) {
		        	line = line.strip();
		            String[] boardRow = line.split(" ");
		            gameMap.add(boardRow);
		         }
		        
		 }catch (IOException e) {
		        System.out.println("Error reading file: " + e.getMessage());
		    }
		 
		 // Arraylist gameMap is converted to a 2d array for faster accessing
		 String[][] fasterGameMap = convertToArrayOfArrays(gameMap);
		 gameMapCopy = deepCopy(fasterGameMap);
		 return fasterGameMap;
	}
	/**
	 * Creates a deep copy of a 2D array of strings.
	 * This method ensures that the copied array is independent of the original array,
	 * meaning that changes to the copy do not affect the original and vice versa.
	 * Useful to avoid to read a file more than once
	 * 
	 * @param original GameMap The 2D array of strings to be copied. Can be {@code null}.
	 * @return A new 2D array containing the same elements as the original, 
	 *         or {@code null} if the original is {@code null}.
	 */
	public static String[][] deepCopy(String[][] originalGameMap) {
	    if (originalGameMap == null) return null; // Handle null case
	    
	    String[][] gameMapCopy = new String[originalGameMap.length][];
	    for (int i = 0; i < originalGameMap.length; i++) {
	        gameMapCopy[i] = Arrays.copyOf(originalGameMap[i], originalGameMap[i].length);
	    }
	    return gameMapCopy;
	}

	
    /**
     * Converts an {@code ArrayList} of string arrays into a 2D string array.
     * This transformation ensures faster access and manipulation of matrix data.
     *
     * @param arrayList the {@code ArrayList} containing the matrix datas.
     * @return a 2D string array containing the definitive matrix.
     */
	public static String[][] convertToArrayOfArrays(ArrayList<String[]> arrayList) {
	    // Create a new array of arrays with the same size as the ArrayList
	    String[][] arrayOfArrays = new String[arrayList.size()][];
	    
	    // Use toArray() to populate the array of arrays
	    arrayOfArrays = arrayList.toArray(arrayOfArrays);
	    
	    return arrayOfArrays;
	}
	
    /**
     * Prints the contents of the provided matrix for debugging purposes.
     * Displays the matrix in a structured format followed by its object type.
     *
     * @param fasterGameMap the 2D string array representing the game map.
     */

	public static void debugMatrixPrinter(String[][] fasterGameMap ) {
		 System.out.println("Game Map Contents:");
		    for (String[] row : fasterGameMap) {
		        for (String cell : row) {
		            System.out.print(cell + " "); // Print each element in the row
		        }
		        System.out.println(); // Newline for each row
			}
		    System.out.println("\n Object type"+fasterGameMap.getClass().getName());
	}
	
	public static String[][] resetGameMap(){
		return gameMapCopy;
	}
}