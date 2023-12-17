package model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    ArrayList<Email> emails = new ArrayList<>();

    public Email() {
    }

    public Email(String ID, String sender, String receiver, String text, String object, String date) {

        //DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        //LocalDateTime now = LocalDateTime.now();
        //dtf.format(now)
        this.date = date;
        this.sender = sender;
        this.receiver_ = receiver;
        this.text_ = text;
        this.object_ = object;
        this.ID = ID;

    }

    public static boolean isCorrectEmailFormat(String emails) {

        if (emails==null) return false;

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
    public ArrayList<Email> getUserEmails(String email_json) throws IOException, ParseException {

        //System.out.println("email json " + email_json);
        /*
         * We create a cache JSON file containing the user email
         * */
        if (email_json == null) return null;
        /*try {
            File user_emails = new File("/Users/marvel/Programming/Uni/ProgettoProg3/src/main/java/com/example/progettoprog3/emails.json");

            FileWriter load_email_on_file = new FileWriter("/Users/marvel/Programming/Uni/ProgettoProg3/src/main/java/com/example/progettoprog3/emails.json");
            load_email_on_file.write(email_json);
            /*
            * if the file doesn't exist we create a new one
            * and return null(because there are no email to read)
            *
            if (user_emails.createNewFile() || user_emails.length()==0){
                return null;
            }
        }catch (IOException e){e.printStackTrace();}*/

        JSONParser jsonParser = new JSONParser();

        for (Object o : (JSONArray) jsonParser.parse(email_json)) {

            JSONObject rootObj = (JSONObject) o;
            //
            String ID = (String) rootObj.get("ID");
            String sender = (String) rootObj.get("sender");
            String receiver = (String) rootObj.get("receiver");
            String text = (String) rootObj.get("text");
            String object = (String) rootObj.get("object");
            String date = (String) rootObj.get("date");
            //
            emails.addFirst(new Email(ID, sender, receiver, text, object, date));
        }
        return emails;
    }

    public ArrayList<Email> getUserNewEmail(JSONArray email_json) {

        ArrayList<Email> email_list = new ArrayList<>();
        for (Object emails : email_json) {
            JSONObject email = (JSONObject) emails;

            String ID = (String) email.get("ID");
            String sender = (String) email.get("sender");
            String receiver = (String) email.get("receiver");
            String text = (String) email.get("text");
            String object = (String) email.get("object");
            String date = (String) email.get("date");

            this.emails.add(new Email(ID, sender, receiver, text, object, date));
            email_list.add(new Email(ID, sender, receiver, text, object, date));
        }

        //

        return email_list;

    }

    public Email getEmail(String emailID) {

        for (Email email : emails) {

            if (email.getID().equals(emailID)) {

                return email;
            }
        }
        return null;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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

    public void reply(Email email, String reply) {

        String text = email.getText_() + reply;
    }

}