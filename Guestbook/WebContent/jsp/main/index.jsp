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
		<title>Kleines Beispiel</title>
	</head>
	<body>
		<h1>Softwarepraktikum</h1>
		<div>
			<h2 class="center">Viel Spaß beim Programmieren. Anbei ein kleines Gästebuch.</h2>
			<br />
			<p class="center"><a href="<c:url value="/really" />">Das will ich sehen!</a></p>
		</div>
	</body>
</html>