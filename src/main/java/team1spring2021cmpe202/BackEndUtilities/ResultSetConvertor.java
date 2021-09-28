package team1spring2021cmpe202.BackEndUtilities;

import org.json.JSONArray;
import org.json.JSONObject;


import team1spring2021cmpe202.SpringBackEnd.SessionKey;
import team1spring2021cmpe202.SpringBackEnd.Account;
import team1spring2021cmpe202.SpringBackEnd.User;
import team1spring2021cmpe202.SpringBackEnd.Transaction;

import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class ResultSetConvertor {

	/**
	   * Converts a mysql query result set to a JSONArray
	   * @param type is ResultSet
	   * @return JSONArray conversion of the result set
	   */
	public static JSONArray convertToJSON(ResultSet resultSet)
            throws Exception {
        JSONArray jsonArray = new JSONArray();
        while (resultSet.next()) {
            int total_rows = resultSet.getMetaData().getColumnCount();
            for (int i = 0; i < total_rows; i++) {
                JSONObject obj = new JSONObject();
                obj.put(resultSet.getMetaData().getColumnLabel(i + 1)
                        .toLowerCase(), resultSet.getObject(i + 1));
                jsonArray.put(obj);
            }
        }
        return jsonArray;
    }
	
	/**
	   * Converts a mysql query result set to a list of Identity
	   * @param ResultSet of Identity
	   * @return List<Identity> conversion of ResultSet
	   */
	public static List<User> convertToUserList(ResultSet resultSet) throws SQLException {
		List<User> ll = new LinkedList<User>();
	
		while (resultSet.next()) {
			String userName = resultSet.getString("Username");
			String userPassword = resultSet.getString("Hashed_Password");
			String roleType = resultSet.getString("Roletype");
	
		  User acc = new User(userName, userPassword, roleType);
	
		  ll.add(acc);
		}
		
		return ll;
	}
	
	public static List<SessionKey> convertToSessionKeyList(ResultSet resultSet) throws SQLException {
		List<SessionKey> ll = new LinkedList<SessionKey>();
	
		while (resultSet.next()) {
			String skid = resultSet.getString("SessionKeyID");
			LocalDateTime tc = new java.sql.Timestamp(resultSet.getDate("TimeCreated").getTime()).toLocalDateTime();
			String owner = resultSet.getString("Owner");
	
		  SessionKey sk = new SessionKey();
		  sk.setSessionKey(skid);
		  sk.setSessionKeyIssuedTimestamp(tc);
		  sk.setUserName(owner);
		  
	
		  ll.add(sk);
		}
		
		return ll;
	}

	public static List<Account> convertToAccountList(ResultSet resultSet) throws SQLException {
		List<Account> accountList = new ArrayList<Account>();

		while(resultSet.next()){
			Account account = new Account();
			account.setAccountNumber(resultSet.getString("Account_Number"));
			account.setAccountType(resultSet.getString("Account_Type"));
			account.setAccountBalance(resultSet.getFloat("Balance"));
			accountList.add(account);
		}

		return accountList;
	}

	public static List<Transaction> convertToTransactionList(ResultSet resultSet) throws SQLException {
		List<Transaction> transactionList = new ArrayList<Transaction>();

		while(resultSet.next()){
			Transaction transaction = new Transaction();
			transaction.setOriginAccountNumber(resultSet.getString("Origin"));
			transaction.setTargetAccountNumber(resultSet.getString("Target"));
			transaction.setTransactionAmount(resultSet.getFloat("Amount"));
			transaction.setInitialTransactionDate(new java.sql.Timestamp(resultSet.getDate("Date").getTime()).toLocalDateTime());
			transaction.setTransactionPeriodInDays(resultSet.getInt("Recurring_Period_In_Hours"));
			transaction.setRecurring(resultSet.getBoolean("Recurring"));
			transaction.setDescription(resultSet.getString("Description"));

			transactionList.add(transaction);
		}
		return transactionList;
	}



	public static int countFromResultSet(ResultSet resultSet, String countType) throws SQLException {
		if (resultSet.next() && resultSet != null) {
			return resultSet.getInt(countType);
		}
		return -1;
	}
}