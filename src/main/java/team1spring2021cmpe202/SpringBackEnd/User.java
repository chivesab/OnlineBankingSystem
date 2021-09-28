package team1spring2021cmpe202.SpringBackEnd;

public class User {
	private String userName;
	private String userPassword;
	private String userHashedPassword;
	private String roleType;
	
	public User(String userName, String userHashedPassword, String roleType) {
		this.userName = userName;
		this.userHashedPassword = userHashedPassword;
		this.roleType = roleType;
	};
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public String getRoleType() {
		return roleType;
	}
	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}
	public String getUserHashedPassword() {
		return userHashedPassword;
	}
	public void setUserHashedPassword(String userHashedPassword) {
		this.userHashedPassword = userHashedPassword;
	}
	
}
