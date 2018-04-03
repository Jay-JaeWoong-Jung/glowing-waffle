package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.UserDAO;

public class UsernameCheckController {
	public ModelAndView HandleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String username = request.getParameter("username");
		boolean doesExist = UserDAO.getInstanceOf().isUsernaemAvailable(username);
		if (!doesExist) {
			HttpSession session = request.getSession();
			session.setAttribute("doesExist", doesExist);
		}
		return new ModelAndView("register.jsp", true);
	}
}
