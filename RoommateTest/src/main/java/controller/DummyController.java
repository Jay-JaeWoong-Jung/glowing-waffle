package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.UserDAO;

public class DummyController implements Controller{
	public ModelAndView HandleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return new ModelAndView("guestdashboard.jsp", true);
	}
}
