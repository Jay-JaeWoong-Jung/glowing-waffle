package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.User;
import model.UserDAO;
import servlet.CalendarGet;
import servlet.Client;

public class LoginController implements Controller,Runnable {
    Boolean completed = false;
    ArrayList<String> calendarList;
    String userIdentifier = null;
    public void run(){
        try {
            CalendarGet calendarGet = new CalendarGet();
            calendarList = new ArrayList<String>();
            HashMap<String,String> calendarId = calendarGet.getSummaryToId();
            for(Map.Entry<String, String> entry: calendarId.entrySet()){
                calendarList.add(entry.getKey());
                System.out.println(entry.getKey());
            }
            Thread.sleep(2000);
            Client client = new Client(userIdentifier, "localhost", 6789);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

	@Override
	public ModelAndView HandleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        CalendarGet calendarGet = new CalendarGet();
        calendarList = new ArrayList<String>();
        HashMap<String,String> calendarId = calendarGet.getSummaryToId();
        for(Map.Entry<String, String> entry: calendarId.entrySet()){
            calendarList.add(entry.getKey());
            System.out.println(entry.getKey());
        }
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		userIdentifier = username;
		PrintWriter out = response.getWriter();
		
		User user = UserDAO.getInstanceOf().login(username, password);
		String houseId = UserDAO.getInstanceOf().getHouseHandle(username);;
		if (user != null) {
			HttpSession session = request.getSession();
			session.setAttribute("user", user);
			String classcalendarId = UserDAO.getInstanceOf().getClassCalendar(username);
			String socialcalendarId = UserDAO.getInstanceOf().getSocialCalendar(username);
			String groupcalendarId = UserDAO.getInstanceOf().getGroupCalendar(username);
			//session.setAttribute("classcalendarId", classcalendarId);
			//session.setAttribute("socialcalendarId", socialcalendarId);
			//session.setAttribute("groupcalendarId",groupcalendarId);
            if(!completed){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            session.setAttribute("calendarList", calendarList);
            session.setAttribute("houseid", houseId);
			//if(user.getGroupCalendarId() == null)session.setAttribute("show", false);
			return new ModelAndView("Dashboard.jsp", true);
		}
		else {
			return new ModelAndView("login.jsp?loginfail=true", true);
		}
		
	}
}
