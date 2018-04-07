package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.House;
import model.User;
import model.UserDAO;

public class CreateHouseController implements Controller {

	@Override
	public ModelAndView HandleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		String username = user.getUsername();
		String houseHandle = request.getParameter("houseHandle");
		String url = "houseRegister.jsp";
		
		boolean updateSuccess = UserDAO.getInstanceOf().updateHouseHandle( username, houseHandle);
		if(updateSuccess) {
			House house = new House(houseHandle);
			session.setAttribute("house", house);
			url = "dashboard.jsp";
		}
		
		
		return new ModelAndView(url, false);
	}

}
