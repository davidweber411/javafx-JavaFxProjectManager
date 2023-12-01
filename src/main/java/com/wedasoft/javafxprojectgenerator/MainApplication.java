package com.wedasoft.javafxprojectgenerator;

import com.wedasoft.javafxprojectgenerator.views.createProject.CreateProjectViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "/com/wedasoft/javafxprojectgenerator/views/createProject/create-project.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        CreateProjectViewController controller = fxmlLoader.getController();
        controller.init();
        stage.setTitle("JavaFX project generator");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}