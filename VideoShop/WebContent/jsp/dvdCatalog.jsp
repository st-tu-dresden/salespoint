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
	<div class="allItems">
		<c:forEach var="item" items="${items}">
			<a class="item" href="detail?pid=${item.identifier}">
				<div class="item">
					<h3>${item.name}</h3>
					<img class="itemThumbnail" src="<c:url value="/res/img/imageDummy.png" />" />
					<p class="price">${item.price}</p>
				</div>
			</a>
		</c:forEach>
	</div>
	
	<p>----</p>
</body>
</html>