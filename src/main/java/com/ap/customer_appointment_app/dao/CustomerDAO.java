package com.ap.customer_appointment_app.dao;

import com.ap.customer_appointment_app.models.Customer;
import com.ap.customer_appointment_app.models.User;
import com.ap.customer_appointment_app.utility.JDBC;
import com.ap.customer_appointment_app.utility.Query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/** DAO class to be used on the customers table in the database.
 * @author Allen Phillips
 * @version 1.0 */
public class CustomerDAO {
   private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss");
   private static final String customerTableName = "customers";
   private static final String customerIDColumn = "Customer_ID";
   private static final String customerNameColumn = "Customer_Name";
   private static final String customerAddressColumn = "Address";
   private static final String customerPostCodeColumn = "Postal_Code";
   private static final String customerPhoneColumn = "Phone";
   private static final String customerDivisionIDColumn = "Division_ID";
   private static final String customerLastUpdateColumn = "Last_Update";
   private static final String customerLastUpdateByColumn = "Last_Updated_By";
   private static final String customerCreateDateColumn = "Create_Date";
   private static final String customerCreatedByColumn = "Created_By";
   private static final String divisionTableName = "first_level_divisions";
   private static final String divisionNameColumn = "Division";
   private static final String divisionCountryIDColumn = "Country_ID";
   private static final String countryTableName = "countries";
   private static final String countryNameColumn = "Country";

   /** Gets all customer records from the database.
    * @return ObservableList of all customer objects.
    * @throws SQLException Database access error. */
   public static ObservableList<Customer> getAllCustomers() throws SQLException {
      ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
      JDBC.openConnection();
      String sqlStatement = "SELECT " + customerIDColumn + "," + customerNameColumn + "," + customerAddressColumn
              + "," + customerPostCodeColumn + "," + customerPhoneColumn + ",t1." + customerDivisionIDColumn + ","
              + divisionNameColumn + "," + countryNameColumn + " FROM " + customerTableName + " t1 JOIN "
              + divisionTableName + " t2 ON t1." + customerDivisionIDColumn + " = t2." + customerDivisionIDColumn
              + " JOIN " + countryTableName + " t3 ON t2." + divisionCountryIDColumn + " = t3."
              + divisionCountryIDColumn;
      Query.makeQuery(sqlStatement);
      ResultSet resultSet = Query.getResultSet();
      while (resultSet.next()) {
         int customerID = resultSet.getInt(customerIDColumn);
         String customerName = resultSet.getString(customerNameColumn);
         String address = resultSet.getString(customerAddressColumn);
         String postCode = resultSet.getString(customerPostCodeColumn);
         String phone = resultSet.getString(customerPhoneColumn);
         int divisionID = resultSet.getInt(customerDivisionIDColumn);
         String divisionName = resultSet.getString(divisionNameColumn);
         String countryName = resultSet.getString(countryNameColumn);
         Customer currentCustomer = new Customer(customerID, customerName, address, postCode, phone, divisionID,
                 divisionName, countryName);
         allCustomers.add(currentCustomer);
      }
      JDBC.closeConnection();
      return allCustomers;
   }

   /** Gets all customer IDs from the database.
    * @return ObservableList of integers for all customer IDs.
    * @throws SQLException Database access error. */
   public static ObservableList<Integer> getAllCustomerIDs() throws SQLException {
      ObservableList<Integer> allCustomerIDs = FXCollections.observableArrayList();
      JDBC.openConnection();
      String sqlStatement = "SELECT " + customerIDColumn + " FROM " + customerTableName;
      Query.makeQuery(sqlStatement);
      ResultSet resultSet = Query.getResultSet();
      while (resultSet.next()) {
         int customerID = resultSet.getInt(customerIDColumn);
         allCustomerIDs.add(customerID);
      }
      JDBC.closeConnection();
      return allCustomerIDs;
   }

   /** Adds a customer record to the database.
    * @param customer Customer object to add.
    * @param user User object used to stamp a customer record.
    * @throws SQLException Database access error. */
   public static void addCustomer(Customer customer, User user) throws SQLException {
      JDBC.openConnection();
      String timestamp = LocalDateTime.now(ZoneOffset.UTC).format(df);

      String sqlStatement = "INSERT INTO " + customerTableName + "(" + customerNameColumn + "," + customerAddressColumn
              + "," + customerPostCodeColumn + "," + customerPhoneColumn + "," + customerCreateDateColumn
              + "," + customerCreatedByColumn + "," + customerLastUpdateColumn + "," + customerLastUpdateByColumn
              + "," + customerDivisionIDColumn + ") VALUES ('" + customer.getName() + "','" + customer.getAddress()
              + "','" + customer.getPostCode() + "','" + customer.getPhone() + "','" + timestamp + "','"
              + user.getUsername() + "','" + timestamp + "','" + user.getUsername() + "'," + customer.getDivisionID()
              + ")";
      Query.makeQuery(sqlStatement);
      JDBC.closeConnection();
   }

   /** Deletes a customer record from the database.
    * @param customer Customer object to delete.
    * @throws SQLException Database access error. */
   public static void deleteCustomer(Customer customer) throws SQLException {
      JDBC.openConnection();
      String sqlStatement = "DELETE FROM " + customerTableName + " WHERE " + customerIDColumn + " = "
              + customer.getCustomerID();
      Query.makeQuery(sqlStatement);
      JDBC.closeConnection();
   }

   /** Updates a customer record in the database.
    * @param customer Customer object to update/edit.
    * @param user User object used to stamp a customer record update.
    * @throws SQLException Database access error. */
   public static void editCustomer(Customer customer, User user) throws SQLException {
      JDBC.openConnection();
      String timestamp = LocalDateTime.now(ZoneOffset.UTC).format(df);

      String sqlStatement = "UPDATE " + customerTableName + " SET " + customerNameColumn + "='" + customer.getName()
              + "'," + customerAddressColumn + "='" + customer.getAddress() + "'," + customerPostCodeColumn + "='"
              + customer.getPostCode() + "'," + customerPhoneColumn + "='" + customer.getPhone() + "',"
              + customerLastUpdateColumn + "='" + timestamp + "'," + customerLastUpdateByColumn + "='"
              + user.getUsername() + "'," + customerDivisionIDColumn + "=" + customer.getDivisionID() + " WHERE "
              + customerIDColumn + "=" + customer.getCustomerID();
      Query.makeQuery(sqlStatement);
      JDBC.closeConnection();
   }
}
