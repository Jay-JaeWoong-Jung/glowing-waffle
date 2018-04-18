package controller;

import model.House;
import model.User;
import model.UserDAO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

public class GuestUserController implements  Controller{
        public ModelAndView HandleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
            HttpSession session = request.getSession();
            String houseHandle = request.getParameter("houseHandle");
            String url = "Dummy.jsp";

            boolean existHouse = UserDAO.getInstanceOf().doesHouseHandleExist(houseHandle);
            if(existHouse) {
                ArrayList<String> groupCalendar = UserDAO.getInstanceOf().getGroupCalendars(houseHandle);
                String individualCalendar = groupCalendar.get(0);
                session.setAttribute("calendarId", individualCalendar);
                session.setAttribute("houseId",houseHandle);
                url = "GuestDashBoard.jsp";
            }

            return new ModelAndView(url, false);
        }

}
