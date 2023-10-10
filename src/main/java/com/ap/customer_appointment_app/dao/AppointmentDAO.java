package com.ap.customer_appointment_app.dao;

import com.ap.customer_appointment_app.models.Appointment;
import com.ap.customer_appointment_app.models.User;
import com.ap.customer_appointment_app.utility.JDBC;
import com.ap.customer_appointment_app.utility.Query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

/** DAO class to be used on the appointments table in the database.
 * @author Allen Phillips
 * @version 1.0 */
public class AppointmentDAO {
   private static final String appointmentTableName = "appointments";
   private static final String contactTableName = "contacts";
   private static final String appointmentIDColumn = "Appointment_ID";
   private static final String titleColumn = "Title";
   private static final String descriptionColumn = "Description";
   private static final String locationColumn = "Location";
   private static final String contactNameColumn = "Contact_Name";
   private static final String typeColumn = "Type";
   private static final String startColumn = "Start";
   private static final String endColumn = "End";
   private static final String createDateColumn = "Create_Date";
   private static final String createdByColumn = "Created_By";
   private static final String lastUpdateColumn = "Last_Update";
   private static final String lastUpdatedByColumn = "Last_Updated_By";
   private static final String customerIDColumn  = "Customer_ID";
   private static final String userIDColumn = "User_ID";
   private static final String contactIDColumn = "Contact_ID";
   private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss");

   /** Get all appointment records from the database.
    * @return ObservableList of appointment objects.
    * @throws SQLException Database access error. */
   public static ObservableList<Appointment> getAllAppointments() throws SQLException {
      ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
      JDBC.openConnection();
      String sqlStatement = "SELECT " + appointmentIDColumn + "," + titleColumn + "," + descriptionColumn + ","
              + locationColumn + "," + contactNameColumn + "," + typeColumn + "," + startColumn + "," + endColumn + ","
              + customerIDColumn + "," + userIDColumn + " FROM " + appointmentTableName + " t1 JOIN "
              + contactTableName + " t2 ON t1." + contactIDColumn + " = t2." + contactIDColumn;
      Query.makeQuery(sqlStatement);
      ResultSet resultSet = Query.getResultSet();
      while (resultSet.next()) {
         int appointmentID = resultSet.getInt(appointmentIDColumn);
         String title = resultSet.getString(titleColumn);
         String description = resultSet.getString(descriptionColumn);
         String location = resultSet.getString(locationColumn);
         String contactName = resultSet.getString(contactNameColumn);
         String type = resultSet.getString(typeColumn);
         ZoneId zoneId = ZoneId.systemDefault();

         Date startDate = resultSet.getDate("start");
         LocalDate localStartDate = startDate.toLocalDate();
         Time startTime = resultSet.getTime("start");
         LocalTime localStartTime = startTime.toLocalTime();
         ZonedDateTime utcStart = ZonedDateTime.of(localStartDate, localStartTime, ZoneId.of("UTC"));
         ZonedDateTime zonedLocalStart = utcStart.withZoneSameInstant(zoneId);
         Date endDate = resultSet.getDate("end");
         LocalDate localEndDate = endDate.toLocalDate();
         Time endTime = resultSet.getTime("end");
         LocalTime localEndTime = endTime.toLocalTime();
         ZonedDateTime utcEnd = ZonedDateTime.of(localEndDate, localEndTime, ZoneId.of("UTC"));
         ZonedDateTime zonedLocalEnd = utcEnd.withZoneSameInstant(zoneId);
         int customerID = resultSet.getInt(customerIDColumn);
         int userID = resultSet.getInt(userIDColumn);
         Appointment appointment = new Appointment(appointmentID, title, description, location, contactName, type,
                 zonedLocalStart, zonedLocalEnd, customerID, userID);
         allAppointments.add(appointment);
      }
      JDBC.closeConnection();
      return allAppointments;
   }

