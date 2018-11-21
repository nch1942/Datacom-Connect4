package datacomc4;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * C4Client houses the main logic for the Server side, including instantiate
 * necessary Objects for the Game to work. E.g. Player, Game and Socket, Session
 *
 * @author Gabriela Rueda
 */
public class C4Server  {

    
    private int serverPort;
    private ServerSocket serverSocket;
    private Socket clientSocket;


    public C4Server(int serverPort) throws IOException {
        //Set seerver port we want to listen to
        this.serverPort = serverPort;
        //Create server socket
        acceptClientRequest();
    }

    /**
     * Open a new Socket for Client connection
     *
     * @throws IOException
     */
    private void acceptClientRequest() throws IOException {
        //Create socket to be able to accept client's connection request
        serverSocket = new ServerSocket(this.serverPort);
        //Interact with client
        serviceConnection();
    }


    /**
     * Send And Receive Packet from Client
     *
     * @throws IOException
     */
    private void serviceConnection() throws IOException {
        //Run loop forever accepting and serving connections
        for (;;) {
            System.out.println("Server Started\n");
            clientSocket = serverSocket.accept();
            //Create server logic - sending in client socket
            ServerLogic server = new ServerLogic(clientSocket);
            //Create thread
            Thread thread = new Thread(server);
            thread.start();
        }
    }
}
