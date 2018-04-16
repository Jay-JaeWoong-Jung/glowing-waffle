package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.House;
import model.User;
import model.UserDAO;
import servlet.CalendarGet;
import servlet.Client;

public class JoinHouserController implements Controller, Runnable{
    Boolean completed = false;
    ArrayList<String> calendarList;
    String userIdentifier = null;
    public void run(){
        try {
            CalendarGet calendarGet = new CalendarGet();
            calendarList = new ArrayList<String>();
            HashMap<String,String> calendarId = calendarGet.getSummaryToId();
            for(Map.Entry<String, String> entry: calendarId.entrySet()){
                System.out.println(entry.getKey());
                calendarList.add(entry.getKey());
            }
            Thread.sleep(2000);
            Client client = new Client(userIdentifier, "localhost", 6789);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

	@Override
	public ModelAndView HandleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		UserDAO userDAO = UserDAO.getInstanceOf();
		HttpSession session = request.getSession();
		String houseHandle = request.getParameter("houseHandle");
		User user = (User)session.getAttribute("user");
		String username = user.getUsername();
		userIdentifier = username;
        CalendarGet calendarGet = new CalendarGet();
        calendarList = new ArrayList<String>();
        HashMap<String,String> calendarId = calendarGet.getSummaryToId();
        for(Map.Entry<String, String> entry: calendarId.entrySet()){
            calendarList.add(entry.getKey());
            System.out.println(entry.getKey());
        }
		
		boolean joinHouseHandle = userDAO.joinHouseHandle(username, houseHandle);
		if (!joinHouseHandle) {
			return new ModelAndView("houseRegister.jsp?joinfail=true", true);
		}
        if(!completed){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        session.setAttribute("calendarList", calendarList);
		ArrayList<User> listOfUsers = userDAO.getUsers(houseHandle);
		House house = new House(houseHandle);
		house.setRoommates(listOfUsers);
		session.setAttribute("house", house);
		return new ModelAndView("Dashboard.jsp");
	}

}
