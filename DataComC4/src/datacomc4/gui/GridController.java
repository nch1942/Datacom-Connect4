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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

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
    @FXML
    private Button quitBtn;
    @FXML
    private Button resetBtn;

    private int ROW = 0;
    private int COL = 0;
    private int moveCounter = 0;
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
        // Get the Client From ConnectionController
        client = getClientFromConnectionController();
        // Add circle to GridPane
        addCircleToGrid();
        addHandlerForQuitBtn();
        addHandlerForResetBtn();
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

    /**
     * Add handler for Quit button, which will close the Socket, and exit the
     * program.
     */
    private void addHandlerForQuitBtn() {
        quitBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    onClickQuitBtn();
                } catch (IOException error) {
                    System.out.println("There is a problem when trying to close the socket " + error);
                }
            }
        });
    }

    /**
     * Add handler for Reset button, which will reset the Board, plus all the
     * counters. But it keeps the Socket.
     */
    private void addHandlerForResetBtn() {
        resetBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    onClickResetBtn();
                } catch (IOException error) {
                    System.out.println("There is a problem wth connection to server " + error);
                }
            }
        });
    }

    /**
     * Handler for MouseEnter Event, which will highlight a potential legal move
     * for each column when user hover their mouse on that column
     *
     * @param e The event that is fired upon hovering the mouse
     */
    private void onHoverHandler(MouseEvent e) {
        Circle source = (Circle) e.getSource();

        int selectRowIndex = GridPane.getRowIndex(source);
        int selectColumnIndex = GridPane.getColumnIndex(source);
        // Increment both Column and Row by 1 to display for player correctly.
        // Normally, we start at 1, not 0
        selectRowIndex++;
        selectColumnIndex++;
        coordinate.setText("ROW: " + selectRowIndex + " COL: " + selectColumnIndex);
        // Reset Column back to correct order (Start counting at 0)
        selectColumnIndex--;
        checkPotentialCircle(selectColumnIndex);
    }

    /**
     * Handler for MouseClick Event, which will set the Circle at the legal move
     * position at that column to either Red (Human Player) or Blue (AI Player).
     * Then calls the necessary method to make that move registered in the Game
     * logic, then check if the move is a winning move or not.
     *
     * IF the move is a winning move: Display appropriate message to Player,
     * increase all the necessary counters. Disable the Grid, and ask if User
     * want to Continue or Quit the Game.
     *
     * IF the move is NOT a winning move: Send the Player's move to Server, and
     * listen to Server respond
     *
     * @param e The event that is fired upon hovering the mouse
     */
    private void onClickHandler(MouseEvent e) {
        Circle source = (Circle) e.getSource();
        int selectColumnIndex = GridPane.getColumnIndex(source);
        // Display the move of Player on the Grid, and registered that move to the Game logic
        checkCircle(selectColumnIndex);
        client.getHumanPlayer().play((byte) selectColumnIndex);
        // Check if player's move is a winning move
        if (client.getHumanPlayer().getGame().playerHasWon((byte) 1)) {
            playerWinCounter++;
            playerCounter.setText((Integer.toString(playerWinCounter)));
            winDisplay.setText("YOU WON THE MATCH.\nClick Reset to replay, , or Quit to Close the Game");
            // Disable the Grid so user Cannot continue after they won
            grid.setDisable(true);
            // Exit the method
            return;
        } // --------------------- \\
        // If the last move from Player is NOT a winning move, send the Player's move to server    
        else {
            // Disable the Grid so user cannot make move while waiting for AI to respond
            grid.setDisable(true);
            try {
                serverPackage = client.sendAndReceive(client.getSocket(), client.getPackage(), 1, selectColumnIndex);
            } catch (IOException error) {
                System.out.println("Connection error " + error);
            }
        }

        // Get the move from the server
        int serverMove = client.checkPackage(serverPackage);
        // Display the move of AI on the Grid, and registered that move to the Game logic
        checkCircle(serverMove);
        client.getAIPlayer().play((byte) serverMove);
        // Check if AI's move is a winning move
        if (client.getAIPlayer().getGame().playerHasWon((byte) 1)) {
            AIwinCounter++;
            aiCounter.setText(Integer.toString(AIwinCounter));
            winDisplay.setText("COMPUTER WON THE MATCH.\nClick Reset to replay, or Quit to Close the Game");
            grid.setDisable(true);
            // Exit the method
            return;
        }

        // If 42 moves have been made, and noone win, then it's a draw
        if (moveCounter == 42) {
            winDisplay.setText("IT IS A DRAW.\nClick Reset to replay, or Quit to Close the Game");
            grid.setDisable(true);
            // Exit the method
            return;
        }
        // Enable the Grid again so User can Play
        grid.setDisable(false);
    }

    /**
     * Look for the circle that WILL BE a legit move of the given column
     *
     * @param col
     */
    private void checkCircle(int col) {
        moveCounter++;
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

    /**
     * Handler for Quit button, which will close the socket, and quit the game.
     */
    private void onClickQuitBtn() throws IOException {
        Stage stage = (Stage) quitBtn.getScene().getWindow();
        // Send a packet of byte[2,0] to indicate the Client is done playing, and going to quit the game
        client.sendOnly(client.getSocket(), serverPackage, 2, 0);
        client.getSocket().close();
        stage.close();
    }

    /**
     * Handler for Reset button, which will reset the Grid, all counters and
     * settings of the game.
     */
    private void onClickResetBtn() throws IOException {
        // Send a packet of byte[3,0] to indicate the Client is done playing, and going to quit the game
        client.sendOnly(client.getSocket(), serverPackage, 3, 0);
        playerWinCounter = 0;
        AIwinCounter = 0;
        moveCounter = 0;
        isRed = true;
        turnCircle.setFill(Paint.valueOf("Red"));
        turnDisplay.setText(humanTurn);
        isRed = true;
        playerCounter.setText("0");
        aiCounter.setText("0");
        resetGrid();
        grid.setDisable(false);
    }

    /**
     * Set all Circle in the Grid back to Black
     */
    private void resetGrid() {
        ObservableList<Node> childrens = grid.getChildren();
        for (Node node : childrens) {
            Circle temp = (Circle) node;
            temp.setFill(Paint.valueOf("Black"));
        }
    }

    /**
     * Get the C4Client Object from ConnectionController, which will be used to
     * access the Socket Object.
     *
     * @return
     */
    private C4Client getClientFromConnectionController() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("connection.fxml"));
        try {
            loader.load();
        } catch (IOException error) {
            System.out.println("There is an error while passing the Socket between controllers " + error);
        }
        ConnectionController connection = loader.getController();
        // Getting the Socket Object
        return connection.getClient();
    }

    public void setConnection(C4Client client) {
        this.client = client;
    }
}
