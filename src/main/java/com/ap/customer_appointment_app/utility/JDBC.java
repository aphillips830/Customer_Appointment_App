package com.ap.customer_appointment_app.utility;

import java.sql.Connection;
import java.sql.DriverManager;

/** Handles accessing, opening, and closing the connection to the database. */
public abstract class JDBC {
   private static final String protocol = "jdbc";
   private static final String vendor = ":mysql:";
   private static final String location = "//localhost/";
   private static final String databaseName = "client_schedule";
   private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL
   private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
   private static final String userName = "sqlUser"; // Username
   private static final String password = "Passw0rd!"; // Password

   /** Connection variable for accessing the database. */
   public static Connection connection;  // Connection Interface

   /** Opens the connection to the database. */
   public static void openConnection()
   {
      try {
         Class.forName(driver); // Locate Driver
         connection = DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection object
      }
      catch(Exception e)
      {
         System.out.println("Error:" + e.getMessage());
      }
   }

   /** Closes the connection to the database. */
   public static void closeConnection() {
      try {
         connection.close();
      }
      catch(Exception e)
      {
         System.out.println("Error:" + e.getMessage());
      }
   }
}