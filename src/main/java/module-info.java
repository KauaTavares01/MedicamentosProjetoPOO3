module org.pacientesys {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.pacientesys to javafx.fxml;
    exports org.pacientesys;
    exports org.pacientesys.controller;
    opens org.pacientesys.controller to javafx.fxml;
}