package com.wedasoft.javafxprojectmanager;

import com.wedasoft.javafxprojectmanager.views.main.MainViewController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "/com/wedasoft/javafxprojectmanager/views/main/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        MainViewController controller = fxmlLoader.getController();
        controller.init();
        stage.setTitle("JavaFX project manager");
        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
        stage.setScene(scene);
        stage.setWidth(1200);
        stage.setHeight(1000);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}