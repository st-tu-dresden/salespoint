<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.joda.org/joda/time/tags" prefix="joda" %>
<%@ taglib uri="http://www.salespoint-framework.org/web/taglib" prefix="sp" %>

<nav>
	<a href="<c:url value="/index" />"><spring:message code="nav.home" /></a> |

	<sp:hasCapability capabilityName="boss">
		<a href="<c:url value="/orders" />"><spring:message code="nav.orders" /></a> |
		<a href="<c:url value="/stock" />"><spring:message code="nav.stock" /></a> |
	</sp:hasCapability>
	
	<sp:hasCapability capabilityName="boss" test="false">
		<a href="<c:url value="/dvdCatalog" />"><spring:message code="nav.dvdCatalog" /></a> | 
		<a href="<c:url value="/blurayCatalog" />"><spring:message code="nav.bluerayCatalog" /></a> | 
		<a href="<c:url value="/shoppingBasket" />"><spring:message code="nav.shoppingBasket" /></a>
	</sp:hasCapability>
</nav>