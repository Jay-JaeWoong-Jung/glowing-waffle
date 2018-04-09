package controller;

import servlet.Client;
import model.House;
import model.User;
import model.UserDAO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RegisterHouseController implements Controller {

	@Override
	public ModelAndView HandleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		String username = user.getUsername();
		String houseHandle = request.getParameter("houseHandle");
		String url = "houseRegister.jsp";
		
		boolean createHouseHandle = UserDAO.getInstanceOf().createHouseHandle(username, houseHandle);
		if(createHouseHandle) {
			House house = new House(houseHandle);
			house.addRoommate(user);
			//Client player = new Client( "Username","localhost", 6789);
			session.setAttribute("house", house);
			url = "Dashboard.jsp";
		}
		
		return new ModelAndView(url, false);
	}

}
