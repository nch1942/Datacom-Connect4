/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datacomc4;
import java.util.*;
/**
 *
 * @author 1635547
 */
public class Session {
    private List<Game> games;
    
    public Session(){
        games = new ArrayList<Game>();
    }
    
    public void createNewGame(byte gameId){
        for(int i = 0; i < games.size(); i++){
            if(games.get(i).getId() == gameId){
                throw new IllegalArgumentException("Game with that Id already exists!");
            }
        }
        games.add(new Game(gameId));
    }
    
    public Game getGame(byte gameId){
        Game game = null;
        for(int i = 0; i < games.size(); i++){
            if(games.get(i).getId() == gameId){
                game = games.get(i);
            }
        }
        if(game == null){
            throw new IllegalArgumentException("Requested game does not exist!");
        }
        return game;
    }
}
