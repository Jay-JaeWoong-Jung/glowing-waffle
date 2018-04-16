package model;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import constants.Credential;
import constants.Sql;

public class UserDAO {
	private static UserDAO dao=new UserDAO();
	private  MysqlDataSource ds;
	
	private static Connection conn = null;
	private static ResultSet rs = null;
	private static PreparedStatement ps = null;
	
	private UserDAO(){
		
				ds = new MysqlDataSource();
			    ds.setUser(Credential.USER);
			    ds.setPassword(Credential.PASSWORD);
			    ds.setUseSSL(false);
			    ds.setDatabaseName(Credential.DATABASE);

			    
			System.out.println("DataSource connected......");
	
	
	}
	public void closeAll(PreparedStatement pstmt, Connection con) throws SQLException{
		if(pstmt!=null)
			pstmt.close();
		if(con!=null)
			con.close();
	}
	public void closeAll(ResultSet rs,PreparedStatement pstmt, Connection con) throws SQLException{
		if(rs!=null)
			rs.close();
		closeAll(pstmt, con);
	}
	
	public static UserDAO getInstanceOf() {
		return dao;
	}
	
	private Connection getConnection() throws SQLException{
		return ds.getConnection();
	}
	
	public static void close(){
		try{
			if (rs!=null){
				rs.close();
				rs = null;
			}
			if(conn != null){
				conn.close();
				conn = null;
			}
			if(ps != null ){
				ps = null;
			}
		}catch(SQLException sqle){
			System.out.println("connection close error");
			sqle.printStackTrace();
		}
	}
	
	public User login(String username, String password) {
		User user = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			String sql = Sql.LOGIN;
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				user = new User(username, password, rs.getString(1), rs.getString(2), rs.getLong(3), rs.getLong(4), rs.getString(5));

			}
			closeAll(rs, pstmt, conn);
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	public boolean registerUser(String username, String password, String firstName, String lastName, 
			long cellNumber, long emergencyNumber, String email) {
		Connection conn = null;
		PreparedStatement pstmt = null;	
		try {
			boolean usernameAvailable = isUsernameAvailable(username);
			boolean emailAvailable = isEmailAvailable(email);
			if (!usernameAvailable) {
				return false;
			}
			if (!emailAvailable) {
				return false;
			}
			conn = getConnection();
			String insertIntoHouse = Sql.REGISTER;
			pstmt = conn.prepareStatement(insertIntoHouse);
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			pstmt.setString(3, firstName);
			pstmt.setString(4, lastName);
			pstmt.setLong(5, cellNumber);
			pstmt.setLong(6, emergencyNumber);
			pstmt.setString(7, email);
			pstmt.setString(8, "inroom");
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
        } finally {
        	try {
				closeAll(rs, pstmt, conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		return true;
	}

	public String getClassCalendar(String username) throws SQLException{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String calendarId = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(Sql.GET_CLASSCALENDAR);
			pstmt.setString(1, username);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				calendarId = rs.getString(1);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll(rs, pstmt, conn);
		}
		return calendarId;
	}

	public String getHouseHandle(String username) throws SQLException{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String houseId = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(Sql.GET_HOUSEHANDLE);
			pstmt.setString(1, username);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				houseId = rs.getString(1);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll(rs, pstmt, conn);
		}
		return houseId;
	}

	public String getSocialCalendar(String username) throws SQLException{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String calendarId = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(Sql.GET_SOCIALCALENDAR);
			pstmt.setString(1, username);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				calendarId = rs.getString(1);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll(rs, pstmt, conn);
		}
		return calendarId;
	}


