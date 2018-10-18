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

    private Player player;
    private Game game;
    private final byte[] packet = new byte[2];
    private boolean isConnected = false;

    public C4Client(String IP, int portNumber) {
        this.IP = IP;
        this.portNumber = portNumber;

        this.game = new Game((byte) 1);
        this.player = new HumanPlayer(game);
    }

    public void requestServerConnection() throws IOException {
        // Set packet to a [0,0] => Request a connection
        setPackage(packet, 0, 0);
        Socket socket = new Socket();
        // New socket Object, with timeout in 15s
        socket.connect(new InetSocketAddress(IP, portNumber), 15000);

        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();
        // Send the packet to server
        out.write(packet);
        boolean stilPlaying = true;

        int totalByteReceived = 0;
        int byteReceived;
        while (stilPlaying) {
            while (totalByteReceived < packet.length) {
                if ((byteReceived = in.read(packet, totalByteReceived, packet.length - totalByteReceived)) == -1) {
                    throw new SocketException("Server is toasted");
                }
                totalByteReceived += byteReceived;
            }
            if (checkPackage(packet, 0) && (int) packet[1] == 0) {
                isConnected = true;
                System.out.println("Connect Successful");
            } else {
                socket.close();
                System.out.println("Connect FAIL");
            }
        }
    }

    public void sendMove(Socket socket, byte[] packet, int columnCoor) throws IOException {
        if (isConnected) {
            OutputStream out = socket.getOutputStream();
            setPackage(packet, 1, columnCoor);
            out.write(packet);
        }
    }

    public byte[] convertSizeOfArray() {
        return null;
    }

    public boolean getConnectionStatus() {
        return isConnected;
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
    private boolean checkPackage(byte[] packet, int offset) {
        if (packet.length != 2) {
            throw new IllegalArgumentException("Wrong packet format");
        }
        if ((int) packet[0] == offset) {
            return true;
        }
        return false;
    }
}
