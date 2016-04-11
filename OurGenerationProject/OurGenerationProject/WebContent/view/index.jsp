<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="st" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE HTML>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>PersonalityTestGame</title>
<c:set value="${pageContext.request.contextPath}" var="path" scope="page" />

<link rel="stylesheet" href="${path}/css/bootstrap.css">
<link rel="stylesheet" href="${path}/css/bootswatch.min.css">

<script src="${path}/js/jquery-1.10.2.min.js"></script>
<script src="${path}/js/bootstrap.min.js"></script>
<script src="${path}/js/bootswatch.js"></script>
<link rel="stylesheet" href="${path}/js/jqwidgets/styles/jqx.base.css" type="text/css" />
<script type="text/javascript" src="${path}/js/jqwidgets/jqxcore.js"></script>
<script type="text/javascript" src="${path}/js/jqwidgets/jqxdata.js"></script>
<script type="text/javascript" src="${path}/js/jqwidgets/jqxdraw.js"></script>
<script type="text/javascript" src="${path}/js/jqwidgets/jqxchart.core.js"></script>
<script type="text/javascript" src="${path}/js/jqwidgets/jqxslider.js"></script>
<script type="text/javascript" src="${path}/js/jqwidgets/jqxbuttons.js"></script>
<script type="text/javascript" src="${path}/js/jqwidgets/jqxlistbox.js"></script>
<script type="text/javascript" src="${path}/js/jqwidgets/jqxscrollbar.js"></script>
<script type="text/javascript" src="${path}/js/jqwidgets/jqxdropdownlist.js"></script>
<script type="text/javascript" src="${path}/js/jqwidgets/base.js"></script>
<script type="text/javascript">

</script>
</head>
<body style="background-color: #eeeeee" onload="getDataSet()">
	<div class="span12"  scroll=no style="overflow:hidden">
			<iframe src="${path}/MultipleChoiceGame.htm?type=${type}" id="contentFrame"
				name="contentFrame" frameBorder=0 width="100%" height="1500"
				onload="iFrameHeight(this)"></iframe>
		</div>
</body>