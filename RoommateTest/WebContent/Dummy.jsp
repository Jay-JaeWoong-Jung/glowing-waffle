<%--
  Created by IntelliJ IDEA.
  User: angelahe
  Date: 4/12/18
  Time: 10:54 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>GuestUser</title>
    Which House Are You Interested In?
    <form method="POST" action="DispatcherServlet">
        <input type = "text" name = "houseHandle">
        <input type="hidden" name="command" value="guestUser">
    </form>
</head>
<body>

</body>
</html>
