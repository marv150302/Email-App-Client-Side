package model;
import javafx.beans.property.SimpleStringProperty;
public class DataModel {

    private SimpleStringProperty receiver_email = null;

    public SimpleStringProperty getReceiver_email() {
        return receiver_email;
    }

    public DataModel(){

        this.receiver_email = new SimpleStringProperty();
    }
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
