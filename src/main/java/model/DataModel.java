package model;

import com.example.progettoprog3.Controller;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.ListView;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;

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
    public DataModel(int socketPort, Controller controller){

        this.receiver_email = new SimpleStringProperty();
        this.email_text = new SimpleStringProperty();
        this.email_object = new SimpleStringProperty();
        this.sender_email = new SimpleStringProperty();
        this.inbox_list = new SimpleStringProperty();
        this.client = new Client(this,socketPort, this.getSender_email().get());
        this.controller = controller;



        test = "hello";

    }

    public SimpleStringProperty getReceiver_email() {
        return receiver_email;
    }

    public SimpleStringProperty getEmail_text() {return email_text;}

    public SimpleStringProperty getEmail_object(){return this.email_object;}

    public SimpleStringProperty getSender_email() {return sender_email;}

    public SimpleStringProperty getInbox_list(){return inbox_list;}

    public void setClient(Socket socket){

        //this.client = new Client(socket, this.getSender_email().getValue());
    }


    public class Client {


        private Socket socket;
        private BufferedReader bufferedReader;
        private BufferedWriter bufferedWriter;
        private String username;

        public DataModel model;

        public String msg_from_server;

        public Client(DataModel model, int port, String username){

            try{

                this.socket = new Socket("localhost", port);
                this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                this.username = username;
                this.model = model;
                this.sendMessage("login",username);
                this.listenForMessage();

            }catch (IOException e){

            }
        }
        public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){

            try{

                if (bufferedReader!=null){

                    bufferedReader.close();
                }
                if (bufferedWriter!=null){

                    bufferedWriter.close();
                }
                if (socket != null){

                    socket.close();
                }
            }catch (IOException e){closeEverything(socket, bufferedReader, bufferedWriter);}
        }

        public void sendMessage(String action, String msg, String... email_info){

            JSONObject json = new JSONObject();
            if (action.equalsIgnoreCase("login")){

                json.put("action", action);
                json.put("msg", msg);
            }else {

                json.put("action", action);
                /*
                * the receiver email will be at the position 0 of the optional parameter
                * (this will still be an array, in the case that we want to send the email to multiple people)
                * */
                org.json.JSONArray ja = new JSONArray();
                ja.put(email_info[0]);
                json.put("receiver", ja);
                /*
                *
                * */
                json.put("object", email_info[1]);//the object of email will be at the position 0 of the optional parameter
                json.put("msg", msg);//the message
            }



            try{

                bufferedWriter.write(json.toString());
                bufferedWriter.newLine();
                bufferedWriter.flush();


            }catch (IOException e){

                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }

        private void setMsg_from_server(String msg){

            this.msg_from_server = msg;
        }
        public String getMsgFromServer(){

            return this.msg_from_server;
        }
        public void listenForMessage(){

            final String[] old_message = {null};
            final JSONObject[] json = new JSONObject[1];
            Thread listenMsg = new Thread(new Runnable() {
                @Override
                public void run() {

                    String msgFromServer = null;
                    while (socket.isConnected()){

                        try{

                            //System.out.println(bufferedReader.readLine());
                            JSONParser parser = new JSONParser();
                            JSONObject message = (JSONObject) parser.parse(bufferedReader.readLine());
                            //System.out.println(action);
                            // action = action.get("action")
                            switch ((String) message.get("action")){

                                case "inbox":{
                                    setMsg_from_server((String) message.get("emails"));
                                    controller.loadUserInbox();
                                    break;
                                }

                            }

                        }catch (IOException e){

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

        public void closeConnection(){

            try{

                this.socket.close();
            }catch (IOException e){e.printStackTrace();}
        }

    }
}
