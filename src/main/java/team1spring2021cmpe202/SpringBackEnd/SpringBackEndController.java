package team1spring2021cmpe202.SpringBackEnd;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.sql.ResultSet;
import java.util.List;
import java.util.UUID;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.javatuples.Triplet;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import team1spring2021cmpe202.AccountDAL.AccountCreate;
import team1spring2021cmpe202.AccountDAL.AccountDelete;
import team1spring2021cmpe202.AccountDAL.AccountRead;
import team1spring2021cmpe202.AccountDAL.AccountUpdate;
import team1spring2021cmpe202.BackEndUtilities.PasswordHasher;
import team1spring2021cmpe202.BackEndUtilities.ResultSetConvertor;
import team1spring2021cmpe202.MySQLConnector.MySQLConnector;
import team1spring2021cmpe202.TransactionDAL.TransactionCreate;
import team1spring2021cmpe202.TransactionDAL.TransactionRead;
import team1spring2021cmpe202.UserDAL.UserCreate;
import team1spring2021cmpe202.SessionKeyManager.SessionKeyCreate;
import team1spring2021cmpe202.SessionKeyManager.SessionKeyDelete;
import team1spring2021cmpe202.SessionKeyManager.SessionKeyRead;
import team1spring2021cmpe202.UserDAL.UserRead;

@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RestController
public class SpringBackEndController {

	private boolean isValid(String uuidAsString, String accountName) {
		MySQLConnector myConnector = new MySQLConnector();
		myConnector.makeJDBCConnection();
		//this.cleanSessionKeys(); // remove all old session keys (optional)
		int validKeyCount;
		try {
			validKeyCount = ResultSetConvertor.countFromResultSet(SessionKeyRead.readCountSessionKeyByKeyValueAndUsername(uuidAsString, accountName, myConnector), "NumberOfValidSK");
		} catch (SQLException e) {
			e.printStackTrace();
			myConnector.closeJDBCConnection();
			return false;
		}
		myConnector.closeJDBCConnection();
		if(validKeyCount > 0) {
			return true;
		}
		return false;
	}

	private void cleanSessionKeys() {
		MySQLConnector myConnector = new MySQLConnector();
		myConnector.makeJDBCConnection();
		SessionKeyDelete.cleanUpSessionKeys(myConnector);
		myConnector.closeJDBCConnection();
	}
	
