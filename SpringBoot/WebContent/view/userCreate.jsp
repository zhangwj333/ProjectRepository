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
<script type="text/javascript">
	goback()
	{
		history.back();
	}
</script>
<script type="text/javascript">
	function selectInput(choose, id) {
		document.getElementById(id).value = choose.options[choose.selectedIndex].value;
	}
</script>
<style>
</style>

</head>
<body style="background-color: #eeeeee">
	<div class="row-fluid">
		<div class="span12">
			<form class="form-horizontal" id="${action}"
				modelAttribute="${modelAttribute}" name="${action}" method="post"
				action="${path}/${action}.htm">
				<fieldset>
					<c:forEach var="tableproperty" items="${tablepropertys}"
						varStatus="status">

						<div class="form-group">
							<label for=${tableproperty.key } class="col-${screen}-2 control-label">${tableproperty.value}</label>
							<div class="col-${screen}-4">
								<input type="text" id="focusedInput" class="form-control"
									id="${tableproperty.key}" name="${tableproperty.key}"
									value="${tableline[tableproperty.key]}">
							</div>
						</div>
					</c:forEach>

					<c:forEach var="privilege" items="${tableline.privileges}"
						varStatus="status">
						<div class="form-group">
							<label for="select" class="col-${screen}-2 control-label">${privilege.criteria.name}</label>
							<div class="col-${screen}-4">
								<input id="privileges[${status.count-1}].programid"
									name="privileges[${status.count-1}].programid" type="hidden"
									value="${privilege.criteria.id}" /> <input
									id="privileges[${status.count-1}].privilege"
									name="privileges[${status.count-1}].privilege" type="hidden"
									value="" /> <select class="form-control"
									id="choose${status.count-1}"
									onchange="selectInput(this, 'privileges[${status.count-1}].privilege')">
									<option value="">无权限</option>
									<option value="R">可查询</option>
									<option value="RW">可修改</option>
									<option value="RWD">全控制</option>
								</select>
							</div>
						</div>
					</c:forEach>
					<div class="form-group">
						<div class="col-${screen}-4 col-${screen}-offset-2">
							<a href="javascript:history.go(-1)" class="btn btn-default">返回</a>
							<button type="submit" class="btn btn-primary">新建</button>
						</div>
					</div>
				</fieldset>
			</form>
		</div>
	</div>
</body>
</html>