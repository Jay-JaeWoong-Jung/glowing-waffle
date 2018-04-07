
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomePageController implements Controller {
    @Override
    public ModelAndView HandleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		String username = request.getParameter("username");
//		String password = request.getParameter("password");
//		String fullname = request.getParameter("fullname");
//		String address = request.getParameter("address");
//		String cellNum = request.getParameter("cellNum");
//		String emergencyNum = request.getParameter("emergencyNum");
//		String email = request.getParameter("email");
//		String venmoHandle = request.getParameter("venmoHandle");
//
//		User user = new User(username, password, fullname, address, cellNum, emergencyNum, email, venmoHandle);
//		boolean regResult = UserDAO.getInstance().registerMember(user);
//		request.setAttribute("regResult", regResult);
//
//		if (!regResult) {
//			return new ModelAndView("RegisterMember.jsp?registerFail=true", true);
//		}
        //String eventDescription = request.getParameter();
        //String event
        return new ModelAndView("HouseRegister.jsp", true);
    }
}