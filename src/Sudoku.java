public class Sudoku {
    /**
    * Driver method to test the Board class and get it working.
    */
    public static void main(String[] args) {
        Board board = new Board();
        // Accept the board
        board.getBoard();
        if (board.isValidBoard()) {
            board.displayBoard();
            board.solve();
        } else {
            System.out.println("ENTER A VALID BOARD.");
        }
    }
}       
