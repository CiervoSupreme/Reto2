module org.example.reto2 {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;
    requires java.naming;
    
    requires objectdb;
    requires java.persistence;

    opens org.example.reto2.Model to objectdb;
    exports org.example.reto2;
    opens org.example.reto2.controller to javafx.fxml;
}