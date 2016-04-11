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

<link rel="stylesheet" href="${path}/css/bootstrap.css">
<link rel="stylesheet" href="${path}/css/bootswatch.min.css">

<script src="${path}/js/jquery-1.10.2.min.js"></script>
<script src="${path}/js/bootstrap.min.js"></script>
<script src="${path}/js/bootswatch.js"></script>
<script src="${path}/js/controller.js"></script>
</head>
<body style="background-color: #eeeeee" onload="getDataSet()">
	<input id="dataSet" name="dataSet" type="hidden" value='${dataSet}' />
	<input id="questionPerPage" name="questionPerPage" type="hidden"
		value='${questionPerPage}' />
	<form:form class="form-horizontal" id="GetResult" name="GetResult"
		method="post" action="${path}/GetResult.htm">
		<fieldset>
			<div class="container">
				<div class="row">
					<div class="span12"></div>
				</div>

				<div class="row">
					<div id="questions">
						<div class="info">
							<blockquote>
								<h3 id="title">${questions[0].title}</h3>
							</blockquote>
						</div>
						<div class="well">
							<div id="selections" class="form-group">
								<div id="choice" class="radio">
									<label> <input type="radio" name="optionsRadios"
										id="optionsRadios" value="option"> option
									</label>
								</div>

							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-4">
							<a id="prev" class="btn btn-default" onclick="prev()">上一条</a>

						</div>
						<div class="col-sm-4"></div>
						<div id="submitButton" class="col-sm-4">
							<a id="next" class="btn btn-primary" onclick="next()">下一条</a>
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
</body>