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
<link rel="stylesheet" type="text/css"	href="<c:url value="/res/css/style.css"/>" />
<title>BluRay Catalog</title>
</head>

<body>

		
	<div class="navi">
		<h1>BluRay Catalog</h1>
		<jsp:include page="templates/navigation.jsp"></jsp:include>
	</div>
	
	<div class="content">
	</div>

</body>
</html>