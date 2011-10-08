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
<title>Salespoint 5 - Calendar</title>
</head>
<body>
	<h2>Create a new calendar entry</h2>
	<br />
	<p>
		<form:form method="post" action="newEntry">
			<fieldset>
				<legend>Create entry</legend>
				<h2>${message}</h2>
				<label for="title">Title</label>
				<br />
				<input type="text" name="title" id="title" type="text" value="${title}"/>
				<br />
				<label for="description">Description</label>
				<br />
				<textarea name="description" id="description"></textarea>
				<br />
				<label for="startDate">Start</label>
				<table>
					<tr>
						<td><input type="text" name="startDate" id="startDate" /></td>
						<td><input type="text" name="startTime" id="startTime" /></td>
					</tr>
				</table>
				<br />
                <label for="endDate">End</label>
				<table>
					<tr>
						<td><input type="text" name="endDate" id="endDate" /></td>
						<td><input type="text" name="endTime" id="endTime" /></td>
					</tr>
				</table>
				<br />
				<table>
					<tr>
						<td><input type="submit" value="Save"/></td>
						<td><input type="button" value="Cancel" onclick="window.location.href = 'calendar';"/></td>
					</tr>
				</table>
			</fieldset>
		</form:form>
	</p>
</body>
</html>