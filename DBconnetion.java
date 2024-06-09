package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnetion {
	private static final String DB_IP = "127.0.0.1"; // Replace with the IP address of your MySQL server
	private static final String DB_PORT = "3306"; // Default MySQL port
	private static final String DB_NAME = "project"; // Name of your database
	private static final String DB_URL = "jdbc:mysql://" + DB_IP + ":" + DB_PORT + "/" + DB_NAME;
	private static final String DB_USERNAME = "root";
	private static final String DB_PASSWORD = "dania@942003";

	// Method to establish database connection
	public static Connection getConnection() throws SQLException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver"); // Load the MySQL JDBC driver
			return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD); // Connect to the database
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new SQLException("MySQL JDBC Driver not found", e);
		}
	}

	// Method to close database connection
	public static void closeConnection(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}