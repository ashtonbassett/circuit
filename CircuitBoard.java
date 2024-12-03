import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Represents a 2D circuit board as read from an input file.
 * 
 * @author mvail, Ashton Bassett
 */
public class CircuitBoard {
	/** current contents of the board */
	private char[][] board;
	/** location of row,col for '1' */
	private Point startingPoint;
	/** location of row,col for '2' */
	private Point endingPoint;

	// constants you may find useful
	private final int ROWS; // initialized in constructor
	private final int COLS; // initialized in constructor
	private final char OPEN = 'O'; // capital 'o', an open position
	private final char CLOSED = 'X';// a blocked position
	private final char TRACE = 'T'; // part of the trace connecting 1 to 2
	private final char START = '1'; // the starting component
	private final char END = '2'; // the ending component
	private final String ALLOWED_CHARS = "OXT12"; // useful for validating with indexOf

	/**
	 * Construct a CircuitBoard from a given board input file, where the first
	 * line contains the number of rows and columns as ints and each subsequent
	 * line is one row of characters representing the contents of that position.
	 * Valid characters are as follows:
	 * 'O' an open position
	 * 'X' an occupied, unavailable position
	 * '1' first of two components needing to be connected
	 * '2' second of two components needing to be connected
	 * 'T' is not expected in input files - represents part of the trace
	 * connecting components 1 and 2 in the solution
	 * 
	 * @param filename
	 *                 file containing a grid of characters
	 * @throws FileNotFoundException      if Scanner cannot open or read the file
	 * @throws InvalidFileFormatException for any file formatting or content issue
	 */
	public CircuitBoard(String filename) throws FileNotFoundException {
		Scanner fileScan = new Scanner(new File(filename));

		// TODO: parse the given file to populate the char[][]
		try {
			ROWS = fileScan.nextInt();
			COLS = fileScan.nextInt();
		} catch (InputMismatchException e) {
			fileScan.close();
			throw new InvalidFileFormatException("First row of the file must contain two integers");
		}

		fileScan.nextLine();
		int countRows = 0;
		int countCols = 0;
		board = new char[ROWS][COLS];

		while (fileScan.hasNextLine()) {
			String line = fileScan.nextLine();
			Scanner lineScanner = new Scanner(line);

			while (lineScanner.hasNext()) {
				String character = lineScanner.next();

				if (character.length() != 1 || ALLOWED_CHARS.indexOf(character) == -1) {
					lineScanner.close();
					fileScan.close();
					throw new InvalidFileFormatException("Invalid character at row " + countRows + " and at column "
							+ countCols + ". The following character: " + character +
							" is not valid. Valid characters are O, X, 1, and 2. ");
				} else if (ALLOWED_CHARS.indexOf(character) == 2) {
					lineScanner.close();
					fileScan.close();
					throw new InvalidFileFormatException(
							"The character \"T\", is not valid as it records the trace path. Please change.");
				}

				try {
					board[countRows][countCols] = character.charAt(0);
				} catch (ArrayIndexOutOfBoundsException e) {
					lineScanner.close();
					fileScan.close();
					throw new InvalidFileFormatException("Column number is too great");
				}

				if (character.charAt(0) == START) {
					if (startingPoint != null) {
						lineScanner.close();
						fileScan.close();
						throw new InvalidFileFormatException(
								"Too many START values. There can only be one \"1\" character in the file");
					}
					startingPoint = new Point(countRows, countCols);
				} else if (character.charAt(0) == END) {
					if (endingPoint != null) {
						lineScanner.close();
						fileScan.close();
						throw new InvalidFileFormatException(
								"Too many END values. There can only be one \"2\" character in the file.");
					}
					endingPoint = new Point(countRows, countCols);

				}
				countCols++;

			}

			if (countCols != COLS) {
				lineScanner.close();
				fileScan.close();
				throw new InvalidFileFormatException("Incorrect amount of columns");
			}

			countCols = 0;
			countRows++;
			lineScanner.close();
		}
			if (startingPoint == null) {
				fileScan.close();
				throw new InvalidFileFormatException("There is no starting point in the file");
			} else if (endingPoint == null) {
				fileScan.close();
				throw new InvalidFileFormatException("There is no ending point in the file");
			} else if (countRows != ROWS) {
				fileScan.close();
				throw new InvalidFileFormatException("Incorrect amount of rows");
			}
		

		fileScan.close();
	}

	/**
	 * Copy constructor - duplicates original board
	 * 
	 * @param original board to copy
	 */
	public CircuitBoard(CircuitBoard original) {
		board = original.getBoard();
		startingPoint = new Point(original.startingPoint);
		endingPoint = new Point(original.endingPoint);
		ROWS = original.numRows();
		COLS = original.numCols();
	}

	/**
	 * Utility method for copy constructor
	 * 
	 * @return copy of board array
	 */
	private char[][] getBoard() {
		char[][] copy = new char[board.length][board[0].length];
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				copy[row][col] = board[row][col];
			}
		}
		return copy;
	}

	/**
	 * Return the char at board position x,y
	 * 
	 * @param row row coordinate
	 * @param col col coordinate
	 * @return char at row, col
	 */
	public char charAt(int row, int col) {
		return board[row][col];
	}

	/**
	 * Return whether given board position is open
	 * 
	 * @param row
	 * @param col
	 * @return true if position at (row, col) is open
	 */
	public boolean isOpen(int row, int col) {
		if (row < 0 || row >= board.length || col < 0 || col >= board[row].length) {
			return false;
		}
		return board[row][col] == OPEN;
	}

	/**
	 * Set given position to be a 'T'
	 * 
	 * @param row
	 * @param col
	 * @throws OccupiedPositionException if given position is not open
	 */
	public void makeTrace(int row, int col) {
		if (isOpen(row, col)) {
			board[row][col] = TRACE;
		} else {
			throw new OccupiedPositionException("row " + row + ", col " + col + "contains '" + board[row][col] + "'");
		}
	}

	/** @return starting Point(row,col) */
	public Point getStartingPoint() {
		return new Point(startingPoint);
	}

	/** @return ending Point(row,col) */
	public Point getEndingPoint() {
		return new Point(endingPoint);
	}

	/** @return number of rows in this CircuitBoard */
	public int numRows() {
		return ROWS;
	}

	/** @return number of columns in this CircuitBoard */
	public int numCols() {
		return COLS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				str.append(board[row][col] + " ");
			}
			str.append("\n");
		}
		return str.toString();
	}

}// class CircuitBoard
