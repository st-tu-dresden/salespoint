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
<title>Salespoint 5 - Calendar</title>
</head>
<body>
	<h1>Welcome ${user.identifier}</h1>

	<table id="calendar">
		<c:set var="days" value="WEEK,MON,TUE,WED,THU,FRI,SAT,SUN" />

		<tr id="calHead">
			<c:forEach var="col" items="${days}">
				<th>${col}</th>
			</c:forEach>
		</tr>
		<c:forEach begin="1" end="6" varStatus="y">
			<tr>
				<th id="kwCell">KW ${y.index}</th>
				<c:forEach begin="1" end="7" varStatus="x">
					<td id="dayCell">${x.index}</td>
				</c:forEach>
			</tr>
		</c:forEach>
	</table>
	<a href="<c:url value="logout" />">Logout</a>
</body>
</html>