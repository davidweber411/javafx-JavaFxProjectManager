package com.wedasoft.javafxprojectgenerator.views.main;

import com.wedasoft.javafxprojectgenerator.helper.DialogHelper;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("ClassCanBeRecord")
@Getter
public class MainViewControllerService {

    private final MainViewController controller;

    public MainViewControllerService(MainViewController controller) {
        this.controller = controller;
    }

    public void onMenuItemHowToImportInEclipseClick() {
        DialogHelper.displayDialogWithColumns("About", 3, List.of(
                new Label("Step 1: Start the import wizard"),
                new Label("Step 2: Select the build management system"),
                new Label("Step 3: 'Next' and continue the installer wizard"),
                new ImageView(Objects.requireNonNull(getClass().getResource(
                        "/com/wedasoft/javafxprojectgenerator/images/import-in-eclipse-1.jpg")).toString()),
                new ImageView(Objects.requireNonNull(getClass().getResource(
                        "/com/wedasoft/javafxprojectgenerator/images/import-in-eclipse-2.jpg")).toString())));
    }

}
