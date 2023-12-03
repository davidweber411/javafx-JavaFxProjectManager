package com.wedasoft.javafxprojectgenerator.helper;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.function.Consumer;

public class HelperFunctions {

    public static void switchBorderPaneCenter(
            BorderPane borderPane,
            URL absoluteFxmlFileUrl,
            @SuppressWarnings("rawtypes") Consumer initMethodOfController)
            throws IOException {

        FXMLLoader loader = new FXMLLoader(absoluteFxmlFileUrl);
        borderPane.setCenter(loader.load());
        Object viewController = loader.getController();
        if (initMethodOfController != null) {
            //noinspection unchecked
            initMethodOfController.accept(viewController);
        }
    }

    public static boolean isNumeric(
            String str) {

        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void openUrlInBrowser(String url) throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI(url));
    }

}
