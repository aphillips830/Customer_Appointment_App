package com.ap.customer_appointment_app.dao;

import com.ap.customer_appointment_app.utility.JDBC;
import com.ap.customer_appointment_app.utility.Query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

/** DAO class to be used on the contacts table in the database.
 * @author Allen Phillips
 * @version 1.0 */
public class ContactDAO {
   private static final String contactTableName = "contacts";
   private static final String contactNameColumn = "Contact_Name";
   private static final String contactIDColumn = "Contact_ID";

   /** Get all contact names from the database.
    * @return ObservableList of Strings with contact names.
    * @throws SQLException Database access error. */
   public static ObservableList<String> getAllContactNames() throws SQLException {
      ObservableList<String> allContactNames = FXCollections.observableArrayList();
      JDBC.openConnection();
      String sqlStatement = "SELECT " + contactNameColumn + " FROM " + contactTableName;
      Query.makeQuery(sqlStatement);
      ResultSet resultSet = Query.getResultSet();
      while (resultSet.next()) {
         String contactName = resultSet.getString(contactNameColumn);
         allContactNames.add(contactName);
      }
      JDBC.closeConnection();
      return allContactNames;
   }

   /** Gets a contact ID from the database using a contact name.
    * @param contactName String for the contact's name.
    * @return Integer for the contact's ID.
    * @throws SQLException Database access error. */
   public static int getContactID(String contactName) throws SQLException {
      int contactID;
      JDBC.openConnection();
      String sqlStatement = "SELECT " + contactIDColumn + " FROM " + contactTableName +  " WHERE "
              + contactNameColumn + "='" + contactName + "'";
      Query.makeQuery(sqlStatement);
      ResultSet resultSet = Query.getResultSet();
      if (resultSet.next()) {
         contactID = resultSet.getInt(contactIDColumn);
         JDBC.closeConnection();
         return contactID;
      }
      JDBC.closeConnection();
      return -1;
   }
}
