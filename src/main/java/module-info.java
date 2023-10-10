module com.ap.customer_appointment_app {
   requires javafx.controls;
   requires javafx.fxml;
   requires java.sql;
   requires mysql.connector.j;


   opens com.ap.customer_appointment_app to javafx.fxml;
   exports com.ap.customer_appointment_app;
   exports com.ap.customer_appointment_app.controllers;
   exports com.ap.customer_appointment_app.models;
   opens com.ap.customer_appointment_app.controllers to javafx.fxml;
}