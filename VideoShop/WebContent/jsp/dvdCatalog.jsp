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
<title>DVD Catalog</title>
</head>
<body>
	<h1>DVD Catalog</h1>
	<p>welcome</p>
	<br />
	<p>----</p>
	<ul>
		<c:forEach var="item" items="${items}">
			<li><a href="detail?pid=${item.identifier}">${item.name} - ${item.price}</a></li>
		</c:forEach>
	</ul>
	<p>----</p>
</body>
</html>