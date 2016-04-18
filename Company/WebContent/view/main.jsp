<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>Junstech</title>
<c:set value="${pageContext.request.contextPath}" var="path"
	scope="page" />
<link rel="shortcut icon" href="${path}/img/icon.png" type="image/x-icon" />
<link rel="stylesheet" href="${path}/css/bootstrap.css" media="screen">
<link rel="stylesheet" href="${path}/css/bootswatch.min.css">

<script src="${path}/js/jquery-1.10.2.min.js"></script>
<script src="${path}/js/bootstrap.min.js"></script>
<script src="${path}/js/bootswatch.js"></script>
<script src="${path}/js/junstech.js"></script>
<script type="text/javascript">
function reloadFrameHeight() {
	
	iFrameHeight(document.getElementById("contentFrame"));
}
setInterval("reloadFrameHeight()",100)
</script>
</head>

<div class="container-fluid" style="background-color: #eeeeee">
	<div class="row-fluid">
		<div class="span12">
			<%@ include file="header.jsp"%>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span12" id="searchFrame"></div>
	</div>
	<div class="row-fluid">
		<div class="span12"  scroll=no style="overflow:hidden">
			<iframe src="" id="contentFrame"
				name="contentFrame" frameBorder=0 width="100%" onload="iFrameHeight(this)"></iframe>
		</div>
	</div>

	<div class="row-fluid">
		<div class="span12"></div>
	</div>
</div>