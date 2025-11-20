module com.sikora.atomsexplocik {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.sikora.atomsexplocik to javafx.fxml;
    exports com.sikora.atomsexplocik;
}