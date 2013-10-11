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
			<h1><spring:message code="stock.title" /></h1>
			<jsp:include page="templates/navigation.jsp" />
		</header>
		
		<div class="content">
			<table>
				<caption><spring:message code="stock.title" /></caption>
				<thead>
					<tr>
						<th><spring:message code="shoppingBasket.movieTitle" /></th>
					<th><spring:message code="shoppingBasket.count" /></th>
					</tr>
				</thead>
				<tbody>
					<sp:forEach var="item" items="${stock}">
						<tr>
							<td>${item.product.name}</td>
							<td>${item.quantity}</td>
						</tr>
					</sp:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>