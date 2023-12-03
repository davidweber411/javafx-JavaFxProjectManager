package com.wedasoft.javafxprojectmanager.views.packageFatJarForWindows;

import com.wedasoft.javafxprojectmanager.enums.AppTypeToCreate;
import com.wedasoft.javafxprojectmanager.enums.MainClassReferenceType;
import com.wedasoft.javafxprojectmanager.enums.PackageContentType;
import com.wedasoft.javafxprojectmanager.enums.UsedJpackage;
import com.wedasoft.javafxprojectmanager.exceptions.NotValidException;
import com.wedasoft.simpleJavaFxApplicationBase.fileSystemUtil.FileSystemUtil;
import com.wedasoft.simpleJavaFxApplicationBase.jfxDialogs.JfxDialogUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.wedasoft.javafxprojectmanager.views.packageFatJarForWindows.ApplicationPaths.APP_DATA_INCLUDED_JPACKAGE_EXE_PATH;
import static com.wedasoft.javafxprojectmanager.views.packageFatJarForWindows.ApplicationPaths.APP_DATA_TMP_PATH;

public class PackageFatJarForWindowsViewController {

    @FXML
    private ChoiceBox<UsedJpackage> chooseOwnJpackageExeChoiceBox;
    @FXML
    private Button chooseOwnJpackageExeButton;
    @FXML
    private TextField chooseOwnJpackageExeTextField;
    @FXML
    private ChoiceBox<AppTypeToCreate> fileTypeToCreateChoiceBox;
    @FXML
    private ChoiceBox<PackageContentType> filePackageTypeChoiceBox;
    @FXML
    private Button directoryWithAllFilesToPackageButton;
    @FXML
    private TextField directoryWithAllFilesToPackageTextField;
    @FXML
    private TextField mainJarFileNameTextField;
    @FXML
    private ChoiceBox<MainClassReferenceType> mainClassTypeChoiceBox;
    @FXML
    private TextField mainClassNameTextField;
    @FXML
    private TextField outputFileDestinationDirectoryTextField;
    @FXML
    private TextField appNameTextField;
    @FXML
    private TextField appVersionTextField;
    @FXML
    private TextField iconFilePathTextField;
    @FXML
    private CheckBox showDirectoryChooserDialogForInstallationCheckbox;
    @FXML
    private CheckBox createDesktopShortcutCheckbox;
    @FXML
    private CheckBox addApplicationToSystemMenuCheckbox;
    @FXML
    private TextArea jPackageCommandTextArea;

    public void init() {
        try {
            initChooseJdkChoiceBox();
            initFileTypeToCreateChoiceBox();
            initPackageTypeChoiceBox();
            initMainClassTypeChoiceBox();
        } catch (Exception e) {
            e.printStackTrace();
            JfxDialogUtil.createErrorDialog("An error occured.", e);
        }
    }

