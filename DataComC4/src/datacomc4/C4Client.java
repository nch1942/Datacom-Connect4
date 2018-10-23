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
 * IMPORTANT NOTE: When seeing byte[X,Y]. E.g. byte[0,0], it means 1D array with
 * the size 2, NOT 2D array!
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

    /**
     * Create a byte[0,0] and send to server for creating a new Socket Object.
     *
     * If server respond with a byte[0,0], then Connection is successful
     *
     * If server respond with something else, then Connection is not successful,
     * and Socket will be closed.
     *
     * @throws IOException When there is a connection problem
     */
    public void requestServerConnection() throws IOException {
        // Set packet to a [0,0] => Request a connection
        setPackage(packet, 0, 0);
        socket = new Socket();
        // New socket Object, with timeout in 15s
        socket.connect(new InetSocketAddress(IP, portNumber));

        if (checkPackage(sendAndReceive(socket, packet, 0, 0)) == 0) {
            System.out.println("Connection succesful");
            isConnected = true;
        } else {
            socket.close();
        }
    }

    /**
     * Listen to server respond. Usually called right after Client send
     * something to server.
     *
     * @param socket The Socket Object that is used to carry out send and
     * receive information.
     * @return a byte[X,Y], which is the respond from the server
     * @throws IOException When there is a problem receiving Server respond
     */
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

    /**
     * Prepare the Socket, the packet of byte[X,Y], the offset (X) and actual
     * value (Y) for the packet
     *
     * Right after sending the packet, call the serverListener method to listen
     * to server's respond, and return the respond.
     *
     * @param socket the Socket object which will carry out the transmission
     * @param packet the information which will be transmitted
     * @param offset
     * @param value
     * @return
     * @throws IOException
     */
    public byte[] sendAndReceive(Socket socket, byte[] packet, int offset, int value) throws IOException {
        OutputStream out = socket.getOutputStream();
        setPackage(packet, offset, value);
        out.write(packet);
        return serverListener(socket);
    }

    /**
     * Similar to sendAndReceive method, but this time, it will only send the
     * packet, and NOT listening to server respond.
     *
     * @param socket
     * @param packet
     * @param offset
     * @param value
     * @throws IOException
     */
    public void sendOnly(Socket socket, byte[] packet, int offset, int value) throws IOException {
        OutputStream out = socket.getOutputStream();
        setPackage(packet, offset, value);
        out.write(packet);
    }


    public boolean getConnectionStatus() {
        return this.isConnected;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public byte[] getPackage() {
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
        ;
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
