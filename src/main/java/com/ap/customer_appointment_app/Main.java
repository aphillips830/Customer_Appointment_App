package com.ap.customer_appointment_app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/** Customer appointment management application.
 * @author Allen Phillips
 * @version 1.0
 * */
public class Main extends Application {

   /** JavaFX application start window. */
   @Override
   public void start(Stage stage) throws IOException {
      // Locale french = new Locale("fr");
      ResourceBundle resourceBundle = ResourceBundle.getBundle("Resources", Locale.getDefault());
      FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
      fxmlLoader.setResources(resourceBundle);
      Scene scene = new Scene(fxmlLoader.load());
      stage.setTitle("iConsult");
      stage.centerOnScreen();
      stage.setScene(scene);
      stage.show();
   }

   /** Starts the application.
    * @param args Array of optional arguments. */
   public static void main(String[] args) {
      launch();
   }
}