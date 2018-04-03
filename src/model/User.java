package model;

public class User {
	private String username;
	private String password;//we have to do something about this
	private String fullname;
	private String address;
	private String cellNum;
	private String emergencyNum;
	private String email;
	private String venmoHandle;
	private int groupId;
	private String checkedInStatus;
	
	
	
	//constructor using all fields (taking data from Signup)
	public User(String username, String password, String fullname, String address, String cellNum, String emergencyNum, String email, String venmoHandle, int groupId) {
		super();
		this.username = username;
		this.password = password;
		this.fullname = fullname;
		this.address = address;
		this.cellNum = cellNum;
		this.emergencyNum = emergencyNum;
		this.email = email;
		this.venmoHandle = venmoHandle;
		this.groupId = groupId;
	}

	//constructor using required fields (taking data from Login)
	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}


//setters and getters
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCellNum() {
		return cellNum;
	}
	public void setCellNum(String cellNum) {
		this.cellNum = cellNum;
	}
	public String getEmergencyNum() {
		return emergencyNum;
	}
	public void setEmergencyNum(String emergencyNum) {
		this.emergencyNum = emergencyNum;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getVenmoHandle() {
		return venmoHandle;
	}
	public void setVenmoHandle(String venmoHandle) {
		this.venmoHandle = venmoHandle;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public String getCheckedInStatus() {
		return checkedInStatus;
	}
	public void setCheckedInStatus(String checkedInStatus) {
		this.checkedInStatus = checkedInStatus;
	}
}
