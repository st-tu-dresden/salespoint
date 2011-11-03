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
	<title></title>
</head>
<body>
	<div class="all">
		<header>
			<h1><spring:message code="orders.title" /></h1>
			<jsp:include page="templates/navigation.jsp" />
		</header>
		
		<div class="content">
			<table>
				<caption><spring:message code="orders.completed" /></caption>
				<thead>
					<tr>
						<th><spring:message code="orders.date" /></th>
						<th><spring:message code="orders.customer" /></th>
						<th><spring:message code="orders.payed" /></th>
					</tr>
				</thead>
				<tbody>
					<sp:forEach var="order" items="${ordersCompleted}">
						<tr>
							<td><joda:format value="${order.dateCreated}" style="SM" /></td>
							<td>${order.userIdentifier}</td>
							<td>${order.totalPrice}</td>
						</tr>
					</sp:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>