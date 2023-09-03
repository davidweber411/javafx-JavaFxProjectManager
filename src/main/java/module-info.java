module com.wedasoft.javafxprojectgenerator {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.wedasoft.javafxprojectgenerator to javafx.fxml;
    exports com.wedasoft.javafxprojectgenerator;
}