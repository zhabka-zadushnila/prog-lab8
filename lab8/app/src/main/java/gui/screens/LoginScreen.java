package gui.screens;

import gui.managers.AuthManager;
import gui.managers.LocaleManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import structs.User;

public class LoginScreen {
    User returnableUser = null;
    private final LocaleManager localeManager = LocaleManager.getInstance();

    public User start() {
        Stage primaryStage = new Stage();
        primaryStage.setTitle(localeManager.getString("login.title"));


        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));


        Text scenetitle = new Text(localeManager.getString("login.welcome"));
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);


        Label userName = new Label(localeManager.getString("login.username"));
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label pw = new Label(localeManager.getString("login.password"));
        grid.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);

        Button loginBtn = new Button(localeManager.getString("login.button.login"));
        Button registerBtn = new Button(localeManager.getString("login.button.register"));
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().addAll(registerBtn, loginBtn);
        grid.add(hbBtn, 1, 4);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 0, 6, 2, 1);


        loginBtn.setOnAction(e -> {
            String username = userTextField.getText();
            String password = pwBox.getText();

            boolean success = (new AuthManager()).login(username, password);
            if (success) {
                this.returnableUser = new User(username, password);
                primaryStage.close();
            } else {
                actiontarget.setFill(Color.FIREBRICK);
                actiontarget.setText(localeManager.getString("login.result.loginFailed"));
            }
        });

        registerBtn.setOnAction(e -> {
            String username = userTextField.getText();
            String password = pwBox.getText();


            boolean success = (new AuthManager()).register(username, password);
            if (success) {
                this.returnableUser = new User(username, password);
                primaryStage.close();
            } else {
                actiontarget.setFill(Color.FIREBRICK);
                actiontarget.setText(localeManager.getString("login.result.registrationFailed"));
            }
        });

        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.showAndWait();

        return returnableUser;
    }
}