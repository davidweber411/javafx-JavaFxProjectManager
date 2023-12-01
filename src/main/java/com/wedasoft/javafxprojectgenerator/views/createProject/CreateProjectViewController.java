package com.wedasoft.javafxprojectgenerator.views.createProject;

import com.wedasoft.javafxprojectgenerator.enums.ProjectType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

public class CreateProjectViewController {

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

    private CreateProjectViewControllerService createProjectViewControllerService;

    public void init() {
        createProjectViewControllerService = new CreateProjectViewControllerService(this);
        projectTypeChoiceBox.getItems().addAll(ProjectType.values());
        projectTypeChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(ProjectType object) {
                return object == null ? null : object.uiText;
            }

            @Override
            public ProjectType fromString(String string) {
                return null;
            }
        });
        projectTypeChoiceBox.setValue(projectTypeChoiceBox.getItems().get(0));
    }

    public void onResetButtonClick(
            ActionEvent event) {

        createProjectViewControllerService.onResetButtonClick(event);
    }

    public void onCreateProjectButtonClick(
            ActionEvent event) {

        createProjectViewControllerService.onCreateProjectButtonClick(event);
    }

    public void onChooseDestinationDirectoryButtonClick(
            ActionEvent event) {

        createProjectViewControllerService.onChooseDestinationDirectoryButtonClick(event);
    }

    public void onMenuItemCloseClick() {
        createProjectViewControllerService.onMenuItemCloseClick();
    }

    public void onMenuItemAboutClick() {
        createProjectViewControllerService.onMenuItemAboutClick();
    }

    public void onMenuItemHowToImportInEclipseClick() {
        createProjectViewControllerService.onMenuItemHowToImportInEclipseClick();
    }

    public TextField getApplicationNameTextField() {
        return applicationNameTextField;
    }

    public TextField getGroupIdTextField() {
        return groupIdTextField;
    }

    public TextField getVersionTextField() {
        return versionTextField;
    }

    public ChoiceBox<ProjectType> getProjectTypeChoiceBox() {
        return projectTypeChoiceBox;
    }

    public TextField getDestinationDirectoryTextField() {
        return destinationDirectoryTextField;
    }

    public CheckBox getUsePreconfiguredDatabaseCheckbox() {
        return usePreconfiguredDatabaseCheckbox;
    }

    public TextField getUsePreconfiguredDatabaseVersionTextfield() {
        return usePreconfiguredDatabaseVersionTextfield;
    }

    public CheckBox getUseLibraryWedasoftCheckbox() {
        return useLibraryWedasoftCheckbox;
    }

    public TextField getUseLibraryWedasoftVersionTextfield() {
        return useLibraryWedasoftVersionTextfield;
    }

    public CheckBox getUseLibraryLombokCheckbox() {
        return useLibraryLombokCheckbox;
    }

    public TextField getUseLibraryLombokVersionTextfield() {
        return useLibraryLombokVersionTextfield;
    }

}