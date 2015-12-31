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
				method="post" action="${path}/queryUsers.htm" target="contentFrame">
					<legend>
						<strong>搜索</strong>
					</legend>
				<fieldset>
					<input name="page" type="hidden" value="1" /> 
					<input name="size" type="hidden" value="10" />
					<div class="form-group">
						<label class="col-md-2 control-label">模块</label>
						<div class="col-md-4">
							<select class="form-control" id="select"
								onchange="selectSearchModule(this, module)">
								<option value="Users" selected="selected">用户查询</option>
							</select>
						</div>
					</div>
					<div class="span12">
						<div class="form-group">
							<label class="col-md-2 control-label">ID</label>
							<div class="col-md-4">
								<input type="text" class="form-control" id="id" name="id"/>
							</div>

							<label class="col-md-2 control-label">用户名</label>
							<div class="col-md-4">
								<input type="text" class="form-control" id="username" name="username" />
							</div>
						</div>
					</div>
					<div class="span12">
						<div class="form-group">
							<label class="col-md-2 control-label">开始时间</label>
							<div class="col-md-4">
								<input type="date" class="form-control" id="startdate" name="startdate"/>
							</div>

							<label class="col-md-2 control-label">结束时间</label>
							<div class="col-md-4">
								<input type="date" class="form-control" id="enddate" name="enddate" />
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-md-4 col-md-offset-2">
							<button id="searchSubmit" type="submit"
								class="btn btn-primary">查询</button>
						</div>
					</div>
				</fieldset>
			</form:form>
		</div>
	</div>
</body>
</html>