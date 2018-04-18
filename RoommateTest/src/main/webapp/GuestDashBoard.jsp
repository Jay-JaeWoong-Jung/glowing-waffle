<%@ page import="model.UserDAO" %>
<%@ page import="model.User" %>
<%@ page import="java.util.ArrayList" %><%--
  Created by IntelliJ IDEA.
  User: angelahe
  Date: 4/17/18
  Time: 5:09 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>GuestUserDashBoard</title>
</head>
<%
    String groupCalendarId = (String)session.getAttribute("calendarId");
    String houseId = (String)session.getAttribute("houseId");
    ArrayList<User> allCurrentUsers = UserDAO.getInstanceOf().getUsers(houseId);
%>
<body>

<div class = "exist" id = "calendarforlogin">
    <%if(groupCalendarId != null){
        String constantbefore = "<iframe src=\"https://calendar.google.com/calendar/embed?src=";
        String constantafter = "&ctz=America%2FLos_Angeles\" style=\"border: 0\" width=\"800\" height=\"600\" frameborder=\"0\" scrolling=\"no\"></iframe>";
        String overall = constantbefore + groupCalendarId + constantafter;
    %>
    <%=overall%>
    <%
        }
    %>
</div>


<div id="myevent">
    You are currently in the household: <%=houseId%> <br/>
    Now their status is <br/>
    <%
        for(int i = 0; i<allCurrentUsers.size(); i++){
            String currentUser = allCurrentUsers.get(i).getUsername();
            String currentStatus = allCurrentUsers.get(i).getCheckedInStatus();
    %>
    <%=currentUser%> : <%=currentStatus%>   <br/>
    <%
        }
    %>
</div>

</body>
</html>
