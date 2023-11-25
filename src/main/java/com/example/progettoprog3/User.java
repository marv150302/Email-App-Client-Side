package com.example.progettoprog3;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class User {

    private String nome;
    private String cognome;
    private String email;

    public User(String nome, String cognome, String email){

        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
    }

    /*
     * We will make a call to the server
     * which will give us back our inbox list as a JSON file
     * @return the JSON array containing our Inbox List
     * */
    public JSONArray getUserInbox() throws IOException, ParseException {

        String src = "/Users/marvel/Programming/Uni/ProgettoProg3/src/main/java/com/example/progettoprog3/test.json";
            /*Object obj = new JSONParser().parse(new FileReader("/Users/marvel/Programming/Uni/ProgettoProg3/src/main/java/com/example/progettoprog3/test.json"));
            JSONObject jo = (JSONObject) obj;

            JSONArray ja = (JSONArray) jo.get("emails");
            System.out.println(ja.get(0));*/

        JSONParser jsonParser = new JSONParser();
        return (JSONArray) jsonParser.parse(new FileReader(src));
    }
}