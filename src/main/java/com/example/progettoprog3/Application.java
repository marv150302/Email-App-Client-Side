package com.example.progettoprog3;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("View.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 850, 584);
        //stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        Controller contr = fxmlLoader.getController();
        contr.initModel();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}