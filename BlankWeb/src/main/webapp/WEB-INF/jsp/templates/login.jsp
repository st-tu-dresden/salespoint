<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<div class="salespoint">
	<form action="login" method="post">
		<fieldset>
			<legend><spring:message code="org.salespointframework.form.login.title" /></legend>
			<label for="name"><spring:message code="org.salespointframework.form.login.user" /></label><br />
			<input id="name" type="text" name="identifier" />
			<br />
			<label for="password"><spring:message code="org.salespointframework.form.login.password" /></label><br />
			<input id="password" type="text" name="password" />
			<br />
			<button type="submit"><spring:message code="org.salespointframework.form.login.button" /></button>
		</fieldset>
	</form>
</div>