package model;

import javafx.beans.property.SimpleStringProperty;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class DataModel {

    private SimpleStringProperty sender_email = null;
    private SimpleStringProperty receiver_email = null;
    private SimpleStringProperty email_text = null;
    private SimpleStringProperty email_object = null;
    public Email email = new Email();

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
