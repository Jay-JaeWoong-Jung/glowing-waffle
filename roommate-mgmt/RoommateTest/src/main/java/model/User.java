package model;

public class User {
	private String username;
	private String password;
	private String firstName;
	private String lastname;
	private long cellNumber;
	private long emergencyNumber;
	private String email;
	private String address;
	private String houseHandle;
	private String permanentAddress;
	private String venmoHandle;
	private String classCalendarId;
	private String socialCalendarId;
	private String groupCalendarId;
	private String checkedInStatus;
	
	//Constructor using fields required at sign up
	public User (String username, String password, String firstName, String lastName, long cellNumber, long emergencyNumber, String email) {
		super();
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastname = lastName;
		this.cellNumber = cellNumber;
		this.emergencyNumber = emergencyNumber;
		this.email = email;
		this.checkedInStatus = "inroom";
	}

	//constructor using required fields (taking data from Login)
	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
	//constructor used when creating user that already exists
	public User (String username, String password, String firstName, String lastName, long cellNumber, long emergencyNumber, String email, String houseHandle, String checkedInStatus)
	{
		super();
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastname = lastName;
		this.cellNumber = cellNumber;
		this.emergencyNumber = emergencyNumber;
		this.email = email;
		this.houseHandle = houseHandle;
		this.checkedInStatus = checkedInStatus;
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

	public String getLastname() {
		return lastname;
	}

	public long getCellNumber() {
		return cellNumber;
	}

	public long getEmergencyNumber() {
		return emergencyNumber;
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

	public String getHouseHandle() {
		return houseHandle;
	}

	public String getClassCalendarId() {
		return classCalendarId;
	}

	public String getSocialCalendarId() {
		return socialCalendarId;
	}

	public String getGroupCalendarId() {
		return groupCalendarId;
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
