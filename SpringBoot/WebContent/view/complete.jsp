<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="st" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Complete</title>

<c:set value="${pageContext.request.contextPath}" var="path"
	scope="page" />
<link rel="stylesheet" href="${path}/css/bootstrap.css" media="screen">
<link rel="stylesheet" href="${path}/css/bootswatch.min.css">

<script src="${path}/js/jquery-1.10.2.min.js"></script>
<script src="${path}/js/bootstrap.min.js"></script>
<script src="${path}/js/bootswatch.js"></script>

</head>
<body style="background-color: #eeeeee">
	<div class="col-xs-3"></div>
	<div class="col-xs-6">
		<div class="panel panel-${type}">
			<div class="panel-heading">
				<h3 class="panel-title">${title}</h3>
			</div>
			<div class="panel-body">
				<span></span>
				<p>
					<strong>${message}</strong>
				</p>
				<span></span>
				<!-- <a href="${path}/${page}" target="${target}"
					class="btn btn-${type} btn-sm pull-right"><strong>返回</strong></a>
					 -->
			</div>
		</div>
		<!-- 
		<div class="bs-component">
			<div class="alert alert-dismissible alert-${type}">
				<button type="button" class="close" data-dismiss="alert">×</button>
				<h4>${title}</h4>
				<span></span>
				<p>
					<strong>${message}</strong>
				</p>
				<span></span> <a href="${path}/${page}" target="${target}"
					class="alert-link">返回</a>
			</div>
		</div>
		 -->
	</div>
	<div class="col-xs-3"></div>
</body>
</html>