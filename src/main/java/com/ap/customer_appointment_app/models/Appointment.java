package com.ap.customer_appointment_app.models;

import java.time.ZonedDateTime;

/** Model for an appointment.
 * @author Allen Phillips
 * @version 1.0 */
public class Appointment {
   private final int appointmentID;
   private final String title;
   private final String description;
   private final String location;
   private final String contact;
   private final String type;
   private final ZonedDateTime start;
   private final ZonedDateTime end;
   private final int customerID;
   private final int userID;

   /** Constructor to create a new appointment object.
    * @param appointmentID Integer for appointment's ID.
    * @param title String for appointment's title.
    * @param description String for appointment's description.
    * @param location String for appointment's location.
    * @param contact String for appointment's contact name.
    * @param type String for appointment's type.
    * @param start ZonedDateTime for appointment's starting date and time.
    * @param end ZonedDateTime for appointment's ending date and time.
    * @param customerID Integer for appointment's associated customer ID.
    * @param userID Integer for appointment's associated user ID. */
   public Appointment(int appointmentID, String title, String description, String location, String contact,
                      String type, ZonedDateTime start, ZonedDateTime end, int customerID, int userID) {
      this.appointmentID = appointmentID;
      this.title = title;
      this.description = description;
      this.location = location;
      this.contact = contact;
      this.type = type;
      this.start = start;
      this.end = end;
      this.customerID = customerID;
      this.userID = userID;
   }

   /** Getter for appointment's ID.
    * @return Integer for appointment's ID. */
   public int getAppointmentID() {
      return appointmentID;
   }

   /** Getter for appointment's title.
    * @return String for appointment's title. */
   public String getTitle() {
      return title;
   }

   /** Getter for appointment's description.
    * @return String for appointment's description. */
   public String getDescription() {
      return description;
   }

   /** Getter for appointment's location.
    * @return String for appointment's location. */
   public String getLocation() {
      return location;
   }

   /** Getter for appointment's type.
    * @return String for appointment's type. */
   public String getType() {
      return type;
   }

   /** Getter for appointment's starting date and time.
    * @return ZonedDateTime for appointment's starting date and time. */
   public ZonedDateTime getStart() {
      return start;
   }

   /** Getter for appointment's ending date and time.
    * @return ZonedDateTime for appointment's ending date and time. */
   public ZonedDateTime getEnd() {
      return end;
   }

   /** Getter for appointment's contact name.
    * @return String for appointment's contact name. */
   public String getContact() {
      return contact;
   }

   /** Getter for appointment's associated customer ID.
    * @return Integer for appointment's associated customer ID. */
   public int getCustomerID() {
      return customerID;
   }

   /** Getter for appointment's associated user ID.
    * @return Integer for appointment's associated user ID. */
   public int getUserID() {
      return userID;
   }
}
