package controller;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.User;
import model.UserDAO;

public class LoginController implements Controller {
	
	@Override
	public ModelAndView HandleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		PrintWriter out = response.getWriter();
		
		User user = UserDAO.getInstanceOf().login(username, password);
		if (user != null) {
			HttpSession session = request.getSession();
			session.setAttribute("user", user);
			return new ModelAndView("newMain.jsp", true);
		}
		else {
			return new ModelAndView("login.jsp?loginfail=true", true);
		}
		
	}
}