	public String getGroupCalendar(String username) throws SQLException{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String calendarId = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(Sql.GET_GROUPCALEDNAR);
			pstmt.setString(1, username);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				calendarId = rs.getString(1);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll(rs, pstmt, conn);
		}
		return calendarId;
	}

	public boolean isUsernameAvailable(String username) throws SQLException {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(Sql.CHECK_USERNAME);
			pstmt.setString(1, username);
			rs = pstmt.executeQuery();
			if (!rs.next()) {
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll(rs, pstmt, conn);
		}
		return result;
	}// idCheck
	
	public boolean isEmailAvailable(String email) throws SQLException {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(Sql.CHECK_EMAIL);
			pstmt.setString(1, email);
			rs = pstmt.executeQuery();
			if (!rs.next()) {
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll(rs, pstmt, conn);
		}
		return result;
	}
	
	public boolean doesHouseHandleExist(String houseHandle) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(Sql.CHECK_HOUSEHANDLE);
			pstmt.setString(1, houseHandle);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				closeAll(rs, pstmt, conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public User getUser(String username) {
		User user = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			String sql = Sql.GET_USER;
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, username);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				System.out.println(rs.getString(1));
				user = new User(username, rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5), rs.getString(6), rs.getString(7) ,rs.getString(8));
			}
			closeAll(rs, pstmt, conn);
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}
	
	public boolean joinHouseHandle(String username, String houseHandle) {
		Connection conn = null;
		PreparedStatement pstmt = null;	
		try {
			boolean doesHouseHandleExist = doesHouseHandleExist(houseHandle);
			if (!doesHouseHandleExist) {
				return false;
			}
			conn = getConnection();
			String createHouse = Sql.UPDATE_HOUSEHANDLE;
			pstmt = conn.prepareStatement(createHouse);
			pstmt.setString(1, houseHandle);
			pstmt.setString(2, username);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
        } finally {
        	try {
				closeAll(rs, pstmt, conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		return true;
	}

	
	public boolean createHouseHandle(String username, String houseHandle) {
		Connection conn = null;
		PreparedStatement pstmt = null;	
		try {
			boolean doesHouseHandleExist = doesHouseHandleExist(houseHandle);
			if (doesHouseHandleExist) {
				return false;
			}
			conn = getConnection();
			String createHouse = Sql.UPDATE_HOUSEHANDLE;
			pstmt = conn.prepareStatement(createHouse);
			pstmt.setString(1, houseHandle);
			pstmt.setString(2, username);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
        } finally {
        	try {
				closeAll(rs, pstmt, conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		return true;
	}

	public void addClassCalendar(String username, String classCalendarId){
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = getConnection();
			String updatedCalendar = Sql.UPDATE_CLASSCALENDAR;
			pstmt = conn.prepareStatement(updatedCalendar);
			pstmt.setString(1, classCalendarId);
			pstmt.setString(2, username);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				closeAll(rs, pstmt, conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void addSocialCalendar(String username, String socialCalendarId){
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = getConnection();
			String updatedCalendar = Sql.UPDATE_SOCIALCALENDAR;
			pstmt = conn.prepareStatement(updatedCalendar);
			pstmt.setString(1, socialCalendarId);
			pstmt.setString(2, username);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				closeAll(rs, pstmt, conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void addGroupCalendar(String username, String groupCalendarId){
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = getConnection();
			String updatedCalendar = Sql.UPDATE_GROUPCALENDAR;
			pstmt = conn.prepareStatement(updatedCalendar);
			pstmt.setString(1, groupCalendarId);
			pstmt.setString(2, username);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				closeAll(rs, pstmt, conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public ArrayList<User> getUsers(String houseHandle) {
		User user = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<User> listOfUsers = new ArrayList<User>();
		try {
			conn = getConnection();
			String sql = Sql.GET_USERS;
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, houseHandle);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				user = new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getLong(5), rs.getLong(6), rs.getString(7), rs.getString(8), rs.getString(9));
				listOfUsers.add(user);
			}
			closeAll(rs, pstmt, conn);
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return listOfUsers;
	}

	public void updateStatus(String username, String status){
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = getConnection();
			String updatedCalendar = Sql.UPDATE_STATUS;
			pstmt = conn.prepareStatement(updatedCalendar);
			pstmt.setString(1, status);
			pstmt.setString(2, username);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				closeAll(rs, pstmt, conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//Test here DAO methods here
	public static void main(String args[] ) {
		UserDAO dao = UserDAO.getInstanceOf();
		dao.addClassCalendar("aaa","ttt");
		dao.addGroupCalendar("aaa","???");
		dao.addSocialCalendar("aaa", "whatthefuck?");

		String classCalendar = null;
		String socialCalendar = null;
		String groupCalendar = null;
		try {
			classCalendar = dao.getClassCalendar("aaa");
			socialCalendar = dao.getSocialCalendar("aaa");
			groupCalendar = dao.getGroupCalendar("aaa");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("I get the class calendar Id as " + classCalendar);
		System.out.println("I get the social calendar Id as " + socialCalendar);
		System.out.println("I get the group calendar Id as " + groupCalendar);

	}
}
