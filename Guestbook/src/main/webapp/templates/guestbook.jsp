<!DOCTYPE html>

<html xmlns:th="http://www.thymeleaf.org">
	<head>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<link rel="stylesheet" type="text/css" href="../resources/css/style.css" />
		<title><spring:message code="guestbook.title" /></title>
	</head>
	
	<body>
	<h1 th:text="#{guestbook.title}">Gästebuch</h1>

	<div class="entries" th:each="entry,iterStat : ${guestbookEntries}">
			<div class="entry">
				<!-- 			
				<c:url value="removeEntry" var="url">
					<c:param name="id" value="${entry.id}" />
				</c:url>
				 -->
				<h2 th:text="${iterStat.count} + ${entry.name}">
					 (<a th:text="#{guestbook.delete}" href="">Löschen</a>)
				</h2>
				<p class="entrytext" th:text="${entry.text}"></p>
				<p class="date" th:text="${entry.date}">
					<!--  pattern="dd.MM.yyyy, HH:mm "  --> 
				</p>

			</div>
	</div>

	<div class="login"> 
		<form id="form" action="addEntry" method="post"> 
			<fieldset> 
				<legend><spring:message code="guestbook.form.title" /></legend> 
				<label for="name"><spring:message code="guestbook.form.name" /></label><br /> 
				<input name="name" id="name" type="text" /><br /> 
				<label for="text"><spring:message code="guestbook.form.text" /></label><br /> 
				<textarea name="text" id="text"></textarea><br /> 
				<input type="submit" class="btn"><spring:message code="guestbook.form.submit" /></button>
			</fieldset> 
		</form> 
	</div>

	<p class="center">
		<a href="<c:url value="/" />"><spring:message code="guestbook.back" /></a>
	</p>
</body>
</html>