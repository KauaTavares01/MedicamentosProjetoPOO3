package org.pacientesys.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PacienteController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}