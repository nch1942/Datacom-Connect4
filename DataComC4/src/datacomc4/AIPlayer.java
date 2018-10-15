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
    
    
}
