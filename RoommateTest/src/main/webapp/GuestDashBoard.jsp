
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.House" %>
<%@ page import="model.User" %>
<%@ page import="model.UserDAO" %>
<!DOCTYPE html>
<html>
<head>
    <style>
        input[type=text], select {
            width: 100%;
            padding: 12px 20px;
            margin: 8px 0;
            display: inline-block;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
        }
        input[type=submit] {
            width: 100%;
            background-color: #4CAF50;
            color: white;
            padding: 14px 20px;
            margin: 8px 0;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        input[type=submit]:hover {
            background-color: #45a049;
        }
    </style>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
    <title>GuestUserDashBoard</title>
    <%
        String houseId = (String)session.getAttribute("houseId");
        ArrayList<User> allCurrentUsers = UserDAO.getInstanceOf().getUsers(houseId);
        String groupCalendarId = (String) session.getAttribute("calendarId");

    %>
</head>
<%
    groupCalendarId = (String)session.getAttribute("calendarId");
    houseId = (String)session.getAttribute("houseId");
    allCurrentUsers = UserDAO.getInstanceOf().getUsers(houseId);
%>
<body onload="connectToServer()">

<div class="w3-container w3-Light-blue" style="position:relative">
    <a class="w3-btn w3-circle w3-xlarge w3-right w3-white" style="position:absolute;top:126px;right:42px;"><i>+</i></a>
    <h1 class="w3-jumbo w3-text-blue" style="text-shadow:1px 1px 0 #444">DashBoard</h1>
</div>

<div class="w3-pale-blue w3-animate-zoom" style="padding:20px 50px;background-image:url('pic_boat_portrait.jpg');
background-size:cover;">

    <div class="w3-section w3-row-padding">

        <div class="w3-twothird">
            <div class="w3-card-4">
                <div class="w3-display-container">
                    <div class="w3-display-bottomleft w3-container w3-xlarge w3-text-black"><p></p></div>
                </div>
                <div class="w3-container w3-white">

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

                    <div id ="showCalendar" class="responsive-iframe-container big-container">

                    </div>

                </div>
            </div>
        </div>
        <div class="w3-third w3-container w3-center">
            <div class="w3-card-4">
                <div class="w3-container w3-white">
                </div>
                <div class="w3-container w3-Light-white">

                </div>
            </div>
            <div class="w3-card-4 w3-section">
                <div class="w3-container w3-white">

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

                </div>
                <div class="w3-container">

                </div>
            </div>
        </div>
    </div>


    <div class="w3-section w3-container">
        <div class="w3-card-4">
            <div class="w3-container w3-padding-16 w3-black w3-xxlarge">
                <p> Making Life simpler </p>
            </div>
        </div>
    </div>

</div>

<footer class="w3-container">
    <p>Powered by
        <a href="Homepage.jsp">Roommates</a></p>
</footer>

</body>
</html>