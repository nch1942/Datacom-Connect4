/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datacomc4;

/**
 *
 * @author 1635547
 */
public class AIPlayer implements Player{

    private Game game;
    
    /**
     * Public constructor that takes as input the game AIPlayer is currently playing
     * @param game 
     */
    public AIPlayer(Game game){
        this.game = game;
    }
    
    /**
     * 
     * @param chosen
     */
    @Override
    public byte play(byte chosen) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private byte getPositionNextToken(){
        byte[][] board = game.getBoard().getBoard();
        byte token = -1;
        for (int row = 5; row > -1; row--){
            //Check for 3 in a row - to win
            for(int column = 0; column  < 4; column++){
                if (board[row][column] == 2 && board[row][column] == board[row][column+1] && board[row][column+1] == board[row][column+2] && board[row][column+3] == 0){
                    token =(byte)(column+3);
                }
            }
            for(int column = 6; column > 0; column--){
                if (board[row][column] == 2 && board[row][column] == board[row][column -1] && board[row][column-1] == board[row][column-2] && board[row][column-3] == 0){
                    token = (byte)(column-3);
                }
            }
            //Check for 3 in a row - block other player
            for(int column = 0; column  < 4; column++){
                if (board[row][column] == 1 && board[row][column] == board[row][column+1] && board[row][column+1] == board[row][column+2] && board[row][column+3] == 0){
                    token =(byte)(column+3);
                }
            }
            for(int column = 6; column > 0; column--){
                if (board[row][column] == 1 && board[row][column] == board[row][column -1] && board[row][column-1] == board[row][column-2] && board[row][column-3] == 0){
                    token = (byte)(column-3);
                }
            }
        }
        for (int column = 0; column < 7; column++){
            for (int row = 5; row > 0; row--){
                
            }
        }
        return token;
    }
}
