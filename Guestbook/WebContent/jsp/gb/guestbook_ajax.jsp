<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html>
     
<html>
	<head>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<script type="text/javascript" src="../res/js/jquery-1.4.3.min.js"> </script>
		<script type="text/javascript" src="../res/js/jquery.tmpl.min.js"> </script>
		<link rel="stylesheet" type="text/css" href="<c:url value="/res/css/style.css" />" />
		<title><spring:message code="guestbook.ajax.title" /></title>
	</head>
	<body>
	

		<h1><spring:message code="guestbook.ajax.title" /></h1>
		<div class="entries">
			<c:forEach var="e" items="${guestbookEntries}">
				<div class="entry" id="entry${e.id}">
					<button class="delBtn">x</button>
					<h2>${e.name}</h2>
					<p class="entrytext">${e.text}</p>
					<p class="date">
						<fmt:formatDate value="${e.date}" pattern="dd.MM.yyyy, HH:mm " />
					</p>
				</div>
			</c:forEach>
		</div>

		<div class="login"> 
			<form id="form" action=""> 
				<fieldset> 
					<legend><spring:message code="guestbook.form.title" /></legend> 
					<label for="name"><spring:message code="guestbook.form.name" /></label><br /> 
					<input name="name" id="name" type="text" /><br /> 
					<label for="text"><spring:message code="guestbook.form.text" /></label><br /> 
					<textarea name="text" id="text"></textarea><br /> 
					<button class="btn"><spring:message code="guestbook.form.submit" /></button>
				</fieldset> 
			</form> 
		</div>
		
		<p class="center"><a href="<c:url value="/really" />"><spring:message code="guestbook.back" /></a></p>
		
		<!--  http://api.jquery.com/category/plugins/templates/ -->
		<script id="entryTemplate" type="text/x-jquery-tmpl">
			<div class="entry" id="entry{{= id}}">
				<button class="delBtn">x</button>
				<h2>{{= name}}</h2>
				<p class="entrytext">{{= text}}</p>
				<p class="date"> {{= formatDate(date) }}
				</p>
			</div>
		</script>
		
		<!--  http://www.evotech.net/blog/2007/05/including-javascript-in-xhtml/ -->  
		<script type="text/javascript">
			//<![CDATA[
			
			$('#form .btn').click(addEntry);
			
			//$('.delBtn').click(function() { //.live bindet auch dynamisch hinzugefügte Elemente
			$('.delBtn').live("click", function() { 
					var id = getIdbyBtn(this);
					removeEntry(id);
				}
			);

			// eine Date Format Lib wäre 1000x besser
			function formatDate(date) {
				//var d = new Date(date);	// will nicht ganz, macht aber in diesem Fall nichts
				var d = new Date();
				var fd = d.getDate() + "." + (d.getMonth() + 1) + "." + d.getFullYear();
				var ft = d.getHours() + ":" + d.getMinutes(); 
				return fd + ", " + ft;
			}
			
			function addEntry() {
				$.getJSON("addEntry", { name:$('#name').val(), text:$('#text').val()} , function(e) {
					$("#text").val("");
					
					var entry = $("#entryTemplate").tmpl(e);
				
					entry.hide();
					entry.appendTo(".entries");
					entry.slideDown(500);
				});
				return false; 
			}
		
			function removeEntry(id) {
				$.getJSON("removeEntry", { id: id }, function(success) {
					if(success) {
						$('#entry'+id).slideUp(500, function() { $(this).remove(); });
					}
				});
			}
			
			function getIdbyBtn(btn) {
				var div = btn.parentNode;
				var attr_id = div.getAttribute('id');
				var id = (/entry(\d+)/).exec(attr_id)[1];
				return parseInt(id);
			}
			//]]>
		</script>
	</body>
</html>