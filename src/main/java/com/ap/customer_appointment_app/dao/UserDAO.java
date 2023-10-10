package com.ap.customer_appointment_app.dao;

import com.ap.customer_appointment_app.models.User;
import com.ap.customer_appointment_app.utility.JDBC;
import com.ap.customer_appointment_app.utility.Query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;


/** DAO class to be used on the users table in the database.
 * @author Allen Phillips
 * @version 1.0 */
public class UserDAO {
   private static final String userTableName = "users";
   private static final String userIDColumnName = "User_ID";
   private static final String usernameColumnName = "User_Name";
   private static final String passwordColumnName = "Password";

   /** Gets a user record from the database.
    * @param username String for the user's name.
    * @return User object for the given user's name.
    * @throws SQLException Database access error. */
   public static User getUser(String username) throws SQLException {
      JDBC.openConnection();
      String sqlStatement = "SELECT * FROM " + userTableName + " WHERE " + usernameColumnName + " = '" + username + "'";
      Query.makeQuery(sqlStatement);
      User userResult;
      ResultSet resultSet = Query.getResultSet();
      if (resultSet.next()) {
         int userIDResult = resultSet.getInt(userIDColumnName);
         String usernameResult = resultSet.getString(usernameColumnName);
         String passwordResult = resultSet.getString(passwordColumnName);
         userResult = new User(userIDResult, usernameResult, passwordResult);
         JDBC.closeConnection();
         return userResult;
      }
      JDBC.closeConnection();
      return null;
   }

   /** Gets all user IDs from the database.
    * @return ObservableList of integers of all user IDs.
    * @throws SQLException Database access error. */
   public static ObservableList<Integer> getAllUserIDs() throws SQLException {
      ObservableList<Integer> allUserIDs = FXCollections.observableArrayList();
      JDBC.openConnection();
      String sqlStatement = "SELECT " + userIDColumnName + " FROM " + userTableName;
      Query.makeQuery(sqlStatement);
      ResultSet resultSet = Query.getResultSet();
      while (resultSet.next()) {
         int userIDResult = resultSet.getInt(userIDColumnName);
         allUserIDs.add(userIDResult);
      }
      JDBC.closeConnection();
      return allUserIDs;
   }
}
