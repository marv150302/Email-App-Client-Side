package com.example.progettoprog3;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

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

    /*
    * Object of type model,
    * used to handle the model class
    * */
    private DataModel model = new DataModel();
    private boolean email_flag = false, object_flag = false, text_flag=false;//flags used to check for errors in the fields
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


        System.out.println("email flag: "+this.checkEmail());
        System.out.println("object flag: "+object_flag);
        System.out.println("text flag: "+text_flag);
        System.out.println("------------------------------");
        /*
        * we check the email correctness
        * */
        if (!this.checkEmail() && !this.email_flag){

            this.email_flag = true;//we flag it as an error
            receiver_email.getStyleClass().add("text-field-area-error");//we add the error text border to the text field

        }else if (this.checkEmail() && this.email_flag){

            this.email_flag = false;
            receiver_email.getStyleClass().remove("text-field-area-error");//we remove the error red border to the text field
        }

        /*
        * we check the email object corrected
        * */
        if(this.checkEmailObject() && !this.object_flag){

            this.object_flag = true;
            email_object.getStyleClass().add("text-field-area-error");

        }else if (!this.checkEmailObject() && this.object_flag){

            this.object_flag = false;
            email_object.getStyleClass().remove("text-field-area-error");
        }

        /*
        * we check the email text(empty or not)
        * */
        if (this.checkEmailText() && !this.text_flag){

            this.text_flag = true;
            email_text.getStyleClass().add("text-field-area-error");
        }else if (!this.checkEmailText() && this.text_flag){

            this.text_flag = false;
            email_text.getStyleClass().remove("text-field-area-error");
        }

        return email_flag && object_flag && text_flag;
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
        return matcher.matches();
    }

    private boolean checkEmailObject(){

        return model.getEmail_object().getValue() == null || model.getEmail_object().getValue().isEmpty();
        //if (model.getEmail_object().getValue() == null || model.getEmail_object().getValue().isEmpty()) return false;
        //return this.model.getEmail_object().getValue().isEmpty();
    }
    private boolean checkEmailText(){

        return (model.getEmail_text().getValue() == null || model.getEmail_text().getValue().isEmpty());
        //if (model.getEmail_text().getValue() == null || model.getEmail_text().getValue().isEmpty()) return false;
        //return !this.model.getEmail_text().getValue().isEmpty();
    }




}