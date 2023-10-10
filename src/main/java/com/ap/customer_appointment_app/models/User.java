package com.ap.customer_appointment_app.models;

/** Model for a user.
 * @author Allen Phillips
 * @version 1.0 */
public class User {
   private final int userID;
   private final String username;
   private final String password;

   /** Constructor to create a new user object.
    * @param userID Integer for user's ID.
    * @param username String for user's name.
    * @param password String for user's password. */
   public User(int userID, String username, String password) {
      this.userID = userID;
      this.username = username;
      this.password = password;
   }

   /** Getter for user's ID.
    * @return Integer for user's ID. */
   public int getUserID() {
      return userID;
   }

   /** Getter for user's name.
    * @return String for user's name. */
   public String getUsername() {
      return username;
   }

   /** Getter for user's password.
    * @return String for user's password. */
   public String getPassword() {
      return password;
   }
}
