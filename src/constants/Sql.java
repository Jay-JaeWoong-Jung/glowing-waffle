package constants;

public interface Sql {
	public static final String CHECK_USERNAME = "SELECT username FROM User WHERE username=?"; 
	public static final String CHECK_EMAIL = "SELECT email FROM User WHERE email=?";
	public static final String LOGIN = "SELECT first_name, last_name, cell_number, emerg_number, email FROM User WHERE username=? AND password=?";
	public static final String REGISTER = "INSERT INTO User(username, password, first_name, last_name, cell_number, emerg_number, email) VALUES(?,?,?,?,?,?,?)";
	public static final String CREATE_HOUSE = "UPDATE User SET house_handle=? WHERE username=?";
	public static final String GET_USER = "SELECT password, first_name, last_name, cell_number, emerg_number, email, house_handle FROM User WHERE username=?";
	public static final String CHECK_HOUSEHANDLE = "SELECT house_handle FROM User WHERE house_handle=?";
	public static final String GET_USERS = "SELECT username, password, first_name, last_name, cell_number, emerg_number, email, house_handle FROM User WHERE house_handle=?";
}
