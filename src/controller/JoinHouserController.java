package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.UserDAO;

public class JoinHouserController implements Controller{

	@Override
	public ModelAndView HandleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String houseHandle = request.getParameter("houseHandle");
		UserDAO userDAO = UserDAO.getInstanceOf();
		boolean existingHouse = !userDAO.isHouseHandleAvailable(houseHandle);
		if (!existingHouse) {
			return new ModelAndView("houseRegister.jsp?joinfail=true", true);
		}
		return null;
	}

}
