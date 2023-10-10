package com.ap.customer_appointment_app.models;

/** Model for a customer.
 * @author Allen Phillips
 * @version 1.0 */
public class Customer {
   private final int customerID;
   private final String name;
   private final String address;
   private final String postCode;
   private final String phone;
   private final int divisionID;
   private final String division;
   private final String country;


   /** Constructor to create a new customer object.
    * @param customerID Integer for a customer's ID.
    * @param name String for a customer's name.
    * @param address String for a customer's address.
    * @param postCode String for a customer's postal code.
    * @param phone String for a customer's phone number.
    * @param divisionID Integer for a customers division ID.
    * @param division String for a customer's division name.
    * @param country String for a customer's country name. */
   public Customer(int customerID, String name, String address, String postCode, String phone, int divisionID,
                   String division, String country) {
      this.customerID = customerID;
      this.name = name;
      this.address = address;
      this.postCode = postCode;
      this.phone = phone;
      this.divisionID = divisionID;
      this.division = division;
      this.country = country;
   }

   /** Getter for customer's ID.
    * @return Integer for customer's ID. */
   public int getCustomerID() {
      return customerID;
   }

   /** Getter for customer's name.
    * @return String for customer's name. */
   public String getName() {
      return name;
   }

   /** Getter for customer's address.
    * @return String for customer's address. */
   public String getAddress() {
      return address;
   }

   /** Getter for customer's postal code.
    * @return String for customer's postal code. */
   public String getPostCode() {
      return postCode;
   }

   /** Getter for customer's phone number.
    * @return String for customer's phone number. */
   public String getPhone() {
      return phone;
   }

   /** Getter for customer's division ID.
    * @return Integer for customer's division ID. */
   public int getDivisionID() {
      return divisionID;
   }

   /** Getter for customer's division name.
    * @return String for customer's division name. */
   public String getDivision() {
      return division;
   }

   /** Getter for customer's country name.
    * @return String for customer's country name. */
   public String getCountry() {
      return country;
   }
}
