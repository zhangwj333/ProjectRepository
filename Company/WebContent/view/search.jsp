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

<title>Menu</title>

<c:set value="${pageContext.request.contextPath}" var="path"
	scope="page" />

<link rel="stylesheet" href="${path}/css/bootstrap.css" media="screen">
<link rel="stylesheet" href="${path}/css/bootswatch.min.css">

<script src="${path}/js/jquery-1.10.2.min.js"></script>
<script src="${path}/js/bootstrap.min.js"></script>
<script src="${path}/js/bootswatch.js"></script>
<script src="${path}/js/junstech.js"></script>
</head>
<body style="background-color: #eeeeee">
	<div class="row-fluid">
		<div class="span12">
			<form:form class="form-horizontal" id="module" name="module"
				method="post" action="" target="contentFrame">
				<legend>
						<strong>搜索</strong>
				</legend>
				<fieldset>
					<input name="page" type="hidden" value="1" /> <input name="size"
						type="hidden" value="10" />
					<div class="form-group">
						<label class="col-md-2 control-label">模块</label>
						<div class="col-md-4">
							<select class="form-control" id="select"
								onchange="selectSearchModule(this, module)">
								<option value="">模块选择</option>
								<option value="Purchase">采购查询</option>
								<option value="Sale">销售查询</option>
								<option value="Finance">财务查询</option>
								<option value="Users">用户查询</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<div class="col-md-4 col-md-offset-2">
							<button id="searchSubmit" type="reset"
								class="btn btn-primary disabled">查询</button>
						</div>
					</div>
				</fieldset>
			</form:form>
		</div>
	</div>
</body>
</html>