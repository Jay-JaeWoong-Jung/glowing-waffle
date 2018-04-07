<%--
  Created by IntelliJ IDEA.
  User: angelahe
  Date: 3/8/18
  Time: 10:26 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Calendar</title>
    <style>
        @media (max-width: 550px) {
            .big-container {
                display: none;
            }
        }
        @media (min-width: 550px) {
            .small-container {
                display: none;
            }
        }
        /* Responsive iFrame */
        .responsive-iframe-container {
            position: relative;
            padding-bottom: 56.25%;
            padding-top: 30px;
            height: 0;
            overflow: hidden;
        }
        .responsive-iframe-container iframe,
        .vresponsive-iframe-container object,
        .vresponsive-iframe-container embed {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
        }
    </style>

    <script>
        var socket;
        function connectToServer() {
            socket = new WebSocket("ws://localhost:8080/CSCI201-WebSockets/ws");
            socket.onopen = function(event) {
                document.getElementById("mychat").innerHTML += "Connected!";
            }
            socket.onmessage = function(event) {
                document.getElementById("mychat").innerHTML += event.data + "<br />";
            }
            socket.onclose = function(event) {
                document.getElementById("mychat").innerHTML += "Disconnected!";
            }
        }
        function sendMessage() {
            socket.send("Miller: " + document.chatform.message.value);
            return false;
        }
    </script>
</head>


<body onload = "connectToServer()">
    <div class="responsive-iframe-container big-container">
        <iframe src="https://calendar.google.com/calendar/embed?src=jiayuehe%40usc.edu&ctz=America%2FLos_Angeles" style="border: 0" width="800" height="600" frameborder="0" scrolling="no"></iframe>
    </div>

    <form name = "addEvent" onsubmit = "return sendMessage();">
        <input type = "text" name = "description" value = "Type Here! " /><br />
        <input type = "submit" name = "submit" value = "Send Message" />
    </form>

    <div id = "myCalendar"> </div>

    <form name = "toggle" onsubmit = "return sendMessage()">
        <div>
            <input type = "radio" name = "radios" value = "inroom">
            In Room
            <br/>
            <input type = "radio" name = "radios" value = "notinroom">
            Not In Room
            <br/>
            <input type = "radio" name = "radios" value = "donotdisturb"> Do not disturb
            <br/>
        </div>
        <input type="hidden" name="command" value = "toggle"/>
    </form>

</body>
</html>

