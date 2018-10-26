package datacomc4;

/**
 * The Board represents the board in a Connect Four Game. Since the board
 * contains Row and Column, we use a 2D byte[] to represent the board
 *
 * @author Benjamin Kearney
 */
public class Board {

    private byte[][] board;

    public Board() {
        board = new byte[6][];
        for (int i = 0; i < 6; i++) {
            board[i] = new byte[7];
            for (int j = 0; j < 7; j++) {
                board[i][j] = 0;
            }
        }
    }

    /**
     * Return the board of the current Game
     *
     * @return
     */
    public byte[][] getBoard() {
        return board;
    }

    /**
     * Takes as input which column to insert into and what the token is (e.g. 1
     * for player1, 2 for player2)
     *
     * @param column
     * @param token
     * @throws IllegalArgumentException if column is full
     */
    public void insertToken(byte column, byte token) {
        boolean inserted = false;
        for (int i = 5; i >= 0; i--) {
            if (board[i][column] == 0) {
                board[i][column] = token;
                inserted = true;
                break;
            }
        }
        if (!inserted) {
            throw new IllegalArgumentException("Column is full!");
        }
    }
}
