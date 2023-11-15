package model;
import javafx.beans.property.SimpleStringProperty;
public class DataModel {

    private SimpleStringProperty receiver_email = null;
    private SimpleStringProperty email_text = null;
    private SimpleStringProperty email_object = null;

    public DataModel(){

        this.receiver_email = new SimpleStringProperty();
        this.email_text = new SimpleStringProperty();
        this.email_object = new SimpleStringProperty();
    }

    public SimpleStringProperty getReceiver_email() {
        return receiver_email;
    }

    public SimpleStringProperty getEmail_text() {return email_text;}

    public SimpleStringProperty getEmail_object(){return this.email_object;}
    public class Utente{

        private String nome;
        private String cognome;
        private String email;

        public Utente (String nome, String cognome, String email){

            this.nome = nome;
            this.cognome = cognome;
            this.email = email;
        }
    }
}
