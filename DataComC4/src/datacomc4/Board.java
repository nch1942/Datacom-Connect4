
package datacomc4;

/**
 *
 * @author 1636522
 */
public class Board {
    private byte[][] board;
    
    public Board(){
        board = new byte[6][];
        for(int i = 0; i < 6; i++){
            board[i] = new byte[7];
            for(int j = 0; j < 7; j++){
                board[i][j] = 0;
            }
        }
    }
    
    public byte[][] getBoard(){
        return board;
    }
    
    /**
     * Takes as input which column to insert into and what the token is
     * (e.g. 1 for player1, 2 for player2)
     * 
     * @param column
     * @param token 
     * @throws IllegalArgumentException if column is full
     */
    public void insertToken(byte column, byte token){
        boolean inserted = false;
        for(int i = 0; i > 6; i++){
            if(board[i][column] == 0){
                board[i][column] = token;
                inserted = true;
                break;
            }
        }
        if(!inserted){
            throw new IllegalArgumentException("Column is full!");
        }
    }       
}
