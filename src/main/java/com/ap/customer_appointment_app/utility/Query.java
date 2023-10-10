package com.ap.customer_appointment_app.utility;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.ap.customer_appointment_app.utility.JDBC.connection;

/** Helper class for querying the database and getting the results. */
public class Query {
   private static ResultSet resultSet;

   /** Performs a database query.
    * @param query String representing the query to be run. */
   public static void makeQuery(String query) {
      Statement statement;

      try {
         statement = connection.createStatement();
         if (query.toLowerCase().startsWith("select")) {
            resultSet = statement.executeQuery(query);
         }
         if (query.toLowerCase().startsWith("delete") || query.toLowerCase().startsWith("insert")
                 || query.toLowerCase().startsWith("update")) {
            statement.executeUpdate(query);
         }
      } catch (SQLException e) {
         System.out.println("Oops something went wrong with a Query: " + e.getMessage());
      }
   }

   /** Gets the queries result set.
    * @return ResultSet of the query. */
   public static ResultSet getResultSet() {
      return resultSet;
   }
}
