package team1spring2021cmpe202.TransactionDAL;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import team1spring2021cmpe202.MySQLConnector.MySQLConnector;

public class TransactionRead {
	static PreparedStatement preparedStatement = null;
	
	public static ResultSet readTransactionByAccountNumber (String accountNumber, MySQLConnector databaseConnector) {
		try {
			String readQueryStatement = "SELECT T.Origin, T.Target, T.Amount, T.Date, T.Recurring," +
					" T.Recurring_Period_In_Hours, T.Description FROM Transactions T JOIN Accounts A ON " +
					"(T.Origin = A.Account_Number OR T.Target = A.Account_Number) WHERE A.Account_Number = ?";
			//return all transactions involving the given account (receiver/sender)

			preparedStatement = databaseConnector.cmpe202BankingConnection.prepareStatement(readQueryStatement);
			
			preparedStatement.setString(1,  accountNumber);
			
			ResultSet rs = preparedStatement.executeQuery();


			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
}
