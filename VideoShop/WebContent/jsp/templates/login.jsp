<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<div style="border: 2px solid #FAD614; margin:2em; padding:1em;">
	<!--  TODO multiple values select/dropdown/shit -->
	<form method="post" action="">
		<input type="text" name="SP_LOGIN_PARAM" hidden="true" />
		<label for="SP_LOGIN_PARAM_IDENTIFIER">UserIdentifier</label>
		<input id="SP_LOGIN_PARAM_IDENTIFIER" type="text" name="SP_LOGIN_PARAM_IDENTIFIER" />
		<br />
		<label for="SP_LOGIN_PARAM_PASSWORD">Password</label>
		<input id="SP_LOGIN_PARAM_PASSWORD" type="text" name="SP_LOGIN_PARAM_PASSWORD" />
		<br />
		<input type="submit" name="button" value="Login" />
	</form>
</div>