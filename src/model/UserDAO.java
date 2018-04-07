package model;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import constants.Credential;
import constants.Sql;
;

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
				user = new User(username, password, rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getString(5));
			}
			closeAll(rs, pstmt, conn);
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}
	
	public boolean registerUser(String username, String password, String firstName, String lastName, 
			int cellNumber, int emergencyNumber, String email) {
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
			pstmt.setInt(5, cellNumber);
			pstmt.setInt(6, emergencyNumber);
			pstmt.setString(7, email);
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
	
//	
//	public static void increment(String usr, String page){
////		System.out.println("getting username from increment: "+ usr+" "+ page);
//		connect();
//		try{
//			ps = conn.prepareStatement("SELECT userID FROM User WHERE username =?");
//			ps.setString(1, usr);
//			rs = ps.executeQuery();
//			int userID = -1;
//			if(rs.next()){
//				userID = rs.getInt("userID");
//			}
//			rs.close();
//			ps = conn.prepareStatement("SELECT pageID FROM Page WHERE name= ?");
//			ps.setString(1, page);
//			rs = ps.executeQuery();
//			int pageID = -1;
//			
//			if(rs.next()){
//				pageID = rs.getInt("pageID");
//			}
//			rs.close();
//			ps = conn.prepareStatement("SELECT count FROM PageVisited WHERE userID = '"+userID+"' AND pageID='"+pageID+"'");
//			rs = ps.executeQuery();
//			if(rs.equals(null)){
//				System.out.println("nothing returned");
//			}
//			int count = -1;
//			if (rs.next()){
//				count = rs.getInt("count");
//			}
//			rs.close();
//			if(count >0){
//				ps = conn.prepareStatement("UPDATE pageVisited pv SET pv.count = "+(count+1)+" WHERE userID = "+userID+" AND pageID = "+pageID);
//			}else{
//				ps = conn.prepareStatement("INSERT INTO pageVisited (userID, pageID, count) VALUES ('"+userID+"', '"+pageID+"', 1) ");
//			}
//			ps.executeUpdate();
//		}catch(SQLException sqle){
//			System.out.println("SQLException in function \"increment\"");
//			sqle.printStackTrace();
//		}
//		finally{
//			close();
//		}
//	}
//	public static ArrayList<ArrayList<String> >getData(){
//		ArrayList<ArrayList<String>>  stat = new ArrayList<ArrayList<String>>();
//		connect();
//		try {
//			conn = DriverManager.getConnection("jdbc:mysql://localhost/lab10?user=root&password=BlackSheepWall&useSSL=false");
//			ps = conn.prepareStatement("SELECT u.username, p.name, pv.count FROM User u, Page p, PageVisited pv "
//					+ "WHERE u.userID = pv.userID AND p.pageID = pv.pageID ORDER BY u.userID, p.pageID");
//			rs = ps.executeQuery();
//			while(rs.next()){
//				ArrayList<String> row = new ArrayList<String>();
//				row.add(rs.getString("u.username"));
//				row.add(rs.getString("p.name"));
//				row.add(rs.getString("pv.count"));
//				stat.add(row);
//			}
//		}catch(SQLException sqle){
//			System.out.println("SQLException in function \" getData\": ");
//			sqle.printStackTrace();
//		}finally{
//			close();
//		}
//		return stat;
//	}
	
	
	//Test here DAO methods here
//	public static void main(String args[] ) {
//		UserDAO dao = UserDAO.getInstanceOf();
//		User jayBitch = null;
//		boolean work = dao.registerUser("jschaider", "pokemon", "jacob", "schaider", 708708, 0000, "jschaider@mgail.com");
//		if (work) {
//			System.out.println("success");
//		}
//
//	}
}
