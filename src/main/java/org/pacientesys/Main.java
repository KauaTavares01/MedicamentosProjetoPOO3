package org.pacientesys;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.pacientesys.utils.PatchFXML;

import java.io.FileInputStream;
import java.io.IOException;

import static javafx.application.Application.launch;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(new FileInputStream(PatchFXML.pathBase()+"\\main-view.fxml"));
        Scene scene = new Scene (root, 800, 600);
        stage.setTitle("Medicamentos");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}