<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.salespoint-framework.org/web/taglib" prefix="sp"%>

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
			<h1>Bestellungen</h1>
			<jsp:include page="templates/navigation.jsp" />
		</header>
		
		<div class="content">
			<p>Offene Bestellungen</p>
			<table>
				<caption>FOOBAR</caption>
				<thead>
					<tr>
						<th>Identifier</th>
						<th>Customer</th>
						<th>Price</th>
					</tr>
				</thead>
			<tbody>
				<sp:forEach var="o" items="${openOrders}">
					<tr>
					<td></td>
					</tr>
				</sp:forEach>
			</tbody>
			</table>
			
			<p>Abgeschlossene Bestellungen</p>
		</div>
	</div>
</body>
</html>