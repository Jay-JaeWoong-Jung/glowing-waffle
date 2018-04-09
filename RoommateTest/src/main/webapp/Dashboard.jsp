
<!DOCTYPE html>
<html>
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
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
            else
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