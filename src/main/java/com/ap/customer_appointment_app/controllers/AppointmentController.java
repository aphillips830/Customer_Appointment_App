package com.ap.customer_appointment_app.controllers;

import com.ap.customer_appointment_app.dao.AppointmentDAO;
import com.ap.customer_appointment_app.dao.ContactDAO;
import com.ap.customer_appointment_app.dao.CustomerDAO;
import com.ap.customer_appointment_app.dao.UserDAO;
import com.ap.customer_appointment_app.models.Appointment;
import com.ap.customer_appointment_app.models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

/** Controller for appointment.fxml.
 * @author Allen Phillips
 * @version 1.0 */
public class AppointmentController implements Initializable {
   @FXML
   private Label appointmentLabel;
   @FXML
   private TextField titleText;
   @FXML
   private TextField descriptionText;
   @FXML
   private TextField locationText;
   @FXML
   private TextField typeText;
   @FXML
   private ComboBox<String> contactComboBox;
   @FXML
   private ComboBox<Integer> customerIDComboBox;
   @FXML
   private ComboBox<Integer> userIDComboBox;
   @FXML
   private DatePicker startDatePicker;
   @FXML
   private ComboBox<String> startTimeComboBox;
   @FXML
   private DatePicker endDatePicker;
   @FXML
   private ComboBox<String> endTimeComboBox;
   @FXML
   private Label notificationLabel;
   @FXML
   private Button cancelButton;
   @FXML
   private Button saveButton;
   private User currentUser;
   private Appointment currentAppointment;
   private boolean isNewAppointment = true;

   /** Populates the combo boxes on load. */
   @Override
   public void initialize(URL url, ResourceBundle resourceBundle) {
      populateTimeComboBoxes();

      try {
         ObservableList<String> allContactNames = ContactDAO.getAllContactNames();
         ObservableList<Integer> allCustomerIDs = CustomerDAO.getAllCustomerIDs();
         ObservableList<Integer> allUserIDs = UserDAO.getAllUserIDs();
         contactComboBox.setItems(allContactNames);
         customerIDComboBox.setItems(allCustomerIDs);
         userIDComboBox.setItems(allUserIDs);
      } catch (SQLException e)  {
         System.out.println(e.getMessage());
         notificationLabel.setText("AppointmentController failed to initialize: " + e.getMessage());
      }
   }

   /** Used by DashboardController to send a user object.
    * @param user User object representing the current user of the application. */
   public void sendUser(User user) {
      this.currentUser = user;
      userIDComboBox.setValue(user.getUserID());
   }

   /** Used by DashboardController to send an appointment object.
    * @param a Appointment object to be updated/edited. */
   public void sendAppointment(Appointment a) {
      appointmentLabel.setText("Edit Appointment");
      currentAppointment = a;
      titleText.setText(a.getTitle());
      descriptionText.setText(a.getDescription());
      locationText.setText(a.getLocation());
      typeText.setText(a.getType());
      contactComboBox.setValue(a.getContact());
      customerIDComboBox.setValue(a.getCustomerID());
      userIDComboBox.setValue(a.getUserID());
      startDatePicker.setValue(a.getStart().toLocalDate());
      startTimeComboBox.setValue(a.getStart().getHour() + ":" + (a.getStart().getMinute() < 10 ? "0" : "")
              + a.getStart().getMinute());
      endDatePicker.setValue(a.getEnd().toLocalDate());
      endTimeComboBox.setValue(a.getEnd().getHour() + ":" + (a.getEnd().getMinute() < 10 ? "0" : "")
              + a.getEnd().getMinute());
      isNewAppointment = false;
   }

   private void populateTimeComboBoxes() {
      ObservableList<Integer> hours = FXCollections.observableArrayList(
              8,9,10,11,12,13,14,15,16,17,18,19,20,21);
      LocalTime userTimeInEST = LocalTime.now(ZoneId.of("America/New_York"));
      LocalTime userTime = LocalTime.now();
      int estTimeOffset = (int) userTimeInEST.until(userTime, ChronoUnit.HOURS);
      // Adjust EST hours list to user's local hours
      for (int i = 0; i < hours.size(); i++) {
         hours.set(i, hours.get(i) + estTimeOffset);
         if (hours.get(i) > 24) {
            hours.set(i, hours.get(i) - 24);
         }
      }
      ObservableList<String> startTimes = FXCollections.observableArrayList();
      ObservableList<String> endTimes = FXCollections.observableArrayList();
      // Populate lists with times as strings
      for (int i = 0; i < hours.size(); i++) {
         if (i == 0) {
            startTimes.add(hours.get(i) + ":00");
         } else {
            startTimes.add(hours.get(i) + ":00");
            endTimes.add(hours.get(i) + ":00");
         }
         startTimes.add(hours.get(i) + ":15");
         startTimes.add(hours.get(i) + ":30");
         startTimes.add(hours.get(i) + ":45");
         endTimes.add(hours.get(i) + ":15");
         endTimes.add(hours.get(i) + ":30");
         endTimes.add(hours.get(i) + ":45");
         if (i == hours.size() - 1) {
            endTimes.add((hours.get(i) + 1) + ":00");
         }
      }
      startTimeComboBox.setItems(startTimes);
      endTimeComboBox.setItems(endTimes);
   }

