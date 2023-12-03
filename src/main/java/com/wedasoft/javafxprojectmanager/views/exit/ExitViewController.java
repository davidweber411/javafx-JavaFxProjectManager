package com.wedasoft.javafxprojectmanager.views.exit;

import javafx.application.Platform;

public class ExitViewController {

    public void init() {

    }

    public void onYesButtonClick() {
        Platform.exit();
        System.exit(0);
    }

    public void onNoButtonClick() {

    }

}
