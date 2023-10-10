package com.ap.customer_appointment_app.dao;

import com.ap.customer_appointment_app.models.Division;
import com.ap.customer_appointment_app.utility.JDBC;
import com.ap.customer_appointment_app.utility.Query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;


/** DAO class to be used on the first_level_divisions table in the database.
 * @author Allen Phillips
 * @version 1.0 */
public class DivisionDAO {
   private static final String divisionTableName = "first_level_divisions";
   private static final String divisionNameColumn = "Division";
   private static final String divisionIDColumn = "Division_ID";
   private static final String divisionCountryIDColumn = "Country_ID";

   /** Gets all division records from the database.
    * @return ObservableList of all division objects.
    * @throws SQLException Database access error. */
   public static ObservableList<Division> getAllDivisions() throws SQLException {
      ObservableList<Division> allDivisions = FXCollections.observableArrayList();
      JDBC.openConnection();
      String sqlStatement = "SELECT " + divisionIDColumn + "," + divisionNameColumn + "," + divisionCountryIDColumn
              + " FROM " + divisionTableName;
      Query.makeQuery(sqlStatement);
      ResultSet resultSet = Query.getResultSet();
      while (resultSet.next()) {
         int divisionID = resultSet.getInt(divisionIDColumn);
         String divisionName = resultSet.getString(divisionNameColumn);
         int countryID = resultSet.getInt(divisionCountryIDColumn);
         Division currentDivision = new Division(divisionID, divisionName, countryID);
         allDivisions.add(currentDivision);
      }
      JDBC.closeConnection();
      return allDivisions;
   }
}
