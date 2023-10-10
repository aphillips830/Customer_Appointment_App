package com.ap.customer_appointment_app.models;

/** Model for a country.
 * @author Allen Phillips
 * @version 1.0 */
public class Country {
   private final int countryID;
   private final String countryName;

   /** Constructor to create a new country object.
    * @param countryID Integer for a countries ID.
    * @param countryName String for a countries name. */
   public Country(int countryID, String countryName) {
      this.countryID = countryID;
      this.countryName = countryName;
   }

   /** Getter for countries ID.
    * @return Integer for countries ID. */
   public int getCountryID() {
      return countryID;
   }

   /** Getter for countries name.
    * @return String for countries name. */
   public String getCountryName() {
      return countryName;
   }
}
