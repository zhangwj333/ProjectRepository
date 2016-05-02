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
<script type="text/javascript">
	function selectInput(choose, id) {
		document.getElementById(id).value = choose.options[choose.selectedIndex].value;
	}

	function closeModal() {
		window.parent.document.getElementById("cancelModal").click();
	}
	function selectType(choose, id) {
		var selected = choose.options[choose.selectedIndex].value;
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

</head>
<body style="background-color: #eeeeee">
	<div class="row-fluid">
		<div class="span12">
			<div class="col-${screen}-12">
				<div class="col-${screen}-3"></div>
				<div class="col-${screen}-6">
					<form:form class="form-horizontal" id="${action}"
						modelAttribute="${modelAttribute}" name="${action}" method="post"
						action="${path}/${action}.htm">
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
										<c:otherwise>
											<label for=${tableproperty.key }
												class="col-${screen}-2 control-label">${tableproperty.value}</label>
											<div class="col-${screen}-10">
												<c:choose>
													<c:when test="${tableproperty.key eq 'companytype'}">
														<input id="${tableproperty.key}"
															name="${tableproperty.key}" type="hidden" value="${tableline[tableproperty.key]}" />
														<select class="form-control" id="choose${status.count-1}"
															onchange="selectType(this, '${tableproperty.key}')" ${disable}>
															<option value="">必选</option>
															<c:choose>
																<c:when
																	test="${'supplier' eq tableline[tableproperty.key]}">
																	<option value="supplier" selected="selected">供应商</option>
																	<option value="customer">客户</option>
																</c:when>
																<c:when
																	test="${'customer' eq tableline[tableproperty.key]}">
																	<option value="supplier">供应商</option>
																	<option value="customer" selected="selected">客户</option>
																</c:when>
																<c:otherwise>
																	<option value="supplier">供应商</option>
																	<option value="customer">客户</option>
																</c:otherwise>
															</c:choose>

														</select>
													</c:when>
													<c:when test="${tableproperty.key eq 'companyid'}">
														<input id="${tableproperty.key}"
															name="${tableproperty.key}" type="hidden" value="${tableline[tableproperty.key]}" />
														<select class="form-control" id="choosecompanyid"
															onchange="selectInput(this, '${tableproperty.key}')" ${disable}>
															<option value="">必选</option>
															<c:if test="${!empty suppliers}">
																<c:if
																	test="${'supplier' eq tableline['companytype']}">
																	<c:forEach var='supplier' items='${suppliers}'
																		varStatus='status'>"
																		<c:choose>
																			<c:when
																				test="${supplier.id eq tableline[tableproperty.key]}">
																				<option value='${supplier.id}' selected="selected">${supplier.supplier}</option>
																			</c:when>
																			<c:otherwise>
																				<option value='${supplier.id}'>${supplier.supplier}</option>
																			</c:otherwise>
																		</c:choose>
																	</c:forEach>
																</c:if>
															</c:if>
															<c:if test="${!empty customers}">
																<c:if
																	test="${'customer' eq tableline['companytype']}">
																	<c:forEach var='customer' items='${customers}'
																		varStatus='status'>
																		<c:choose>
																			<c:when
																				test="${customer.id eq tableline[tableproperty.key]}">
																				<option value='${customer.id}' selected="selected">${customer.name}</option>
																			</c:when>
																			<c:otherwise>
																				<option value='${customer.id}'>${customer.name}</option>
																			</c:otherwise>
																		</c:choose>
																	</c:forEach>
																</c:if>
															</c:if>

														</select>
													</c:when>
													<c:when test="${tableproperty.key eq 'financetype'}">
														<input id="${tableproperty.key}"
															name="${tableproperty.key}" type="hidden" value="${tableline[tableproperty.key]}" />
														<select class="form-control" id="choose${status.count-1}"
															onchange="selectInput(this, '${tableproperty.key}')" ${disable}>
															<option value="">必选</option>
															<c:forEach var="type" items="${types}" varStatus="status">
																<c:choose>
																	<c:when
																		test="${type.id eq tableline[tableproperty.key]}">
																		<option value="${type.id}" selected="selected">${type.name}</option>
																	</c:when>
																	<c:otherwise>
																		<option value="${type.id}">${type.name}</option>
																	</c:otherwise>
																</c:choose>
															</c:forEach>
														</select>
													</c:when>
													<c:when test="${tableproperty.key eq 'payman'}">
														<input id="${tableproperty.key}"
															name="${tableproperty.key}" type="hidden" value="${tableline[tableproperty.key]}" />
														<select class="form-control" id="choose${status.count-1}"
															onchange="selectInput(this, '${tableproperty.key}')">
															<option value="">必选</option>
															<c:forEach var="type" items="${paymentaccounts}"
																varStatus="status">
																<c:choose>
																	<c:when
																		test="${type.payaccount eq tableline[tableproperty.key]}">
																		<option value="${type.payaccount}" selected="selected">${type.payaccount}</option>
																	</c:when>
																	<c:otherwise>
																		<option value="${type.payaccount}">${type.payaccount}</option>
																	</c:otherwise>
																</c:choose>
															</c:forEach>												
														</select>
													</c:when>
													<c:when test="${tableproperty.key eq 'paydate'}">
														<input type="date" class="form-control"
															id="${tableproperty.key}" name="${tableproperty.key}"
															value=<fmt:formatDate value="${tableline[tableproperty.key]}" pattern="yyyy-MM-dd" />>
													</c:when>
													<c:when test="${tableproperty.key eq 'note'}">
														<textarea class="form-control" rows="10"
															id="${tableproperty.key}" name="${tableproperty.key}">${tableline[tableproperty.key]}</textarea>
													</c:when>
													<c:when test="${tableproperty.key eq 'purchasename'}">
														<input type="text" class="form-control" disabled=""
															id="${tableproperty.key}" name="${tableproperty.key}"
															value="${tableline[tableproperty.key]}">
													</c:when>
													<c:when test="${tableproperty.key eq 'goodid'}">
														<input id="${tableproperty.key}"
															name="${tableproperty.key}" type="hidden" value="${tableline[tableproperty.key]}" />
														<select class="form-control" id="choose${status.count-1}"
															onchange="selectInput(this, '${tableproperty.key}')">
															<option value="">必选</option>
															<c:forEach var="good" items="${goods}" varStatus="status">
																<c:choose>
																	<c:when
																		test="${good.id eq tableline[tableproperty.key]}">
																		<option value="${good.id}" selected="selected">${good.goodname}|
																			${good.supplier.supplier}</option>
																	</c:when>
																	<c:otherwise>
																		<option value="${good.id}">${good.goodname}|
																			${good.supplier.supplier}</option>
																	</c:otherwise>
																</c:choose>
															</c:forEach>
														</select>
													</c:when>
													<c:when test="${tableproperty.key eq 'status'}">
														<input id="${tableproperty.key}"
															name="${tableproperty.key}" type="hidden"
															value="${tableline[tableproperty.key]}" />
														<select class="form-control"
															id="choose${tableproperty.key}"
															onchange="selectInput(this, '${tableproperty.key}')">
															<option value="">必选</option>
															<c:forEach var="Option" items="${statusOptions}"
																varStatus="status">
																<c:choose>
																	<c:when
																		test="${Option eq tableline[tableproperty.key]}">
																		<option value="${Option}" selected="selected">${Option}</option>
																	</c:when>
																	<c:otherwise>
																		<option value="${Option}">${Option}</option>
																	</c:otherwise>
																</c:choose>
															</c:forEach>
														</select>
													</c:when>
													<c:when test="${tableproperty.key eq 'goodsortid'}">
														<input id="${tableproperty.key}"
															name="${tableproperty.key}" type="hidden" value="${tableline[tableproperty.key]}" />
														<select class="form-control"
															id="choose${tableproperty.key}"
															onchange="selectInput(this, '${tableproperty.key}')">
															<option value="">必选</option>
															<c:forEach var="types" items="${types}"
																varStatus="status">
																<option value="${types.id}">${types.goodname}</option>
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
										</c:otherwise>
									</c:choose>
								</div>

							</c:forEach>


							<div class="form-group">
								<div class="col-${screen}-10 col-xs-offset-2 pull-right">
									<a onclick="closeModal();" class="btn btn-default">返回</a>
									<button type="submit" class="btn btn-primary">修改</button>
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