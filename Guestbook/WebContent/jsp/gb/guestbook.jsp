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
<title>Guestbook</title>
</head>
<body>
	<h1>Guestbook</h1>

	<!-- http://download.oracle.com/javaee/5/jstl/1.1/docs/tlddocs/overview-summary.html -->
	<div class="entries">
		<c:forEach var="e" items="${guestbookEntries}" varStatus="status">
			<c:choose>
				<c:when test="${status.index % 2 == 0}">
					<c:set var="style" value="entry" />
				</c:when>
				<c:otherwise>
					<c:set var="style" value="entry" />
				</c:otherwise>
			</c:choose>
			<div class="${style}">
				<c:url value="removeEntry" var="url">
					<c:param name="id" value="${e.id}" />
				</c:url>
				<h2>
					${status.index+1}. ${e.name} (<a href="${url}">delete</a>)
				</h2>
				<p class="entrytext">${e.text}</p>
				<p class="date">
					<fmt:formatDate value="${e.date}" pattern="dd.MM.yyyy, HH:mm " />
				</p>

			</div>
		</c:forEach>
	</div>

	<div class="login">
		<form:form method="post" action="addEntry">
			<fieldset>
				<legend>add Entry</legend>
				<label for="name">Name</label><br /> <input name="name" id="name"
					type="text" value="${lastName}" /><br /> <label for="text">Text</label><br />
				<textarea name="text" id="text"></textarea>
				<br />
				<button type="submit">send</button>
			</fieldset>
		</form:form>
	</div>

	<p class="center">
		<a href="<c:url value="/really" />">back</a>
	</p>
</body>
</html>