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
	<link rel="stylesheet" type="text/css"	href="<c:url value="/res/css/style.css" />" />
	<title>${disc.name}</title>
</head>

<body>
	<div class="all">
		<header class="top">
			<h1>${disc.name}</h1>
			<jsp:include page="templates/navigation.jsp"></jsp:include>
		</header>
			
		<div class="content">
			${disc.name}.<br /> 
			<spring:message code="catalog.price" />: ${disc.price }.<br /> 
			<spring:message code="detail.numberInStock" />: ${count} <br />
			
			<sp:loggedIn status="true">
				<form method="post" action="addDisc">
					<input type="hidden" name="pid" value="${disc.identifier}" /> 
					<input	type="submit" value="<spring:message code="detail.addToBasket" />" />
				</form>
			</sp:loggedIn>
	
			<p>
				What other Customers said about "${disc.name}":<br />
			</p>
		
			<ul>
				<sp:forEach var="comment" items="${disc.comments}">
					<li>${comment.text}</li>
				</sp:forEach>
			</ul>
		
			<p><spring:message code="detail.comment.addComment" />:</p>

			<form method="post" action="comment">
				<input type="hidden" name="pid" value="${disc.identifier}" />
				<textarea name="comment" cols="40" rows="5"></textarea><br>
				<label for="rating"><spring:message code="detail.comment.rating" />:</label>
				<input type="text" 	name="rating" value="5" /> 
				<input type="submit" value="<spring:message code="detail.comment.submit" />" />
			</form>
		</div>
		
		<jsp:include page="templates/footer.jsp"></jsp:include>
	</div>
</body>
</html>