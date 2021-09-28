package team1spring2021cmpe202.TransactionDAL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import team1spring2021cmpe202.MySQLConnector.MySQLConnector;

public class TransactionCreate {
	static PreparedStatement preparedStatement = null;
	
	public static int createTransaction (String sourceAccountID, String targetAccountID, float transactionAmount, boolean recurring, int transactionPeriodInHours, String Description, MySQLConnector databaseConnector) {
		try {
			String createQueryStatement = "INSERT INTO Transactions (Origin, Target, Amount, Recurring, Recurring_Period_In_Hours, Description)" +
					" VALUES (?, ?, ?, ?, ?, ?)";
			//create transaction with the given data

			preparedStatement = databaseConnector.cmpe202BankingConnection.prepareStatement(createQueryStatement);
			
			preparedStatement.setString(1,  sourceAccountID);
			preparedStatement.setString(2,  targetAccountID);
			preparedStatement.setFloat(3,  transactionAmount);
			preparedStatement.setBoolean(4,  recurring);
			preparedStatement.setInt(5,  transactionPeriodInHours);
			preparedStatement.setString(6,  Description);

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}

		return 0;
		
	}
}
