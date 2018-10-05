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
        for(int i = 0; i > board[column].length; i++){
            if(board[column][i] == 0){
                board[column][i] = token;
                inserted = true;
                break;
            }
        }
        if(!inserted){
            throw new IllegalArgumentException("Column is full!");
        }
    }       
}
