package controller;

// import java.io.PrintWriter;

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
		
		// PrintWriter out = response.getWriter();
		String displayError = "";
		
		if(username.equals("") || username.equals(null)) {
			empty = true;
			displayError += "username\n";
		} else if(password.equals("") || password.equals(null)) {
			empty = true;
			displayError += "password\n";
		} else if(firstName.equals("") || firstName.equals(null)) {
			empty = true;
			displayError += "first name\n";
		} else if(lastName.equals("") || lastName.equals(null)) {
			empty = true;
			displayError += "last name\n";
		} else if(cellNumString.equals("") || cellNumString.equals(null)) {
			empty = true;
			displayError += "cell phone number\n";
		} else if(emergencyNumString.equals("") || emergencyNumString.equals(null)) {
			empty = true;
			displayError += "emergency phone number\n";
		} else if(email.equals("") || email.equals(null)) {
			empty = true;
			displayError += "email\n";
		}
		
		if(empty) {
			// out.println(displayError);
			
			System.out.println(displayError);
			
			return new ModelAndView("userRegister.jsp?registerFail=true", true);
		}
		
		// check to make sure both cell phone numbers are 10 digits
		// else check to make sure all 10 character values are digits
		if(cellNumString.length() != 10 || emergencyNumString.length() != 10) {
			displayError += "cell phone numbers length are not 10\n";
			// out.println(displayError);
			
			System.out.println(displayError);
			
			return new ModelAndView("userRegister.jsp?registerFail=true", true);
		} else {
			for(int i = 0; i < 10; i++) {
				if(cellNumString.charAt(i) < 48 || cellNumString.charAt(i) > 57) {
					displayError += "cell phone numbers contain non digits\n";
					// out.println(displayError);
					
					System.out.println(displayError);
					
					return new ModelAndView("userRegister.jsp?registerFail=true", true);
				} else if(emergencyNumString.charAt(i) < 48 || emergencyNumString.charAt(i) > 57) {
					displayError += "emergency cell phone numbers contain non digits\n";
					// out.println(displayError);
					
					System.out.println(displayError);
					
					return new ModelAndView("userRegister.jsp?registerFail=true", true);	
				}
			}
		}
		
		//System.out.println(request.getParameter("cellNum"));
		
		// Does not need a try catch for integer conversion because they are numbers for sure
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
