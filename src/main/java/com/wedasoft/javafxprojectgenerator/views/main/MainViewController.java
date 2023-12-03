package com.wedasoft.javafxprojectgenerator.views.main;

import com.wedasoft.javafxprojectgenerator.helper.HelperFunctions;
import com.wedasoft.javafxprojectgenerator.views.createProject.CreateProjectViewController;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainViewController {

    @FXML
    private BorderPane borderPane;

    public void init() throws IOException {
        onCreateProjectButtonClick();
    }

    public void onCreateProjectButtonClick() throws IOException {
        HelperFunctions.switchBorderPaneCenter(
                borderPane,
                getClass().getResource("/com/wedasoft/javafxprojectgenerator/views/createProject/create-project.fxml"),
                controller -> ((CreateProjectViewController) controller).init());
    }

    public void onPackageForWindowsButtonClick() throws IOException {
        //        HelperFunctions.switchBorderPaneCenter(
        //                borderPane,
        //                getClass().getResource("/com/wedasoft/javafxprojectgenerator/views/createProject/create-project.fxml"),
        //                controller -> ((CreateProjectViewController) controller).init());
    }

    public void onHelpButtonClick() throws IOException {
        //        HelperFunctions.switchBorderPaneCenter(
        //                borderPane,
        //                getClass().getResource("/com/wedasoft/javafxprojectgenerator/views/createProject/create-project.fxml"),
        //                controller -> ((CreateProjectViewController) controller).init());
    }

    public void onAboutButtonClick() throws IOException {
        //        HelperFunctions.switchBorderPaneCenter(
        //                borderPane,
        //                getClass().getResource("/com/wedasoft/javafxprojectgenerator/views/createProject/create-project.fxml"),
        //                controller -> ((CreateProjectViewController) controller).init());
    }

}
