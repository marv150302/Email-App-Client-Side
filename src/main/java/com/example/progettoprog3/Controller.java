package com.example.progettoprog3;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.regex.*;

import javafx.scene.layout.VBox;
import model.*;

/*
* This is the controller class used to handle our views elements
* @Author Marvel Asuenimhen
* */
public class Controller {

    /*-------------------------------------
    * newEmailView
    * When you want to send a new email,
    * we open a new smaller view
    * which will contain the textfields and textareas,
    * for the receiver email, the email object, and the email text
    * as well as the sending and closing button(in case you don't want to send an email anymore)
    * @FXML
    * */
    @FXML
    private AnchorPane newEmailView;

    /*-------------------------------------
    * mainView
    * this is the first view you see when you open
    * the application
    * @FXML
    * */
    @FXML
    private AnchorPane mainView;

    /*-------------------------------------
    * closeNewEmailButton
    * this is the  button used to close the
    * "new email view" mentioned above,
    * which will be used to close the view if we don't want to send the email
    * @FXML
    * */
    @FXML
    private Button closeNewEmailButton;

    /*-------------------------------------
    * newEmailButton
    * This button is used to create a new Email
    * @FXML
    * */
    @FXML
    private Button new_email_button;

    /*-------------------------------------
    * inboxButton
    * this button is (for now the only one avalible on the side menu)
    * that allows the user to view previous and new emails
    *@FXML
    * */
    @FXML
    private Button inbox_button;

    /*-------------------------------------
    * left_menu
    * the left side menu which contains the inbox button
    * mentioned above
    * */
    @FXML
    private VBox left_menu;

    /*-------------------------------------
    * receiver_email
    * this is a textField used to get the receiver's email
    * typed by the sender
    * */
    @FXML
    private TextField receiver_email;

    @FXML
    private TextField email_object;

    @FXML
    private TextArea email_text;

    private boolean[] checkFields = {true, true, true};
    /*
    * Object of type model,
    * used to handle the model class
    * */
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

    /*
    * InitModel
    * Used to make binding with elements of our view
    * */
    public void initModel(){

        receiver_email.textProperty().bindBidirectional(model.getReceiver_email());
        email_object.textProperty().bindBidirectional(model.getEmail_object());
        this.email_text.textProperty().bindBidirectional(model.getEmail_text());
    }
    @FXML
    protected void onCloseNewEmailButton(){

        newEmailView.setVisible(false);//hide new email view
        new_email_button.setDisable(false);//allow pressing new email button
        mainView.setDisable(false);//allow the main view to function
        inbox_button.setDisable(false);//allow the inbox button to work
    }

    @FXML
    protected void onSendEmailButton(){

        /*
        * we check the fields
        * */
        /*
        * we are going to make
        * our HTTP request from here
        * */
        this.checkFields();
    }

    private boolean checkFields(){


        /*
        * we check the email correctness
        * */
        if (!this.checkEmail()){

            receiver_email.getStyleClass().add("text-field-area-error");//we add the error text border to the text field
            return false;
        }else{

            receiver_email.getStyleClass().remove("text-field-area-error");//we remove the error red border to the text field
        }

        /*
        * we check the email object corrected
        * */

        return true;
        //return checkEmail() && this.checkEmailText() && this.checkEmailObject();
    }

    protected boolean checkEmail(){
        /*
         * if the email is empty or null we return false
         * */
        if (model.getReceiver_email().getValue() == null || model.getReceiver_email().getValue().isEmpty()) return false;
        /**/
        String regex = "^(.+)@(\\S+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(model.getReceiver_email().getValue());
        this.checkFields[0] = matcher.matches();
        return matcher.matches();
    }

    private boolean checkEmailObject(){

        this.checkFields[1] = !this.model.getEmail_object().getValue().isEmpty();
        return !this.model.getEmail_object().getValue().isEmpty();
    }
    private boolean checkEmailText(){


        this.checkFields[2] = !this.model.getEmail_text().getValue().isEmpty();
        return !this.model.getEmail_text().getValue().isEmpty();
    }




}