package model;

import com.example.progettoprog3.Controller;
import javafx.beans.property.SimpleStringProperty;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DataModel {

    private final SimpleStringProperty sender_email;
    private final SimpleStringProperty receiver_email;
    private final SimpleStringProperty email_text;
    private final SimpleStringProperty email_object;
    private final SimpleStringProperty inbox_list;
    public Email email = new Email();

    public Client client;

    public Controller controller;

    public DataModel(Controller controller) {

        this.receiver_email = new SimpleStringProperty();
        this.email_text = new SimpleStringProperty();
        this.email_object = new SimpleStringProperty();
        this.sender_email = new SimpleStringProperty();
        this.inbox_list = new SimpleStringProperty();
        this.controller = controller;

    }

    public void setClient(String username) {

        this.client = new Client(this, username);
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


    public class Client {


        private Socket socket;
        private BufferedReader bufferedReader;
        private String username;

        public DataModel model;

        public String msg_from_server;

        Thread thread;

        public Client(DataModel model, String username) {

            try {
                this.username = username;
                this.model = model;

                this.sendMessage("login");

            } catch (IOException e) {

                model.controller.displayAlert("SERVER IS NOT AVAILABLE, PLEASE TRY AGAIN LATER");
                /*
                 * if the socket is not open we will keep trying to connect every 3 seconds
                 * */
                ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
                ses.scheduleAtFixedRate(() -> {
                    if (socket == null) {

                        try {
                            sendMessage("login");

                        } catch (Exception ignored) {
                        }
                    } else {

                        /*
                         * once the server is available
                         * we stop this scheduled thread
                         * then we display an alert to the user that the server is back on
                         * */
                        model.controller.displayAlert("server is avalible!");
                        ses.shutdown();
                    }


                }, 0, 3000, TimeUnit.MILLISECONDS);
            }
        }

        public void setUsername(String username) {

            this.username = username;
        }

        public String getUsername() {
            return username;
        }

        public void sendMessage(String action) throws IOException {

            this.socket = new Socket("localhost", 5056);
            bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            JSONObject json = new JSONObject();
            switch (action) {

                case "login": {

                    this.listenForMessage();
                    json.put("action", action);
                    json.put("user", this.getUsername());
                    break;
                }

                case "send email": {

                    json.put("action", action);
                    json.put("receiver", getReceiver_email().get());
                    json.put("object", getEmail_object().get());//the object of email will be at the position 0 of the optional parameter
                    json.put("text", getEmail_text().get());//the message
                    json.put("sender", this.username);
                    break;
                }
            }
            try {

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
                bufferedWriter.write(json.toString());
                bufferedWriter.newLine();
                bufferedWriter.flush();

            } catch (IOException e) {

                //closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }

        public void listenForMessage() {

            //exec.shutdown();
            ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
            ses.scheduleAtFixedRate(() -> {

                if (socket != null && socket.isConnected()) {

                    try {
                        socket.setSoTimeout(2000);
                    } catch (SocketException e) {
                    }
                    try {
                        JSONParser parser = new JSONParser();
                        JSONObject message = (JSONObject) parser.parse(bufferedReader.readLine());
                        System.out.println(message);
                        switch ((String) message.get("action")) {


                            case "inbox": {
                                setMsg_from_server((String) message.get("emails"));
                                controller.loadUserInbox((String) message.get("emails"));
                                break;
                            }
                            case "receiving email": {

                                System.out.println("receiving");
                                JSONObject email = (JSONObject) message.get("email");
                                System.out.println(email.toString());
                                //setMsg_from_server((String) message.get("email"));
                                String sender = (String) email.get("email");
                                String receivers = (String) email.get("receiver");
                                String text = (String) message.get("text");
                                String object = (String) email.get("object");
                                //System.out.println("email_ " + text);
                                controller.updateUserInbox(email);
                                model.controller.readNewEmail(new Email("erggere", sender, receivers, text, object, ""));
                                break;
                            }
                            case "confirmed delivery": {

                                model.controller.onCloseNewEmailButtonClick();
                                break;
                            }
                            case "error in delivery": {

                                /*
                                 * there was a problem in delivering the email
                                 * */
                                String failed_emails = (String) message.get("failed-emails");
                                String content = "The email failed to deliver to the following addresses: " + failed_emails;
                                model.controller.displayAlert(content);
                                break;

                            }

                        }

                    } catch (IOException e) {

                        //closeEverything(socket, bufferedReader, bufferedWriter);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }

            }, 0, 3000, TimeUnit.MILLISECONDS);
        }

        private void setMsg_from_server(String msg) {

            this.msg_from_server = msg;
        }

        public String getMsgFromServer() {

            return this.msg_from_server;
        }

    }
}
