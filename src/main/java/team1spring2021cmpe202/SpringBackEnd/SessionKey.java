package team1spring2021cmpe202.SpringBackEnd;

import java.time.LocalDateTime;


public class SessionKey {
	private String sessionKey;
	private String userName;
	private LocalDateTime sessionKeyIssuedTimestamp;
	
	public String getSessionKey() {
		return sessionKey;
	}
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public LocalDateTime getSessionKeyIssuedTimestamp() {
		return sessionKeyIssuedTimestamp;
	}
	public void setSessionKeyIssuedTimestamp(LocalDateTime sessionKeyIssuedTimestamp) {
		this.sessionKeyIssuedTimestamp = sessionKeyIssuedTimestamp;
	}
}
