module com.example.progettoprog3 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires org.json;
    requires json.simple;

    opens com.example.progettoprog3 to javafx.fxml;
    exports com.example.progettoprog3;
}