	/*
	 * requestBody:
	 * 
	 * sessionKey : String (this is the session key)
	 * userName : Username of the currently logged in user
	 */
	@PostMapping("/session")
	public ResponseEntity<List<String>> validateSession(@RequestBody SessionKey sessionKeyBody) throws SQLException, IOException {
		if (!this.isValid(sessionKeyBody.getSessionKey(), sessionKeyBody.getUserName())) {
			return new ResponseEntity<List<String>>(HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<List<String>>(HttpStatus.OK);
	}

	/*
	 * userReqBody
	 * 
	 * userName : String
	 * userPassword : String
	 * 
	 * returns: List containing the session key for the current session
	 */
	@PostMapping("/users/login")
	public ResponseEntity<List<SessionKey>> validateAccount(@RequestBody User userReqBody) throws Exception {

		MySQLConnector myConnector = new MySQLConnector();
		myConnector.makeJDBCConnection();

		int matchingAccounts = ResultSetConvertor.countFromResultSet(
				UserRead.readUserCountByUsernameAndHPass(userReqBody.getUserName(),
						PasswordHasher.generateStrongPasswordHash(userReqBody.getUserPassword()), myConnector),
				"NumberOfUsers");
		
		System.out.println(String.valueOf(matchingAccounts));

		if (matchingAccounts == 1) {
			SessionKeyCreate.CreateSessionKeyByUsername(userReqBody.getUserName(), myConnector);
			List<SessionKey> rs = ResultSetConvertor.convertToSessionKeyList(SessionKeyRead.readLatestSessionKeyByUsername(userReqBody.getUserName(), myConnector));
			myConnector.closeJDBCConnection();
			return new ResponseEntity<List<SessionKey>>(rs, HttpStatus.OK);
		}

		myConnector.closeJDBCConnection();
		return new ResponseEntity<List<SessionKey>>(HttpStatus.UNAUTHORIZED);
	}

	/*
	 * requestBody
	 *
	 * userName : String
	 */
	@PostMapping("/users/logout")
	public ResponseEntity<List<String>> logoutUser(@RequestBody User userReqBody) throws Exception {

		MySQLConnector myConnector = new MySQLConnector();
		myConnector.makeJDBCConnection();

		int resp = SessionKeyDelete.deleteSessionKeyByUsername(userReqBody.getUserName(), myConnector);

		this.cleanSessionKeys();

		myConnector.closeJDBCConnection();

		if(resp==0) {
			return new ResponseEntity<List<String>>(HttpStatus.OK);
		}

		return new ResponseEntity<List<String>>(HttpStatus.INTERNAL_SERVER_ERROR);

	}
	
	/*
	 * userReqBody
	 * 
	 * userPassword : String
	 * userName : String
	 * roleType : String (valid values: "admin" or "user")
	 */
	@PostMapping("/users/create")
	public ResponseEntity<List<String>> createUser(@RequestBody User userReqBody, @RequestHeader("SessionKey") String sessionKey,
			@RequestHeader("UserName") String userName) throws Exception {
		if(!isValid(sessionKey, userName)) {
			return new ResponseEntity<List<String>>(HttpStatus.UNAUTHORIZED);
		}
		//check to see if there already exists a user with that username
		//if already exists: return an error.
		//else: send user information to UserDAL for account creation

		//get mysql connection
		MySQLConnector myConnector = new MySQLConnector();
		myConnector.makeJDBCConnection();

		// check if user exists or not
		ResultSet resultSet = UserRead.readUsersByUsernameAndHPass(userReqBody.getUserName(), userReqBody.getUserHashedPassword(), myConnector);
		List<User> userList = ResultSetConvertor.convertToUserList(resultSet);
		boolean empty = userList.isEmpty();
		int result;
		if (empty == true){  // user doesn't exist
			result = UserCreate.createUser(userReqBody.getUserName(), PasswordHasher.generateStrongPasswordHash(userReqBody.getUserPassword()), userReqBody.getRoleType(), myConnector);
		}
		else{
			String error = "User already exists";
			HttpStatus httpStatus = Arrays.stream(HttpStatus.values())
			        .filter(status -> status.name().equals(error))
			        .findAny()
			        .orElse(HttpStatus.INTERNAL_SERVER_ERROR);
			return new ResponseEntity<List<String>>(httpStatus);
		}

		// close mysql connection
		myConnector.closeJDBCConnection();
		
		if (result == 0) {
			return new ResponseEntity<List<String>>(HttpStatus.OK);
		} else {
			return new ResponseEntity<List<String>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	/*
	 * accReqBody
	 * 
	 * accountHolder : String (the owner of this account)
	 * accountType : String (valid values: "checkings" "savings"
	 * accountBalance : float
	 */
	@PostMapping("/accounts/create")
	public ResponseEntity<List<String>> createAccount(@RequestBody Account accReqBody, @RequestHeader("SessionKey") String sessionKey,
			@RequestHeader("UserName") String userName) throws Exception {
		if(!isValid(sessionKey, userName)) {
			return new ResponseEntity<List<String>>(HttpStatus.UNAUTHORIZED);
		}
		//if accountBalance > 0, also send information to TransactionDAL 
		//to create an initial account balance transaction

		//get mysql connection
		MySQLConnector myConnector = new MySQLConnector();
		myConnector.makeJDBCConnection();

		// call DB
		int result = AccountCreate.createAccount(accReqBody.getAccountHolder(), accReqBody.getAccountType(), accReqBody.getAccountBalance(), myConnector);

		//close mysql connection
		myConnector.closeJDBCConnection();
		
		if (result == 0) {
			return new ResponseEntity<List<String>>(HttpStatus.OK);
		} else {
			return new ResponseEntity<List<String>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/*
	 * accReqBody
	 * 
	 * accountNumber : String
	 */
	@PostMapping("/accounts/delete")
	public ResponseEntity<List<String>> deleteAccount(@RequestBody Account accReqBody, @RequestHeader("SessionKey") String sessionKey,
			@RequestHeader("UserName") String userName) throws Exception {
		if(!isValid(sessionKey, userName)) {
			return new ResponseEntity<List<String>>(HttpStatus.UNAUTHORIZED);
		}
		//get mysql connection
		MySQLConnector myConnector = new MySQLConnector();
		myConnector.makeJDBCConnection();

		// call DB
		AccountDelete.deleteAccountByAccountNumber(accReqBody.getAccountNumber(), myConnector);

		//close mysql connection
		myConnector.closeJDBCConnection();
		
		return new ResponseEntity<List<String>>(HttpStatus.OK);
	}
	
	/*
	 * userReqBody
	 * 
	 * userName : String (the owner of the accounts)
	 * 
	 * returns: list of accounts owned by the given User
	 */
	@PostMapping("/accounts/read")
	public ResponseEntity<List<Account>> readAccount(@RequestBody User userReqBody, @RequestHeader("SessionKey") String sessionKey,
			@RequestHeader("UserName") String userName) throws Exception {
		if(!isValid(sessionKey, userName)) {
			return new ResponseEntity<List<Account>>(HttpStatus.UNAUTHORIZED);
		}

		// get mysql connection
		MySQLConnector myConnector = new MySQLConnector();
		myConnector.makeJDBCConnection();

		// call DB
		ResultSet resultSet = AccountRead.readAccountByUsername(userReqBody.getUserName(), myConnector);

		List<Account> accountList = ResultSetConvertor.convertToAccountList(resultSet);

		// close mysql connection
		myConnector.closeJDBCConnection();

		return ResponseEntity.status(HttpStatus.OK).body(accountList);
	}
	
	/*
	 * transactionBody
	 * 
	 * targetAccountNumber : String
	 * originAccountNumber : String
	 * transactionAmount : float
	 * recurring : boolean
	 * transactionPeriodInHours : int
	 * isInternal : Boolean
	 */
	@PostMapping("transactions/create")
	public ResponseEntity<List<String>> createTransaction(@RequestBody Transaction transactionBody, @RequestHeader("SessionKey") String sessionKey,
			@RequestHeader("UserName") String userName) throws Exception {
		if(!isValid(sessionKey, userName)) {
			return new ResponseEntity<List<String>>(HttpStatus.UNAUTHORIZED);
		}
		// get mysql connection
		MySQLConnector myConnector = new MySQLConnector();
		myConnector.makeJDBCConnection();

		int result = TransactionCreate.createTransaction(transactionBody.getOriginAccountNumber(), transactionBody.getTargetAccountNumber(),
				 transactionBody.getTransactionAmount(), transactionBody.isRecurring(),
				 transactionBody.getTransactionPeriodInDays(), transactionBody.getDescription(), myConnector);

		if(transactionBody.isInternal()) {
			List<Account> accountListOrigin = ResultSetConvertor.convertToAccountList(AccountRead.readAccountByAccountNumber(transactionBody.getOriginAccountNumber(), myConnector));
			List<Account> accountListTarget = ResultSetConvertor.convertToAccountList(AccountRead.readAccountByAccountNumber(transactionBody.getTargetAccountNumber(), myConnector));
			if(accountListOrigin.get(0)==null && accountListTarget.get(0)==null) {
				String error = "Origin Account and Target Account do not exist";
				HttpStatus httpStatus = Arrays.stream(HttpStatus.values())
				        .filter(status -> status.name().equals(error))
				        .findAny()
				        .orElse(HttpStatus.INTERNAL_SERVER_ERROR);
				return new ResponseEntity<List<String>>(httpStatus);
			} else if (accountListOrigin.get(0)==null) {
				String error = "Origin Account does not exist";
				HttpStatus httpStatus = Arrays.stream(HttpStatus.values())
				        .filter(status -> status.name().equals(error))
				        .findAny()
				        .orElse(HttpStatus.INTERNAL_SERVER_ERROR);
				return new ResponseEntity<List<String>>(httpStatus);
			} else if (accountListTarget.get(0)==null) {
				String error = "Target Account does not exist";
				HttpStatus httpStatus = Arrays.stream(HttpStatus.values())
				        .filter(status -> status.name().equals(error))
				        .findAny()
				        .orElse(HttpStatus.INTERNAL_SERVER_ERROR);
				return new ResponseEntity<List<String>>(httpStatus);
			} else {
				System.out.println("Spot1");
				AccountUpdate.updateAccountBalance(transactionBody.getOriginAccountNumber(), accountListOrigin.get(0).getAccountBalance()-transactionBody.getTransactionAmount(), myConnector);
				AccountUpdate.updateAccountBalance(transactionBody.getTargetAccountNumber(), accountListTarget.get(0).getAccountBalance()+transactionBody.getTransactionAmount(), myConnector);
			}
		} else {
			List<Account> accountListOrigin = ResultSetConvertor.convertToAccountList(AccountRead.readAccountByAccountNumber(transactionBody.getOriginAccountNumber(), myConnector));
			if(accountListOrigin.get(0)==null) {
				String error = "Origin Account does not exist";
				HttpStatus httpStatus = Arrays.stream(HttpStatus.values())
				        .filter(status -> status.name().equals(error))
				        .findAny()
				        .orElse(HttpStatus.INTERNAL_SERVER_ERROR);
				return new ResponseEntity<List<String>>(httpStatus);
			} else {
				System.out.println("Spot2");
				AccountUpdate.updateAccountBalance(transactionBody.getOriginAccountNumber(), accountListOrigin.get(0).getAccountBalance()-transactionBody.getTransactionAmount(), myConnector);
			}
		}
		
		// close mysql connection
		myConnector.closeJDBCConnection();
		
		if (result == 0) {
			return new ResponseEntity<List<String>>(HttpStatus.OK);
		} else {
			return new ResponseEntity<List<String>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/*
	 * accReqBody
	 * 
	 * originAccountNumber : String
	 * 
	 * returns: List of all Transactions involving that account. (All debits and credits)
	 */
	@PostMapping("transactions/read")
	public ResponseEntity<List<Transaction>> retrieveTransactions(@RequestBody Transaction transactionBody, @RequestHeader("SessionKey") String sessionKey,
			@RequestHeader("UserName") String userName) throws Exception {
		if(!isValid(sessionKey, userName)) {
			return new ResponseEntity<List<Transaction>>(HttpStatus.UNAUTHORIZED);
		}
		// get mysql connection
		MySQLConnector myConnector = new MySQLConnector();
		myConnector.makeJDBCConnection();

		ResultSet resultSet = TransactionRead.readTransactionByAccountNumber(transactionBody.getOriginAccountNumber(), myConnector);
		List<Transaction> transactionList = ResultSetConvertor.convertToTransactionList(resultSet);

		// close mysql connection
		myConnector.closeJDBCConnection();
		return ResponseEntity.status(HttpStatus.OK)
				.body(transactionList);
	}
	
	
}
