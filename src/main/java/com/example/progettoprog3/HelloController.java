package com.example.progettoprog3;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.BoxBlur;
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
    private Button inbox_button;

    @FXML
    protected void onNewEmailButtonClick(){

        /*
        * enabling and disabling the buttons
        * */
        new_email_button.setDisable(true);
        inbox_button.setDisable(true);
        closeNewEmailButton.setDisable(false);
        newEmailView.setDisable(false);

        /*
         * adding a blur effect
         *
        BoxBlur bb = new BoxBlur();
        bb.setWidth(5);
        bb.setHeight(5);
        bb.setIterations(2);
        inbox_button.setEffect(bb);*/
        //
        //open the new View for sending a new email
        newEmailView.setVisible(true);
    }

    @FXML
    protected void onCloseNewEmailButton(){

        newEmailView.setVisible(false);
        new_email_button.setDisable(false);
        mainView.setDisable(false);
        inbox_button.setDisable(false);

    }
}