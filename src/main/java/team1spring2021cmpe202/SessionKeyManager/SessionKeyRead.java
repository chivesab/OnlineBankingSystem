package team1spring2021cmpe202.SessionKeyManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import team1spring2021cmpe202.MySQLConnector.MySQLConnector;

public class SessionKeyRead {
	static PreparedStatement preparedStatement = null;
	
	public static ResultSet readCountSessionKeyByKeyValueAndUsername (String sessionKey, String userName, MySQLConnector databaseConnector) {
		try {
			String getQueryStatement = "SELECT COUNT(SessionKeyID) AS NumberOfValidSK FROM SessionKeys "
					+ "WHERE Owner = BINARY ? "
					+ "AND SessionKeyID = BINARY ? "
					+ "AND TimeCreated >= DATE_SUB(CURRENT_TIME, INTERVAL 1 DAY)";
			//Select count of valid keys with corresponding username and key value

			preparedStatement = databaseConnector.cmpe202BankingConnection.prepareStatement(getQueryStatement);

			preparedStatement.setString(1, userName);
			preparedStatement.setString(2, sessionKey);
			
			ResultSet rs = preparedStatement.executeQuery();
			
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public static ResultSet readLatestSessionKeyByUsername (String userName, MySQLConnector databaseConnector) {
		try {
			String createQueryStatement = "SELECT * FROM SessionKeys WHERE Owner = BINARY ? "
					+ "ORDER BY TimeCreated DESC LIMIT 1";
			//read most recent session key entry with the given owner

			preparedStatement = databaseConnector.cmpe202BankingConnection.prepareStatement(createQueryStatement);
			
			preparedStatement.setString(1, userName);
			
			ResultSet rs = preparedStatement.executeQuery();
			
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
}
