<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html>
     
<html>
	<head>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<link rel="stylesheet" type="text/css" href="<c:url value="/res/css/style.css" />" />
		<title>Viel Spaß</title>
	</head>
	<body>
		<h1>I can has Salezpoint</h1>
		<div>
			<img class="center" src="res/img/owl_orly.png" alt="" />
			<br />
			<p class="center"><a href="<c:url value="/guestbook/" />">Ya rly!</a></p>
			<p class="center"><a href="<c:url value="/ajaxbook/" />">Ya rly! AJAX</a></p>
		</div>
	</body>
</html>