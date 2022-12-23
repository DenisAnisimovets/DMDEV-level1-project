<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/registration" method="post">
    <label for="userNameId">User name:
        <input type="text" name="username" id="userNameId">
    </label><br><br>
    <label for="birthdayId">Birthday:
        <input type="date" name="birthday" id="birthdayId" required>
    </label><br><br>
    <label for="fileId">File:
        <input type="file" name="image" id="fileId">
    </label><br><br>
    <label for="cityId">City:
        <input type="text" name="city" id="cityId">
    </label><br><br>
    <label for="emailId">Email:
        <input type="text" name="email" id="emailId">
    </label><br><br>
    <label for="passwordId">Password:
        <input type="password" name="password" id="passwordId">
    </label><br><br>
    <label for="role">Role:
        <select name="role" id="role">
            <c:forEach var="role" items="${requestScope.roles}">
                <option value="${role}">${role}</option>
            </c:forEach>
        </select>
    </label><br><br>
    <c:forEach var="gender" items="${requestScope.genders}">
        <input type="radio" name="gender" value="${gender}"> ${gender}
    </c:forEach>
    <br><br>
    <button type="submit">Send</button>
</form>
</body>
</html>
