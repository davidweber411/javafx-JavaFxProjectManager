package com.wedasoft.javafxprojectmanager.views.main;

import com.wedasoft.javafxprojectmanager.helper.HelperFunctions;
import com.wedasoft.javafxprojectmanager.views.about.AboutViewController;
import com.wedasoft.javafxprojectmanager.views.createProject.CreateProjectViewController;
import com.wedasoft.javafxprojectmanager.views.exit.ExitViewController;
import com.wedasoft.javafxprojectmanager.views.packageFatJarForWindows.PackageFatJarForWindowsViewController;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainViewController {

    @FXML
    private BorderPane borderPane;

    public void init() throws IOException {
        onCreateJavaFxProjectButtonClick();
    }

    public void onCreateJavaFxProjectButtonClick() throws IOException {
        HelperFunctions.switchBorderPaneCenter(
                borderPane,
                getClass().getResource("/com/wedasoft/javafxprojectmanager/views/createProject/create-project.fxml"),
                controller -> ((CreateProjectViewController) controller).init());
    }

    public void onPackageForWindowsButtonClick() throws IOException {
        HelperFunctions.switchBorderPaneCenter(
                borderPane,
                getClass().getResource("/com/wedasoft/javafxprojectmanager/views/packageFatJarForWindows/package-fat-jar-for-windows-view.fxml"),
                controller -> ((PackageFatJarForWindowsViewController) controller).init());
    }

    public void onAboutButtonClick() throws IOException {
        HelperFunctions.switchBorderPaneCenter(
                borderPane,
                getClass().getResource("/com/wedasoft/javafxprojectmanager/views/about/about-view.fxml"),
                controller -> ((AboutViewController) controller).init());
    }

    public void onExitButtonClick() throws IOException {
        HelperFunctions.switchBorderPaneCenter(
                borderPane,
                getClass().getResource("/com/wedasoft/javafxprojectmanager/views/exit/exit-view.fxml"),
                controller -> ((ExitViewController) controller).init());
    }

}