   /** Get all appointment records for a customer.
    * @param aID Integer for an appointment ID.
    * @param cID Integer for a customer ID.
    * @return ObservableList of appointment records for a customer.
    * @throws SQLException Database access error. */
   public static ObservableList<Appointment> getAllCustomerUTCAppointments(int aID, int cID) throws SQLException {
      ObservableList<Appointment> allCustomerAppointments = FXCollections.observableArrayList();
      JDBC.openConnection();
      String sqlStatement = "SELECT " + appointmentIDColumn + "," + titleColumn + "," + descriptionColumn + ","
              + locationColumn + "," + contactNameColumn + "," + typeColumn + "," + startColumn + "," + endColumn + ","
              + customerIDColumn + "," + userIDColumn + " FROM " + appointmentTableName + " t1 JOIN "
              + contactTableName + " t2 ON t1." + contactIDColumn + " = t2." + contactIDColumn + " WHERE "
              + customerIDColumn + " = " + cID;
      Query.makeQuery(sqlStatement);
      ResultSet resultSet = Query.getResultSet();
      while (resultSet.next()) {
         int appointmentID = resultSet.getInt(appointmentIDColumn);
         String title = resultSet.getString(titleColumn);
         String description = resultSet.getString(descriptionColumn);
         String location = resultSet.getString(locationColumn);
         String contactName = resultSet.getString(contactNameColumn);
         String type = resultSet.getString(typeColumn);

         Date startDate = resultSet.getDate("start");
         LocalDate localStartDate = startDate.toLocalDate();
         Time startTime = resultSet.getTime("start");
         LocalTime localStartTime = startTime.toLocalTime();
         ZonedDateTime utcStart = ZonedDateTime.of(localStartDate, localStartTime, ZoneId.of("UTC"));
         Date endDate = resultSet.getDate("end");
         LocalDate localEndDate = endDate.toLocalDate();
         Time endTime = resultSet.getTime("end");
         LocalTime localEndTime = endTime.toLocalTime();
         ZonedDateTime utcEnd = ZonedDateTime.of(localEndDate, localEndTime, ZoneId.of("UTC"));

         int customerID = resultSet.getInt(customerIDColumn);
         int userID = resultSet.getInt(userIDColumn);
         Appointment appointment = new Appointment(appointmentID, title, description, location, contactName, type,
                 utcStart, utcEnd, customerID, userID);
         if (appointment.getAppointmentID() != aID) {
            allCustomerAppointments.add(appointment);
         }
      }
      JDBC.closeConnection();
      return allCustomerAppointments;
   }

   /** Adds a new appointment to the database.
    * @param a Appointment object to add.
    * @param u User object used to stamp an appointment record.
    * @throws SQLException Database access error. */
   public static void addAppointment(Appointment a, User u) throws SQLException {
      String timestamp = LocalDateTime.now(ZoneOffset.UTC).format(df);
      int contactID = ContactDAO.getContactID(a.getContact());
      JDBC.openConnection();

      String sqlStatement = "INSERT INTO " + appointmentTableName + " (" + titleColumn + "," + descriptionColumn
              + "," + locationColumn + "," + typeColumn + "," + startColumn + "," + endColumn + ","
              + createDateColumn + "," + createdByColumn + "," + lastUpdateColumn + "," + lastUpdatedByColumn
              + "," + customerIDColumn + "," + userIDColumn + "," + contactIDColumn + ") VALUES ('"
              + a.getTitle() + "','" + a.getDescription() + "','" + a.getLocation() + "','" + a.getType()
              + "','" + a.getStart().format(df)  + "','" + a.getEnd().format(df) + "','" + timestamp + "','" + u.getUsername() + "','"
              + timestamp + "','" + u.getUsername() + "'," + a.getCustomerID() + "," + a.getUserID() + ","
              + contactID + ")";
      Query.makeQuery(sqlStatement);
      JDBC.closeConnection();
   }

   /** Deletes an appointment from the database.
    * @param a Appointment object to delete.
    * @throws SQLException Database access error. */
   public static void deleteAppointment(Appointment a) throws SQLException {
      JDBC.openConnection();
      String sqlStatement = "DELETE FROM " + appointmentTableName + " WHERE " + appointmentIDColumn + " = "
              + a.getAppointmentID();
      Query.makeQuery(sqlStatement);
      JDBC.closeConnection();
   }

   /** Updates an appointment in the database.
    * @param a Appointment object to update.
    * @param u User object used to stamp an appointment record update.
    * @throws SQLException Database access error. */
   public static void editAppointment(Appointment a, User u) throws SQLException {
      String timestamp = LocalDateTime.now(ZoneOffset.UTC).format(df);
      int contactID = ContactDAO.getContactID(a.getContact());
      JDBC.openConnection();

      String sqlStatement = "UPDATE " + appointmentTableName + " SET " + titleColumn + "='" + a.getTitle() + "',"
              + descriptionColumn + "='" + a.getDescription() + "'," + locationColumn + "='" + a.getLocation()
              + "'," + typeColumn + "='" + a.getType() + "'," + startColumn + "='" + a.getStart().format(df)
              + "'," + endColumn + "='" + a.getEnd().format(df) + "'," + lastUpdateColumn + "='" + timestamp
              + "'," + lastUpdatedByColumn + "='" + u.getUsername() + "'," + customerIDColumn + "="
              + a.getCustomerID() + "," + userIDColumn + "=" + a.getUserID() + "," + contactIDColumn + "="
              + contactID + " WHERE " + appointmentIDColumn + "=" + a.getAppointmentID();
      Query.makeQuery(sqlStatement);
      JDBC.closeConnection();
   }
}
