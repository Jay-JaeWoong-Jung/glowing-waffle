package model;

public class User {
	private String username;
	private String password;
	private String firstName;
	private String lastname;
	private int cellNumber;
	private int emergencyNumber;
	private String email;
	private String address;
	private String houseHandle;
	private String permanentAddress;
	private String venmoHandle;
	private int classCalendarId;
	private int socialCalendarId;
	private int groupCalendarId;
	private String checkedInStatus;
	
	//Constructor using fields required at sign up
	public User (String username, String password, String firstName, String lastName, int cellNumber, int emergencyNumber, String email) {
		super();
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastname = lastName;
		this.cellNumber = cellNumber;
		this.emergencyNumber = emergencyNumber;
		this.email = email;
	}

	//constructor using required fields (taking data from Login)
	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
	//constructor used when creating user that already exists
	public User (String username, String password, String firstName, String lastName, int cellNumber, int emergencyNumber, String email, String houseHandle) {
		super();
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastname = lastName;
		this.cellNumber = cellNumber;
		this.emergencyNumber = emergencyNumber;
		this.email = email;
		this.houseHandle = houseHandle;
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
		return lastname;
	}
	public void setLastName(String lname) {
		this.lastname = lname;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getCellNumber() {
		return cellNumber;
	}
	public void setCellNumber(int cellNum) {
		this.cellNumber = cellNum;
	}
	public int getEmergencyNumber() {
		return emergencyNumber;
	}
	public void setEmergencyNumber(int emergencyNum) {
		this.emergencyNumber = emergencyNum;
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
	public String getHouseHandle() {
		return houseHandle;
	}
	public void setHouseHandle(String houseHandle) {
		this.houseHandle = houseHandle;
	}
	public String getPermanentAddress() {
		return permanentAddress;
	}
	public void setPermanentAddress(String addi) {
		this.permanentAddress = addi;
	}
}
