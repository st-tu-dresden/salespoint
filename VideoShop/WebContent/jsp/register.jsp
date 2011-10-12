<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.salespoint-framework.org/web/taglib" prefix="sp"%>

<!DOCTYPE html>

<html>
<head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<link rel="stylesheet" type="text/css" href="<c:url value="/res/css/style.css" />" />
	<title><spring:message code="register.title" /></title>
</head>
<body>
	<div class="all">
		<header class="top">
			<h1><spring:message code="register.title" /></h1>
			<jsp:include page="templates/navigation.jsp"></jsp:include>
		</header>
		
		<div class="content">
			<div class="login">
				<form method="post" action="new">
					<label for="name"><spring:message code="register.name" /></label><br />
					<input id="name" type="text" name="name" />
					<br />
					<label for="password"><spring:message code="register.password" /></label><br />
					<input id="password" type="text" name="password" />
					<br/>
					<br/>
					<label for="address"><spring:message code="register.street" /></label><br />
					<input id="address" type="text" name="street" />
					<br/>
					<label for="city"><spring:message code="register.city" /></label><br />
					<input id="city" type="text" name="city" /><br/>
					<input type="submit" name="submit" /> 
				</form>
			</div>
		</div>
		
		<jsp:include page="templates/footer.jsp"></jsp:include>
	</div>
</body>
</html>