   @FXML
   private void cancelButtonAction() {
      Stage stage = (Stage) cancelButton.getScene().getWindow();
      stage.close();
   }

   private boolean checkForEmptyFields() {
      return !titleText.getText().equals("") && !descriptionText.getText().equals("") && !locationText.getText().equals("")
              && !typeText.getText().equals("") && !contactComboBox.getSelectionModel().isEmpty()
              && !customerIDComboBox.getSelectionModel().isEmpty() && !userIDComboBox.getSelectionModel().isEmpty()
              && startDatePicker.getValue() != null && !startTimeComboBox.getSelectionModel().isEmpty()
              && endDatePicker.getValue() != null && !endTimeComboBox.getSelectionModel().isEmpty();
   }

   @FXML
   private void saveButtonAction() {
      if (checkForEmptyFields()) {
         String title = titleText.getText();
         String description = descriptionText.getText();
         String location = locationText.getText();
         String type = typeText.getText();
         String contactName = contactComboBox.getValue();
         int customerID = customerIDComboBox.getValue();
         int userID = userIDComboBox.getValue();
         LocalDate startDate = startDatePicker.getValue();
         String startStringTime = startTimeComboBox.getValue();
         LocalDate endDate = endDatePicker.getValue();
         String endStringTime = endTimeComboBox.getValue();
         DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
         LocalTime startTime = LocalTime.parse(startStringTime, timeFormatter);
         LocalTime endTime = LocalTime.parse(endStringTime, timeFormatter);
         ZonedDateTime localStart = ZonedDateTime.of(startDate, startTime, ZoneId.systemDefault());
         ZonedDateTime localEnd = ZonedDateTime.of(endDate,endTime, ZoneId.systemDefault());
         ZonedDateTime utcStart = localStart.withZoneSameInstant(ZoneId.of("UTC"));
         ZonedDateTime utcEnd = localEnd.withZoneSameInstant(ZoneId.of("UTC"));
         try {
            if (isNewAppointment) {
               if (dateCheck(utcStart, utcEnd, -1, customerID)) {
                  Appointment appointment = new Appointment(-1, title, description, location, contactName, type
                          , utcStart, utcEnd, customerID, userID);
                  AppointmentDAO.addAppointment(appointment, currentUser);
                  Stage stage = (Stage) saveButton.getScene().getWindow();
                  stage.close();
               } else {
                  notificationLabel.setText("Overlapping appointment times for customer!");
               }
            } else {
               if (dateCheck(utcStart, utcEnd, currentAppointment.getAppointmentID(), customerID)) {
                  Appointment updatedAppointment = new Appointment(currentAppointment.getAppointmentID(), title
                          , description, location, contactName, type, utcStart, utcEnd, customerID, userID);
                  AppointmentDAO.editAppointment(updatedAppointment, currentUser);
                  Stage stage = (Stage) saveButton.getScene().getWindow();
                  stage.close();
               } else {
                  notificationLabel.setText("Overlapping appointment times for customer!");
               }
            }
         } catch (SQLException e) {
            notificationLabel.setText("Failed saving appointment data.");
            System.out.println("saveButtonAction failed:" + e.getMessage());
         }
      } else {
         notificationLabel.setText("Empty field! Please correct and try again.");
      }
   }

   private boolean dateCheck(ZonedDateTime start, ZonedDateTime end, int appointmentID, int customerID) throws SQLException {
      ObservableList<Appointment> customerAppointments = AppointmentDAO.getAllCustomerUTCAppointments(appointmentID, customerID);
      if (start.isAfter(end)) {
         return false;
      } else {
         for (Appointment a : customerAppointments) {
            if (a.getStart().isBefore(start) && a.getEnd().isAfter(start))
               return false;
            if (a.getStart().isEqual(start))
               return false;
            if (a.getStart().isAfter(start) && a.getStart().isBefore(end))
               return false;
         }
      }
      return true;
   }
}
