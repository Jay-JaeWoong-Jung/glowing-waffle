package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginController implements Controller {
	
	@Override
	public ModelAndView HandleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		boolean loginResult = MemberDAO.getInstance().loginCheck(username, password);
		if (loginResult) {
			MemberVo vo = MemberDAO.getInstance().login(username, password);
			HttpSession session = request.getSession();
			session.setAttribute("mvo", vo);
			return new ModelAndView("newMain.jsp", true);
		}
		else {
			return new ModelAndView("login.jsp?loginfail=true", true);
		}
		
	}
}
