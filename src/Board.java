/*
This file follows the Google Code guidelines
for Java developers.

If you are contributing to this code, please
do conform to the guidelines or else, I will
NOT I repeat NOT merge the code.

If you still send me unformatted code, I will
hunt you down and fuck you :')
*/
import java.util.Scanner;
import java.util.ArrayList;

/**
* @author HANS BALA
* @version 1.1 2017
* The following class is a representation of the
* Sudoku board. It is represented as a 2D array
* of integers. A '0' number in the board indicates
* that the position it is in is empty.
*
* This program only works for very easy puzzles
* i.e., a puzzle in which at least one position
* in the has a guarenteed solution. If the program
* fails to solve the board, it spits out the message
* "Could not solve it."
*
* All in all, use this program to solve only very
* basic puzzles.
*/
public class Board {
	// Magic numbers don't mean anything :D
	final int NUM_ROWS = 9;
	final int NUM_COLS = 9;
    int[][] board;

    /**
    * Non-parameterized constructor to set the board up
    */
    Board() {
    	board = new int[NUM_ROWS][NUM_COLS];
    }

    /**
    * Method to accept the board from the user as a grid
    * from stdin using Scanner class.
    */
    public void getBoard() {
    	Scanner sc = new Scanner(System.in);
    	System.out.println("ENTER THE BOARD: ");
    	/*
    	Board is represented as a grid of numbers.
    	'0' is to represent an unfilled spot in the 
    	grid.
    	*/
    	for (int i = 0; i < NUM_ROWS; i++) {
    		for (int j = 0; j < NUM_COLS; j++) {
    			board[i][j] = sc.nextInt();
    		}
    	}
    }

    /**
    * Checks whether the board entered by the user is a
    * valid puzzle or not.
    * It does a simple check to see if any non-zero numbers
    * have been repeated in the respective row, column, or
    * a the small 3x3 grid of which it is a part of.
    * @return Whether the entered board is valid or not
    */
    public boolean isValidBoard() {
    	for (int i = 0; i < NUM_ROWS; i++) {
    		for (int j = 0; j < NUM_COLS; j++) {
    			if (board[i][j] == 0) {
    				continue;
    			} else if (!(isValidNumberInPos(i, j, board[i][j]))) {
    				return false;
    			}
    		}
    	}
    	return true;
    }

    /**
    * Displays the puzzle (Solved + Unsolved) well formatted
    * to stdout using the System.out.print() method.
    * Adds some padding after every 3x3 small grid.
    */
    public void displayBoard() {
    	// Add some padding above and below the board
    	System.out.println();
    	int row_count = 0, col_count = 0;
    	for (int i = 0; i < NUM_ROWS; i++) {
    		row_count++;
    		for (int j = 0; j < NUM_COLS; j++) {
    			col_count++;
    			System.out.print(board[i][j]);
    			if (col_count < 3) {
    				System.out.print(" ");
    			} else {
    				System.out.print("\t");
    				col_count = 0;
    			}
    		}
    		if (row_count < 3) {
    			System.out.println();
    		} else {
    			row_count = 0;
    			System.out.println("\n");
    		}
    	}
    }

    /**
    * Actually solving of the puzzle takes place here.
    * The program goes through every position in the
    * 2D array holding a zero value (empty position).
    *
    * Once it finds an empty position, it checks to see
    * if it can place all numbers from 1 - 9 in that
    * position (maintaining an arraylist of all possibilities).
    * If the number of possibilities == 1, then that number
    * is guarenteed to be the solution in that position.
    *
    * We continue this till the entire grid is full of non-zero
    * numbers, used for checking whether the puzzle has been
    * solved.
    */
    public void solve() {
        int upper_limit = 0;
        while (!isGridSolved()) {
            /*
            Used as a saftey mechanism to make sure 
            the program doesn't crash if the program
            cannot solve it, resulting in an infinite
            loop.
            */
            if (upper_limit++ == 100000) {
                // If we fail to find a solution using this method,
                // use the recursive + backtracking solution to find
                // a solution.
                solve(0, 0);
            }
            for (int i = 0; i < NUM_ROWS; i++) {
                for (int j = 0; j < NUM_COLS; j++) {
                    if (board[i][j] != 0) {
                        continue;
                    }
                    // Holds all possible numbers that could go into this position.
                    // If the no. of possibilities == 1, then that is the solution.
                    ArrayList<Integer> possibilities = new ArrayList<Integer>();
                    for (int k = 1; k <= 9; k++) {
                        if (isValidNumberInPos(i, j, k)) {
                            possibilities.add(k);
                        }
                    }
                    // Storing the solution in the array.
                    if (possibilities.size() == 1) {
                        board[i][j] = possibilities.get(0);
                    }
                }
            }
        }
        System.out.println("SOLUTION WITH ITERATION: ");
        displayBoard();
    }

