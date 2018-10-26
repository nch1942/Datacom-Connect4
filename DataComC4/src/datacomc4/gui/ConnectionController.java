package datacomc4.gui;

import datacomc4.C4Client;
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
 * FXML Controller class. Responsible for building the GUI where user can enter
 * IP address and Port number to connect the server. Establish C4Client Object
 *
 * @author Cao Hoang Nguyen
 */
public class ConnectionController implements Initializable {

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

    private C4Client client;

    private static final Pattern PATTERN = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        portInput.setText("50000");
    }

    /**
     * Display a form for user to enter IP number and Port number. Validate user
     * input. If the input is valid and server is running, this method will
     * setup the connection Socket and display the main GUI for user to play.
     *
     * @param event The event that fired the handler
     * @throws IOException
     */
    @FXML
    private void startGame(ActionEvent event) throws IOException {

        String ip = ipInput.getText();
        String port = portInput.getText();

        if (ip.isEmpty() || !validateIP(ip)) {
            ipWarn.setText("Invalid IP");
        } else if (port.isEmpty() || !validatePort(port)) {
            ipWarn.setText("");
            portWarn.setText("Invalid Port");
        } else {
            System.out.println("Port Number and IP are good");
            client = new C4Client(ip, Integer.parseInt(port));
            System.out.println("Connecting to server...Stand by\n");
            client.requestServerConnection();

            if (client.getConnectionStatus()) {
                Stage primaryStage = (Stage) connectButton.getScene().getWindow();
                primaryStage.close();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("grid.fxml"));

                Parent root = loader.load();
                GridController game = loader.getController();
                game.setConnection(client);

                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Connect Four");
                stage.show();
            } else {
                System.out.println("Cannot Connect to Server. Please try again later\n");
            }
        }
    }

    public C4Client getClient() {
        return this.client;
    }

    private boolean validateIP(String ip) {
        return PATTERN.matcher(ip).matches();
    }

    private boolean validatePort(String port) {
        int temp = 0;
        try {
            temp = Integer.parseInt(port);
        } catch (Exception e) {
            System.out.println("Error when trying to process the Port number: " + e + "\n");
            return false;
        }
        return temp >= 0 && temp <= 65535;
    }
}
