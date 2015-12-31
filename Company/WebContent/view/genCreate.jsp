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
	function selectInput(choose, id) {
		document.getElementById(id).value = choose.options[choose.selectedIndex].value;
	}
	function selectType(choose, id) {
		var selected =  choose.options[choose.selectedIndex].value;
		document.getElementById(id).value = selected;
		var options;
		if (selected == 'supplier') {
			options = "<option value=''>必选</option>"
					+ "<c:forEach var='supplier' items='${suppliers}' varStatus='status'>"
					+ "<option value='${supplier.id}'>${supplier.supplier}</option>"
					+ "</c:forEach>";
		} else if (selected == 'customer') {
			options = "<option value=''>必选</option>"
					+ "<c:forEach var='customer' items='${customers}' varStatus='status'>"
					+ "<option value='${customer.id}'>${customer.name}</option>"
					+ "</c:forEach>";
		}
		document.getElementById('choosecompanyid').innerHTML = options;
	}
</script>
<style>
</style>

</head>
<body style="background-color: #eeeeee">
	<div class="row-fluid">
		<div class="span12">
			<div class="col-${screen}-12">
				<div class="col-${screen}-8">
					<form class="form-horizontal" id="${action}"
						modelAttribute="${modelAttribute}" name="${action}" method="post"
						action="${path}/${action}.htm">
						<fieldset>
							<c:forEach var="tableproperty" items="${tablepropertys}"
								varStatus="status">

								<div class="form-group">
									<label for=${tableproperty.key }
										class="col-${screen}-4 control-label">${tableproperty.value}</label>
									<div class="col-${screen}-8">
										<c:choose>
											<c:when test="${tableproperty.key eq 'companytype'}">
												<input id="${tableproperty.key}" name="${tableproperty.key}"
													type="hidden" value="" />
												<select class="form-control" id="choose${status.count-1}"
													onchange="selectType(this, '${tableproperty.key}')">
													<option value="">必选</option>
													<option value="supplier">供应商</option>
													<option value="customer">客户</option>
												</select>
											</c:when>
											<c:when test="${tableproperty.key eq 'companyid'}">
												<input id="${tableproperty.key}" name="${tableproperty.key}"
													type="hidden" value="" />
												<select class="form-control" id="choosecompanyid"
													onchange="selectInput(this, '${tableproperty.key}')">
													<option value="">必选</option>
												</select>
											</c:when>
											<c:when test="${tableproperty.key eq 'financetype'}">
												<input id="${tableproperty.key}" name="${tableproperty.key}"
													type="hidden" value="" />
												<select class="form-control" id="choose${status.count-1}"
													onchange="selectInput(this, '${tableproperty.key}')">
													<option value="">必选</option>
													<c:forEach var="type" items="${types}" varStatus="status">
														<option value="${type.id}">${type.name}</option>
													</c:forEach>
												</select>
											</c:when>
											<c:when test="${tableproperty.key eq 'paydate'}">
												<input type="date" class="form-control"
													id="${tableproperty.key}" name="${tableproperty.key}"
													value="${tableline[tableproperty.key]}">
											</c:when>
											<c:when test="${tableproperty.key eq 'note'}">
												<textarea class="form-control" rows="10"
													id="${tableproperty.key}" name="${tableproperty.key}">${tableline[tableproperty.key]}</textarea>
											</c:when>
											<c:when test="${tableproperty.key eq 'goodid'}">
												<input id="${tableproperty.key}" name="${tableproperty.key}"
													type="hidden" value="" />
												<select class="form-control" id="choose${status.count-1}"
													onchange="selectInput(this, '${tableproperty.key}')">
													<option value="">必选</option>
													<c:forEach var="good" items="${goods}" varStatus="status">
														<option value="${good.id}">${good.goodname}|
															${good.supplier.supplier}</option>
													</c:forEach>
												</select>
											</c:when>
											<c:when test="${tableproperty.key eq 'status'}">
												<input id="${tableproperty.key}" name="${tableproperty.key}"
													type="hidden" value="" />
												<select class="form-control" id="choose${tableproperty.key}"
													onchange="selectInput(this, '${tableproperty.key}')">
													<option value="">必选</option>
													<c:forEach var="Option" items="${statusOptions}"
														varStatus="status">
														<option value="${Option}">${Option}</option>
													</c:forEach>
												</select>
											</c:when>
											<c:otherwise>
												<input type="text" id="focusedInput" class="form-control"
													id="${tableproperty.key}" name="${tableproperty.key}"
													value="${tableline[tableproperty.key]}">
											</c:otherwise>
										</c:choose>
									</div>
								</div>
							</c:forEach>
							<div class="form-group">
								<div class="col-${screen}-8 col-${screen}-offset-4">
									<a href="javascript:history.go(-1)" class="btn btn-default">返回</a>
									<button type="submit" class="btn btn-primary">新建</button>
								</div>
							</div>
						</fieldset>
					</form>
				</div>
				<div class="col-${screen}-2"></div>
			</div>
		</div>
	</div>
</body>
</html>