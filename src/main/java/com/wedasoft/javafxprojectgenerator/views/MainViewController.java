package com.wedasoft.javafxprojectgenerator.views;

import com.wedasoft.javafxprojectgenerator.enums.ModuleSystemType;
import com.wedasoft.simpleJavaFxApplicationBase.sceneSwitcher.FxmlSceneControllerBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private ChoiceBox<ModuleSystemType> moduleSystemTypeChoiceBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        moduleSystemTypeChoiceBox.getItems().addAll(ModuleSystemType.values());
        moduleSystemTypeChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(ModuleSystemType object) {
                return object == null ? null : object.name();
            }

            @Override
            public ModuleSystemType fromString(String string) {
                return null;
            }
        });
    }

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