    private void initChooseJdkChoiceBox() {
        chooseOwnJpackageExeChoiceBox.getItems().addAll(UsedJpackage.values());
        chooseOwnJpackageExeChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(UsedJpackage object) {
                return object.getLabel();
            }

            @Override
            public UsedJpackage fromString(String string) {
                return null;
            }
        });
        chooseOwnJpackageExeChoiceBox.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            if (newValue.equals(UsedJpackage.FROM_WRAPPED_OPEN_JDK_17)) {
                chooseOwnJpackageExeButton.setDisable(true);
                chooseOwnJpackageExeTextField.setText("");
                chooseOwnJpackageExeTextField.setDisable(true);
            } else if (newValue.equals(UsedJpackage.FROM_CONFIGURED_JDK)) {
                chooseOwnJpackageExeButton.setDisable(false);
                chooseOwnJpackageExeTextField.setDisable(false);
            }
        });
        chooseOwnJpackageExeChoiceBox.setValue(UsedJpackage.FROM_WRAPPED_OPEN_JDK_17);
    }

    private void initMainClassTypeChoiceBox() {
        mainClassTypeChoiceBox.getItems().addAll(MainClassReferenceType.values());
        mainClassTypeChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(MainClassReferenceType object) {
                return object.getLabel();
            }

            @Override
            public MainClassReferenceType fromString(String string) {
                return null;
            }
        });
        mainClassTypeChoiceBox.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            if (newValue.equals(MainClassReferenceType.MAINCLASS_OF_MAINJAR)) {
                mainClassNameTextField.setDisable(true);
            } else if (newValue.equals(MainClassReferenceType.DEVIATING_MAINCLASS)) {
                mainClassNameTextField.setDisable(false);
            }
        });
        mainClassTypeChoiceBox.setValue(MainClassReferenceType.MAINCLASS_OF_MAINJAR);
    }

    private void initPackageTypeChoiceBox() {
        filePackageTypeChoiceBox.getItems().addAll(PackageContentType.values());
        filePackageTypeChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(PackageContentType object) {
                return object.getLabel();
            }

            @Override
            public PackageContentType fromString(String string) {
                return null;
            }
        });
        filePackageTypeChoiceBox.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            if (newValue.equals(PackageContentType.SINGLE_JAR_ONLY)) {
                directoryWithAllFilesToPackageButton.setDisable(true);
                directoryWithAllFilesToPackageTextField.setDisable(true);
            } else if (newValue.equals(PackageContentType.ALL_FILES_FROM_DIR)) {
                directoryWithAllFilesToPackageButton.setDisable(false);
                directoryWithAllFilesToPackageTextField.setDisable(false);
            }
        });
        filePackageTypeChoiceBox.setValue(PackageContentType.SINGLE_JAR_ONLY);
    }

    private void initFileTypeToCreateChoiceBox() {
        fileTypeToCreateChoiceBox.getItems().addAll(AppTypeToCreate.values());
        fileTypeToCreateChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(AppTypeToCreate object) {
                return object.getLabel();
            }

            @Override
            public AppTypeToCreate fromString(String string) {
                return null;
            }
        });
        fileTypeToCreateChoiceBox.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            if (newValue.equals(AppTypeToCreate.MSI)) {
                showDirectoryChooserDialogForInstallationCheckbox.setDisable(false);
                createDesktopShortcutCheckbox.setDisable(false);
                addApplicationToSystemMenuCheckbox.setDisable(false);
            } else if (newValue.equals(AppTypeToCreate.EXE)) {
                showDirectoryChooserDialogForInstallationCheckbox.setDisable(true);
                createDesktopShortcutCheckbox.setDisable(true);
                addApplicationToSystemMenuCheckbox.setDisable(true);
            }
        });
        fileTypeToCreateChoiceBox.setValue(AppTypeToCreate.EXE);
    }

    public void onMenuItemHelpClick() {
        PackageFatJarForWindowsViewControllerService.displayMenuItemHelp();
    }

    public void onAllFilesInSelectedDirectoryChooseButtonClick() {
        DirectoryChooser dc = new DirectoryChooser();
        Stage actualStage = (Stage) directoryWithAllFilesToPackageTextField.getScene().getWindow();
        File file = dc.showDialog(actualStage);
        if (file != null) {
            directoryWithAllFilesToPackageTextField.setText(file.getAbsolutePath());
        }
    }

    public void onChooseOwnJpackageExeButtonClick() {
        FileChooser dc = new FileChooser();
        Stage actualStage = (Stage) chooseOwnJpackageExeButton.getScene().getWindow();
        File file = dc.showOpenDialog(actualStage);
        if (file != null) {
            chooseOwnJpackageExeTextField.setText(file.getAbsolutePath());
        }
    }

    public void onPathToMainJarFileChooseButtonClick() {
        FileChooser fc = new FileChooser();
        Stage actualStage = (Stage) mainJarFileNameTextField.getScene().getWindow();
        File file = fc.showOpenDialog(actualStage);
        if (file != null) {
            mainJarFileNameTextField.setText(file.getAbsolutePath());
        }
    }

    public void onOutputFileDestinationDirChooseButtonClick() {
        DirectoryChooser dc = new DirectoryChooser();
        Stage actualStage = (Stage) outputFileDestinationDirectoryTextField.getScene().getWindow();
        File file = dc.showDialog(actualStage);
        if (file != null) {
            outputFileDestinationDirectoryTextField.setText(file.getAbsolutePath());
        }
    }

    public void onApplicationIconChooseButtonClick() {
        FileChooser fc = new FileChooser();
        Stage actualStage = (Stage) iconFilePathTextField.getScene().getWindow();
        File file = fc.showOpenDialog(actualStage);
        if (file != null) {
            iconFilePathTextField.setText(file.getAbsolutePath());
        }
    }

    private String createJPackageCommand() throws NotValidException {
        return PackageFatJarForWindowsViewControllerService.createJPackageCommandString(
                fileTypeToCreateChoiceBox.getValue().getJPackageArgumentValue(),
                outputFileDestinationDirectoryTextField.getText(),
                mainJarFileNameTextField.getText(),
                filePackageTypeChoiceBox.getSelectionModel().getSelectedItem(),
                directoryWithAllFilesToPackageTextField.getText(),
                mainClassNameTextField.getText(),
                appNameTextField.getText(),
                appVersionTextField.getText(),
                iconFilePathTextField.getText(),
                showDirectoryChooserDialogForInstallationCheckbox.isSelected(),
                createDesktopShortcutCheckbox.isSelected(),
                addApplicationToSystemMenuCheckbox.isSelected(),
                mainClassTypeChoiceBox.getSelectionModel().getSelectedItem(),
                fileTypeToCreateChoiceBox.getSelectionModel().getSelectedItem());
    }

    public void onCreateJPackageCommandButtonClick() {
        try {
            jPackageCommandTextArea.setText(createJPackageCommand());
        } catch (NotValidException nve) {
            jPackageCommandTextArea.setText("");
            JfxDialogUtil.createErrorDialog(nve.getMessage()).showAndWait();
        }
    }

    public void onCreateNativeApplicationButtonClick() {
        try {
            String jPackageCommand = createJPackageCommand();
            if (jPackageCommand == null || jPackageCommand.isEmpty()) {
                return;
            }
            jPackageCommandTextArea.setText(jPackageCommand);

            /* prepare environment for single jar packaging mode */
            if (filePackageTypeChoiceBox.getSelectionModel().getSelectedItem().equals(PackageContentType.SINGLE_JAR_ONLY)) {
                FileSystemUtil.clearDir(APP_DATA_TMP_PATH);
                FileSystemUtil.copyFile(Path.of(mainJarFileNameTextField.getText()), APP_DATA_TMP_PATH, true);
            }

            /* create a list of separated arguments for the process builder */
            List<String> jPackageArgValueStrings = new ArrayList<>();
            if (chooseOwnJpackageExeChoiceBox.getSelectionModel().getSelectedItem() == UsedJpackage.FROM_WRAPPED_OPEN_JDK_17) {
                jPackageArgValueStrings.add(APP_DATA_INCLUDED_JPACKAGE_EXE_PATH.toString());
            } else {
                jPackageArgValueStrings.add("\"" + Path.of(chooseOwnJpackageExeTextField.getText()) + "\"");
            }
            Arrays.stream(jPackageCommand.substring("jpackage".length()).split(" --"))
                    .filter(e -> !e.isBlank() && !e.isEmpty())
                    .map(e -> "--" + e)
                    .forEach((e) -> Collections.addAll(jPackageArgValueStrings, e.split(" ", 2)));

            /* start the creation process with process builder */
            ProcessBuilder pb = new ProcessBuilder(jPackageArgValueStrings);
            System.out.println("pb-command: " + pb.command());
            pb.start().getInputStream().readAllBytes();

            JfxDialogUtil.createInformationDialog("The native application is created.").showAndWait();

            /* clear environment for single jar packaging mode */
            if (filePackageTypeChoiceBox.getSelectionModel().getSelectedItem().equals(PackageContentType.SINGLE_JAR_ONLY)) {
                FileSystemUtil.clearDir(APP_DATA_TMP_PATH);
            }
        } catch (NotValidException nve) {
            JfxDialogUtil.createErrorDialog(nve.getMessage()).showAndWait();
        } catch (Exception e) {
            JfxDialogUtil.createErrorDialog("An error occurred while creating a native application.", e).showAndWait();
        }
    }

}
