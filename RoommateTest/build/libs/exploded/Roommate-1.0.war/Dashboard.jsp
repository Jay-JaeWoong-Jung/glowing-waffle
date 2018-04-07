
<!DOCTYPE html>
<html>
<head>
    <title>Chat Client</title>
    <script>
        var socket;
        function connectToServer() {
            socket = new WebSocket('ws://127.0.0.1:4444');
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
<body onload="connectToServer()">

<div class="responsive-iframe-container big-container">
    <iframe src="https://calendar.google.com/calendar/embed?src=jiayuehe%40usc.edu&ctz=America%2FLos_Angeles" style="border: 0" width="800" height="600" frameborder="0" scrolling="no"></iframe>
</div>

<form name="chatform" onsubmit="return sendMessage();">
    <input type="text" name="message"/><br />
    <input type="submit" name="submit" value="Send Message" />
</form>
<br />
<div id="mychat"></div>
</body>
</html>