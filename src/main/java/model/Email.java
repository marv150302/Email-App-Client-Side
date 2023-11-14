package model;

import javafx.beans.property.SimpleStringProperty; //simple string property library

import java.util.Date;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

/*
* The Email class is used by istances of the messages of the emails
*
*
* @author Marvel Asuenimhen
* @version 1.0
*
* */
public class Email {

    private String ID;
    private Utente sender;
    private SimpleStringProperty receiver = null;
    private SimpleStringProperty argument = null ;
    private SimpleStringProperty text = null;
    //private DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private String date;

    public Email(String ID, Utente sender){

        this.ID = ID;

        /*
        * Date handling that will be pushed to the sending function
        * */
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        this.date = dtf.format(now);
        /*
        * ----------------
        * */

        this.sender = sender;
    }

    public boolean sendEmail(String text, String argument){


        return true;
    }

    /*
     * we are going to ask the server to
     * look in its "database"(wich is going to be a json file),
     * if the sender is among the list of its avalible user
     * @param receiver the object of the receiver
     * @return boolean true if the receiver is found
     * */
    public boolean search(Utente receiver){

        return true;
    }
}
