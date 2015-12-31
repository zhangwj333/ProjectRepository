<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="st" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />

<title>Content</title>

<c:set value="${pageContext.request.contextPath}" var="path"
	scope="page" />
<link rel="stylesheet" href="${path}/css/bootstrap.css" media="screen">
<link rel="stylesheet" href="${path}/css/bootswatch.min.css">

<script src="${path}/js/jquery-1.10.2.min.js"></script>
<script src="${path}/js/bootstrap.min.js"></script>
<script src="${path}/js/bootswatch.js"></script>
<script src="${path}/js/junstech.js"></script>
<style>
</style>

</head>
<body style="background-color: #eeeeee">
	<form:form class="form-horizontal" id="${action}"
		modelAttribute="${modelAttribute}" name="${action}" method="post"
		action="${path}/${action}.htm">
		<fieldset>
			<c:forEach var="tableproperty" items="${tablepropertys}"
				varStatus="status">

				<div class="form-group">

					<c:choose>
						<c:when test="${tableproperty.key == 'id'}">
							<div class="col-${screen}-10">
								<input type="hidden" class="form-control" id="id"
									name="${tableproperty.key}"
									value="${tableline[tableproperty.key]}">
							</div>
						</c:when>
						<c:otherwise>

							<label for=${tableproperty.key } class="col-${screen}-2 control-label">${tableproperty.value}</label>
							<div class="col-${screen}-4">
								<input type="text" class="form-control" id="id"
									name="${tableproperty.key}"
									value="${tableline[tableproperty.key]}">
							</div>
						</c:otherwise>
					</c:choose>
				</div>

			</c:forEach>

			<c:forEach var="criteria" items="${tableline.privileges}"
				varStatus="status">
				<div class="form-group">
					<label for="select" class="col-${screen}-2 control-label">${criteria.criteria.name}</label>
					<div class="col-${screen}-4">
						<input id="privileges[${status.count-1}].key"
							name="privileges[${status.count-1}].key" type="hidden"
							value="${criteria.key}" /> <input
							id="privileges[${status.count-1}].id"
							name="privileges[${status.count-1}].id" type="hidden"
							value="${criteria.id}" /> <input
							id="privileges[${status.count-1}].programid"
							name="privileges[${status.count-1}].programid" type="hidden"
							value="${criteria.programid}" /> <input
							id="privileges[${status.count-1}].privilege"
							name="privileges[${status.count-1}].privilege" type="hidden"
							value="${criteria.privilege}" /> <select class="form-control"
							id="select"
							onchange="selectInput(this, 'privileges[${status.count-1}].privilege')">
							<c:choose>
								<c:when test="${criteria.privilege == ''}">
									<option value="" selected="selected">无权限</option>
									<option value="RW">可修改</option>
									<option value="RWD">全控制</option>
								</c:when>
								<c:when test="${criteria.privilege == 'R'}">
									<option value="">无权限</option>
									<option value="R" selected="selected">可查询</option>
									<option value="RW">可修改</option>
									<option value="RWD">全控制</option>
								</c:when>
								<c:when test="${criteria.privilege == 'RW'}">
									<option value="">无权限</option>
									<option value="R">可查询</option>
									<option value="RW" selected="selected">可修改</option>
									<option value="RWD">全控制</option>
								</c:when>
								<c:when test="${criteria.privilege == 'RWD'}">
									<option value="">无权限</option>
									<option value="R">可查询</option>
									<option value="RW">可修改</option>
									<option value="RWD" selected="selected">全控制</option>
								</c:when>
								<c:otherwise>
									<option value="">无权限</option>
									<option value="R">可查询</option>
									<option value="RW">可修改</option>
									<option value="RWD">全控制</option>
								</c:otherwise>
							</c:choose>
						</select>
					</div>
				</div>
			</c:forEach>


			<div class="form-group">
				<div class="col-${screen}-10 col-${screen}-offset-2">
					<a href="javascript:history.go(-1)" class="btn btn-default">返回</a>
					<button type="submit" class="btn btn-primary">修改</button>
				</div>
			</div>
		</fieldset>
	</form:form>
</body>
</html>