package team1spring2021cmpe202.SpringBackEnd;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Transaction {
	private String originAccountNumber;
	private String targetAccountNumber;
	private float transactionAmount;
	private LocalDateTime initialTransactionDate;
	private boolean recurring;
	private int transactionPeriodInHours;
	private String Description;
	private boolean internal;

	public boolean isRecurring() {
		return recurring;
	}
	public void setRecurring(boolean recurring) {
		this.recurring = recurring;
	}
	public String getTargetAccountNumber() {
		return targetAccountNumber;
	}
	public void setTargetAccountNumber(String targetAccountID) {
		this.targetAccountNumber = targetAccountID;
	}
	public float getTransactionAmount() {
		return transactionAmount;
	}
	public void setTransactionAmount(float transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	public int getTransactionPeriodInDays() {
		return transactionPeriodInHours;
	}
	public void setTransactionPeriodInDays(int transactionPeriodInDays) {
		this.transactionPeriodInHours = transactionPeriodInDays;
	}
	public LocalDateTime getInitialTransactionDate() {
		return initialTransactionDate;
	}
	public void setInitialTransactionDate(LocalDateTime initialTransactionDate) {
		LocalDateTime ldt = initialTransactionDate;
		this.initialTransactionDate = ldt;
	}
	public String getOriginAccountNumber() {
		return originAccountNumber;
	}
	public void setOriginAccountNumber(String originAccountID) {
		this.originAccountNumber = originAccountID;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		this.Description = description;
	}
	public boolean isInternal() {
		return internal;
	}
	public void setInternal(boolean isInternal) {
		this.internal = isInternal;
	}
}
