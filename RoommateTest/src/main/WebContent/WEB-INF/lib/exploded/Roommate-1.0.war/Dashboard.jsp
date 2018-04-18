<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="servlet.Client" %>
<%@ page import="model.House" %>
<%@ page import="model.User" %>
<!DOCTYPE html>
<html>
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <title>Chat servlet.Client</title>
    <script>
        var socket;
        function connectToServer() {
            socket = new WebSocket('ws://127.0.0.1:4444');
            socket.onopen = function(event) {
                document.getElementById("myevent").innerHTML += "Connected!";
            }
            socket.onmessage = function(event) {
                var variable = event.data;
                var constantbefore = "<iframe src=\"https://calendar.google.com/calendar/embed?src=";
                var constantafter = "&ctz=America%2FLos_Angeles\" style=\"border: 0\" width=\"800\" height=\"600\" frameborder=\"0\" scrolling=\"no\"><\/iframe>";
                console.log(variable);
                document.getElementById("showCalendar").innerHTML = constantbefore + variable + constantafter;
                //document.getElementById("myevent").innerHTML += event.data + "<br />";
            }
            socket.onclose = function(event) {
                document.getElementById("myevent").innerHTML += "Disconnected!";
            }
        }
        function sendMessage(formname) {
            if(formname === "eventform"){
                console.log("eventform");
                var startingTime = document.getElementById("startingTime").value;
                var endingTime = document.getElementById("endingTime").value;
                console.log(startingTime);
                console.log(endingTime);
                // get the calendarId
                var radios = document.getElementsByName("calendarId");
                var radiobutton;
                for (var i = 0, length = radios.length; i < length; i++) {
                    console.log("Inside of the check button");
                    if (radios[i].checked) {
                        radiobutton = (radios[i].value);
                        console.log(radiobutton);
                        break;
                    }
                }

                socket.send("eventform," + document.eventform.message.value + "," + startingTime +
                "," + endingTime + "," + radiobutton);
            }
            else if(formname === "toggle")
            {
                console.log("toggle");
                // get the calendarId
                var status = document.getElementsByName("radios");
                var statusbutton;
                for (var i = 0, length = status.length; i < length; i++) {
                    console.log("Inside of the check button");
                    if (status[i].checked) {
                        statusbutton = (status[i].value);
                        console.log(statusbutton);
                        break;
                    }
                }

                socket.send("toggle ," + statusbutton);
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
                console.log("groupCalendar");
                socket.send("calendar ," + formname + "," + document.getElementById("summary").value);
            }
            return false;
        }
    </script>
    <%
        User user = (User)session.getAttribute(("user"));
        ArrayList<String> theListOfMap = (ArrayList<String>) session.getAttribute("calendarList");
        House houseHandle = (House)session.getAttribute("house");
        String houseId = "empty";
        if(houseHandle != null) {
            houseId = houseHandle.getHouseHandle();
        }
    %>
</head>
<body onload="connectToServer()">

<div class = "choose">
    Choose your class calendar:
    <form name = "classForm" onsubmit = "return sendMessage('class')">
        <% for(int i = 0; i<theListOfMap.size(); i++){
            %>
        <input type = "radio" name = "calendarId" value = <%=theListOfMap.get(i)%>>
        <%=theListOfMap.get(i)%>
        <br/>
        <%
        }%>
        <input type = "hidden" id = "userId" name = "userId" value = <%=user.getUsername()%>/>
        <input type = "hidden" id = "houseId" name = "houseId" value = <%=houseId%>/>
        <input type="submit" name="submit" value="choose calendar" />
    </form>
</div>


<div class = "choose">
    Choose your social calendar:
    <form name = "socialForm" onsubmit = "return sendMessage('social')">
        <% for(int i = 0; i<theListOfMap.size(); i++){
        %>
        <input type = "radio" name = "calendarId" value = <%=theListOfMap.get(i)%>>
        <%=theListOfMap.get(i)%>
        <br/>
        <%
            }%>
        <%=houseId%>
        <input type = "hidden" id = "userId" name = "userId" value = <%=user.getUsername()%>/>
        <input type = "hidden" id = "houseId" name = "houseId" value = <%=houseId%>/>
        <input type="submit" name="submit" value="choose calendar" />
    </form>
</div>

<div class = "choose">
    Now you would add a group calendar, <br/>
    How would you name your group calendar? <br/>
    <form name = "groupForm" onsubmit = "return sendMessage('group')">
        <input type = "hidden" id = "userId" name = "userId" value = <%=user.getUsername()%>/>
        <input type = "text" id = "summary" name = "calendarSummary" >
        <input type="submit" name="submit" value="choose calendar" />
    </form>
</div>

<div id ="showCalendar" class="responsive-iframe-container big-container">

</div>

<h3> Add a new event </h3>
<form name="eventform" onsubmit="return sendMessage('eventform');">
    Summary: <input type="text" name="message" value = "Title"/><br />
    Starting Time:  <input id="startingTime" type="datetime-local" name="partydate" value="2017-06-01T08:30">
    Ending Time: <input id="endingTime" type="datetime-local" name="partydate" value="2017-06-01T08:30">
    <input type = "radio" name = "calendarId" value = "Class Calendar">
    Class Calendar
    <br/>
    <input type = "radio" name = "calendarId" value = "Social Calendar">
    Social Calendar
    <br/>
    <input type = "radio" name = "calendarId" value = "Group Calendar">
    Group Calendar
    <br/>
    <input type="submit" name="submit" value="Send Message" />
</form>

<br />
<div id="myevent"></div>

<form name = "toggle" onsubmit = "return sendMessage('toggle');">
    <input type = "radio" name = "radios" value = "inroom">
    In Room
    <br/>
    <input type = "radio" name = "radios" value = "notinroom">
    Not In Room
    <br/>
    <input type = "radio" name = "radios" value = "donotdisturb"> Do not disturb
    <br/>
    <input type="submit" name="submit" value="Updated Status" />
</form>


</body>
</html>