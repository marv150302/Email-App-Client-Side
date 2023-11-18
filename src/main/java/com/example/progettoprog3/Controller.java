package com.example.progettoprog3;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.util.regex.*;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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
    * login info
    * */
    @FXML
    private TextField sender_email;
    @FXML
    private Pane login_view;
    @FXML
    private Text username;

    /*
    * Object of type model,
    * used to handle the model class
    * */
    private final DataModel model = new DataModel();
    private boolean sender_email_error_flag = false,receiver_email_error_flag = false, email_object_error_flag = false, email_text_error_flag =false;//flags used to check for errors in the fields
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

    @FXML
    private void onLoginButtonClick(){

        /*
        * if the email we used to enter is written correctly then we "login"
        * */
        if (isCorrectEmail(model.getSender_email().getValue())){
        //newEmailView.setVisible(false);//hide new email view
            left_menu.setDisable(false);
            new_email_button.setDisable(false);//allow pressing new email button
            mainView.setDisable(false);//allow the main view to function
            login_view.setVisible(false);
            username.textProperty().set(model.getSender_email().getValue());
            this.model.user = new DataModel.User("","",model.getSender_email().getValue());
        }else {

            handleLoginError();
        }

    }

    private void handleLoginError(){

        if (!this.isCorrectEmail(model.getSender_email().getValue()) && !this.sender_email_error_flag){

            this.sender_email_error_flag = true;//we flag it as an error
            sender_email.getStyleClass().add("text-field-area-error");//we add the error text border to the text field

        }else if (this.isCorrectEmail(model.getSender_email().getValue()) && this.sender_email_error_flag){

            this.sender_email_error_flag = false;
            sender_email.getStyleClass().remove("text-field-area-error");//we remove the error red border to the text field
        }
    }
    /*
    * InitModel
    * Used to make binding with elements of our view
    * */
    public void initModel(){

        this.receiver_email.textProperty().bindBidirectional(model.getReceiver_email());
        this.email_object.textProperty().bindBidirectional(model.getEmail_object());
        this.email_text.textProperty().bindBidirectional(model.getEmail_text());
        this.sender_email.textProperty().bindBidirectional(model.getSender_email());
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
        System.out.println(this.newEmailformIsFilled());
    }

    private boolean newEmailformIsFilled(){
        /*
        * we check the email correctness
        * */
        if (!this.isCorrectEmail(model.getReceiver_email().getValue()) && !this.receiver_email_error_flag){

            this.receiver_email_error_flag = true;//we flag it as an error
            receiver_email.getStyleClass().add("text-field-area-error");//we add the error text border to the text field

        }else if (this.isCorrectEmail(model.getReceiver_email().getValue()) && this.receiver_email_error_flag){

            this.receiver_email_error_flag = false;
            receiver_email.getStyleClass().remove("text-field-area-error");//we remove the error red border to the text field
        }

        /*
        * we check the email object corrected
        * */
        if(this.emptyEmailObject() && !this.email_object_error_flag){

            this.email_object_error_flag = true;
            email_object.getStyleClass().add("text-field-area-error");

        }else if (!this.emptyEmailObject() && this.email_object_error_flag){

            this.email_object_error_flag = false;
            email_object.getStyleClass().remove("text-field-area-error");
        }

        /*
        * we check the email text(empty or not)
        * */
        if (this.emptyEmailText() && !this.email_text_error_flag){

            this.email_text_error_flag = true;
            email_text.getStyleClass().add("text-field-area-error");
        }else if (!this.emptyEmailText() && this.email_text_error_flag){

            this.email_text_error_flag = false;
            email_text.getStyleClass().remove("text-field-area-error");
        }

        return !(receiver_email_error_flag && email_object_error_flag && email_text_error_flag);
        //return checkEmail() && this.checkEmailText() && this.checkEmailObject();
    }

    protected boolean isCorrectEmail(String email){
        /*
         * if the email is empty or null we return false
         * */
        if (email == null || email.isEmpty()) return false;
        /**/
        String regex = "^(.+)@(\\S+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    /*
    * return:
    * - true: if the field is empty or null
    * */
    private boolean emptyEmailObject(){

        return model.getEmail_object().getValue() == null || model.getEmail_object().getValue().isEmpty();
    }
    private boolean emptyEmailText(){

        return (model.getEmail_text().getValue() == null || model.getEmail_text().getValue().isEmpty());
    }




}