package datacomc4;

/**
 *
 * @author 1636522
 */
public class Game {

    private Board board;
    private byte id;

    public Game(byte id) {
        this.id = id;
        this.board = new Board();
    }

    public Board getBoard() {
        return board;
    }

    public byte getId() {
        return id;
    }

    /**
     * checks if the player with the designated token has won
     *
     * @param token
     * @return boolean
     */
    public boolean playerHasWon(byte token) {

        // horizontalCheck
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 7; i++) {
                if (this.board.getBoard()[j][i] == token && this.board.getBoard()[j + 1][i] == token && this.board.getBoard()[j + 2][i] == token && this.board.getBoard()[j + 3][i] == token) {
                    return true;
                }
            }
        }
        // verticalCheck
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {
                if (this.board.getBoard()[j][i] == token && this.board.getBoard()[j][i + 1] == token && this.board.getBoard()[j][i + 2] == token && this.board.getBoard()[j][i + 3] == token) {
                    return true;
                }
            }
        }
        // ascendingDiagonalCheck 
        for (int i = 3; i < 7; i++) {
            for (int j = 0; j < 3; j++) {
                if (this.board.getBoard()[j][i] == token && this.board.getBoard()[j - 1][i + 1] == token && this.board.getBoard()[j - 2][i + 2] == token && this.board.getBoard()[j - 3][i + 3] == token) {
                    return true;
                }
            }
        }
        // descendingDiagonalCheck
        for (int i = 3; i < 7; i++) {
            for (int j = 3; j < 6; j++) {
                if (this.board.getBoard()[j][i] == token && this.board.getBoard()[j - 1][i - 1] == token && this.board.getBoard()[j - 2][i - 2] == token && this.board.getBoard()[j - 3][i - 3] == token) {
                    return true;
                }
            }
        }
        return false;
    }
}
