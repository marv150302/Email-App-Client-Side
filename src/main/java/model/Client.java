package model;

import java.io.*;
import java.net.Socket;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public String msg_from_server;

    public Client(int port, String username){

        try{

            System.out.println(username);
            this.socket = new Socket("localhost", port);
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.sendMessage("login "+username);
            this.username = username;
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

    public void sendMessage(String msg){

        try{

            bufferedWriter.write(msg);
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

        Thread listenMsg = new Thread(new Runnable() {
            @Override
            public void run() {

                String msgFromServer;
                while (socket.isConnected()){

                    try{

                        setMsg_from_server(bufferedReader.readLine());
                        //System.out.println(msgFromServer);
                    }catch (IOException e){

                        closeEverything(socket, bufferedReader, bufferedWriter);
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
