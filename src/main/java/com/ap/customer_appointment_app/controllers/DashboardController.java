package com.ap.customer_appointment_app.controllers;

import com.ap.customer_appointment_app.Main;
import com.ap.customer_appointment_app.dao.AppointmentDAO;
import com.ap.customer_appointment_app.dao.CustomerDAO;
import com.ap.customer_appointment_app.models.Appointment;
import com.ap.customer_appointment_app.models.Customer;
import com.ap.customer_appointment_app.models.User;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Optional;
import java.util.ResourceBundle;

/** Controller for dashboard.fxml.
 * @author Allen Phillips
 * @version 1.0 */
public class DashboardController implements Initializable {

   private final String DATABASE_ERROR = "Database access error.";
   private final String CONTROLLER_ERROR = "Controller access error.";
   @FXML
   private TableView<Customer> customerTableView;
   @FXML
   private TableView<Appointment> appointmentTableView;
   @FXML
   private TableColumn<Customer, Integer> customerIDColumn;
   @FXML
   private TableColumn<Customer, String> customerNameColumn;
   @FXML
   private TableColumn<Customer, String> customerAddressColumn;
   @FXML
   private TableColumn<Customer, String> customerPostCodeColumn;
   @FXML
   private TableColumn<Customer, String>  customerPhoneColumn;
   @FXML
   private TableColumn<Customer, String> customerDivisionColumn;
   @FXML
   private TableColumn<Customer, String> customerCountryColumn;
   @FXML
   private TableColumn<Appointment, Integer> appointmentIDColumn;
   @FXML
   private TableColumn<Appointment, String> appointmentTitleColumn;
   @FXML
   private TableColumn<Appointment, String> appointmentDescriptionColumn;
   @FXML
   private TableColumn<Appointment, String> appointmentLocationColumn;
   @FXML
   private TableColumn<Appointment, String> appointmentContactColumn;
   @FXML
   private TableColumn<Appointment, String> appointmentTypeColumn;
   @FXML
   private TableColumn<Appointment, ZonedDateTime> appointmentStartColumn;
   @FXML
   private TableColumn<Appointment, ZonedDateTime> appointmentEndColumn;
   @FXML
   private TableColumn<Appointment, Integer> appointmentCustomerIDColumn;
   @FXML
   private TableColumn<Appointment, Integer> appointmentUserIDColumn;
   @FXML
   private TextField customerSearchField;
   @FXML
   private Label customerNotificationLabel;
   @FXML
   private Label appointmentNotificationLabel;
   @FXML
   private ToggleGroup appointmentReports;
   private ObservableList<Customer> allCustomers;
   private ObservableList<Appointment> allAppointments;
   private User currentUser;

