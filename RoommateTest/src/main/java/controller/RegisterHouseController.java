package controller;

import com.google.api.services.calendar.model.CalendarListEntry;
import servlet.CalendarGet;
import servlet.Client;
import model.House;
import model.User;
import model.UserDAO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterHouseController implements Controller {
	Boolean completed = false;
	ArrayList<String> calendarList;
	String userIdentifier = null;


	@Override
	public ModelAndView HandleRequest(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		String username = user.getUsername();
		userIdentifier = username;
		String houseHandle = request.getParameter("houseHandle");
		String url = "houseRegister.jsp";
		
		boolean createHouseHandle = UserDAO.getInstanceOf().createHouseHandle(username, houseHandle);
		if(createHouseHandle) {
			House house = new House(houseHandle);
			house.addRoommate(user);
			if(!completed){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			session.setAttribute("houseid", house.getHouseHandle());
			url = "Dashboard.jsp";
		}
		
		return new ModelAndView(url, false);
	}
}
