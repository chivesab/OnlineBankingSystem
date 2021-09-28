package team1spring2021cmpe202.MySQLConnector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnector {
	public Connection cmpe202BankingConnection = null;
	
	public void makeJDBCConnection() {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			//Class.forName("com.mysql.cj.jdbc.Driver"); for new MySQL version
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}

		try {
			// DriverManager: The basic service for managing a set of JDBC drivers.
			String jdbcConnString = "jdbc:mysql://onlinetransaction.chdrc7t7199w.us-west-1.rds.amazonaws.com:3306/SpringBackEnd?useSSL=false";
			String jdbcUser = "yuchelin";
			String jdbcPassword = "willlinsjsu";

			cmpe202BankingConnection = DriverManager.getConnection(jdbcConnString, jdbcUser, jdbcPassword);
			if (cmpe202BankingConnection != null) {
			} else {
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
 
	}
	
	public void closeJDBCConnection() {
		try {
			cmpe202BankingConnection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}