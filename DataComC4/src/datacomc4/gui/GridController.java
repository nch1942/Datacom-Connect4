/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datacomc4.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
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

    private int ROW = 0;
    private int COL = 0;
    private boolean isRed = true;
    private final String humanTurn = "Human";
    private final String aiTurn = "AI";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ROW = grid.getRowConstraints().size();
        COL = grid.getColumnConstraints().size();
//        System.out.println("ROW is: " + ROW + " COL is " + COL);
        addCircleToGrid();
    }

    private void addCircleToGrid() {
        for (int rowCount = 0; rowCount < ROW; rowCount++) {
            for (int colCount = 0; colCount < COL; colCount++) {
                Circle circle = new Circle(32, Paint.valueOf("black"));
                circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        onClickHandler(e);
                    }
                });
                grid.add(circle, colCount, rowCount);
                GridPane.setHgrow(circle, Priority.ALWAYS);
                GridPane.setVgrow(circle, Priority.ALWAYS);
                GridPane.setHalignment(circle, HPos.CENTER);
                GridPane.setValignment(circle, VPos.CENTER);
            }
        }
    }

    @FXML
    private void onClickHandler(MouseEvent e) {
        Circle source = (Circle) e.getSource();

        int selectRowIndex = GridPane.getRowIndex(source);
        int selectColumnIndex = GridPane.getColumnIndex(source);
        coordinate.setText("ROW: " + selectRowIndex + " COL: " + selectColumnIndex);

        checkCircle(selectColumnIndex);
    }

    private void checkCircle(int col) {
        // For GridPane, Col start at 1
        // (Row - 1 * Col) will give you the max row of correspondong Col
//        col += 1;
        int lastRowIndex = col + ((ROW - 1) * COL);

//        System.out.println("Column is: " + col + " AND last row is: " + lastRowIndex);
//        System.out.println("Children size is: " + grid.getChildren().size());

        ObservableList<Node> childrens = grid.getChildren();
        Node firstRow = childrens.get(col);
        Node lastRow = childrens.get(lastRowIndex);

        if (isBlack(lastRow)) {
//            System.out.println("Last Row");
            switchColor(lastRow);
        } else if (!isBlack(firstRow)) {
//            System.out.println("First Row");
        } else {
            System.out.println("Loop");
            for (int i = lastRowIndex - COL; i >= 0; i -= COL) {
                System.out.println("Row is: " + i);
                Node temp = childrens.get(i);
                System.out.println(temp);
                if (isBlack(childrens.get(i))) {
//                    System.out.println("SWITCH\n");
//                    System.out.println("-------");
                    switchColor(temp);
                    break;
                }
            }
        }
    }

    /**
     * This method sound racist as hell...
     *
     * @param node
     * @return
     */
    private boolean isBlack(Node node) {
        Circle temp = (Circle) node;
        return temp.getFill() == Paint.valueOf("black");
    }

    /**
     * THIS NEED TO UPDATE
     *
     * @param node
     */
    private void switchColor(Node node) {
        Circle temp = (Circle) node;
        /*
        If isRed true, set the color of the circle to red.
        Then set the color of "turn indicator" to blue.
        "Turn Indicator" is a small circle outside the GridPane to indicate 
        the color of whoever's turn is playing (red for player, blue for AI)
         */
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

}
