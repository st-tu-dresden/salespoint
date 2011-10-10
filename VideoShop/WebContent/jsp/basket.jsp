<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.salespoint-framework.org/web/taglib"
	prefix="sp"%>

<!DOCTYPE html>

<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css"
	href="<c:url value="/res/css/style.css" />" />
<title>Shopping Basket</title>
</head>

<body>
	<div class="navi">
		<h1>Shopping Basket</h1>
		<jsp:include page="templates/navigation.jsp"></jsp:include>
	</div>

	<div class="content">
		<c:choose>
			<c:when test="${!isEmpty}">
				<ul>
					<sp:forEach var="item" items="${items}">
						<li>${item.productName} -- ${item.numberOrdered}</li>
					</sp:forEach>
				</ul>

				<form method="post" action="buy">
					<input type="submit" value="buy!!!" />
				</form>
			</c:when>
			<c:otherwise>Your cart is empty.</c:otherwise>
		</c:choose>
	</div>

</body>
</html>