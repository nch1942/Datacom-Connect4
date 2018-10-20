/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datacomc4.gui;

import datacomc4.C4Client;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 * FXML Controller class
 *
 * @author Crusader2142
 */
public class GridController implements Initializable {

    @FXML
    private GridPane grid;
    @FXML
    private Label coordinate;
    @FXML
    private Label turnDisplay;
    @FXML
    private Circle turnCircle;
    @FXML
    private Label playerCounter;
    @FXML
    private Label aiCounter;
    @FXML
    private Label winDisplay;

    private int ROW = 0;
    private int COL = 0;
    private int playerWinCounter = 0;
    private int AIwinCounter = 0;
    private boolean isRed = true;
    private final String humanTurn = "Human";
    private final String aiTurn = "AI";
    private Circle lastHighlight = new Circle();
    private C4Client client;
    private byte[] serverPackage;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ROW = grid.getRowConstraints().size();
        COL = grid.getColumnConstraints().size();
        // Try accessing to ConnectionGuiController to get the Socket Object
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("connectionGUI.fxml"));
        try {
            loader.load();
        } catch (IOException error) {
            System.out.println("There is an error while passing the Socket between controllers " + error);
        }
        ConnectionGUIController connection = loader.getController();
        // Getting the Socket Object
        client = connection.shareDataBetweenController();
        // Add circle to GridPane
        addCircleToGrid();
    }

    /**
     * Create circles with a radius of 32, paint it black and put them in
     * GridPane
     */
    private void addCircleToGrid() {
        for (int rowCount = 0; rowCount < ROW; rowCount++) {
            for (int colCount = 0; colCount < COL; colCount++) {
                Circle circle = new Circle(32, Paint.valueOf("black"));
                addHandlerForCircle(circle);

                grid.add(circle, colCount, rowCount);
                GridPane.setHgrow(circle, Priority.ALWAYS);
                GridPane.setVgrow(circle, Priority.ALWAYS);
                GridPane.setHalignment(circle, HPos.CENTER);
                GridPane.setValignment(circle, VPos.CENTER);
            }
        }
    }

    /**
     * Add 2 handlers to each circle. One for when Player Click on the circle,
     * and One for when Player Hover on the circle
     *
     * @param circle
     */
    private void addHandlerForCircle(Circle circle) {
        circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                onClickHandler(e);
            }
        });

        circle.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                onHoverHandler(e);
            }
        });
    }

    private void onHoverHandler(MouseEvent e) {
        Circle source = (Circle) e.getSource();

        int selectRowIndex = GridPane.getRowIndex(source);
        int selectColumnIndex = GridPane.getColumnIndex(source);
        selectRowIndex++;
        selectColumnIndex++;
        coordinate.setText("ROW: " + selectRowIndex + " COL: " + selectColumnIndex);
        selectColumnIndex--;
        checkPotentialCircle(selectColumnIndex);
    }

    private void onClickHandler(MouseEvent e) {
        Circle source = (Circle) e.getSource();
        int selectColumnIndex = GridPane.getColumnIndex(source);
        checkCircle(selectColumnIndex);
        client.getHumanPlayer().play((byte) selectColumnIndex);
        // Check if player's move is a winning move
        if (client.getHumanPlayer().getGame().playerHasWon((byte) 1)) {
            playerWinCounter++;
            playerCounter.setText((Integer.toString(playerWinCounter)));
            winDisplay.setText("YOU WON THE MATCH. Click Reset to replay");
            grid.setDisable(true);

            // DO PLAYER WIN STUFF HERE
        }
        grid.setDisable(true);
        // If the last player's move does not win the game, send the move to server.
        try {
            serverPackage = client.serverSender(client.getSocket(), client.getPackage(), 1, selectColumnIndex);
        } catch (IOException error) {
            System.out.println("Connection error " + error);
        }
        // Get the move from the server
        int serverMove = client.checkPackage(serverPackage);
        checkCircle(serverMove);
        client.getAIPlayer().play((byte) serverMove);
        // Check if AI's move is a winning move
        if (client.getAIPlayer().getGame().playerHasWon((byte) 1)) {
            AIwinCounter++;
            aiCounter.setText(Integer.toString(AIwinCounter));
            winDisplay.setText("COMPUTER WON THE MATCH. Click Reset to replay");
            grid.setDisable(true);
        }
        grid.setDisable(false);
    }

    /**
     * Look for the circle that WILL BE a legit move of the given column
     *
     * @param col
     */
    private void checkCircle(int col) {
        int lastRowIndex = col + ((ROW - 1) * COL);
        ObservableList<Node> childrens = grid.getChildren();
        Node firstRow = childrens.get(col);
        Node lastRow = childrens.get(lastRowIndex);

        if (isBlack(lastRow)) {
            switchColor(lastRow);
        } else if (!isBlack(firstRow)) {

        } else {
            for (int i = lastRowIndex - COL; i >= 0; i -= COL) {
                Node temp = childrens.get(i);
                if (isBlack(childrens.get(i))) {
                    switchColor(temp);
                    break;
                }
            }
        }
        checkPotentialCircle(col);
    }

    /**
     * Check if a circle has the color black or not
     *
     * @param node
     * @return
     */
    private boolean isBlack(Node node) {
        Circle temp = (Circle) node;
        return temp.getFill() == Paint.valueOf("black");
    }

    /**
     * Switch the color of the circle to either red or blue
     *
     * @param node
     */
    private void switchColor(Node node) {
        Circle temp = (Circle) node;

        if (isRed) {
            temp.setFill(Paint.valueOf("Red"));
            turnCircle.setFill(Paint.valueOf("Blue"));
            turnDisplay.setText(aiTurn);
            isRed = false;
        } else {
            temp.setFill(Paint.valueOf("Blue"));
            turnCircle.setFill(Paint.valueOf("Red"));
            turnDisplay.setText(humanTurn);
            isRed = true;
        }
    }

    /**
     * Look for the circle that WILL be POTENTIALLY the next move of the given
     * column
     *
     * @param col
     */
    private void checkPotentialCircle(int col) {
        int lastRowIndex = col + ((ROW - 1) * COL);
        Node temp = null;

        ObservableList<Node> childrens = grid.getChildren();
        Node firstRow = childrens.get(col);
        Node lastRow = childrens.get(lastRowIndex);

        if (isBlack(lastRow)) {
            highlightCircle(lastRow);
        } else if (!isBlack(firstRow)) {
            temp = childrens.get(col);
            Circle tempCircle = (Circle) temp;
            tempCircle.setStroke(Paint.valueOf("black"));
            tempCircle.setStrokeWidth(1);

        } else {
            for (int i = lastRowIndex - COL; i >= 0; i -= COL) {
                temp = childrens.get(i);
                if (isBlack(childrens.get(i))) {
                    highlightCircle(temp);
                    break;
                }
            }
        }
    }

    /**
     * Highlight the circle that will be the next move of a column
     *
     * @param node
     */
    private void highlightCircle(Node node) {
        Circle temp = (Circle) node;

        if (temp != lastHighlight) {
            temp.setStrokeWidth(4.5);
            temp.setStroke(Paint.valueOf("Aqua"));
            lastHighlight.setStroke(Paint.valueOf("black"));
            lastHighlight.setStrokeWidth(1);
        }
        lastHighlight = temp;
    }

}
