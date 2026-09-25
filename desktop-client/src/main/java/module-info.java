module com.medvora.desktopclient {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.medvora.desktopclient to javafx.fxml;
    exports com.medvora.desktopclient;
}