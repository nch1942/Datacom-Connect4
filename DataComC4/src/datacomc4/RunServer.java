/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datacomc4;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 1635547
 */
public class RunServer {
    public static void main(String [] args){
        try {
            C4Server server = new C4Server(50000);
        } catch (IOException ex) {
            Logger.getLogger(C4Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
