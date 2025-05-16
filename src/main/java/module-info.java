module com.example.projet_restaurant {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.swing;
    requires javafx.media;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;

    opens com.example.projet_restaurant to javafx.fxml;
    opens com.example.projet_restaurant.controllers to javafx.fxml;
    opens com.example.projet_restaurant.models to javafx.base;
    
    exports com.example.projet_restaurant;
    exports com.example.projet_restaurant.controllers;
    exports com.example.projet_restaurant.models;
} 