package com.example.progettoprog3;

import javafx.beans.property.SimpleStringProperty; //simple string property library
import model.DataModel;

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
    private DataModel.User sender;
    private SimpleStringProperty receiver = null;
    private SimpleStringProperty argument = null ;
    private SimpleStringProperty text = null;
    //private DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private String date;

    public Email(DataModel.User sender){

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


    /*
    * @param receiverEmail  we use the receiver's email to identify him
    * @param text the email's text
    * @param argument the email's argument
    *
    * this function is used to send an email, if the search function throws an error,
    * we are going to catch it,
    * and we will notify the sender that there was an error
    * */
    public void sendEmail(String[] receiverEmail, String text, String argument){

        if (search(receiverEmail)){


        }
    }

    /*
     * @param receiver the object of the receiver
     * @return boolean true if the receiver is found
     * if the receiver is not found the server is going to throw an error
     * -----------------------------------------------------------------
     * we are going to ask the server to
     * look in its "database"(wich is going to be a json file),
     * if the sender is among the list of its avalible user
     * */
    public boolean search(String[] receiverEmail){

        return true;
    }
}
