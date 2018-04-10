package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.User;
import model.UserDAO;

public class RegisterUserController implements Controller {
	@Override
	public ModelAndView HandleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User user = null;
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String cellNumString = request.getParameter("cellNum");
		String emergencyNumString = request.getParameter("emergencyNum");
		String email = request.getParameter("email");
		
		// Created by Joseph. Check all fields to make sure that they are not blank before 
		// checking the database to see if this set of inputs is valid
		boolean empty = false;
		
		if(username.equals("") || username.equals(null)) {
			empty = true;
		} else if(password.equals("") || password.equals(null)) {
			empty = true;
		} else if(firstName.equals("") || firstName.equals(null)) {
			empty = true;
		} else if(lastName.equals("") || lastName.equals(null)) {
			empty = true;
		} else if(cellNumString.equals("") || cellNumString.equals(null)) {
			empty = true;
		} else if(emergencyNumString.equals("") || emergencyNumString.equals(null)) {
			empty = true;
		} else if(email.equals("") || email.equals(null)) {
			empty = true;
		}
		
		if(empty) {
			return new ModelAndView("userRegister.jsp?registerFail=true", true);
		}
		
		//System.out.println(request.getParameter("cellNum"));
		
		int cellNumber = Integer.parseInt(request.getParameter("cellNum"));
		int emergencyNumber = Integer.parseInt(request.getParameter("emergencyNum"));

		boolean regResult = UserDAO.getInstanceOf().registerUser(username, password, firstName, lastName, cellNumber, emergencyNumber, email);
		
		if (!regResult) {
			return new ModelAndView("userRegister.jsp?registerFail=true", true);
		}
		
		user = new User(username, password, firstName, lastName, cellNumber, emergencyNumber, email);
		HttpSession session = request.getSession();
		session.setAttribute("user", user);
		return new ModelAndView("houseRegister.jsp", true);
	}
}
