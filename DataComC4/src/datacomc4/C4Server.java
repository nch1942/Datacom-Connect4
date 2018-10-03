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
    private static final int BUFSIZE = 1; // Size of receive buffer
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private InputStream in;
    private OutputStream out;

    public C4Server(int serverPort) throws IOException {
        //Set seerver port we want t listen to
        this.serverPort = serverPort;
        //Create server socket
        acceptClientRequest();

    }

    private void acceptClientRequest() throws IOException {
        //Create socket to be able to accept client's connection request
        serverSocket = new ServerSocket(this.serverPort);
        //Begin session
        beginSession();
        //Interact with client
        serviceConnection();
    }
    
    

    private void beginSession(){
        session = new Session();
        player = new AIPlayer();
    }

    private void serviceConnection() throws IOException {
        //Size of message received from client
        int receivedMessageSize;
        //byte array buffer
        byte[] byteBuffer = new byte[BUFSIZE];
        //Run loop forever accepting and serving connections
        for (;;) {
            clientSocket = serverSocket.accept();
            in = clientSocket.getInputStream();     //To read data from socket
            out = clientSocket.getOutputStream();   //To write data to socket
            
            //Recevieve until client closes connection (-1)
            while ((receivedMessageSize = in.read(byteBuffer)) != -1){
                //Check user want to continue playing (
                if (byteBuffer[0] == (byte)2){
                    
                }
            }
        }
    }
}
