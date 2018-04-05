package model;

public class User {
	private String username;
	private String password;//we have to do something about this
	private String firstName;
	private String lastName;
	private String address;
	private int cellNum;
	private int emergencyNum;
	private String email;
	private String venmoHandle;
	private int groupId;
	private String checkedInStatus;
	private int socialCalendarId;
	private int classCalendarId;
	private int groupCalendarId;
	
	
	
	//constructor using all fields (taking data from Signup)
	public User(String username, String password, String firstName, String lastName, String address, int cellNum, int emergencyNum, String email, String venmoHandle) {
		super();
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.cellNum = cellNum;
		this.emergencyNum = emergencyNum;
		this.email = email;
		this.venmoHandle = venmoHandle;
	}

	//constructor using required fields (taking data from Login)
	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
	public User(String username, String password, String firstName, String lastName, int cellNumber, int emergencyNumber) {
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.cellNum = cellNumber;
		this.emergencyNum = emergencyNumber;
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
	public String getFirstName() {
		return firstName;
	}
	public void setFirstname(String fname) {
		this.firstName = fname;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lname) {
		this.lastName = lname;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getCellNum() {
		return cellNum;
	}
	public void setCellNum(int cellNum) {
		this.cellNum = cellNum;
	}
	public int getEmergencyNum() {
		return emergencyNum;
	}
	public void setEmergencyNum(int emergencyNum) {
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
	public int getSocialCalendarId() {
		return socialCalendarId;
	}
	public void setSocialCalendarId(int scid) {
		this.socialCalendarId = scid;
	}
	public int getClassCalendarId() {
		return classCalendarId;
	}
	public void setClassCalendarId(int ccid) {
		this.classCalendarId = ccid;
	}
	public int getGroupCalendarId() {
		return groupCalendarId;
	}
	public void setGroupCalendarId(int gcid) {
		this.groupCalendarId = gcid;
	}
}
