package com.example.progettoprog3;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import model.*;

public class Controller {

    /*
    * When you want to send a new email,
    * we open a new smaller view
    * which will contain the textfields and textareas,
    * for the receiver email, the email object, and the email text
    * as well as the sending and closing button(in case you don't want to send an email anymore)
    * */
    @FXML
    private AnchorPane newEmailView;

    /*
    *
    * this is the first view you see when you open
    * the application
    * */
    @FXML
    private AnchorPane mainView;

    /*
    * this is the  button used to close the
    * "new email view" mentioned above,
    * which will be used to close the view if we don't want to send the email
    * */
    @FXML
    private Button closeNewEmailButton;
    @FXML
    private Button new_email_button;
    @FXML
    private Button inbox_button;
    @FXML
    private VBox left_menu;
    @FXML
    private TextField receiver_email;

    private DataModel model = new DataModel();

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
        * open the new View for sending a new email
        * */
        newEmailView.setVisible(true);
    }

    protected void setBlurredBackground(){

        BoxBlur bb = new BoxBlur();
        bb.setWidth(5);
        bb.setHeight(5);
        bb.setIterations(2);

        /*
         * adding a blur effect
         */

        inbox_button.setEffect(bb);
        left_menu.setEffect(bb);
        //mainView.setEffect(bb);
        //inbox_button.setEffect(null);
    }

    public void initModel(){

        receiver_email.textProperty().bindBidirectional(model.getReceiver_email());
    }
    @FXML
    protected void onCloseNewEmailButton(){

        newEmailView.setVisible(false);
        new_email_button.setDisable(false);
        mainView.setDisable(false);
        inbox_button.setDisable(false);

    }
}