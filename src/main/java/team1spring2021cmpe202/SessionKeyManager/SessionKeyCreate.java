package team1spring2021cmpe202.SessionKeyManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import team1spring2021cmpe202.MySQLConnector.MySQLConnector;

public class SessionKeyCreate {
	static PreparedStatement preparedStatement = null;
	
	public static int CreateSessionKeyByUsername (String userName, MySQLConnector databaseConnector) {
		try {
			String createQueryStatement = "INSERT INTO SessionKeys (TimeCreated, Owner) VALUES(CURRENT_TIME(), ?)";
			//create session key entry with the given value, use NOW() for timestamp

			preparedStatement = databaseConnector.cmpe202BankingConnection.prepareStatement(createQueryStatement);
			
			preparedStatement.setString(1, userName);
			
			preparedStatement.executeUpdate();
			
			return 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 1;
		
	}
}
