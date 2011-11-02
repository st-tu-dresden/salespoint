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
	<link rel="stylesheet" type="text/css"	href="<c:url value="/res/css/style.css" />" />
	<title><spring:message code="catalog.bluray.title" /></title>
</head>
<body>
	<div class="all">
		<header>
			<h1><spring:message code="catalog.bluray.title" /></h1>
			<jsp:include page="templates/navigation.jsp" />
		</header>
		
		<div class="content">
			<div class="catalogItems">
				<sp:forEach var="item" items="${items}">
					<c:url var="url" value="detail/${item.identifier}" />
					<a href="${url}">
						<div class="item">
							<h4>${item.name}</h4>
							<img class="thumbnail" src="<c:url value="/res/img/imageDummy.png" />" alt="" />
							<p class="price"><spring:message code="catalog.price" />: ${item.price}</p>
						</div>
					</a>
				</sp:forEach>
			</div>
		</div>
	</div>		
</body>
</html>