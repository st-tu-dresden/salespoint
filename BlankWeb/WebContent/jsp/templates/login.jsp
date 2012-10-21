<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<div class="salespointlogin">
	<form action="login" method="post">
		<fieldset>
			<legend><spring:message code="org.salespointframework.form.login.title" /></legend>
			<label for="SP_LOGIN_PARAM_IDENTIFIER"><spring:message code="org.salespointframework.form.login.user" /></label><br />
			<input id="SP_LOGIN_PARAM_IDENTIFIER" type="text" name="SP_LOGIN_PARAM_IDENTIFIER" />
			<br />
			<label for="SP_LOGIN_PARAM_PASSWORD"><spring:message code="org.salespointframework.form.login.password" /></label><br />
			<input id="SP_LOGIN_PARAM_PASSWORD" type="text" name="SP_LOGIN_PARAM_PASSWORD" />
			<br />
			<button type="submit"><spring:message code="org.salespointframework.form.login.button" /></button>
		</fieldset>
	</form>
</div>