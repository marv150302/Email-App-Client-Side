package com.example.progettoprog3;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class User {

    /*name, surname and email used to identify the user*/
    private String nome;
    private String cognome;
    private String email;

    public User(String nome, String cognome, String email){

        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
    }



}