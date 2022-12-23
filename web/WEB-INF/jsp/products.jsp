<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>Products</title>
</head>
<body>
<%@include file="header.jsp"%>
<ul>
    <c:forEach var="product" items="${sessionScope.products}">
        <li>${product.name} ${product.description} ${product.price} ${product.quantity}</li>
    </c:forEach>
</ul>
</body>
</html>
