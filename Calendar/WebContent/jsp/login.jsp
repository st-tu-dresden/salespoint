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
<title>Salespoint 5 - Calendar</title>
</head>
<body>
	<h1>Calendar Demo</h1> <br />
	<h2>Welcome to the demo of the new feature of Salespoint 5: The Calendar :)</h2> <br />
	<p>
        <form:form method="post" action="loginUser">
			<fieldset>
				<legend>Login</legend>
				<h2>${message}</h2>
				<label for="username">Username</label> <br />
				<input type="text" name="username" id="username" type="text" value="${user}"/> <br />
				<label for="password">Password</label> <br /> 
				<input type="password" name="password" id="password" /> <br />
				<button type="submit">Login</button>
				<a href="<c:url value="register" />">Register</a>
			</fieldset>
		</form:form>
	</p>
</body>
</html>