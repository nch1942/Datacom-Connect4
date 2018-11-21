/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datacomc4;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Server Logic implements Runnable and its instance methods 
 * @author Gabriela Rueda
 */
public class ServerLogic implements Runnable{
    private InputStream in;
    private OutputStream out;
    private static final int BUFSIZE = 2; // Size of receive buffer
    private Socket clientSocket;
    private Session session;
    private Player player;
    private Game game;
    private int countGames;
    
    /**
     * Public constructor for Server Logic
     * @param clientSocket 
     */
    public ServerLogic(Socket clientSocket){
        this.clientSocket = clientSocket;
    }
    
    @Override
    public void run() {
        //Size of message received from client
        int receivedMessageSize = 2;
        //byte array buffer
        byte[] byteBuffer = new byte[BUFSIZE];

        try {
            in = clientSocket.getInputStream();     //To read data from socket
            out = clientSocket.getOutputStream();   //To write data to socket
            Board board;
            //Recevieve until client closes connection (-1)
            try {
                while ((receivedMessageSize = in.read(byteBuffer)) != -1) {
                    //Client can start session
                    if (byteBuffer[0] == (byte) 0) {
                        //Begin session
                        System.out.println("Session started\n");
                        beginSession();
                        out.write(new byte[]{0, 0}, 0, receivedMessageSize);
                    } //Client wants to stop (2)
                    else if (byteBuffer[0] == (byte) 2) {
                        System.out.println("Thank you for playing\n");
                        continue;
                    } //Client won - create new game
                    else if (byteBuffer[0] == (byte) 3) {
                        System.out.println("The Game is ended. Waiting for Client to either start a new Game or Quit\n");
                        countGames++;
                        session.createNewGame((byte) countGames);
                        game = session.getGame((byte) countGames);
                        beginSession();
                    } //Client wants to make a move
                    else if (byteBuffer[0] == (byte) 1) {
                        board = game.getBoard();
                        //insert new client token
                        board.insertToken(byteBuffer[1], (byte) 1);
                        //AI player plays
                        byte tokenInput = player.play((byte) 0);
                        //Send client new move
                        out.write(new byte[]{1, tokenInput}, 0, receivedMessageSize);
                    }
                }
                // If Client Crash for some reason, Print out message, but Server will keep running
            } catch (IOException e) {
                System.out.println("=========  WARNING  ========= \n");
                System.out.println("IT SEEMS CLIENT HAS CRASHED. SERVER STILL RUNNING\n");
                System.out.println("The ERROR IS: " + e + "\n");
                System.out.println("Closing Client Socket...\n");
                clientSocket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(C4Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Start a new session for Player Socket
     */
    private void beginSession() {
        session = new Session();
        session.createNewGame((byte) countGames);
        countGames++;
        game = session.getGame((byte) 0);
        player = new AIPlayer(game);
    }
}
