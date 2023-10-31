package com.wedasoft.javafxprojectgenerator.helper;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.util.Iterator;
import java.util.List;

public class DialogHelper {

    public static void displayDialogWithColumns(
            String title,
            int amountOfColumns,
            List<Node> nodes) {

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        for (int i = 0; i < amountOfColumns; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().add(cc);
        }

        int amountOfRows = (int) Math.ceil(nodes.size() / (double) amountOfColumns);
        Iterator<Node> iterator = nodes.iterator();
        for (int rowI = 0; rowI < amountOfRows; rowI++) {
            for (int columnJ = 0; columnJ < amountOfColumns; columnJ++) {
                if (iterator.hasNext()) {
                    grid.add(iterator.next(), columnJ, rowI);
                }
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title != null ? title : "");
        alert.setHeaderText(null);
        // alert.setGraphic(null);
        alert.setResizable(true);
        alert.getDialogPane().setContent(grid);
        alert.showAndWait();
    }

    /*
    Node next = iterator.next();
    next.setStyle("-fx-border-style: solid solid solid solid; -fx-border-width: 1; -fx-border-color: red;");
    */
}
