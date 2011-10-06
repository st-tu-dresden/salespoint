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
<title>Salespoint Framework</title>
</head>
<body>
	<h1>Salespoint Framework</h1>
	<p>
	<sp:LoggedIn status="true">
		<p> hello, ${loggedInUser.identifier}</p>
		<p>
			<c:url value="logout" var="logout"></c:url>
			<a href="${logout}">Logout</a>
		</p>
	</sp:LoggedIn>
	<sp:LoggedIn status="false">
	<jsp:include page="templates/login.jsp"></jsp:include>
	<a href="register">Register account</a>
	</sp:LoggedIn>
	</p>


	<br />
	<p>TODO Links zu den einzelnen Punkten der Doku hier rein</p>

	<p>----</p>
	<ul>
		<c:forEach var="item" items="${items}">
			<c:url value="detail" var="url">
				<c:param name="pid" value="${item.identifier}" />
			</c:url>
			
			<li><a href="${url}">${item.name} - ${item.price}</a></li>
		</c:forEach>
	</ul>
	<p>----</p>

	
	<a href="dvdCatalog">Dvd Catalog</a>
	<br />
	<a href="bluerayCatalog">BlueRay Catalog</a>

</body>
</html>