/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datacomc4;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

/**
 *
 * @author 1635547
 */
public class C4Client {

    String IP;
    int portNumber;

    private Player humanPlayer;
    private Player AIPlayer;
    private Game game;
    private Socket socket;
    private final byte[] packet = new byte[2];
    private boolean isConnected = false;


    public C4Client(String IP, int portNumber) {
        this.IP = IP;
        this.portNumber = portNumber;

        this.game = new Game((byte) 1);
        this.humanPlayer = new HumanPlayer(game);
        this.AIPlayer = new AIPlayer(game);
    }

    public void requestServerConnection() throws IOException {
        // Set packet to a [0,0] => Request a connection
        setPackage(packet, 0, 0);
        socket = new Socket();
        // New socket Object, with timeout in 15s
        socket.connect(new InetSocketAddress(IP, portNumber), 15000);
        
        if (checkPackage( serverSender(socket, packet, portNumber, portNumber) ) == 0) {
            isConnected = true;
        }
        else {
            socket.close();
        }
    }

    public byte[] serverListener(Socket socket) throws IOException {
        InputStream in = socket.getInputStream();
        int totalByteReceived = 0;
        int byteReceived;
        while (totalByteReceived < packet.length) {
            if ((byteReceived = in.read(packet, totalByteReceived, packet.length - totalByteReceived)) == -1) {
                throw new SocketException("Server is toasted");
            }
            totalByteReceived += byteReceived;
        }
        return packet;
    }


    public byte[] serverSender(Socket socket, byte[] packet, int offset, int value) throws IOException {
        OutputStream out = socket.getOutputStream();
        setPackage(packet, offset, value);
        out.write(packet);
        return serverListener(socket);
    }

    public boolean getConnectionStatus() {
        return this.isConnected;
    }
    
    public Socket getSocket() {
        return this.socket;
    }
    
    public byte [] getPackage() {
        return this.packet;
    }
    
    public Player getHumanPlayer() {
        return this.humanPlayer;
    }
    
    public Player getAIPlayer() {
        return this.AIPlayer;
    }
    
    public Game getGame() {
        return this.game;
    }

    private void setPackage(byte[] packet, int offset, int value) {
        if (packet.length != 2) {
            throw new IllegalArgumentException("Packet Size Should be 2");
        }
        packet[0] = (byte) offset;
        packet[1] = (byte) value;
    }

    /**
     * Check if a package is for requesting connection, make move, or quit 1 i
     *
     * @param packet
     * @param offset
     * @return
     */
    public int checkPackage(byte[] packet) {
        if (packet.length != 2) {
            throw new IllegalArgumentException("Wrong packet format");
        }
        int firstByte = packet[0];
        int secondByte = packet[1];

        if (firstByte == 0 && secondByte == 0) {
            return 0;
        } else if (firstByte == 1) {
            return secondByte;
        }
        return -1;
    }
}
