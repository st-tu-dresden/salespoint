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
<div class="all">
	<div class="top">
		<h1>Register new Customer</h1>
		<jsp:include page="templates/navigation.jsp"></jsp:include>
	</div>
	
	<div class="content">
		<div class="login">
			<form method="post" action="new">
					<label for="name">Name</label><br />
					<input id="name" type="text" name="name" />
					<br />
					<label for="password">Password</label><br />
					<input id="password" type="text" name="password" />
					<br/>
					<br/>
					<label for="address">Street</label><br />
					<input id="address" type="text" name="street" />
					<br/>
					<label for="city">City</label><br />
					<input id="city" type="text" name="city" /><br/>
				
				<input type="submit" name="submit" /> 
			</form>
		</div>
	</div>
	<jsp:include page="templates/footer.jsp"></jsp:include>
</div>
</body>
</html>