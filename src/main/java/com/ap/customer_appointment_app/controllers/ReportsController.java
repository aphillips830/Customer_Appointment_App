package com.ap.customer_appointment_app.controllers;

import com.ap.customer_appointment_app.models.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.text.DateFormatSymbols;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;

/** Controller for reports.fxml.
 * @author Allen Phillips
 * @version 1.0 */
public class ReportsController {

   @FXML
   private ComboBox<String> monthComboBox;
   @FXML
   private ComboBox<String> typeComboBox;
   @FXML
   private TextField totalFilteredAppointments;
   @FXML
   private ComboBox<String> selectedContactComboBox;
   @FXML
   private TableView<Appointment> contactTableView;
   @FXML
   private TableColumn<Appointment, Integer> contactAppointmentIDColumn;
   @FXML
   private TableColumn<Appointment, String> contactTitleColumn;
   @FXML
   private TableColumn<Appointment, String> contactTypeColumn;
   @FXML
   private TableColumn<Appointment, String> contactDescriptionColumn;
   @FXML
   private TableColumn<Appointment, ZonedDateTime> contactStartColumn;
   @FXML
   private TableColumn<Appointment, ZonedDateTime> contactEndColumn;
   @FXML
   private TableColumn<Appointment, Integer> contactCustomerIDColumn;
   @FXML
   private TableView<Appointment> expiredAppointmentsTableView;
   @FXML
   private TableColumn<Appointment, Integer> expiredAppointmentIDColumn;
   @FXML
   private TableColumn<Appointment, ZonedDateTime> expiredStartColumn;
   @FXML
   private TableColumn<Appointment, ZonedDateTime> expiredEndColumn;
   private ObservableList<Appointment> allAppointments;
   private final DateTimeFormatter localDF = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM);

   /** Used by DashboardController to send an observable list of all appointments.
    * @param allAppointments ObservableList of appointment objects. */
   public void sendAppointments(ObservableList<Appointment> allAppointments) {
      this.allAppointments = allAppointments;
      // Populate expired appointments table
      ObservableList<Appointment> expiredAppointments = FXCollections.observableArrayList();
      for (Appointment a : allAppointments) {
         if (a.getEnd().isBefore(ZonedDateTime.now())) {
            expiredAppointments.add(a);
         }
      }
      expiredAppointmentsTableView.setItems(expiredAppointments);
      expiredAppointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
      expiredStartColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
      expiredStartColumn.setCellFactory(this::formatColumn);
      expiredEndColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
      expiredEndColumn.setCellFactory(this::formatColumn);
      // Fill combo boxes
      ObservableList<String> allMonths = FXCollections.observableArrayList(new DateFormatSymbols().getMonths());
      monthComboBox.setItems(allMonths);
      ObservableList<String> allTypes = FXCollections.observableArrayList(getAllTypes());
      typeComboBox.setItems(allTypes);
      ObservableList<String> allContacts = FXCollections.observableArrayList(getAllContacts());
      selectedContactComboBox.setItems(allContacts);
   }

   private ArrayList<String> getAllTypes() {
      ArrayList<String> allTypes = new ArrayList<>();
      for (Appointment a : allAppointments) {
         if (!allTypes.contains(a.getType())) {
            allTypes.add(a.getType());
         }
      }
      return allTypes;
   }

   private ArrayList<String> getAllContacts() {
      ArrayList<String> allContacts = new ArrayList<>();
      for (Appointment a : allAppointments) {
         if (!allContacts.contains(a.getContact())) {
            allContacts.add(a.getContact());
         }
      }
      return allContacts;
   }

   @FXML
   private void calculateButtonAction() {
      String selectedMonth = monthComboBox.getValue().toLowerCase();
      String selectedType = typeComboBox.getValue();
      int total = 0;
      for (Appointment a : allAppointments) {
         if (a.getStart().getMonth().toString().toLowerCase().equals(selectedMonth)) {
            if (a.getType().equals(selectedType)) {
               total++;
            }
         }
      }
      totalFilteredAppointments.setText(String.valueOf(total));
   }

   @FXML
   private void selectedContactComboBoxAction() {
      String selectedContact = selectedContactComboBox.getSelectionModel().getSelectedItem();
      ObservableList<Appointment> contactAppointments = FXCollections.observableArrayList();
      for (Appointment a : allAppointments) {
         if (a.getContact().equals(selectedContact)) {
            contactAppointments.add(a);
         }
      }
      contactTableView.setItems(contactAppointments);
      contactAppointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
      contactTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
      contactTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
      contactDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
      contactStartColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
      contactStartColumn.setCellFactory(this::formatColumn);
      contactEndColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
      contactEndColumn.setCellFactory(this::formatColumn);
      contactCustomerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
   }

   private TableCell<Appointment, ZonedDateTime> formatColumn(TableColumn<Appointment, ZonedDateTime> col) {
      return new TableCell<>() {
         @Override
         protected void updateItem(ZonedDateTime dateTime, boolean empty) {
            super.updateItem(dateTime, empty);
            if (empty) {
               setText(null);
            } else {
               setText(String.format(dateTime.format(localDF)));
            }
         }
      };
   }
}
