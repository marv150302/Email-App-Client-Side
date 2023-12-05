package com.example.progettoprog3;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.*;
import org.json.simple.parser.ParseException;

/*
* This is the controller class used to handle our views elements
* @Author Marvel Asuenimhen
* */
public class Controller {

    BoxBlur bb = new BoxBlur();//for blurred background on login
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


    /*
    * inbox_list
    * a listView used to display the user's inbox
    * */
    @FXML
    private TableView<Email> inbox_list;
    @FXML
    private TableColumn<Email, String> tableFromColumn;
    @FXML
    private TableColumn<Email,String> tableObjectColumn;
    @FXML
    private TableColumn<Email, String> tableDateColumn;

    /*
    * readEmailView
    * the view that will be  opened when the user clicks
    * on one of the emails in the  inbox list(inbox_list TableView)
    * */
    @FXML
    private Pane readEmailView;
    @FXML
    private TextField received_email_object;
    @FXML
    private TextArea received_email_text;
    @FXML
    private TextField received_email_receivers;
    @FXML
    private TextField received_email_sender;

    /*-------------------------------------
    * receiver_email
    * this is a textField used to get the receiver's email
    * typed by the sender
    * */
    @FXML
    private TextField receiver_email;

    /*
    * email_object
    * the email object or argument
    * */
    @FXML
    private TextField email_object;

    /*
    * email_text
    * the email text
    * */
    @FXML
    private TextArea email_text;

    /*
    * login_view --> the view that will be opened
    * when the user starts the application
    * it will show the login box
    *
    * */
    @FXML
    private Pane login_view;

    /*
    * The textField in which the user is going to insert his email
    * which (for now), we are going to use
    * as the only necessary credential to login
    * */
    @FXML
    private TextField sender_email;

    /*
    * username
    * A simple text to be displayed at
    * the top of the application
    * to show the username once
    * the user has logged in
    * */
    @FXML
    private Text username;

    /*
    * Object of type model,
    * used to handle the model class
    * */
    public DataModel model;

    /*
    * These Flags are used to signal an error in fields filling
    * */
    private boolean sender_email_error_flag = false,receiver_email_error_flag = false, email_object_error_flag = false, email_text_error_flag =false;//flags used to check for errors in the fields
    private Email selectedEmail = null;

    /*
    *
    * init function used for binding
    * */
    public void initModel(){



        this.model = new DataModel(5056, this);
        /*
         * Binding
         * */
        this.receiver_email.textProperty().bindBidirectional(model.getReceiver_email());
        this.email_object.textProperty().bindBidirectional(model.getEmail_object());
        this.email_text.textProperty().bindBidirectional(model.getEmail_text());
        this.sender_email.textProperty().bindBidirectional(model.getSender_email());
        this.inbox_list.accessibleTextProperty().bindBidirectional((model.getInbox_list()));


        /*
         * We set the background view and its element to blur,
         * while the login view is being shown
         * */
        bb.setWidth(3);
        bb.setHeight(3);
        left_menu.setEffect(bb);
        new_email_button.setEffect(bb);

    }

    /*
    * Function used to handle
    * the moment the user wants to send a new email
    * */
    @FXML
    protected void onNewEmailButtonClick(){

        /*
        * enabling and disabling the buttons
        * */
        new_email_button.setDisable(true);
        inbox_button.setDisable(true);
        closeNewEmailButton.setDisable(false);
        newEmailView.setDisable(false);
        username.setVisible(false);
        inbox_list.setVisible(false);
        readEmailView.setVisible(false);
        /*
        * open the new View for sending a new email
        * */
        newEmailView.setVisible(true);
    }

    /*
    * Function used to handle the moment the user
    * doesn't want to send the email anymore
    * by clicking on the close button
    * */
    @FXML
    protected void onCloseNewEmailButtonClick(){

        newEmailView.setVisible(false);//hide new email view
        new_email_button.setDisable(false);//allow pressing new email button
        mainView.setDisable(false);//allow the main view to function
        inbox_button.setDisable(false);//allow the inbox button to work
        username.setVisible(true);//show the username
        inbox_list.setVisible(true);//show the inbox list
        receiver_email.promptTextProperty().set("To:");

        this.receiver_email.textProperty().set("");
        this.email_object.textProperty().set("");
        this.email_text.textProperty().set("");
    }

    /*
    * Function used to handle the moment
    * the user clicks on one of the emails
    * present in the inbox list
    * */
    @FXML
    public void readEmail(MouseEvent arg0) {


        this.selectedEmail = (Email) inbox_list.getSelectionModel().getSelectedItem();
        if (this.selectedEmail==null) return;
        readEmailView.setVisible(true);
        inbox_list.setVisible(false);
        String selectedEmailId = this.selectedEmail.getID();
        this.selectedEmail = this.model.email.getEmail(selectedEmailId);

        this.received_email_sender.textProperty().set(this.selectedEmail.getSender());
        this.received_email_object.textProperty().set(this.selectedEmail.getObject_());
        this.received_email_receivers.textProperty().set(this.selectedEmail.getReceiver_());
        this.received_email_text.textProperty().set(this.selectedEmail.getText_());
    }

    @FXML
    public void closeEmail(){

        readEmailView.setVisible(false);
        inbox_list.setVisible(true);
    }

    /*
    * function used to log in
    * */


