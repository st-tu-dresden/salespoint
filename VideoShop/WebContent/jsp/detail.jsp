<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.salespoint-framework.org/web/taglib" prefix="sp"%>

<!DOCTYPE html>

<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css"
	href="<c:url value="/res/css/style.css" />" />
</head>
<title>${dvd.name}</title>
<body>

<div class="navi">
	<jsp:include page="templates/navigation.jsp"></jsp:include>
</div>
	
<div class="content">

	This is the detail page of ${dvd.name}.<br>
	You can purchase it for ${dvd.price }.<br>
	What other Customers said about ${dvd.name }:<br>

<form method="post" action="addDisc">
	<input type="hidden" name="pid" value="${dvd.identifier}" />
	<input type="submit" value="zum warenkorb hinzufügen" />
</form>

<ul>
<sp:forEach var="comment" items="${comments}">
<li>${comment.text}</li>
</sp:forEach>
</ul>


<p> add comment</p>

<form method="post" action="comment">
	<input type="hidden" name="pid" value="${dvd.identifier}" />
	<input type="text" name="comment" value="" />
	<input type="text" name="rating" value="5" />
	<input type="submit" value="senden" />
</form>

</div>

</body>
</html>