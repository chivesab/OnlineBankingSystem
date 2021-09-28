package team1spring2021cmpe202.AccountDAL;

import java.sql.PreparedStatement;
import team1spring2021cmpe202.MySQLConnector.MySQLConnector;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountCreate {
	static PreparedStatement preparedStatement = null;

	public static int createAccount (String userName, String accountType, float accountBalance, MySQLConnector databaseConnector) {
		try {
			String createQueryStatement = "INSERT INTO Accounts(UserID, Account_Type, Balance) " +
					"VALUES((SELECT Users.UserID FROM Users WHERE Users.Username = BINARY ?), ?, ?)";
			//userName should be used to set the UserID FK

			preparedStatement = databaseConnector.cmpe202BankingConnection.prepareStatement(createQueryStatement);

			preparedStatement.setString(1,  userName);
			preparedStatement.setString(2,  accountType);
			preparedStatement.setFloat(3,  accountBalance);

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}

		return 0;

	}
}