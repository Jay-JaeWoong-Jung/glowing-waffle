package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.Controller;
import controller.HandlerMapping;
import controller.ModelAndView;

/**
 * Servlet implementation class DispatcherServlet
 */
@WebServlet("/DispatcherServlet")
public class DispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   /**
    * @see HttpServlet#HttpServlet()
    */
   public DispatcherServlet() {
       super();
       // TODO Auto-generated constructor stub
   }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		String cmd = request.getParameter("command");
		System.out.println("dispatcher command:"+cmd);
		
		Controller controller=HandlerMapping.getInstance().createController(cmd);
		
		try{
			ModelAndView mv =controller.HandleRequest(request, response);
			System.out.println("redirecting to view from dispatcher controller");
			if(mv.isRedirect()) response.sendRedirect(mv.getPath());
			else request.getRequestDispatcher(mv.getPath()).forward(request, response);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
