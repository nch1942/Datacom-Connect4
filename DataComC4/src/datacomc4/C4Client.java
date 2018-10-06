/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datacomc4;

import java.net.Socket;

/**
 *
 * @author 1635547
 */
public class C4Client {
    private Player player;
    private GUI gui;
    private Socket socket;
    private Session session;
    private final byte[] packet = new byte[2];
    
    public C4Client(Player player, GUI gui, Socket socket, byte [] packet){
        this.player = player;
        this.gui = gui;
        this.socket = socket;
    }
    
    public void requestServerConnection(){
        setPackage(packet, 0, 0);
        
    }
    
    public void receiveBoard(){
        
    }
    
    public void sendPositionToken(){
        
    }
    
    public byte[] convertSizeOfArray(){
        return null;
    } 
    
    private void setPackage(byte[] packet, int offset, int value) {
        if(packet.length != 2) {
            throw new IllegalArgumentException("Packet Size Should be 2");
        }
        packet[0] = (byte)offset;
        packet[1] = (byte)value;
    }
}
