package model;

import java.util.ArrayList;

public class House {
	private String houseHandle;
	private ArrayList<User> roommates;
	
	public House (String houseHandle) {
		this.houseHandle = houseHandle;
		roommates = new ArrayList<User>();
	}
	
	public String getHouseHandle() {
		return houseHandle;
	}
	
	public void setHouseHandle(String houseHandle) {
		this.houseHandle = houseHandle;
	}
	
	public ArrayList<User> getRooommates() {
		return roommates;
	}
	
	public void setRoommates(ArrayList<User> roommates) {
		this.roommates = roommates;
	}
	
	public void addRoommate(User roommate) {
		roommates.add(roommate);
	}
	
}
