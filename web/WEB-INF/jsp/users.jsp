<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@taglib prefix="с" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>Users</title>
</head>
<body>
<%@include file="header.jsp"%>
<ul>
<с:forEach var="user" items="${requestScope.users}">
    <li>${user.id} ${user.username} ${user.email}</li>
</с:forEach>
</ul>
</body>
</html>
