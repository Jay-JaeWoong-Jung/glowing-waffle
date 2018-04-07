package controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.House;
import model.User;
import model.UserDAO;

public class JoinHouserController implements Controller{

	@Override
	public ModelAndView HandleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		UserDAO userDAO = UserDAO.getInstanceOf();
		HttpSession session = request.getSession();
		String houseHandle = request.getParameter("houseHandle");
		User user = (User)session.getAttribute("user");
		String username = user.getUsername();
		
		boolean joinHouseHandle = userDAO.joinHouseHandle(username, houseHandle);
		if (!joinHouseHandle) {
			return new ModelAndView("houseRegister.jsp?joinfail=true", true);
		}
		ArrayList<User> listOfUsers = userDAO.getUsers(houseHandle);
		House house = new House(houseHandle);
		house.setRoommates(listOfUsers);
		session.setAttribute("house", house);
		return new ModelAndView("dashboard.jsp");
	}

}
