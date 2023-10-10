package com.ap.customer_appointment_app.models;

/** Model for a division within a country.
 * @author Allen Phillips
 * @version 1.0 */
public class Division {
   private final int divisionID;
   private final String divisionName;
   private final int countryID;

   /** Constructor to create a new division object.
    * @param divisionID Integer for division's ID.
    * @param divisionName String for division's name.
    * @param countryID Integer for division's associated country ID. */
   public Division(int divisionID, String divisionName, int countryID) {
      this.divisionID = divisionID;
      this.divisionName = divisionName;
      this.countryID = countryID;
   }

   /** Getter for division's ID.
    * @return Integer for division's ID. */
   public int getDivisionID() {
      return divisionID;
   }

   /** Getter for division's name.
    * @return String for division's name. */
   public String getDivisionName() {
      return divisionName;
   }

   /** Getter for division's country ID.
    * @return Integer for division's country ID. */
   public int getCountryID() {
      return countryID;
   }
}
