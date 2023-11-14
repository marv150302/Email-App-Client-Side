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
    private Utente receiver;
    private String argument;
    private String text;
    //private DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private String date;

    public Email(String ID, Utente sender, Utente receiver){

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
        this.receiver = receiver;

    }
}
