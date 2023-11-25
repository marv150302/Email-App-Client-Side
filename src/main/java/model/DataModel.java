package model;
import com.example.progettoprog3.Email;
import com.example.progettoprog3.User;
import javafx.beans.property.SimpleStringProperty;


public class DataModel {

    private SimpleStringProperty sender_email = null;
    private SimpleStringProperty receiver_email = null;
    private SimpleStringProperty email_text = null;
    private SimpleStringProperty email_object = null;
    public User user;
    public Email email = null;

    public DataModel(){

        this.receiver_email = new SimpleStringProperty();
        this.email_text = new SimpleStringProperty();
        this.email_object = new SimpleStringProperty();
        this.sender_email = new SimpleStringProperty();
    }

    public SimpleStringProperty getReceiver_email() {
        return receiver_email;
    }

    public SimpleStringProperty getEmail_text() {return email_text;}

    public SimpleStringProperty getEmail_object(){return this.email_object;}

    public SimpleStringProperty getSender_email() {return sender_email;}





}
