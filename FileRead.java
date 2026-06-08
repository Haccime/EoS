/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eos;
import java.io.*;
import java.util.*;
import java.util.ArrayList;
import javafx.scene.control.Alert;

public class FileRead {
    private static final String FILE = "users.txt";
    private ArrayList<User> users = new ArrayList<>();
    
    // READ FILE
    public static List<String[]> readUsers() {

        List<String[]> users = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                users.add(line.split(","));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    // CHECK LOGIN
    public static String checkLogin(String username, String password) {
        List<String[]> users = readUsers();
        for (String[] u : users) {
            if (u[0].equals(username) && u[1].equals(password)) {
                return u[2]; // role
            }
        }
        return null;
    }
    
    // REGISTER USER
    public static void registerUser(String username, String password) {
        List<String[]> users = readUsers();
        for (String[] user : users) {
        if (user[0].equalsIgnoreCase(username)) {
            System.out.println("Error: Username already exists!");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Error");
            alert.setHeaderText(null);
            alert.setContentText("Username already exists!");
            alert.showAndWait();
            return;
        }
    }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE, true))) {
                bw.write(username + "," + password + ",CUSTOMER");
                bw.newLine();  
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registration");
                alert.setHeaderText(null);
                alert.setContentText("Registration successful! Please Login.");
                alert.showAndWait();

            }
            catch (IOException e){
            e.printStackTrace();
            }
    }
    
    
}
