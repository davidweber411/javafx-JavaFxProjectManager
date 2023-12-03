package com.wedasoft.javafxprojectmanager;

import com.wedasoft.javafxprojectmanager.views.main.MainViewController;
import javafx.application.Application;
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
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}