/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datacomc4;

/**
 * Human player class implements Player - overrides the play method
 *
 * @author Gabriela Rueda
 */
public class HumanPlayer implements Player {

    private Game game;

    /**
     * Public constructor to human player - takes as input current game playing
     *
     * @param game
     */
    public HumanPlayer(Game game) {
        this.game = game;
    }

    /**
     * Play method inserts token chosen by the player into the board of the game
     *
     * @param chosen
     * @return
     */
    @Override
    public byte play(byte chosen) {
        game.getBoard().insertToken(chosen, (byte) 1);
        return chosen;
    }

    public Game getGame() {
        return this.game;
    }
}
