package model;

import com.example.progettoprog3.Controller;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class DataModel {

    private SimpleStringProperty sender_email = null;
    private SimpleStringProperty receiver_email = null;
    private SimpleStringProperty email_text = null;
    private SimpleStringProperty email_object = null;
    private SimpleStringProperty inbox_list = null;
    public Email email = new Email();

    public String test = "";
    public Client client;

    public Controller controller;

    public DataModel(int socketPort, Controller controller) {

        this.receiver_email = new SimpleStringProperty();
        this.email_text = new SimpleStringProperty();
        this.email_object = new SimpleStringProperty();
        this.sender_email = new SimpleStringProperty();
        this.inbox_list = new SimpleStringProperty();
        this.client = new Client(this, socketPort, this.getSender_email().get());
        this.controller = controller;
    }

    public SimpleStringProperty getReceiver_email() {
        return receiver_email;
    }

    public SimpleStringProperty getEmail_text() {
        return email_text;
    }

    public SimpleStringProperty getEmail_object() {
        return this.email_object;
    }

    public SimpleStringProperty getSender_email() {
        return sender_email;
    }

    public SimpleStringProperty getInbox_list() {
        return inbox_list;
    }

    public void setClient(Socket socket) {

        //this.client = new Client(socket, this.getSender_email().getValue());
    }


    public class Client {


        private Socket socket;
        private BufferedReader bufferedReader;
        private BufferedWriter bufferedWriter;
        private String username;

        public DataModel model;

        public String msg_from_server;

        public Client(DataModel model, int port, String username) {

            try {

                this.socket = new Socket("localhost", port);
                this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                this.username = username;
                this.model = model;
                this.sendMessage("login");
                this.listenForMessage();

            } catch (IOException e) {

            }
        }

        public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {

            try {

                if (bufferedReader != null) {

                    bufferedReader.close();
                }
                if (bufferedWriter != null) {

                    bufferedWriter.close();
                }
                if (socket != null) {

                    socket.close();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }


        public void sendMessage(String action) {

            JSONObject json = new JSONObject();
            switch (action) {

                case "login": {

                    System.out.println("loggin in");
                    json.put("action", action);
                    json.put("user", this.model.getSender_email().getValue());
                    break;
                }

                case "send email": {

                    //System.out.println("receiver email"  +getReceiver_email().getValue());
                    json.put("action", action);
                    json.put("receiver", getReceiver_email().get());
                    json.put("object", getEmail_object().get());//the object of email will be at the position 0 of the optional parameter
                    json.put("text", getEmail_text().get());//the message
                    break;
                }
            }


            try {

                System.out.println(json);
                bufferedWriter.write(json.toString());
                bufferedWriter.newLine();
                bufferedWriter.flush();


            } catch (IOException e) {

                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }

        private void setMsg_from_server(String msg) {

            this.msg_from_server = msg;
        }

        public String getMsgFromServer() {

            return this.msg_from_server;
        }

        public void listenForMessage() {

            final String[] old_message = {null};
            final JSONObject[] json = new JSONObject[1];
            Thread listenMsg = new Thread(new Runnable() {
                @Override
                public void run() {

                    String msgFromServer = null;
                    while (socket.isConnected()) {

                        try {

                            JSONParser parser = new JSONParser();
                            JSONObject message = (JSONObject) parser.parse(bufferedReader.readLine());
                            switch ((String) message.get("action")) {

                                case "inbox": {
                                    setMsg_from_server((String) message.get("emails"));
                                    controller.loadUserInbox();
                                    break;
                                }
                                case "receiving email": {

                                    setMsg_from_server((String) message.get("text"));

                                    String sender = (String) message.get("sender");
                                    String receivers = (String) message.get("receiver");
                                    String text = (String) message.get("text");
                                    String object = (String) message.get("object");
                                    model.controller.readNewEmail(new Email("erggere", sender, receivers, text, object, ""));
                                    ;
                                    break;
                                }
                                case "confirmed delivery":{

                                    model.controller.onCloseNewEmailButtonClick();
                                    break;
                                }
                                case "error in delivery":{

                                    /*
                                    * there was a problem in deliverying the email
                                    * */

                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("Information Dialog");
                                    alert.setHeaderText("Look, an Information Dialog");
                                    alert.setContentText("I have a great message for you!");

                                    alert.showAndWait();
                                    String failed_emails = (String) message.get("failed-emails");
                                    String content = "The email failed to deliver to the following addresses: " + failed_emails;
                                    model.controller.displayAlert("Failed email delivery", content);
                                    //model.controller.notifySendingError((String) message.get("failed-emails"));
                                    break;

                                }

                            }

                        } catch (IOException e) {

                            closeEverything(socket, bufferedReader, bufferedWriter);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
            listenMsg.setDaemon(true);
            listenMsg.start();
        }

        public void closeConnection() {

            try {

                this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
