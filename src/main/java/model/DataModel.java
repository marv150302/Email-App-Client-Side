package model;
import javafx.beans.property.SimpleStringProperty;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        private String sender;
        private String receiver_;
        private String object_;
        private String text_;
        private String date;

        public Email(){}
        public Email(String ID, String sender, String receiver, String text, String object, String date){

            //DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            //LocalDateTime now = LocalDateTime.now();
            //dtf.format(now)
            this.date = date;
            this.sender = sender;
            this.receiver_ = receiver;
            this.text_ = text;
            this.object_ = object;

        }
        public static boolean isCorrectEmailFormat(String emails){


            if (emails.isEmpty()) return false;
            String[] email_list = emails.split(" ");
            for (String s : email_list) {

                String regex = "^(.+)@(\\S+)$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(s);
                if (!matcher.matches()) return false;
            }
            return true;
        }


        /*
         * We will make a call to the server
         * which will give us back our inbox list as a JSON file
         * @return the JSON array containing our Inbox List
         * */
        public ArrayList<Email> getUserEmails(String User) throws IOException, ParseException {

            String src = "/Users/marvel/Programming/Uni/ProgettoProg3/src/main/java/com/example/progettoprog3/test.json";
            ArrayList<Email> emails = new ArrayList<>();
            JSONParser jsonParser = new JSONParser();

            for (Object o : (JSONArray) jsonParser.parse(new FileReader(src))) {

                JSONObject rootObj = (JSONObject) o;
                //
                String ID = (String) rootObj.get("ID");
                String sender = (String) rootObj.get("sender");
                String receiver = (String) rootObj.get("receiver");
                String text = (String) rootObj.get("text");
                String object = (String) rootObj.get("object");
                String date = (String) rootObj.get("date");
                //
                emails.add(new Email(ID, sender,receiver,text, object, date));
            }
            return emails;
        }

        public String getID() {
            return ID;
        }

        public String getDate() {
            return date;
        }

        public String getObject_() {
            return object_;
        }

        public String getSender() {
            return sender;
        }

        public String getReceiver_() {
            return receiver_;
        }

        public String getText_() {
            return text_;
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


}
