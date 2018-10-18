/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datacomc4.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;



/**
 * FXML Controller class
 *
 * @author C.Hoang
 */
public class ConnectionGUIController implements Initializable {

    @FXML
    private Button connectButton;
    @FXML
    private TextField ipInput;
    @FXML
    private TextField portInput;
    @FXML
    private Label ipWarn;
    @FXML
    private Label portWarn;

    private static final Pattern PATTERN = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");



    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void startGame(ActionEvent event) throws IOException {

//        String ip = ipInput.getText();
//        String port = portInput.getText();
//
//        if (ip.isEmpty() || !validateIP(ip)) {
//            ipWarn.setText("Invalid IP");
//        } else if (port.isEmpty() || !validatePort(port)) {
//            ipWarn.setText("");
//            portWarn.setText("Invalid Port");
//        } else {
//            Stage primaryStage = (Stage) connectButton.getScene().getWindow();
//            primaryStage.close();
//            Parent root = FXMLLoader.load(getClass().getResource("grid.fxml"));
//
//            Scene scene = new Scene(root);
//            Stage stage = new Stage();
//            stage.setScene(scene);
//            stage.setTitle("Connect Four");
//            stage.show();
//        }
        Stage primaryStage = (Stage) connectButton.getScene().getWindow();
        primaryStage.close();
        Parent root = FXMLLoader.load(getClass().getResource("grid.fxml"));

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Connect Four");
        stage.show();
    }

    private boolean validateIP(String ip) {
        return PATTERN.matcher(ip).matches();
    }

    private boolean validatePort(String port) {
        int temp = 0;
        try {
            temp = Integer.parseInt(port);
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return false;
        }
        return temp >= 0 && temp <= 65535;
    }
}
