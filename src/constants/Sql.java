package constants;

public interface Sql {
	public static final String CHECK_USERNAME = "SELECT username FROM User WHERE username=?"; 
	public static final String LOGIN = "SELECT first_name, last_name, cell_number, emerg_number FROM User WHERE username=? AND password=?";
}
