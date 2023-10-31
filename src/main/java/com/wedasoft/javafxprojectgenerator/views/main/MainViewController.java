package com.wedasoft.javafxprojectgenerator.views.main;

import com.wedasoft.javafxprojectgenerator.enums.ProjectType;
import com.wedasoft.simpleJavaFxApplicationBase.sceneSwitcher.FxmlSceneControllerBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import lombok.Getter;

import java.net.URL;
import java.util.ResourceBundle;

@Getter
public class MainViewController extends FxmlSceneControllerBase implements Initializable {

    @FXML
    private TextField applicationNameTextField;

    @FXML
    private TextField groupIdTextField;

    @FXML
    private TextField versionTextField;

    @FXML
    private ChoiceBox<ProjectType> projectTypeChoiceBox;

    @FXML
    private TextField destinationDirectoryTextField;

    @FXML
    private CheckBox usePreconfiguredDatabaseCheckbox;

    @FXML
    private TextField usePreconfiguredDatabaseVersionTextfield;

    @FXML
    private CheckBox useLibraryWedasoftCheckbox;

    @FXML
    private TextField useLibraryWedasoftVersionTextfield;

    @FXML
    private CheckBox useLibraryLombokCheckbox;

    @FXML
    private TextField useLibraryLombokVersionTextfield;

    private MainViewControllerService mainViewControllerService;

    @Override
    public void initialize(
            URL location,
            ResourceBundle resources) {

        mainViewControllerService = new MainViewControllerService(this);
        projectTypeChoiceBox.getItems().addAll(ProjectType.values());
        projectTypeChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(ProjectType object) {
                return object == null ? null : object.getUiText();
            }

            @Override
            public ProjectType fromString(String string) {
                return null;
            }
        });
        projectTypeChoiceBox.setValue(projectTypeChoiceBox.getItems().get(0));
    }

    @Override
    public void onFxmlSceneReady() {
    }

    @FXML
    public void onResetButtonClick(
            @SuppressWarnings("unused") ActionEvent event) {

        mainViewControllerService.onResetButtonClick(event);
    }

    @FXML
    public void onCreateProjectButtonClick(
            @SuppressWarnings("unused") ActionEvent event) {

        mainViewControllerService.onCreateProjectButtonClick(event);
    }

    @FXML
    public void onChooseDestinationDirectoryButtonClick(
            @SuppressWarnings("unused") ActionEvent event) {

        mainViewControllerService.onChooseDestinationDirectoryButtonClick(event);
    }

    @FXML
    public void onMenuItemCloseClick() {
        mainViewControllerService.onMenuItemCloseClick();
    }

    @FXML
    public void onMenuItemAboutClick() {
        mainViewControllerService.onMenuItemAboutClick();
    }

    @FXML
    public void onMenuItemHowToImportInEclipseClick(){
        mainViewControllerService.onMenuItemHowToImportInEclipseClick();
    }

}