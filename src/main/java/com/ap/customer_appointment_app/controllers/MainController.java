package com.ap.customer_appointment_app.controllers;

import com.ap.customer_appointment_app.Main;
import com.ap.customer_appointment_app.dao.UserDAO;
import com.ap.customer_appointment_app.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/** Controller for login.fxml.
 * @author Allen Phillips
 * @version 1.0 */
public class MainController implements Initializable {
   @FXML
   private TextField usernameTextField;
   @FXML
   private PasswordField passwordField;
   @FXML
   private Label notificationLabel;
   @FXML
   private Label locationLabel;
   @FXML
   private Button loginButton;
   private ResourceBundle resourceBundle;

   /** Displays user's location and replaces all text with user's locale language on load. */
   @Override
   public void initialize(URL url, ResourceBundle resourceBundle) {
      this.resourceBundle = resourceBundle;
      // Display user location
      locationLabel.setText(ZoneId.systemDefault().getId());
      // Display in language passed by resourceBundle
      usernameTextField.setPromptText(resourceBundle.getString("usernamePrompt"));
      passwordField.setPromptText(resourceBundle.getString("passwordPrompt"));
      loginButton.setText(resourceBundle.getString("loginButton"));
   }

   @FXML
   private void onLoginButtonPressed(ActionEvent actionEvent) {
      String usernameEntered = usernameTextField.getText();
      User user;
      try {
         user = UserDAO.getUser(usernameEntered);
         if (user != null && user.getPassword().equals(passwordField.getText())) {
            logLoginAttempt(usernameEntered, "Successful");
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("dashboard.fxml"));
            fxmlLoader.load();
            DashboardController dashboardController = fxmlLoader.getController();
            dashboardController.sendUser(user);
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Parent scene = fxmlLoader.getRoot();
            stage.setScene(new Scene(scene));
            stage.setTitle("iConsult Dashboard");
            stage.centerOnScreen();
            stage.show();
         } else {
            notificationLabel.setText(resourceBundle.getString("loginError"));
            logLoginAttempt(usernameEntered, "Failed");
         }
      } catch (SQLException e) {
         notificationLabel.setText(resourceBundle.getString("loginError"));
         logLoginAttempt(usernameEntered, "Failed");
      } catch (IOException e) {
         System.out.println("Failed to load dashboard. " + e.getMessage());
      }
   }

   private void logLoginAttempt(String username, String outcome) {
      try {
         DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss");
         String timestamp = LocalDateTime.now(ZoneOffset.UTC).format(df);
         FileWriter fw = new FileWriter("login_activity.txt", true);
         PrintWriter pw = new PrintWriter(fw);
         pw.print(username + " | ");
         pw.print(timestamp + " | ");
         pw.println(outcome);
         pw.close();
      } catch (IOException e) {
         System.out.println("Failed to log login attempt!" + e.getMessage());
      }
   }
}