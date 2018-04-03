package model;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import constants.Sql;
;

public class UserDAO {
	private static UserDAO dao=new UserDAO();
	private DataSource ds;
	
	private static Connection conn = null;
	private static ResultSet rs = null;
	private static PreparedStatement ps = null;
	
	private UserDAO(){
		try{
			//DataSource를 찾는다...
			Context ic = new InitialContext();
			ds=(DataSource)ic.lookup("jdbc:mysql://localhost/csci201FINAL?user=root&password=BlackSheepWall&useSSL=false");
			System.out.println("DataSource connected......");
		}catch(NamingException e){
			e.printStackTrace();
		} 
	
	}
	public void closeAll(PreparedStatement pstmt,Connection con) throws SQLException{
		if(pstmt!=null)
			pstmt.close();
		if(con!=null)
			con.close();
	}
	public void closeAll(ResultSet rs,PreparedStatement pstmt,Connection con) throws SQLException{
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
	
	
	public boolean userNameCheck(String username) throws SQLException {
		boolean result = true;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(Sql.CHECK_USERNAME);
			pstmt.setString(1, username);
			rs = pstmt.executeQuery();
			if (!rs.next()) {
				result = false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll(rs, pstmt, conn);
		}
		return result;
	}// idCheck
	
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
}
