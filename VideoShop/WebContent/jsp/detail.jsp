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
	<link rel="stylesheet" type="text/css"	href="<c:url value="/res/css/style.css" />" />
	<link rel="stylesheet" type="text/css" href="<c:url value="/res/css/login.css" />" />
	<title>${disc.name}</title>
</head>

<body>
	<div class="all">
		<header>
			<h1>${disc.name}</h1>
			<jsp:include page="templates/navigation.jsp" />
		</header>
		
		<div class="content">
			<div class="detailImg">
				<img src="<c:url value="/res/img/cover/${disc.image}.jpg" />" alt="" />
			</div>
			<div class="detailDescription">
				<h2>${disc.name}</h2>
				<spring:message code="catalog.price" />: ${disc.price }<br />
				<spring:message code="detail.genre" />: ${disc.genre }<br />  
				<spring:message code="detail.numberInStock" />: ${count}<br />
				
				<sp:loggedIn>
					<form method="post" action="<c:url value="/addDisc" />">
						<input type="hidden" name="pid" value="${disc.identifier}" /> 
						<label for="number"><spring:message code="detail.number" /></label>
						<input id="number" type="number" name="number" min="1" max="5" step="1" value="1" /><br />
						<input	type="submit" value="<spring:message code="detail.addToBasket" />" />
					</form>
				</sp:loggedIn>
			</div>

	
			<div class="detailComments">
				<spring:message code="detail.comment.comments" />
				<br />
				<sp:forEach var="comment" items="${disc.comments}">
					<article class="comment">${comment.text}</article>
				</sp:forEach>
			</div>
			
			<br />
			
			<div class="salespoint">
				<form method="post" action="<c:url value="/comment" />">
					<fieldset>
						<legend><spring:message code="detail.comment.addComment" /></legend>
						<input type="hidden" name="pid" value="${disc.identifier}" />
						<textarea name="comment" cols="40" rows="5"></textarea><br>
						<label for="rating"><spring:message code="detail.comment.rating" />:</label>
						<input id="rating" type="text" name="rating" value="5" /> 
						<input type="submit" value="<spring:message code="detail.comment.submit" />" />
					</fieldset>
				</form>
			</div>
		</div>
	</div>
</body>
</html>