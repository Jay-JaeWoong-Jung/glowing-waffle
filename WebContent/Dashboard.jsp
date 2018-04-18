<%@ page import="java.util.ArrayList" %>
<%@ page import="model.House" %>
<%@ page import="model.User" %>
<%@ page import="model.UserDAO" %>
<!DOCTYPE html>
<html>
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
    <title>Chat servlet.Client</title>
    <script>
        var socket;
        var calendarId;
        function connectToServer() {
            socket = new WebSocket('ws://127.0.0.1:4444');
            socket.onopen = function(event) {
                document.getElementById("myevent").innerHTML += "Connected!";
            }
            socket.onmessage = function(event) {
                console.log("Received a message");
                console.log("Current calendar id is " + calendarId);
                var div = document.getElementById("calendarforlogin");
                div.style.display = 'none';
                if(calendarId == null) {
                    calendarId = event.data;
                }
                var content = event.data;
                content = content.split(",");
                var first= content.shift();
                if(first === "toggle"){
                    console.log(first);
                    document.getElementById("myevent").innerHTML = content;
                }
                var constantbefore = "<iframe src=\"https://calendar.google.com/calendar/embed?src=";
                var constantafter = "&ctz=America%2FLos_Angeles\" style=\"border: 0\" width=\"800\" height=\"600\" frameborder=\"0\" scrolling=\"no\"><\/iframe>";
                console.log(calendarId);
                document.getElementById("showCalendar").innerHTML = constantbefore + calendarId + constantafter;
            }
            socket.onclose = function(event) {
                document.getElementById("myevent").innerHTML += "Disconnected!";
            }
        }
        function sendMessage(formname) {
            if(formname === "eventform"){
                var startingTime = document.getElementById("startingTime").value;
                var endingTime = document.getElementById("endingTime").value;
                var checkExist = document.getElementById("groupCalendarIdCheck").value;
                if( checkExist != null){
                    calendarId = checkExist;
                }
                // get the calendarId
                var radios = document.getElementsByName("calendarClass");
                var radiobutton;
                for (var i = 0, length = radios.length; i < length; i++) {
                    console.log("Inside of the check button");
                    if (radios[i].checked) {
                        radiobutton = (radios[i].value);
                        console.log(radiobutton);
                        break;
                    }
                }
                // get houseid
                var groupId = document.getElementById("houseId").value;
                socket.send("eventform," + checkExist + "," + document.eventform.message.value + "," + startingTime +
                "," + endingTime + "," + radiobutton + "," + groupId);
            }
            else if(formname === "toggle")
            {
                console.log("toggle");
                // get the calendarId
                var status = document.getElementsByName("radios");
                var statusbutton;
                var userName = document.getElementById("userId").value;
                for (var i = 0;  i < status.length; i++) {
                    console.log("Inside of the check button");
                    if (status[i].checked) {
                        statusbutton = (status[i].value);
                        console.log(statusbutton);
                        break;
                    }
                }
                var calendarExist = document.getElementById("existed").value;
                console.log(calendarExist);
                if( calendarExist != null){
                    calendarId = calendarExist;
                }
                // get houseid
                var groupId = document.getElementById("houseId").value;
                socket.send("status," + userName + "," + statusbutton + "," + groupId);
            }
            else if(formname === "class" || formname === "social"){
                console.log("calendar");
                var calendar = document.getElementsByName("calendarId");
                var calendarButton;
                var groupId = document.getElementById("houseId").value;
                var userName = document.getElementById("userId").value;
                console.log(groupId);
                for (var i = 0, length = calendar.length; i < length; i++) {
                    console.log("Inside of the check button");
                    if (calendar[i].checked) {
                        calendarButton = (calendar[i].value);
                        console.log(calendarButton);
                        break;
                    }
                }
                socket.send("calendar ," + formname + "," + calendarButton + "," + groupId + "," + userName);
            }
            else{
                var userName = document.getElementById("userId").value;
                console.log("groupCalendar");
                socket.send("calendar ," + formname + "," + document.getElementById("summary").value + "," + userName);
            }
            return false;
        }
    </script>
    <%
        User user = (User)session.getAttribute(("user"));
        ArrayList<String> theListOfMap = (ArrayList<String>) session.getAttribute("calendarList");
        House houseHandle = (House)session.getAttribute("house");
        String tempHouseId = (String)session.getAttribute("houseid");
        String houseId = "empty";
        if(tempHouseId != null){
            houseId = tempHouseId;
        }
        if(houseHandle != null) {
            houseId = houseHandle.getHouseHandle();
        }
        ArrayList<User> allCurrentUsers = UserDAO.getInstanceOf().getUsers(houseId);
        String groupCalendarId = (String) session.getAttribute("groupcalendarId");
        session.invalidate();
        System.out.println("The user's group id is " + groupCalendarId);
    %>
</head>
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
        
<div class = "choose">
    <% if(null == groupCalendarId){
        %>
    Now you would add a group calendar, <br/>
    How would you name your group calendar? <br/>
    <form name = "groupForm" onsubmit = "return sendMessage('group')">
        <input type = "hidden" id = "userId" name = "userId" value = <%=user.getUsername()%>/>
        <input type = "text" id = "summary" name = "calendarSummary" >
        <input type="submit" name="submit" value="choose calendar" />
    </form>
    <%
        }
    %>
</div>

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
      
      <h3> Add a new event </h3>
	<form name="eventform" onsubmit="return sendMessage('eventform');">
    Summary: <input type="text" name="message" value = "Title"/><br />
    Starting Time:  <input id="startingTime" type="datetime-local" name="partydate" value="2017-06-01T08:30">
    Ending Time: <input id="endingTime" type="datetime-local" name="partydate" value="2017-06-01T08:30">
    <br />
    <input type = "radio" name = "calendarClass" value = "Class Calendar">
    Class Calendar
    <br/>
    <input type = "radio" name = "calendarClass" value = "Social Calendar">
    Social Calendar
    <br/>
    <input type = "radio" name = "calendarClass" value = "Group Calendar">
    Group Calendar
    <br/>
    <input type = "hidden" id = "houseId" name = "houseId" value = <%=houseId%>/>
    <input type = "hidden" id = "groupCalendarIdCheck" value = <%=groupCalendarId%>>
    <input type="submit" name="submit" value="Send Message" />
</form>
      
     
      </div>
      <div class="w3-container w3-Light-white">
      <p>Some Line</p>
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
You can change your status here:
<form name = "toggle" onsubmit = "return sendMessage('toggle');">
    <input type = "radio" name = "radios" value = "inroom">
    In Room
    <br/>
    <input type = "radio" name = "radios" value = "notinroom">
    Not In Room
    <br/>
    <input type = "radio" name = "radios" value = "donotdisturb"> Do not disturb
    <br/>
    <input type = "hidden" id = "houseId" name = "houseId" value = <%=houseId%>/>
    <input type = "hidden" id = "userId" name = "userId" value = <%=user.getUsername()%>/>
    <input type = "hidden" id = "existed" value = <%=groupCalendarId%>>
    <input type="submit" name="submit" value="Updated Status" />
</form>
      <p class="w3-large">Let me know if we want content here</p>
      </div>
      <div class="w3-container">
      <p>3 Hours Ago</p>
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