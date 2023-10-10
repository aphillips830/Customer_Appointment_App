package com.ap.customer_appointment_app.controllers;

import com.ap.customer_appointment_app.dao.CountryDAO;
import com.ap.customer_appointment_app.dao.CustomerDAO;
import com.ap.customer_appointment_app.dao.DivisionDAO;
import com.ap.customer_appointment_app.models.Country;
import com.ap.customer_appointment_app.models.Customer;
import com.ap.customer_appointment_app.models.Division;
import com.ap.customer_appointment_app.models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/** Controller for customer.fxml.
 * @author Allen Phillips
 * @version 1.0 */
public class CustomerController implements Initializable {
   @FXML
   private Label customerLabel;
   @FXML
   private TextField nameText;
   @FXML
   private TextField addressText;
   @FXML
   private TextField postCodeText;
   @FXML
   private TextField phoneText;
   @FXML
   private ComboBox<String> countryComboBox;
   @FXML
   private ComboBox<String> divisionComboBox;
   @FXML
   private Label notificationLabel;
   @FXML
   private Button cancelButton;
   @FXML
   private Button saveButton;
   private ObservableList<Country> allCountries;
   private ObservableList<Division> allDivisions;
   private ObservableList<String> selectedDivisionNames;
   private int selectedCountryID;
   private User currentUser;
   private Customer currentCustomer;
   private boolean isNewCustomer = true;

   /** Populates the combo boxes on load. */
   @Override
   public void initialize(URL url, ResourceBundle resourceBundle) {
      ObservableList<String> countryNames = FXCollections.observableArrayList();
      selectedDivisionNames = FXCollections.observableArrayList();
      try {
         allCountries = CountryDAO.getAllCountries();
         allDivisions = DivisionDAO.getAllDivisions();
         for (Country c : allCountries) {
            countryNames.add(c.getCountryName());
         }
         countryComboBox.setItems(countryNames);
         countryComboBox.getSelectionModel().selectFirst();
         String selectedCountry = countryComboBox.getValue();
         for (Country c : allCountries) {
            if (c.getCountryName().equals(selectedCountry)) {
               selectedCountryID = c.getCountryID();
            }
         }
         for (Division d : allDivisions) {
            if (d.getCountryID() == selectedCountryID) {
               selectedDivisionNames.add(d.getDivisionName());
            }
         }
         divisionComboBox.setItems(selectedDivisionNames);
         divisionComboBox.getSelectionModel().selectFirst();
      } catch (SQLException e) {
         System.out.println("CustomerController failed to initialize:" + e.getMessage());
      }
   }

   /** Used by DashboardController to send a customer object.
    * @param customer Customer object to be updated/edited. */
   public void sendCustomer(Customer customer) {
      currentCustomer = customer;
      customerLabel.setText("Edit Customer");
      nameText.setText(customer.getName());
      addressText.setText(customer.getAddress());
      postCodeText.setText(customer.getPostCode());
      phoneText.setText(customer.getPhone());
      countryComboBox.setValue(customer.getCountry());
      divisionComboBox.setValue(customer.getDivision());
      isNewCustomer = false;
   }

   /** Used by DashboardController to send a user object.
    * @param user User object representing the current user of the application. */
   public void sendUser(User user) {
      currentUser = user;
   }

   @FXML
   private void countrySelection() {
      selectedDivisionNames.clear();
      for (Country c : allCountries) {
         if (c.getCountryName().equals(countryComboBox.getValue())) {
            selectedCountryID = c.getCountryID();
         }
      }
      for (Division d : allDivisions) {
         if (d.getCountryID() == selectedCountryID) {
            selectedDivisionNames.add(d.getDivisionName());
         }
      }
      divisionComboBox.getSelectionModel().selectFirst();
   }

   @FXML
   private void cancelButtonAction() {
      Stage stage = (Stage) cancelButton.getScene().getWindow();
      stage.close();
   }

   private boolean checkForEmptyFields() {
      return !nameText.getText().equals("") && !addressText.getText().equals("") && !postCodeText.getText().equals("")
              && !phoneText.getText().equals("");
   }

   @FXML
   private void saveButtonAction() {
      if (checkForEmptyFields()) {
         String customerName = nameText.getText();
         String address = addressText.getText();
         String postCode = postCodeText.getText();
         String phone = phoneText.getText();
         String countryName = countryComboBox.getValue();
         String divisionName = divisionComboBox.getValue();
         int divisionID = -1;
         for (Division d : allDivisions) {
            if (d.getDivisionName().equals(divisionComboBox.getValue())) {
               divisionID = d.getDivisionID();
            }
         }
         try {
            if (isNewCustomer) {
               Customer newCustomer = new Customer(-1, customerName, address, postCode, phone, divisionID,
                       divisionName, countryName);
               CustomerDAO.addCustomer(newCustomer, currentUser);
            } else {
               Customer updatedCustomer = new Customer(currentCustomer.getCustomerID(), customerName, address, postCode,
                       phone, divisionID, divisionName, countryName);
               CustomerDAO.editCustomer(updatedCustomer, currentUser);
            }
         } catch (SQLException e) {
            notificationLabel.setText("Failed saving customer data.");
            System.out.println("saveButtonAction failed:" + e.getMessage());
         }
         Stage stage = (Stage) saveButton.getScene().getWindow();
         stage.close();
      } else {
         notificationLabel.setText("Empty field! Please correct and try again.");
      }
   }
}
