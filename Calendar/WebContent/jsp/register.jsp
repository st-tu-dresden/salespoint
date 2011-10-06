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
	<p>Here you can register a new user!</p>
	<br />
	<p>
    
    <form:form method="post" action="registerUser">
        <fieldset>
            <legend>Register</legend>
            <label for="username">Username</label>
            <br />
            <input type="text" name="username" id="username" type="text" />
            <br />
            <label for="pwOne">Password</label>
            <br />
            <input type="password" name="pwOne" id="pwOne" />
            <br />
            <label for="pwTwo">Confirm</label>
            <br />
            <input type="password" name="pwTwo" id="pwTwo" />
            <br />
            <button type="submit">Register</button>
        </fieldset>
    </form:form>    
    </p>
</body>
</html>