<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.salespoint-framework.org/web/taglib" prefix="sp"%>

    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Register new Customer</title>
</head>
<body>
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
</body>
</html>