<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.joda.org/joda/time/tags" prefix="joda" %>
<%@ taglib uri="http://www.salespoint-framework.org/web/taglib" prefix="sp" %>
<!DOCTYPE html>

<html>
<head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<link rel="stylesheet" type="text/css" href="<c:url value="/res/css/style.css" />" />
	<link rel="stylesheet" type="text/css" href="<c:url value="/res/css/login.css" />" />
	<title><spring:message code="home.title" /></title>
</head>
<body>
	<div class="all">
		<header>
			<h1><spring:message code="home.title" /></h1>
			<jsp:include page="templates/navigation.jsp" />
		</header>
		
		<div class="content">
			<p>Hallo ${user.identifier}</p>
			<p><spring:message code="home.welcome" /></p>
			
			<sp:loggedIn>
				<p><a href="<c:url value="/logout" />"><spring:message code="home.logout" /></a></p>
			</sp:loggedIn>
			
			<sp:loggedIn test="false">
				<jsp:include page="templates/login.jsp" />
				<p><a href="<c:url value="/register" />"><spring:message code="register.new" /></a></p>
			</sp:loggedIn>
			
			<form action="money" method="post">
				<label>money</label>
				<input name="xyz" value="10" />
				<button type="submit">go</button>
			</form>
			
			<form action="capability" method="post">
				<label>cap</label>
				<input name="xyz" value="10" />
				<button type="submit">go</button>
			</form>
			
			<form action="units" method="post">
				<label>units</label>
				<input name="xyz" value="10" />
				<button type="submit">go</button>
			</form>
			
		</div>
	</div>
</body>
</html>