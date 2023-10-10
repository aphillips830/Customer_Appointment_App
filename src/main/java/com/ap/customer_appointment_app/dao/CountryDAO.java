package com.ap.customer_appointment_app.dao;

import com.ap.customer_appointment_app.models.Country;
import com.ap.customer_appointment_app.utility.JDBC;
import com.ap.customer_appointment_app.utility.Query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;


/** DAO class to be used on the countries table in the database.
 * @author Allen Phillips
 * @version 1.0 */
public class CountryDAO {
   private static final String countryTableName = "countries";
   private static final String countryIDColumn = "Country_ID";
   private static final String countryNameColumn = "Country";

   /** Get all country records from the database.
    * @return ObservableList of all country objects.
    * @throws SQLException Database access error. */
   public static ObservableList<Country> getAllCountries() throws SQLException {
      ObservableList<Country> allCountries = FXCollections.observableArrayList();
      JDBC.openConnection();
      String sqlStatement = "SELECT " + countryIDColumn + "," + countryNameColumn + " FROM " + countryTableName;
      Query.makeQuery(sqlStatement);
      ResultSet resultSet = Query.getResultSet();
      while (resultSet.next()) {
         int countryID = resultSet.getInt(countryIDColumn);
         String countryName = resultSet.getString(countryNameColumn);
         Country currentCountry = new Country(countryID, countryName);
         allCountries.add(currentCountry);
      }
      JDBC.closeConnection();
      return allCountries;
   }
}
