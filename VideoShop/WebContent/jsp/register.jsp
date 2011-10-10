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
	<title>Register new Customer</title>
</head>

<body>
	<div class="navi">
		<h1>Register new Customer</h1>
		<jsp:include page="templates/navigation.jsp"></jsp:include>
	</div>
	
	<div class="content">
		<div style="border: 1px solid #7010fa;">
			<form method="post" action="new">
				<p>
					<label for="name">Name</label>
					<input id="name" type="text" name="name" />
					<br />
					<label for="password">Password</label>
					<input id="password" type="text" name="password" />
				</p>
				<p>
					<label for="address">Street</label>
					<input id="address" type="text" name="street" />
					<br/>
					<label for="city">City</label>
					<input id="city" type="text" name="city" />
				</p>
				<input type="submit" name="submit" /> 
			</form>
		</div>
	</div>

</body>
</html>