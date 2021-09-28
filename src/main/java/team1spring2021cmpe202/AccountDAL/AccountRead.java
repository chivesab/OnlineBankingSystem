package team1spring2021cmpe202.AccountDAL;

import java.sql.PreparedStatement;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import team1spring2021cmpe202.MySQLConnector.MySQLConnector;
import team1spring2021cmpe202.SpringBackEnd.Account;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountRead {
	static PreparedStatement preparedStatement = null;

	BeanPropertyRowMapper<Account> ACCOUNT_MAPPER = new BeanPropertyRowMapper<>(Account.class);

	public static ResultSet readAccountByUsername (String userName, MySQLConnector databaseConnector) {
		try {
			String getQueryStatement = "SELECT A.Account_Number, A.Account_Type, A.Balance FROM Accounts A JOIN Users U " +
					"on A.UserID = U.UserID WHERE U.Username = BINARY ?";
			//Select accounts that correspond to the given username

			preparedStatement = databaseConnector.cmpe202BankingConnection.prepareStatement(getQueryStatement);

			preparedStatement.setString(1,  userName);

			ResultSet rs = preparedStatement.executeQuery();

			return rs;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;

	}
	
	public static ResultSet readAccountByAccountNumber (String userName, MySQLConnector databaseConnector) {
		try {
			String getQueryStatement = "SELECT A.Account_Number, A.Account_Type, A.Balance FROM Accounts A "
					+ "WHERE A.Account_Number = BINARY ?";
			//Select accounts that correspond to the given username

			preparedStatement = databaseConnector.cmpe202BankingConnection.prepareStatement(getQueryStatement);

			preparedStatement.setString(1,  userName);

			ResultSet rs = preparedStatement.executeQuery();

			return rs;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;

	}
}