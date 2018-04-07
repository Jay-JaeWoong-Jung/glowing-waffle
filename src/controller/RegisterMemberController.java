package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.User;
import model.UserDAO;

public class RegisterMemberController implements Controller {
	@Override
	public ModelAndView HandleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User user = null;
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		int cellNumber = Integer.parseInt(request.getParameter("cellNum"));
		int emergencyNumber = Integer.parseInt(request.getParameter("emergencyNum"));
		String email = request.getParameter("email");
		
		boolean regResult = UserDAO.getInstanceOf().registerMember(username, password, firstName, lastName, cellNumber, emergencyNumber, email);
		
		if (!regResult) {
			return new ModelAndView("RegisterMember.jsp?registerFail=true", true);
		}
		user = new User(username, password, firstName, lastName, cellNumber, emergencyNumber, email);
		HttpSession session = request.getSession();
		session.setAttribute("user", user);
		return new ModelAndView("HouseRegister.jsp", true);
	}
}
