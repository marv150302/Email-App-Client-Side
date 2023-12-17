package com.example.progettoprog3;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.DataModel;
import model.Email;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;

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
    public TableView<Email> inbox_list;
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

    @FXML
    private Button delete_email_button;

    /*
    * Object of type model,
    * used to handle the model class
    * */
    public DataModel model;

    /*
    * Alert DialogPane
    * */
    @FXML
    private Pane alert_pane;
    @FXML
    private TextArea alert_text;
    @FXML
    private Button close_alert_button;
    /*
    * These Flags are used to signal an error in fields filling
    * */
    private boolean sender_email_error_flag = false;
    private Email selectedEmail = null;

    /*
    *
    * init function used for binding
    * */
    public void initModel(){

        this.model = new DataModel(this);
        /*
         * Binding
         * */
        this.receiver_email.textProperty().bindBidirectional(model.getReceiver_email());
        this.email_object.textProperty().bindBidirectional(model.getEmail_object());
        this.email_text.textProperty().bindBidirectional(model.getEmail_text());
        this.sender_email.textProperty().bindBidirectional(model.getSender_email());
        this.inbox_list.accessibleTextProperty().bindBidirectional((model.getInbox_list()));
        inbox_list.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);//for preventing the user from resizing the inbox view


        /*
         * We set the background view and its element to blur,
         * while the login view is being shown
         * */
        bb.setWidth(3);
        bb.setHeight(3);
        left_menu.setEffect(bb);
        new_email_button.setEffect(bb);

    }

    public boolean isInboxEmpty(){

       // ObservableList<> items = inbox_list.getItems();

       return Bindings.isEmpty(inbox_list.getItems()).get();

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
    public void displayAlert(String content){

        //this.alert_pane.setHeaderText(header);
        //this.alert_pane.setContentText(content);
        this.alert_pane.setVisible(true);
        this.alert_text.setText(content);
        bb.setWidth(3);
        bb.setHeight(3);
        inbox_list.setEffect(bb);
        left_menu.setEffect(bb);
        newEmailView.setEffect(bb);

    }
    public void addRemoveErrorClass(){

    }
    @FXML
    public void setCloseAlert(){

        this.alert_pane.setVisible(false);
        inbox_list.setEffect(null);
        left_menu.setEffect(null);
        newEmailView.setEffect(null);
    }

    /*
    * Function used to handle the moment the user
    * doesn't want to send the email anymore
    * by clicking on the close button
    * */
    @FXML
    public void onCloseNewEmailButtonClick(){

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


        this.delete_email_button.setVisible(true);
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
    public void readNewEmail(Email email){

        readEmailView.setVisible(true);
        inbox_list.setVisible(false);
        this.selectedEmail = email;
        this.received_email_sender.textProperty().set(this.selectedEmail.getSender());
        this.received_email_object.textProperty().set(this.selectedEmail.getObject_());
        this.received_email_receivers.textProperty().set(this.selectedEmail.getReceiver_());
        this.received_email_text.textProperty().set(this.selectedEmail.getText_());
    }

    @FXML
    public void closeEmail(){

        readEmailView.setVisible(false);
        inbox_list.setVisible(true);
        this.delete_email_button.setVisible(false);
    }

    /*
    * function used to log in
    * */

    @FXML
    public void deleteEmail(){

        Email selectedItem = inbox_list.getSelectionModel().getSelectedItem();
        inbox_list.getItems().remove(selectedItem);
        closeEmail();
    }

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
            this.model.setClient(username.textProperty().getValue());
            this.model.client.setUsername( username.textProperty().getValue());

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



    public void loadUserInbox(String email_json) throws IOException, ParseException {
        /*
        * we get the JSON array by making
        * a server call with the "getUserInbox" in the  USER class
        * */

        ArrayList<Email> emails = null;
        try {

            emails = model.email.getUserEmails(email_json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        if (emails==null) return;

        inbox_list.getItems().addAll(0,emails);
        tableFromColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSender()));
        tableObjectColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getObject_()));
        tableDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDate()));
    }

    public void updateUserInbox(JSONArray email_json){

        //System.out.println(email_json);
        ArrayList<Email> emails = model.email.getUserNewEmail(email_json);
        System.out.println(email_json);
        if (emails==null)return;
        inbox_list.getItems().addAll(0,emails);
        tableFromColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSender()));
        tableObjectColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getObject_()));
        tableDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDate()));

    }

    @FXML
    protected void onSendEmailButtonClick() throws IOException {

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
        String message = this.email_text.textProperty().getValue();
        if (this.isEmailFormFilled(model.getReceiver_email().getValue())){

            this.model.client.sendMessage("send email");
        }
    }

    private boolean isEmailFormFilled(String emails ){
        /*
        * we check the email correctness
        * */
        boolean receiver_email_error_flag = false;
        if (!Email.isCorrectEmailFormat(emails)){

            receiver_email_error_flag = true;//we flag it as an error
            if (!receiver_email.getStyleClass().contains("text-field-area-error")){
                receiver_email.getStyleClass().add("text-field-area-error");//we add the error text border to the text field
            }


        }else{

            receiver_email_error_flag = false;
            receiver_email.getStyleClass().remove("text-field-area-error");//we remove the error red border to the text field
        }

        /*
        * we check the email object corrected
        * */
        //System.out.println("object ckass " + );
        boolean email_object_error_flag = false;
        if(this.isEmptyEmailObject()){
            email_object_error_flag = true;
            if (!email_object.getStyleClass().contains("text-field-area-error")){
                email_object.getStyleClass().add("text-field-area-error");
            }


        }else{

            email_object_error_flag = false;
            email_object.getStyleClass().remove("text-field-area-error");
        }

        /*
        * we check the email text(empty or not)
        * */
        //flags used to check for errors in the fields
        boolean email_text_error_flag = false;
        if (this.isEmptyEmailText() ){

            email_text_error_flag = true;
            if (!email_text.getStyleClass().contains("text-field-area-error")){

                email_text.getStyleClass().add("text-field-area-error");
            }

        }else{
            email_text_error_flag = false;
            email_text.getStyleClass().remove("text-field-area-error");
        }

        return !(receiver_email_error_flag || email_object_error_flag || email_text_error_flag);
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

        this.delete_email_button.setVisible(false);
        Button source = (Button) event.getSource();
        if (source.getId().equalsIgnoreCase("reply")){

            this.receiver_email.textProperty().set(this.selectedEmail.getSender());
            //System.out.println(this.selectedEmail.getReceiver_());

        }else if (source.getId().equalsIgnoreCase("forward")){

            this.receiver_email.promptTextProperty().set("Insert Email or Emails to forward to:");
        }
        else if (source.getId().equalsIgnoreCase("reply_all")){

            String receivers = this.selectedEmail.getSender() + " " + this.selectedEmail.getReceiver_().replace(this.username.getText(), "");
            this.receiver_email.textProperty().set(receivers);
            //System.out.println("receivers without me: " + );
        }
        this.onNewEmailButtonClick();


        this.email_object.textProperty().set(this.selectedEmail.getObject_());
        this.email_text.textProperty().set(this.selectedEmail.getText_() + "\n\n\n ------------------------------------------------------------------------- \n\n");
    }




}