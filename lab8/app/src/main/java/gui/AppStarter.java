package gui;

import javafx.application.Application;

public class AppStarter {
    public static void main(String[] args) {
        // Application.launch() is the official way to start a JavaFX application.
        // It sets up the JavaFX toolkit and calls the start() method
        // of the class you provide (LoginScreen in this case).
        Application.launch(DragonTableView.class, args);
    }
}