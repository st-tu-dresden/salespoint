<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE html>

<html>
	<head>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/style.css" />" />
		<title><spring:message code="guestbook.title" /></title>
	</head>
	
	<body>
	<h1><spring:message code="guestbook.title" /></h1>

	<!-- http://download.oracle.com/javaee/5/jstl/1.1/docs/tlddocs/overview-summary.html -->
	<div class="entries">
		<c:forEach var="e" items="${guestbookEntries}" varStatus="status">
			<div class="entry">
				<c:url value="removeEntry" var="url">
					<c:param name="id" value="${e.id}" />
				</c:url>
				<h2>
					${status.index+1}. ${e.name} (<a href="${url}"><spring:message code="guestbook.delete" /></a>)
				</h2>
				<p class="entrytext">${e.text}</p>
				<p class="date">
					<fmt:formatDate value="${e.date}" pattern="dd.MM.yyyy, HH:mm " />
				</p>

			</div>
		</c:forEach>
	</div>

	<div class="login"> 
		<form id="form" action="addEntry" method="post"> 
			<fieldset> 
				<legend><spring:message code="guestbook.form.title" /></legend> 
				<label for="name"><spring:message code="guestbook.form.name" /></label><br /> 
				<input name="name" id="name" type="text" /><br /> 
				<label for="text"><spring:message code="guestbook.form.text" /></label><br /> 
				<textarea name="text" id="text"></textarea><br /> 
				<button class="btn"><spring:message code="guestbook.form.submit" /></button>
			</fieldset> 
		</form> 
	</div>

	<p class="center">
		<a href="<c:url value="/" />"><spring:message code="guestbook.back" /></a>
	</p>
</body>
</html>