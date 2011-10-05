<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE html>

<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css"
	href="<c:url value="/res/css/style.css" />" />
<title>Salespoint Framework</title>
</head>
<body>
	<h1>Salespoint Framework</h1>
	<p>welcome</p>
	<br />
	<p>TODO Links zu den einzelnen Punkten der Doku hier rein</p>

	<p>----</p>
	<ul>
		<c:forEach var="item" items="${items}">
			<li>${item.name} - ${item.price}</li>
		</c:forEach>
	</ul>
	<p>----</p>

</body>
</html>