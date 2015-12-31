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

</head>
<body style="background-color: #eeeeee">
	<div class="row-fluid">
		<div class="span12">
			<div class="col-${screen}-12">
				<div class="col-${screen}-3"></div>
				<div class="col-${screen}-6">
					<form:form class="form-horizontal" id="${action}"
						modelAttribute="${modelAttribute}" name="${action}" method="post"
						action="${path}/${action}.htm" enctype="multipart/form-data">
						<fieldset>
							<c:forEach var="tableproperty" items="${tablepropertys}"
								varStatus="status">

								<div class="form-group">

									<c:choose>
										<c:when test="${tableproperty.key eq 'id'}">
											<input type="hidden" class="form-control"
												id="${tableproperty.key}" name="${tableproperty.key}"
												value="${tableline[tableproperty.key]}">
										</c:when>

									</c:choose>

								</div>
								<div class="form-group">
									<label class="col-${screen}-4 control-label">凭据</label>
									<div class="col-${screen}-8">
										<div class="input-group">
											<input type="file" name="img" id="img" class="form-control" style="display: none;" onchange="imgPath.value=this.value">
											<input type="text" name="imgPath" id="imgPath" class="form-control"> <span
												class="input-group-btn">
												<button class="btn btn-primary" type="button"
													onclick="img.click();">上传照片</button>
											</span>
										</div>
									</div>
								</div>

							</c:forEach>


							<div class="form-group">
								<div class="col-${screen}-10 col-xs-offset-2 pull-right">
									<a onclick="closeModal();" class="btn btn-default">返回</a>
									<button type="submit" class="btn btn-primary">提交</button>
								</div>
							</div>
						</fieldset>
					</form:form>
				</div>
				<div class="col-${screen}-3"></div>
			</div>
		</div>
	</div>
</body>
</html>