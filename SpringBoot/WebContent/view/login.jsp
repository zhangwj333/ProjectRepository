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
<title>Junstech</title>
<c:set value="${pageContext.request.contextPath}" var="path" scope="page" />
<link rel="shortcut icon" href="${path}/img/icon.png" type="image/x-icon" />
<link rel="stylesheet" href="${path}/css/bootstrap.css">
<link rel="stylesheet" href="${path}/css/bootswatch.min.css">

<script src="${path}/js/jquery-1.10.2.min.js"></script>
<script src="${path}/js/bootstrap.min.js"></script>
<script src="${path}/js/bootswatch.js"></script>

</head>
<body style="background-color: #eeeeee">
	<div class="container">
		<div class="row">
			<div class="span12"></div>
		</div>
		<div class="row">
			<div class="col-md-3"></div>
			<div class="col-md-5">
				<h1>
					<strong>公司管理系统</strong>
				</h1>
				<form:form class="form-horizontal" id="userLogin"
					modelAttribute="user" name="userLogin" method="post"
					action="${path}/userLogin.htm">
					<fieldset>
						<legend>登陆</legend>
						<div class="form-group">
							<label for="username" class="col-md-2 control-label">用户名</label>
							<div class="col-md-10">
								<input type="text" class="form-control" id="username"
									name="username" placeholder="用户名">
							</div>
						</div>
						<div class="form-group">
							<label for="password" class="col-md-2 control-label">密码</label>
							<div class="col-md-10">
								<input type="password" class="form-control" id="password"
									name="password" placeholder="密码">
							</div>
						</div>
						<div class="form-group">
							<div class="col-md-10 col-md-offset-2">
								<button type="reset" class="btn btn-default">重置</button>
								<button type="submit" class="btn btn-primary">登录</button>
							</div>
						</div>
					</fieldset>
				</form:form>
				<strong>${message}</strong>
			</div>
			<div class="col-md-4"></div>
		</div>
	</div>
	<div class="row">
		<div class="span12"></div>
	</div>
</body>