   /** Populates the table views on load.
    * Contains two lambdas to quickly and easily format the ZonedDateTime table cells "Start" and "End" to a
    * user's recognizable locale format. */
   @Override
   public void initialize(URL url, ResourceBundle resourceBundle) {
      try {
         allCustomers = CustomerDAO.getAllCustomers();
         customerTableView.setItems(allCustomers);
         customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
         customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
         customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
         customerPostCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postCode"));
         customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
         customerDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("division"));
         customerCountryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));

         allAppointments = AppointmentDAO.getAllAppointments();
         appointmentTableView.setItems(allAppointments);
         appointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
         appointmentTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
         appointmentDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
         appointmentLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
         appointmentContactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
         appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

         DateTimeFormatter localDF = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM);
         appointmentStartColumn.setCellValueFactory(new PropertyValueFactory<>("start"));

         appointmentStartColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(ZonedDateTime dateTime, boolean empty) {
               super.updateItem(dateTime, empty);
               if (empty) {
                  setText(null);
               } else {
                  setText(String.format(dateTime.format(localDF)));
               }
            }
         });
         appointmentEndColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
         appointmentEndColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(ZonedDateTime dateTime, boolean empty) {
               super.updateItem(dateTime, empty);
               if (empty) {
                  setText(null);
               } else {
                  setText(String.format(dateTime.format(localDF)));
               }
            }
         });

         appointmentCustomerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
         appointmentUserIDColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));
      } catch (SQLException e) {
         displayCustomerNotification(DATABASE_ERROR, 10);
      }
   }

   /** Used by MainController to send a user object.
    * Notifies user after logging in of any appointment starting within 15 minutes.
    * @param user User object representing the current user of the application. */
   public void sendUser(User user) {
      currentUser = user;
      ZonedDateTime fifteenMinutesLater = ZonedDateTime.now().plusMinutes(15);
      DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT);
      PauseTransition pauseTransition = new PauseTransition(Duration.seconds(5));
      pauseTransition.setOnFinished(e -> {
         Alert alert = new Alert(Alert.AlertType.INFORMATION);
         alert.setTitle("Appointment Notification");
         for (Appointment a : allAppointments) {
            if ((a.getUserID() == currentUser.getUserID()) && (a.getStart().isBefore(fifteenMinutesLater))
                    && (a.getStart().isAfter(ZonedDateTime.now()))) {
               alert.setHeaderText("Appointment ID: " + a.getAppointmentID() + " on "
                       + a.getStart().format(dateTimeFormatter) + " starting soon.");
               break;
            } else {
               alert.setHeaderText("No upcoming appointments.");
            }
         }
         alert.show();
      });
      pauseTransition.play();
   }

   /** Deletes a customer from the database and removes them from the table view.
    * Contains one lambda that checks for and removes all customer's appointments before deleting the customer. */
   @FXML
   public void deleteCustomer() {
      if (customerTableView.getSelectionModel().isEmpty()) {
         displayCustomerNotification("No customer selected in table.", 5);
      } else {
         try {
            Customer customerToRemove = customerTableView.getSelectionModel().getSelectedItem();
            Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION, "Deleting: " + customerToRemove.getName()
                    + " and all associated appointments. Please confirm?");
            Optional<ButtonType> result = deleteAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
               allAppointments.removeIf(a -> a.getCustomerID() == customerToRemove.getCustomerID());
               CustomerDAO.deleteCustomer(customerToRemove);
               allCustomers = CustomerDAO.getAllCustomers();
               customerTableView.setItems(allCustomers);
               displayCustomerNotification("Successfully removed Customer "
                       + customerToRemove.getName(), 5);
            }
         } catch (SQLException e) {
            displayCustomerNotification(DATABASE_ERROR + " Failed to delete selected customer.", 10);
         }
      }
   }

   private void displayCustomerNotification(String notificationString, int seconds) {
      customerNotificationLabel.setText(notificationString);
      PauseTransition pauseTransition = new PauseTransition(Duration.seconds(seconds));
      pauseTransition.setOnFinished(e -> customerNotificationLabel.setText(null));
      pauseTransition.play();
   }

   private void displayAppointmentNotification(String notificationString, int seconds) {
      appointmentNotificationLabel.setText(notificationString);
      PauseTransition pauseTransition = new PauseTransition(Duration.seconds(seconds));
      pauseTransition.setOnFinished(e -> appointmentNotificationLabel.setText(null));
      pauseTransition.play();
   }

   @FXML
   private void addCustomer() {
      try {
         FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("customer.fxml"));
         Scene customerScene = new Scene(fxmlLoader.load());
         Stage customerStage = new Stage();
         CustomerController controller = fxmlLoader.getController();
         controller.sendUser(currentUser);
         customerStage.initModality(Modality.APPLICATION_MODAL);
         customerStage.setScene(customerScene);
         customerStage.setTitle("iConsult Add Customer");
         customerStage.centerOnScreen();
         customerStage.showAndWait();
         allCustomers = CustomerDAO.getAllCustomers();
         customerTableView.setItems(allCustomers);
      } catch (IOException e) {
         displayCustomerNotification(CONTROLLER_ERROR, 10);
      } catch (SQLException e) {
         displayCustomerNotification(DATABASE_ERROR, 10);
      }
   }

   @FXML
   private void addAppointment() {
      try {
         FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("appointment.fxml"));
         Scene appointmentScene = new Scene(fxmlLoader.load());
         Stage appointmentStage = new Stage();
         AppointmentController controller = fxmlLoader.getController();
         controller.sendUser(currentUser);
         appointmentStage.initModality(Modality.APPLICATION_MODAL);
         appointmentStage.setScene(appointmentScene);
         appointmentStage.setTitle("iConsult Add Appointment");
         appointmentStage.centerOnScreen();
         appointmentStage.showAndWait();
         allAppointments = AppointmentDAO.getAllAppointments();
         appointmentTableView.setItems(allAppointments);
      } catch (IOException e) {
         displayAppointmentNotification(CONTROLLER_ERROR, 10);
      } catch (SQLException e) {
         displayAppointmentNotification(DATABASE_ERROR, 10);
      }
   }

   @FXML
   private void editCustomer() {
      try {
         FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("customer.fxml"));
         Scene customerScene = new Scene(fxmlLoader.load());
         Stage customerStage = new Stage();
         CustomerController controller = fxmlLoader.getController();
         controller.sendCustomer(customerTableView.getSelectionModel().getSelectedItem());
         controller.sendUser(currentUser);
         customerStage.initModality(Modality.APPLICATION_MODAL);
         customerStage.setScene(customerScene);
         customerStage.setTitle("iConsult Edit Customer");
         customerStage.centerOnScreen();
         customerStage.showAndWait();
         allCustomers = CustomerDAO.getAllCustomers();
         customerTableView.setItems(allCustomers);
      } catch (IOException e) {
         displayCustomerNotification(CONTROLLER_ERROR, 10);
      } catch (SQLException e) {
         displayCustomerNotification(DATABASE_ERROR, 10);
      }
   }

   @FXML
   private void editAppointment() {
      try {
         FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("appointment.fxml"));
         Scene appointmentScene = new Scene(fxmlLoader.load());
         Stage appointmentStage = new Stage();
         AppointmentController controller = fxmlLoader.getController();
         controller.sendUser(currentUser);
         controller.sendAppointment(appointmentTableView.getSelectionModel().getSelectedItem());
         appointmentStage.initModality(Modality.APPLICATION_MODAL);
         appointmentStage.setScene(appointmentScene);
         appointmentStage.setTitle("iConsult Edit Appointment");
         appointmentStage.centerOnScreen();
         appointmentStage.showAndWait();
         allAppointments = AppointmentDAO.getAllAppointments();
         appointmentTableView.setItems(allAppointments);
      } catch (IOException e) {
         displayAppointmentNotification(CONTROLLER_ERROR, 10);
      } catch (SQLException e) {
         displayAppointmentNotification(DATABASE_ERROR, 10);
      }
   }

   @FXML
   private void deleteAppointment() {
      if (appointmentTableView.getSelectionModel().isEmpty()) {
         displayAppointmentNotification("No appointment selected in table.", 5);
      } else {
         Appointment appointmentToRemove = appointmentTableView.getSelectionModel().getSelectedItem();
         Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION, "Deleting: Appointment with ID = "
                 + appointmentToRemove.getAppointmentID() + " and Type = " + appointmentToRemove.getType()
                 + ". Please confirm?");
         Optional<ButtonType> result = deleteAlert.showAndWait();
         if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
               AppointmentDAO.deleteAppointment(appointmentToRemove);
               allAppointments = AppointmentDAO.getAllAppointments();
               appointmentTableView.setItems(allAppointments);
               displayAppointmentNotification("Successfully removed Appointment with ID = "
                       + appointmentToRemove.getAppointmentID() + " and Type = " + appointmentToRemove.getType(), 5);
            } catch (SQLException e) {
               displayAppointmentNotification(DATABASE_ERROR + " Failed to delete selected appointment.", 10);
            }
         }
      }
   }

   @FXML
   private void searchCustomerButton() {
      ObservableList<Customer> searchList = FXCollections.observableArrayList();
      try {
         int searchID = Integer.parseInt((customerSearchField.getText()));
         for (Customer c : allCustomers) {
            if (c.getCustomerID() == searchID) {
               searchList.add(c);
            }
         }
      } catch (NumberFormatException e) {
         for (Customer c : allCustomers) {
            if (c.getName().toLowerCase().contains(customerSearchField.getText().toLowerCase())) {
               searchList.add(c);
            }
         }
      }
      customerTableView.setItems(searchList);
   }

   @FXML
   private void allAppointmentsAction() {
      appointmentTableView.setItems(allAppointments);
   }

   @FXML
   private void currentWeekAppointmentsAction() {
      ObservableList<Appointment> currentWeekAppointments = FXCollections.observableArrayList();
      ZonedDateTime currentDateTime = ZonedDateTime.now();
      ZonedDateTime currentPlusWeekDateTime = currentDateTime.plusWeeks(1);
      for (Appointment a : allAppointments) {
         if (a.getStart().isEqual(currentDateTime))
            currentWeekAppointments.add(a);
         else if (a.getStart().isAfter(currentDateTime) && a.getStart().isBefore(currentPlusWeekDateTime))
            currentWeekAppointments.add(a);
      }
      appointmentTableView.setItems(currentWeekAppointments);
   }

   @FXML
   private void currentMonthAppointmentsAction() {
      ObservableList<Appointment> currentMonthAppointments = FXCollections.observableArrayList();
      ZonedDateTime currentDateTime = ZonedDateTime.now();
      ZonedDateTime currentPlusMonthDateTime = currentDateTime.plusMonths(1);
      for (Appointment a : allAppointments) {
         if (a.getStart().isEqual(currentDateTime))
            currentMonthAppointments.add(a);
         else if (a.getStart().isAfter(currentDateTime) && a.getStart().isBefore(currentPlusMonthDateTime))
            currentMonthAppointments.add(a);
      }
      appointmentTableView.setItems(currentMonthAppointments);
   }

   @FXML
   private void userAppointmentsAction() {
      ObservableList<Appointment> userAppointments = FXCollections.observableArrayList();
      for (Appointment a : allAppointments) {
         if (a.getUserID() == currentUser.getUserID())
            userAppointments.add(a);
      }
      appointmentTableView.setItems(userAppointments);
   }

   @FXML
   private void reportsButtonAction() {
      try {
         FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("reports.fxml"));
         Scene reportsScene = new Scene(fxmlLoader.load());
         Stage reportsStage = new Stage();
         ReportsController controller = fxmlLoader.getController();
         controller.sendAppointments(allAppointments);
         reportsStage.initModality(Modality.APPLICATION_MODAL);
         reportsStage.setScene(reportsScene);
         reportsStage.setTitle("iConsult Reports");
         reportsStage.centerOnScreen();
         reportsStage.showAndWait();
      } catch (IOException e) {
         displayAppointmentNotification(CONTROLLER_ERROR, 10);
      }
   }
}