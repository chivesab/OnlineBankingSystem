package team1spring2021cmpe202.UserDAL;

import java.sql.PreparedStatement;
import team1spring2021cmpe202.MySQLConnector.MySQLConnector;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRead {
	static PreparedStatement preparedStatement = null;
	
	public static ResultSet readUsersByUsernameAndHPass(String userName, String hPassword, MySQLConnector databaseConnector) {
		 
		try {
			String readQueryStatement = "SELECT Username, Hashed_Password, Roletype FROM Users "
					+ "WHERE Users.Username = BINARY ? "
					+ "AND Users.Hashed_Password = BINARY ?";
 
			preparedStatement = databaseConnector.cmpe202BankingConnection.prepareStatement(readQueryStatement);

			preparedStatement.setString(1, userName);
			preparedStatement.setString(2, hPassword);
 
			// Execute the Query, and get a java ResultSet
			ResultSet rs = preparedStatement.executeQuery();

			return rs;
 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
 
	}
	
	public static ResultSet readUserCountByUsernameAndHPass(String userName, String hPassword, MySQLConnector databaseConnector) {
		try {
			System.out.println(String.format("%s %s", userName, hPassword));
			
			
			String readQueryStatement = "SELECT COUNT(UserID) AS NumberOfUsers FROM Users "
					+ "WHERE Users.Username = BINARY ? "
					+ "AND Users.Hashed_Password = BINARY ?";
 
			preparedStatement = databaseConnector.cmpe202BankingConnection.prepareStatement(readQueryStatement);

			preparedStatement.setString(1, userName);
			preparedStatement.setString(2, hPassword);
 
			// Execute the Query, and get a java ResultSet
			ResultSet rs = preparedStatement.executeQuery();
			
			return rs;
 
		} catch (SQLException e) {
			e.printStackTrace();
		}
 
		return null;
	}
}
