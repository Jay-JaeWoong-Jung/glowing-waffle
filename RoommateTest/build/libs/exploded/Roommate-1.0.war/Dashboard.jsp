
<!DOCTYPE html>
<html>
<head>
    <title>Chat Client</title>
    <script>
        var socket;
        function connectToServer() {
            socket = new WebSocket('ws://127.0.0.1:4444');
            socket.onopen = function(event) {
                document.getElementById("myevent").innerHTML += "Connected!";
            }
            socket.onmessage = function(event) {
                document.getElementById("myevent").innerHTML += event.data + "<br />";
            }
            socket.onclose = function(event) {
                document.getElementById("myevent").innerHTML += "Disconnected!";
            }
        }
        function sendMessage(formname) {
            var str = formname;

            socket.send("Jeffrey Miller " + document.getElementById("message").value);
            return false;
        }
    </script>
</head>
<body onload="connectToServer()">

<div class="responsive-iframe-container big-container">
    <iframe src="https://calendar.google.com/calendar/embed?src=jiayuehe%40usc.edu&ctz=America%2FLos_Angeles" style="border: 0" width="800" height="600" frameborder="0" scrolling="no"></iframe>
</div>

<h3> Add a new event </h3>
<form name="eventform" onsubmit="return sendMessage('eventform');">
    <input type="text" name="message" value = "Title"/><br />
    <input type="date" name="dateOfEvent"><br/>
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

<form name = "toggle" onsubmit = "return sendMessage();">
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