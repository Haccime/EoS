package eos;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.text.FontWeight;


/**
 * JavaFX App
 */
public class App extends Application {

     private Stage mainStage;

    @Override
    public void start(Stage stage) {
        mainStage = stage;

        loginScene();
    }
    
    private void loginScene(){
        VBox loginBox = new VBox(20);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPadding(new Insets(40));
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        
        Label title = new Label("Sunnah Essentials Shop");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #90D5FF;");

        TextField username = new TextField();
        username.setPromptText("Username");
        
        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        
        Button loginButton = new Button("Sign In");
        loginButton.setStyle("-fx-background-color: #90D5FF; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30; -fx-background-radius: 2; -fx-cursor:hand;");

        Button regButton = new Button("Register");
        regButton.setStyle("-fx-background-color: #90D5FF; -fx-text-fill:white; -fx-font-weight: bold; -fx-padding: 10 30; -fx-background-radius:2;  -fx-cursor:hand;");
        
        loginButton.setOnAction(e -> {
            String user = username.getText().trim();
            String pass = password.getText();

            if (user.isEmpty() || pass.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Error");
                alert.setHeaderText(null);
                alert.setContentText("Please enter both username and password.");
                alert.showAndWait();
                return;
            }

            String role = FileRead.checkLogin(user, pass);

            if (role == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setHeaderText(null);
                alert.setContentText("Invalid username or password.");
                alert.showAndWait();

            } 
            else if (role.equalsIgnoreCase("ADMIN")) {
                //adminScene();      // method that loads Admin UI

            } 
            else if (role.equalsIgnoreCase("CUSTOMER")) {
                //customerScene();   // method that loads Customer UI
            }
        });
        
        regButton.setOnAction(e -> {
            String user = username.getText().trim();
            String pass = password.getText();

            if (user.isEmpty() || pass.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Registration Error");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a username and password.");
                alert.showAndWait();

                return;
            }

            FileRead.registerUser(user, pass);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registration");
            alert.setHeaderText(null);
            alert.setContentText("Registration successful! Please Login.");
            alert.showAndWait();

            username.clear();
            password.clear();
        });
        
        
        buttonBox.getChildren().addAll(loginButton, regButton);
        loginBox.getChildren().addAll(title, username, password, buttonBox);
        mainStage.setScene(new Scene(loginBox, 400, 400));
        mainStage.setTitle("Login");
        mainStage.show();
    }
    
    
    public static void main(String[] args) {
        launch();
    }

}