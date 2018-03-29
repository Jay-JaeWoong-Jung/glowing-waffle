package model;

public class User {
	private String username;
	private String password;//we have to do something about this
	private String fullname;
	private String address;
	private String cellNum;
	private String EmergencyNum;
	private String Email;
	private String venmoHandle;
	
	
	
	//constructor using required fields (taking data from Signup)
	public User(String username, String password, String fullname, String email) {
		super();
		this.username = username;
		this.password = password;
		this.fullname = fullname;
		Email = email;
	}

	//constructor taking all the fields (taking data from DB)
	public User(String username, String password, String fullname, String address, String cellNum, String emergencyNum,
			String email, String venmoHandle) {
		super();
		this.username = username;
		this.password = password;
		this.fullname = fullname;
		this.address = address;
		this.cellNum = cellNum;
		EmergencyNum = emergencyNum;
		Email = email;
		this.venmoHandle = venmoHandle;
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
		return EmergencyNum;
	}
	public void setEmergencyNum(String emergencyNum) {
		EmergencyNum = emergencyNum;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public String getVenmoHandle() {
		return venmoHandle;
	}
	public void setVenmoHandle(String venmoHandle) {
		this.venmoHandle = venmoHandle;
	}
	

}
