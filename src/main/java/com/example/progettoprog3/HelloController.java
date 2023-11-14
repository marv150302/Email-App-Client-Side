package com.example.progettoprog3;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class HelloController {

    @FXML
    private AnchorPane newEmailView;
    @FXML
    private AnchorPane mainView;
    @FXML
    private Button closeNewEmailButton;

    @FXML
    private Button new_email_button;
    @FXML
    protected void onNewEmailButtonClick(){

        //this.emailViewFlag = !this.emailViewFlag;
        //mainView.setDisable(true);

        new_email_button.setDisable(true);
        closeNewEmailButton.setDisable(false);
        newEmailView.setDisable(false);
        newEmailView.setVisible(true);

    }

    @FXML
    protected void onCloseNewEmailButton(){

        newEmailView.setVisible(false);
        new_email_button.setDisable(false);
        mainView.setDisable(false);
    }
}