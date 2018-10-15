
package datacomc4;
import java.util.*;
/**
 *
 * @author 1636522
 */
public class Session {
    private List<Game> games;
    
    public Session(){
        games = new ArrayList<Game>();
    }
    /**
     * Adds a game to the session with the designated ID, unless there already 
     * exists a game with the given ID
     * 
     * @param gameId 
     */
    public void createNewGame(byte gameId){
        for(int i = 0; i < games.size(); i++){
            if(games.get(i).getId() == gameId){
                throw new IllegalArgumentException("Game with that Id already exists!");
            }
        }
        games.add(new Game(gameId));
    }
    /**
     * Returns the game of the given ID
     * 
     * @param gameId
     * @return game
     * @throws IllegalArgumentException if the game does not exist
     */
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
