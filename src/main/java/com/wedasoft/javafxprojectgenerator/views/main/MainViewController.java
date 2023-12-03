package com.wedasoft.javafxprojectgenerator.views.main;

import com.wedasoft.javafxprojectgenerator.helper.DialogHelper;
import com.wedasoft.javafxprojectgenerator.helper.HelperFunctions;
import com.wedasoft.javafxprojectgenerator.views.createProject.CreateProjectViewController;
import com.wedasoft.javafxprojectgenerator.views.exit.ExitViewController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.List;

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
        DialogHelper.displayDialogWithColumns("About", 2, List.of(
                new Label("Developed by:"),
                new Label("David Weber"),
                new Label("Homepage:"),
                new Label("www.wedasoft.com")));
    }

    public void onExitButtonClick() throws IOException {
        HelperFunctions.switchBorderPaneCenter(
                borderPane,
                getClass().getResource("/com/wedasoft/javafxprojectgenerator/views/exit/exit-view.fxml"),
                controller -> ((ExitViewController) controller).init());
    }

}
