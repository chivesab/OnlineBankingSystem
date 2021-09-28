package team1spring2021cmpe202.SpringBackEnd;

public class Account {
	private String accountId;
	private String accountNumber;
	private String accountHolder;
	private String accountType;
	private float accountBalance;

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountHolder() {
		return accountHolder;
	}
	public void setAccountHolder(String accountHolder) {
		this.accountHolder = accountHolder;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public float getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(float accountBalance) {
		this.accountBalance = accountBalance;
	}

}