    /**
    * Recursive + Backtracking Solution to solve the Sudoku puzzle.
    * Basically performing an exhaustive search for all possible
    * combinations.
    * @param row The row that we are currently interested in.
    * @param col The column that we are currently interested in.
    */
    public void solve(int row, int col) {
        if (row > 8) {
            System.out.println("SOLUTION WITH BRUTE FORCE:");
            displayBoard();
            System.exit(0);
        }
        if (board[row][col] != 0) {
            next(row, col);
        }
        else {
            for (int i = 1; i < 10; i++) {
                if (isValidNumberInPos(row, col, i)) {
                    board[row][col] = i;
                    next(row, col);
                }
            }
            board[row][col] = 0;
        }
    }

    /**
    * Gets the next valid row + column coordinates and makes the method
    * call to the solve method.
    * @param row The row after which we are interested in.
    * @param col The column after which we are interested in.
    */
    public void next(int row, int col) {
        if (col < 8) {
            solve(row, col + 1);
        } else {
            solve(row + 1, 0);
        }
    }
    /**
    * Checks each row, column and 3x3 small grid to make sure
    * that if we place the number (num) in that position (row, col),
    * there will be no conflicts with other numbers.
    *
    * @param row The row that the current number is in.
    * @param col The column that the current number is in.
    * @param num The number we are looking to place in that position.
    * @return Whether the number if placed in the position is valid or not.
    */
    public boolean isValidNumberInPos(int row, int col, int num) {
    	/*
    	First we check the respective row
    	Second, the column
    	Third, the smaller 3x3 grid.
    	*/
    	// Checking the row
    	for (int i = 0; i < NUM_COLS; i++) {
    		if (board[row][i] == num && (i != col)) {
    			return false;
    		}
    	}
    	// Checking the column
    	for (int i = 0; i < NUM_ROWS; i++) {
    		if (board[i][col] == num && (i != row)) {
    			return false;
    		}
    	}
    	/*
    	Doing this for the small 3x3 grid.

    	The way this works is to see which grid the row + col
    	belong to.
    	[0 - 2] --> 1st small grid
    	[3 - 5] --> 2nd small grid
    	[6 - 8] --> 3rd small grid
    	... and so on.
    	We do this for the row and column and hence, get the
    	bounds for the small 3x3 grid.
    	*/
    	int lower_row, lower_col, upper_row, upper_col;
    	// Getting bounds of row
    	if (row >= 0 && row <= 2) {
    		lower_row = 0; upper_row = 2;
    	} else if (row >= 3 && row <= 5) {
    		lower_row = 3; upper_row = 5;
    	} else {
    		lower_row = 6; upper_row = 8;
    	}
    	// Getting bounds of column
    	if (col >= 0 && col <= 2) {
    		lower_col = 0; upper_col = 2;
    	} else if (col >= 3 && col <= 5) {
    		lower_col = 3; upper_col = 5;
    	} else {
    		lower_col = 6; upper_col = 8;
    	}
    	for (int i = lower_row; i <= upper_row; i++) {
    		for (int j = lower_col; j <= upper_col; j++) {
    			if (board[i][j] == num && (i != row && j != col)) {
    				// If there is a repeat of the number in a different box
    				return false;
    			}
    		}
    	}
    	return true;
    }

    /**
    * Method checks to see whether the puzzle has been solved.
    * It does this by simply checking if there are only non-zero
    * elements in the board.
	*
	* If there is a 0 in the board, we infer that all elements
	* have not been filled in and hence, the puzzle is not solved.
	* @return Whether the puzzle has been solved or not.
	*/
    public boolean isGridSolved() {
    	for (int i = 0; i < NUM_ROWS; i++) {
    		for (int j = 0; j < NUM_COLS; j++) {
    			if (board[i][j] == 0) {
    				// All numbers have not been filled in and return false
    				return false;
    			}
    		}
    	}
    	return true;
    }
}