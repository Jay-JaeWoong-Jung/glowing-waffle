
import java.util.ArrayList;

public class House {
    private String houseName;
    private int groupId;
    private ArrayList<User> roommates;

    public House (String houseName, int groupId) {
        this.houseName = houseName;
        this.groupId = groupId;
    }

    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
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