package model;

import com.example.progettoprog3.Controller;
import javafx.beans.property.SimpleStringProperty;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
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


    /**
     * @param controller
     */
    public DataModel(Controller controller) {

        this.receiver_email = new SimpleStringProperty();
        this.email_text = new SimpleStringProperty();
        this.email_object = new SimpleStringProperty();
        this.sender_email = new SimpleStringProperty();
        this.inbox_list = new SimpleStringProperty();
        this.controller = controller;

    }

    /**
     * @param username
     */

    public void setClient(String username) {

        this.client = new Client(this, username);
    }

    /**
     * @return
     */
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

        public JSONObject message_to_send;

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
                try {
                    reconnectToServer();
                }catch (IOException error){}
            }
        }

        public void setUsername(String username) {

            this.username = username;
        }

        public String getUsername() {
            return username;
        }


        public void sendMessage(String action) throws IOException {



            message_to_send = new JSONObject();
            //System.out.println("Json value: " + json.isEmpty());
            switch (action) {

                case "login": {

                    this.listenForMessage();
                    message_to_send.put("action", action);
                    message_to_send.put("user", this.getUsername());
                    break;
                }

                case "send email": {

                    message_to_send.put("user", this.getUsername());
                    message_to_send.put("action", action);
                    message_to_send.put("receiver", getReceiver_email().get());
                    message_to_send.put("object", getEmail_object().get());//the object of email will be at the position 0 of the optional parameter
                    message_to_send.put("text", getEmail_text().get());//the message
                    message_to_send.put("sender", this.username);
                    this.model.controller.onCloseNewEmailButtonClick();
                    break;
                }

                /*
                * in case we want to reconnect to the server
                * */
                case "reconnect":{

                    //System.out.println("Reconnecting");
                    message_to_send.put("action", action);
                    message_to_send.put("user", this.getUsername());
                    break;
                }
            }
            /*try {

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
                bufferedWriter.write(message_to_send.toString());
                bufferedWriter.newLine();
                bufferedWriter.flush();

            } catch (IOException e) {

                //closeEverything(socket, bufferedReader, bufferedWriter);
            }*/
        }

        public void listenForMessage() {


            //exec.shutdown();
           /* ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
            ses.scheduleAtFixedRate(() -> {

                //System.out.println("The thread is restarting");

            }, 0, 10000, TimeUnit.MILLISECONDS);*/
            //Client this = this;
            Thread thread = new Thread(() -> {
                //System.out.println("in");
                while (true){


                    /*try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }*/
                    String message;
                    try {

                        socket = new Socket("localhost", 5056);

                        /*
                         * if there are messages to send we send them to the server
                         * */
                        if (!message_to_send.isEmpty()){

                            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                            //System.out.println("There is a messafe to send");
                            try {


                                bufferedWriter.write(message_to_send.toString());
                                bufferedWriter.newLine();
                                bufferedWriter.flush();
                                //bufferedWriter.close();
                            } catch (IOException e) {

                                //closeEverything(socket, bufferedReader, bufferedWriter);
                            }
                            message_to_send = new JSONObject();
                        }else{

                            sendMessage("reconnect");
                            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                            bufferedWriter.write(message_to_send.toString());
                            bufferedWriter.newLine();
                            bufferedWriter.flush();

                        }

                        socket.setSoTimeout(5000);
                        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        message = bufferedReader.readLine();
                        if (message!=null) {
                            /*
                             * Whenver you perform a read (or readUTF in your case), your thread will actually block forever. In my experience this is bad practice for the following reasons:
                             * It's difficult to close your application. Just calling socket.close() is dirty.
                             * A clean solution, is a simple read time-out (e.g. 200ms).
                             * You can do this with the setSoTimeoutmethod. When the read() method timeouts
                             * it will throw a SocketTimeoutException. (which is a subclass of IOException).
                             * */

                            try {
                                JSONParser parser = new JSONParser();
                                JSONObject json = (JSONObject) parser.parse(message);
                                switch ((String) json.get("action")) {


                                    case "inbox": {
                                        setMsg_from_server((String) json.get("emails"));
                                        /*
                                         * if our inbox is empty
                                         * it means we are loggin in for the first time,
                                         * we then proceed to load the inbox
                                         * otherwise we do nothing but notify the server that we are re-loggin in
                                         * after the server had an issue
                                         * */
                                        /*if (model.controller.isInboxEmpty()){

                                        }*/
                                        controller.loadUserInbox((String) json.get("emails"));

                                        break;
                                    }
                                    case "receiving email": {

                                        System.out.println("Receiving");
                                        JSONArray email = (JSONArray) json.get("emails");
                                        System.out.println(email);
                                        /*String sender = (String) email.get("email");
                                        String receivers = (String) email.get("receiver");
                                        String text = (String) json.get("text");
                                        String object = (String) email.get("object");*/
                                        controller.updateUserInbox(email);
                                        model.controller.displayAlert("New Email!");
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
                                        String failed_emails = (String) json.get("failed-emails");
                                        String content = "The email failed to deliver to the following addresses: " + failed_emails;
                                        model.controller.displayAlert(content);
                                        break;

                                    }
                                    case "reconnection accepted":{

                                        model.controller.displayAlert("Server back online");
                                        break;
                                    }

                                }

                            } catch (IOException e) {


                                //System.out.println("the server is not avalible");
                                //closeEverything(socket, bufferedReader, bufferedWriter);
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                        } else{

                            //System.out.println("Socket is disconnected");
                            //this.socket=null;
                            //reconnectToServer();
                            //ses.shutdown();
                        }
                    }catch (IOException e){

                /*try {
                    //reconnectToServer();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }*/
                    }


                    try{
                        /*BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                        //System.out.println("Closing");
                        message_to_send = new JSONObject();
                        message_to_send.put("user", this.username);
                        message_to_send.put("action", "closing");
                        bufferedWriter.write(message_to_send.toString());
                        bufferedWriter.newLine();
                        bufferedWriter.flush();*/
                        if (socket!=null){

                            socket.close();
                            ///System.out.println("Socket is closed: " + socket.isClosed());
                            bufferedReader.close();
                        }
                        //message_to_send = new JSONObject();
                        //sendMessage("reconnect");
                        //break;
                    }catch (IOException e){}
                    //System.out.println("At the ending");

                }
            });
            thread.start();

            //System.out.println(thread.isAlive());
        }


        private void reconnectToServer() throws IOException {

            ScheduledExecutorService reconnecting_thread = Executors.newScheduledThreadPool(1);
            reconnecting_thread.scheduleAtFixedRate(() -> {
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
                    //model.controller.displayAlert("server is avalible!");
                    reconnecting_thread.shutdown();
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
