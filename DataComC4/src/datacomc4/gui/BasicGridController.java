/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datacomc4.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
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
    private int ROW = 0;
    private int COL = 0;

//    private final int ROW = grid.getRowConstraints().size();
//    private final int COL = grid.getColumnConstraints().size();

    /**
     * Initializes the controller class.
     */
    @Override

    public void initialize(URL url, ResourceBundle rb) {

        System.out.println(grid);
        ROW = grid.getRowConstraints().size();
        COL = grid.getColumnConstraints().size();
        System.out.println(ROW);
        System.out.println(COL);
        drawCircle();


    }

    private void drawCircle() {

        for (int rowCount = 0; rowCount < ROW; rowCount++) {
            for (int colCount = 0; colCount < COL; colCount++) {
                Circle circle = new Circle(78, Paint.valueOf("black"));
                circle.setMouseTransparent(true);
                grid.add(circle, colCount, rowCount);
                GridPane.setHgrow(circle, Priority.ALWAYS);
                GridPane.setVgrow(circle, Priority.ALWAYS);
                GridPane.setHalignment(circle, HPos.CENTER);
                GridPane.setValignment(circle, VPos.CENTER);
            }
        }
    }

}
