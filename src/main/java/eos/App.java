/* Written By
 * FAIDZRUL ADZHAD BIN FAKHRUL RADZI 2517653
 * DEN MUHAMMAD HAKIM BIN JUMAATUDEN 2514781
 * AHMAD HASBOL KAFFI BIN KHALID 2514079
 * MUHAMMAD ARIF NAUFAL BIN NAZRI 2516439 
 * MUHAMMAD IKHWAN HAKIM BIN MAZRI 2513269
 */

package eos;

import eos.core.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.application.Platform;

/**
 * JavaFX App
 */
public class App extends Application {
    private User currentUser;
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
	title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #000000;");

	TextField username = new TextField();
	username.setPromptText("Username");

	PasswordField password = new PasswordField();
	password.setPromptText("Password");

	Button loginButton = new Button("Log In");
	loginButton.setStyle(
		"-fx-background-color: #2563eb; -fx-text-fill: white; " +
		"-fx-background-radius: 6; -fx-padding: 6 14; -fx-cursor: hand;"
		);

	Button regButton = new Button("Register");
	regButton.setStyle(
		"-fx-background-color: #2563eb; -fx-text-fill: white; " +
		"-fx-background-radius: 6; -fx-padding: 6 14; -fx-cursor: hand;"
		);

	loginButton.setOnAction(e -> {
	    String user = username.getText().trim();
	    String pass = password.getText().trim();

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
		currentUser = new Admin(user, pass);

		AdminPage adminPage = new AdminPage();

		mainStage.setScene(adminPage.createScene());
		mainStage.setTitle("Admin Dashboard");

	    } 
	    else if (role.equalsIgnoreCase("CUSTOMER")) {
		currentUser = new Customer(user, pass);

		CustomerPage customerPage = new CustomerPage();
		Cart cart = new Cart(CartFileHandler.loadCart(user));

		mainStage.setScene(customerPage.createScene(currentUser, cart));
		mainStage.setTitle("EOS Shop");
		mainStage.setOnCloseRequest(exit -> {
		    CartFileHandler.saveCart(cart.getItems(), currentUser.getUsername());
		    Platform.exit();
		});
	    }
	});

	regButton.setOnAction(e -> {
	    String user = username.getText().trim();
	    String pass = password.getText().trim();            

	    if (user.isEmpty() || pass.isEmpty()) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Registration Error");
		alert.setHeaderText(null);
		alert.setContentText("Please enter a username and password.");
		alert.showAndWait();

		return;
	    }

	    FileRead.registerUser(user, pass);

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
