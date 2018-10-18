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
 * @author C.Hoang
 */
public class BasicGridController implements Initializable {

    @FXML
    private GridPane grid;
    @FXML
    private Label testArea;
    private int ROW = 0;
    private int COL = 0;
    private boolean color = true;


    /**
     * Initializes the controller class.
     */
    @Override

    public void initialize(URL url, ResourceBundle rb) {

        ROW = grid.getRowConstraints().size();
        COL = grid.getColumnConstraints().size();
        System.out.println("ROW is: " + ROW + " COL is " + COL);
        addCircleToGrid();
//        Circle a = (Circle) grid.getChildren().get(1);
//        a.setFill(Paint.valueOf("Red"));
    }

    /**
     * Add 42 default circle to the Grid, each with black background color.
     * Circle is centered in each cell, radius is 78
     */
    private void addCircleToGrid() {

        for (int rowCount = 0; rowCount < ROW; rowCount++) {
            for (int colCount = 0; colCount < COL; colCount++) {
                Circle circle = new Circle(78, Paint.valueOf("black"));
                circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        onClickHandler(e);
                    }
                });

//                circle.setMouseTransparent(true);
                grid.add(circle, colCount, rowCount);
                GridPane.setHgrow(circle, Priority.ALWAYS);
                GridPane.setVgrow(circle, Priority.ALWAYS);
                GridPane.setHalignment(circle, HPos.CENTER);
                GridPane.setValignment(circle, VPos.CENTER);
            }
        }
        System.out.println("Children size is: " + grid.getChildren().size());
        System.out.println("Indext at 0 is: " + grid.getChildren().get(0).getClass());
        System.out.println("Indext at 1 is: " + grid.getChildren().get(1));
        Circle a = (Circle) grid.getChildren().get(1);
        a.setFill(Paint.valueOf("Red"));
    }


    @FXML
    private void onClickHandler(MouseEvent e) {
//        System.out.println("e is: " + e);
//        System.out.println("target is: " + e.getTarget());
//        System.out.println("source is: " + e.getSource());
        Circle source = (Circle) e.getSource();
//        System.out.println("source is: " + source.parentProperty());

//        System.out.println(source.getFill());

        int selectRowIndex = GridPane.getRowIndex(source);
        int selectColumnIndex = GridPane.getColumnIndex(source);
        testArea.setText("ROW: " + selectRowIndex + " COL: " + selectColumnIndex);
//        System.out.println(">><<");
//        System.out.println(grid.getChildren().get(1));

        checkCircle(selectColumnIndex);
    }

    private void checkCircle(int col) {
        // For GridPane, Col start at 1
        // (Row - 1 * Col) will give you the max row of correspondong Col
        col += 1;
        int lastRowIndex = col + ((ROW - 1) * COL);

        System.out.println("Column is: " + col + " AND last row is: " + lastRowIndex);

        ObservableList<Node> childrens = grid.getChildren();
        Node firstRow = childrens.get(col);
        Node lastRow = childrens.get(lastRowIndex);

        if (isBlack(lastRow)) {
//            System.out.println("Last Row");
            switchColor(lastRow);
        } else if (!isBlack(firstRow)) {
//            System.out.println("First Row");
            return;
        } else {
//            System.out.println("Loop");
            for (int i = lastRowIndex - COL; i > 0; i -= COL) {
//                System.out.println("Row is: " + i);
                Node temp = childrens.get(i);
                System.out.println(temp);
                if (isBlack(childrens.get(i))) {
//                    System.out.println("SWITCH\n");
//                    System.out.println("-------");
                    switchColor(temp);
                    break;
                } else {
                    System.out.println("FUCKED");
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
        if (temp.getFill() == Paint.valueOf("black")) {
            return true;
        }
        return false;
    }

    /**
     * THIS NEED TO UPDATE
     *
     * @param node
     */
    private void switchColor(Node node) {
        Circle temp = (Circle) node;
        if (color) {
            temp.setFill(Paint.valueOf("Red"));
            color = false;
            grid.setDisable(true);
        } else {
            temp.setFill(Paint.valueOf("Blue"));
            color = true;
        }
    }

}
