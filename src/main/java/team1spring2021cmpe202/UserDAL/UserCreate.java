package team1spring2021cmpe202.UserDAL;

import java.sql.PreparedStatement;
import team1spring2021cmpe202.MySQLConnector.MySQLConnector;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserCreate {
	public static PreparedStatement preparedStatement = null;
	
	public static int createUser(String userName, String hPassword, String roleType, MySQLConnector databaseConnector) {
		 
		try {
			// MySQL Select Query Tutorial
			String createQueryStatement = "INSERT INTO Users (Username, Hashed_Password, Roletype) values (?, ?, ?)";
 
			preparedStatement = databaseConnector.cmpe202BankingConnection.prepareStatement(createQueryStatement);

			preparedStatement.setString(1, userName);
			preparedStatement.setString(2, hPassword);
			preparedStatement.setString(3, roleType);
 
			// Execute the Query, and get a java ResultSet
			preparedStatement.executeUpdate();

			return 0;
 
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
 
	}
}