<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="st" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE HTML>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>PersonalityTestGame</title>
<c:set value="${pageContext.request.contextPath}" var="path"
	scope="page" />


<link rel="stylesheet" href="${path}/css/wechat.css">
<link rel="stylesheet" href="${path}/css/wechatEx.css">
<script src="${path}/js/jquery-1.10.2.min.js"></script>
<script src="${path}/js/bootstrap.min.js"></script>
<script src="${path}/js/bootswatch.js"></script>
<script src="${path}/js/controller.js"></script>
<script src="${path}/js/zepto.min.js"></script>
<script src="${path}/js/router.min.js"></script>
<script src="${path}/js/example.js"></script>
</head>
<body onload="getDataSet()">
	<form:form class="form-horizontal" id="GetResult" name="GetResult"
		method="post" action="${path}/GetResult.htm">
		<fieldset>
			<div class="container">
				<div class="row">
					<div id="questions">
						<div class="weui_cells_title" id="title">单选列表项</div>
						<div class="weui_cells weui_cells_radio" id="choice">
							<label class="weui_cell weui_check_label" for="x11">
								<div class="weui_cell_bd weui_cell_primary">
									<p>cell standard</p>
								</div>
								<div class="weui_cell_ft">
									<input type="radio" class="weui_check" name="radio1" id="x11">
									<span class="weui_icon_checked"></span>
								</div>
							</label>
						</div>
					</div>
					<br />
					<br />
					<div style="left: -0.5px; width: 100%; align: center;">
						<div style="top: 0px">
							<a id="prev" href="javascript:;" class="weui_btn weui_btn_warn"
								onclick="prev()">上一页</a>
						</div>
						<br />
						<br />
						<div id="submitButton">
							<a id="next" href="javascript:;"
								class="weui_btn weui_btn_primary" onclick="next()">下一页</a>
						</div>
					</div>
				</div>
			</div>
			<input id="type" name="type" type="hidden" value="${type}" /> <input
				id="answer" name="answer" type="hidden" value="" />
		</fieldset>
	</form:form>
	<div class="row">
		<div class="span12"></div>
	</div>
	<input id="dataSet" name="dataSet" type="hidden" value='${dataSet}' />
	<input id="questionPerPage" name="questionPerPage" type="hidden"
		value='${questionPerPage}' />
</body>