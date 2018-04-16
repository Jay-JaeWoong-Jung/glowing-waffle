package constants;

public interface Sql {
	public static final String CHECK_USERNAME = "SELECT username FROM User WHERE username=?"; 
	public static final String CHECK_EMAIL = "SELECT email FROM User WHERE email=?";
	public static final String LOGIN = "SELECT first_name, last_name, cell_number, emerg_number, email, status FROM User WHERE username=? AND password=?";
	public static final String REGISTER = "INSERT INTO User(username, password, first_name, last_name, cell_number, emerg_number, email, status) VALUES(?,?,?,?,?,?,?,?)";
	public static final String UPDATE_HOUSEHANDLE = "UPDATE User SET house_handle=? WHERE username=?";
	public static final String GET_USER = "SELECT password, first_name, last_name, cell_number, emerg_number, email, house_handle, status FROM User WHERE username=?";
	public static final String CHECK_HOUSEHANDLE = "SELECT house_handle FROM User WHERE house_handle=?";
	public static final String GET_USERS = "SELECT username, password, first_name, last_name, cell_number, emerg_number, email, house_handle, status FROM User WHERE house_handle=?";
	public static final String UPDATE_CLASSCALENDAR = "UPDATE User SET class_calendar_id=? WHERE username=?";
	public static final String UPDATE_SOCIALCALENDAR = "UPDATE User SET social_calendar_id=? WHERE username=?";
	public static final String UPDATE_GROUPCALENDAR = "UPDATE User SET group_Calendar_id=? WHERE username=?";
	public static final String GET_CLASSCALENDAR = "SELECT class_calendar_id FROM User WHERE username=?";
	public static final String GET_SOCIALCALENDAR = "SELECT social_calendar_id FROM User WHERE username=?";
	public static final String GET_GROUPCALEDNAR = "SELECT group_calendar_id FROM User WHERE username=?";
	public static final String GET_HOUSEHANDLE = "SELECT house_handle FROM User WHERE username=?";
	public static final String UPDATE_STATUS = "UPDATE User SET status=? WHERE username=?";
}
