/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datacomc4;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

/**
 *
 * @author 1635547
 */
public class C4Server {

    private Session session;
    private Player player;
    private int serverPort;
    private static final int BUFSIZE = 2; // Size of receive buffer
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private InputStream in;
    private OutputStream out;
    private Game game;
    private int countGames;

    public C4Server(int serverPort) throws IOException {
        //Set seerver port we want t listen to
        this.serverPort = serverPort;
        //Create server socket
        acceptClientRequest();

    }

    private void acceptClientRequest() throws IOException {
        //Create socket to be able to accept client's connection request
        serverSocket = new ServerSocket(this.serverPort);
        //Interact with client
        serviceConnection();
    }
    
    
    private void beginSession(){
        session = new Session();
        session.createNewGame((byte)countGames);
        countGames++;
        game = session.getGame((byte)0);
        player = new AIPlayer(game);
    }

    
    
    private void serviceConnection() throws IOException {
        //Size of message received from client
        int receivedMessageSize = 2;
        //byte array buffer
        byte[] byteBuffer = new byte[BUFSIZE];
        //Run loop forever accepting and serving connections
        for (;;) {
            clientSocket = serverSocket.accept();
            in = clientSocket.getInputStream();     //To read data from socket
            out = clientSocket.getOutputStream();   //To write data to socket
            Board board;
            
            //Recevieve until client closes connection (-1)
            while ((receivedMessageSize = in.read(byteBuffer)) != -1){
                //Client can start session
                if(byteBuffer[0] == (byte)0){
                    //Begin session
                    System.out.println("Session started");
                    beginSession();
                    out.write(new byte[]{0,0}, 0, receivedMessageSize);
                }
                //Client wants to stop (2)
                else if (byteBuffer[0] == (byte)2){ 
                    System.out.println("Thank you for playing");
                    clientSocket.close();
                } 
                //Client won - create new game
                else if (byteBuffer[0] == (byte)3){
                    System.out.println("Client won - new game can start");
                    session.createNewGame((byte)countGames);
                    game = session.getGame((byte)countGames);
                    countGames++;
                }
                //Client wants to make a move
                else if (byteBuffer[0] == (byte)1){
                    board = game.getBoard();
                    //insert new client token
                    board.insertToken(byteBuffer[1], (byte)1);
                    //AI player plays
                    byte tokenInput = player.play((byte)0);
                    //Send client new move
                    out.write(new byte[]{1,tokenInput}, 0, receivedMessageSize);
                } 
                //Error
                else {
                    System.out.println("ERROR");
                }
            }
        }
    }
}