    @FXML
    private void onLoginButtonClick() throws IOException, ParseException {

        String user_id="";
        /*
        * if the email we used to enter is written correctly then we "login"
        * */
        if (Email.isCorrectEmailFormat(model.getSender_email().getValue())){

            left_menu.setDisable(false);
            new_email_button.setDisable(false);//allow pressing new email button
            mainView.setDisable(false);//allow the main view to function
            login_view.setVisible(false);
            username.textProperty().set(model.getSender_email().getValue());
            bb.setWidth(0);
            bb.setHeight(0);
            inbox_list.setVisible(true);

            this.model.client.sendMessage("login",username.textProperty().getValue());
            //System.out.println(this.model.client.getMsgFromServer());
            //System.out.println(username);
            //this.model.client.getMsgFromServer();
            //this.loadUserInbox();
        }else {

            /*
            * if there is a mistake in logging in we handle the error graphically
            * */
            onLoginError();
        }

    }

    /*
    * if there is an error in the login,
    * we graphically let the user know by highlighting the mistake
    * */
    private void onLoginError(){

        if (!Email.isCorrectEmailFormat(model.getSender_email().getValue()) && !this.sender_email_error_flag){

            this.sender_email_error_flag = true;//we flag it as an error
            sender_email.getStyleClass().add("text-field-area-error");//we add the error text border to the text field

        }else if (Email.isCorrectEmailFormat(model.getSender_email().getValue()) && this.sender_email_error_flag){

            this.sender_email_error_flag = false;
            sender_email.getStyleClass().remove("text-field-area-error");//we remove the error red border to the text field
        }
    }
    /*
    * InitModel
    * Used to make binding with elements of our view
    * */


    /*
    * this function is used to load the user inbox
    * */
    public void loadUserInbox() throws IOException, ParseException {
        /*
        * we get the JSON array by making
        * a server call with the "getUserInbox" in the  USER class
        * */
        //ArrayList<Email> emails = this.model.email.getUserEmails(this.model.getSender_email().getValue());

        //String user_file_path = "/Users/marvel/Programming/Uni/ProgettoProg3/src/main/java/com/example/progettoprog3/"+this.username.textProperty().getValue()+".json";
        inbox_list.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        //System.out.println("message in controller: "+ this.model.client.getMsgFromServer());
        System.out.println("emails: " + model.client.getMsgFromServer());
        ArrayList<Email> emails = null;
        try {

            emails = model.email.getUserEmails(model.client.getMsgFromServer());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        if (emails==null) return;

        //System.out.println("here");
        inbox_list.getItems().addAll(emails);
        tableFromColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSender()));
        tableObjectColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getObject_()));
        tableDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDate()));
       // tableFromColumn.setCellValueFactory(Email -> new SimpleStringProperty(cellData.getValue().getPatientDTO().getPatientName())););
    }

    @FXML
    protected void onSendEmailButtonClick(){

        //ArrayList<String> receivers_email = new ArrayList<>();
        //.replaceAll("\\s+","")
        //String[] emails = model.getReceiver_email().getValue().split(" ");
        //System.out.println("0 "+parts[0] + ", 1 "+parts[1]);
        //System.out.println(parts[0]);
        /*
        * we check the fields
        * */
        /*
        * we are going to make
        * our HTTP request from here
        * */
        System.out.println(this.isEmailFormFilled(model.getReceiver_email().getValue()));
    }

    private boolean isEmailFormFilled(String emails ){
        /*
        * we check the email correctness
        * */
        if (!Email.isCorrectEmailFormat(emails) && !this.receiver_email_error_flag){

            this.receiver_email_error_flag = true;//we flag it as an error
            receiver_email.getStyleClass().add("text-field-area-error");//we add the error text border to the text field

        }else if (Email.isCorrectEmailFormat(emails) && this.receiver_email_error_flag){

            this.receiver_email_error_flag = false;
            receiver_email.getStyleClass().remove("text-field-area-error");//we remove the error red border to the text field
        }

        /*
        * we check the email object corrected
        * */
        if(this.isEmptyEmailObject() && !this.email_object_error_flag){

            this.email_object_error_flag = true;
            email_object.getStyleClass().add("text-field-area-error");

        }else if (!this.isEmptyEmailObject() && this.email_object_error_flag){

            this.email_object_error_flag = false;
            email_object.getStyleClass().remove("text-field-area-error");
        }

        /*
        * we check the email text(empty or not)
        * */
        if (this.isEmptyEmailText() && !this.email_text_error_flag){

            this.email_text_error_flag = true;
            email_text.getStyleClass().add("text-field-area-error");
        }else if (!this.isEmptyEmailText() && this.email_text_error_flag){

            this.email_text_error_flag = false;
            email_text.getStyleClass().remove("text-field-area-error");
        }

        return !(receiver_email_error_flag && email_object_error_flag && email_text_error_flag);
        //return checkEmail() && this.checkEmailText() && this.checkEmailObject();
    }
    /*
    * @return: true: if the field is empty or null
    * */
    private boolean isEmptyEmailObject(){

        return model.getEmail_object().getValue() == null || model.getEmail_object().getValue().isEmpty();
    }

    /*
     * @return: true: if the field is empty or null
     * */
    private boolean isEmptyEmailText(){

        return (model.getEmail_text().getValue() == null || model.getEmail_text().getValue().isEmpty());
    }

    @FXML
    private void onReplyButtonClick(ActionEvent event){

        Button source = (Button) event.getSource();
        if (source.getId().equalsIgnoreCase("reply")){

            this.receiver_email.textProperty().set(this.selectedEmail.getSender());
        }else if (source.getId().equalsIgnoreCase("forward")){

            this.receiver_email.promptTextProperty().set("Insert Email or Emails to forward to:");
        }
        this.onNewEmailButtonClick();


        this.email_object.textProperty().set(this.selectedEmail.getObject_());
        this.email_text.textProperty().set(this.selectedEmail.getText_() + "\n\n\n ------------------------------------------------------------------------- \n\n");
        //this.model.email.reply(this.selectedEmail);
    }




}