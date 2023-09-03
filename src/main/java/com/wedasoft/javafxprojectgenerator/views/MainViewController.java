package com.wedasoft.javafxprojectgenerator.views;

import com.wedasoft.javafxprojectgenerator.enums.ModuleSystemType;
import com.wedasoft.simpleJavaFxApplicationBase.sceneSwitcher.FxmlSceneControllerBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import lombok.Getter;

@Getter
public class MainViewController extends FxmlSceneControllerBase {

    @FXML
    private TextField applicationNameTextField;

    @FXML
    private TextField groupIdTextField;

    @FXML
    private ChoiceBox<ModuleSystemType> moduleSystemTypeChoiceBox;

    @Override
    public void onFxmlSceneReady() {

    }

    @FXML
    public void onResetButtonClick(@SuppressWarnings("unused") ActionEvent event) {
        System.out.println("onResetButtonClick");
    }

    @FXML
    public void onCreateProjectButtonClick(@SuppressWarnings("unused") ActionEvent event) {
        System.out.println("onCreateProjectButtonClick");
    }

}