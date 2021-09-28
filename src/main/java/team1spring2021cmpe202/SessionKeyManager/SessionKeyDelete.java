package team1spring2021cmpe202.SessionKeyManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import team1spring2021cmpe202.MySQLConnector.MySQLConnector;

public class SessionKeyDelete {
	static PreparedStatement preparedStatement = null;
	
	public static int deleteSessionKeyByUsername (String userName, MySQLConnector databaseConnector) {
		try {
			String deleteQueryStatement = "DELETE FROM SessionKeys "
					+ "WHERE Owner = BINARY ?"; 
			//delete all session keys related to this account (logging out of all sessions)

			preparedStatement = databaseConnector.cmpe202BankingConnection.prepareStatement(deleteQueryStatement);

			preparedStatement.setString(1,  userName);
			
			preparedStatement.executeUpdate();
			
			return 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 1;
		
	}
	
	public static int cleanUpSessionKeys (MySQLConnector databaseConnector) {
		try {
			String cleanQueryStatement = "DELETE FROM SessionKeys "
					+ "WHERE TimeCreated <= DATE_SUB(CURRENT_TIME(), INTERVAL 1 DAY)";
			//delete all session keys older than 1 day

			preparedStatement = databaseConnector.cmpe202BankingConnection.prepareStatement(cleanQueryStatement);

			preparedStatement.executeUpdate();
			
			return 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 1;
		
	}
}
