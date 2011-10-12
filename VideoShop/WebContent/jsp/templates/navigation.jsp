<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.salespoint-framework.org/web/taglib" prefix="sp"%>

<!-- 
<nav>
	<a class="navi" href="index"> Home</a> |&nbsp;
	<a class="navi" href="dvdCatalog"> DVD Katalog</a> |&nbsp;
	<a class="navi" href="blurayCatalog"> BluRay Katalog</a> |&nbsp;
	<a class="navi" href="basket"> Shopping Cart</a>
</nav>
-->

<nav>
	<a class="navi" href="index"><spring:message code="nav.home" /></a>
	<a class="navi" href="dvdCatalog"><spring:message code="nav.dvdCatalog" /></a>
	<a class="navi" href="blurayCatalog"><spring:message code="nav.blueRayCatalog" /></a>
	<a class="navi" href="basket"><spring:message code="nav.shoppingBasket" /></a>
</nav>
