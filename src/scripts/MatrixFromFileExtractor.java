package scripts;


import java.util.*;
import java.io.*;

/**
 * The {@code MatrixFromFileExtractor} class is responsible for reading a file 
 * containing a matrix and converting it into a 2D string array. It also includes 
 * methods for debugging and converting data structures for matrix manipulation.
 * 
 * @author Davide Di Stefano
 * @version 1.0.0
 * @since 1.0.0
 */

public class MatrixFromFileExtractor {
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
		 try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
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
		 return fasterGameMap;
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
}
