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
<title>DVD Katalog</title>
</head>
<body>
<div class="all">
		
	<div class="top">
		<h1>DVD Katalog</h1>
		<jsp:include page="templates/navigation.jsp"></jsp:include>
	</div>
	
	<div class="content">
		<c:forEach var="item" items="${items}">
			<a class="item" href="detail?pid=${item.identifier}">
				<div class="item">
					<h4>${item.name}</h4>
					<img class="itemThumbnail" src="<c:url value="/res/img/imageDummy.png" />" />
					<p class="price">Preis: ${item.price}</p>
				</div>
			</a>
		</c:forEach>
	</div>
	<jsp:include page="templates/footer.jsp"></jsp:include>
</div>		
</body>
</html>