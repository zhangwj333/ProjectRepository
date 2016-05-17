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
	window.onload = setCount; 
	var count = 0;
	function setCount(){ 
		count = ${fn:length(salesubs)};
		for (i = 0; i < count; i++) {
		}
	}
	
	function closeModal() {
		window.parent.document.getElementById("cancelModal").click();
	}
	
	function getTotal() {
		var total = 0;
		for (i = 0; i < count; i++) {
			if (document.getElementById("salesubs["+i+"].price") != null) {				
				total = total+Number(document.getElementById("salesubs["+i+"].price").value);
			}
		}
		document.getElementById("total").value = total;
	}
	
	function clearsalesub(index){
		document.getElementById("salesubs["+index+"]").innerHTML = "";
		getTotal();
	}
	
	function deletesalesub(index) {
		var node = document.getElementById("salesubs");
		var childs = node.childNodes;
		for (i = 0; i < childs.length; i++) {
			if (node.childNodes[i].nodeName == ("SALESUB" + index)) {
				node.removeChild(node.childNodes[i]);
			}
		}
		getTotal();
	}

	function addsalesub() {
		var node = document.getElementById("salesubs");
		var newElement = document.createElement("salesub" + count);
		newElement.innerHTML = createSaleSub(count);
		node.appendChild(newElement);
		count = count + 1;
	}
	function createSaleSub(index) {
		return "<div class='form-group' id='salesubs["+index+"]' name='salesubs["+index+"]'>"
				+ "<div class='form-group'>"
				+ "<label class='col-${screen}-1 control-label'>商品</label>"
				+ "</div>"
				+ "<div class='form-group'><div class='col-${screen}-4 '>"
				+ "<input id='salesubs["+index+"].goodid' name='salesubs["+index+"].goodid' type='hidden' value='' />"
				+ "<select class='form-control'	id='choose"
				+ index
				+ "' onchange=\"selectInput(this, 'salesubs["
				+ index
				+ "].goodid')\">"
				+ "<option value=''>必选</option>"
				+ "<c:forEach var='product' items='${products}' varStatus='status'>"
				+ "<option value='${product.id}'>${product.goodname}</option>"
				+ "</c:forEach>"
				+ "</select>"
				+ "</div>"
				+ "<div class='col-${screen}-4'>"
				+ "<input type='date' class='form-control' id='salesubs["+index+"].opertime' name='salesubs["+index+"].opertime'	placeholder='送货时间'>"
				+ "</div>"
				+ "</div>"
				+ "<div class='form-group'>"
				+ "<div class='col-${screen}-4 '>"
				+ "<input type='text' class='form-control' id='salesubs["+index+"].price' name='salesubs["+index+"].price' placeholder='价格' onchange=\"getTotal();\">"
				+ "</div>"
				+ "<div class='col-${screen}-4'>"
				+ "<input type='text' class='form-control' id='salesubs["+index+"].goodqty' name='salesubs["+index+"].goodqty' placeholder='数量（吨）'>"
				+ "</div>"
				+ "<div class='col-${screen}-4'>"
				+ "<a onclick='deletesalesub("
				+ index
				+ ");' class='btn btn-danger'>删除</a>"
				+ "</div>"
				+ "</div>"
				+ "</div>";
	}
</script>
<style>
</style>

</head>
<body style="background-color: #eeeeee">
	<div class="row-fluid">
		<div class="span12">
			<form class="form-horizontal" id="${action}" name="${action}"
				method="post" action="${path}/${action}.htm">
				<fieldset>
					<div class="col-${screen}-12">
						<div class="col-${screen}-6">
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
												class="col-${screen}-4 control-label">${tableproperty.value}</label>
											<div class="col-${screen}-8">
												<c:choose>
													<c:when test="${tableproperty.key eq 'customerid'}">
														<input id="${tableproperty.key}"
															name="${tableproperty.key}" type="hidden"
															value="${tableline[tableproperty.key]}" />
														<select class="form-control"
															id="choose${tableproperty.key}"
															onchange="selectInput(this, '${tableproperty.key}')">
															<option value="">必选</option>
															<c:forEach var="customer" items="${customers}"
																varStatus="status">
																<c:choose>
																	<c:when
																		test="${customer.id eq tableline[tableproperty.key]}">
																		<option value="${customer.id}" selected="selected">${customer.name}|${customer.credit}</option>
																	</c:when>
																	<c:otherwise>
																		<option value="${customer.id}">${customer.name}|${customer.credit}</option>
																	</c:otherwise>
																</c:choose>
															</c:forEach>
														</select>
													</c:when>
													<c:when
														test="${tableproperty.key eq 'total' or tableproperty.key eq 'price'}">
														<input id="${tableproperty.key}"
															name="${tableproperty.key}" type="text"
															class="form-control" value="${tableline[tableproperty.key]}" onchange="getTotal();" />
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
													<c:when test="${tableproperty.key eq 'note'}">
														<textarea class="form-control" rows="10"
															id="${tableproperty.key}" name="${tableproperty.key}">${tableline[tableproperty.key]}</textarea>
													</c:when>
													<c:otherwise>
														<input type="text" class="form-control"
															id="${tableproperty.key}" name="${tableproperty.key}"
															value="${tableline[tableproperty.key]}">
													</c:otherwise>
												</c:choose>
											</div>
										</c:otherwise>
									</c:choose>
								</div>
							</c:forEach>
						</div>
						<div class="col-${screen}-6">
							<div id="salesubs">
								<c:forEach var="salesub" items="${salesubs}" varStatus="status">
									<div class="form-group" id="salesubs[${status.index}]"
										name="SALESUB${status.index}">
										<div class="form-group">
											<label class="col-${screen}-1 control-label">商品</label>
										</div>
										<div class="form-group">
											<div class="col-${screen}-4 ">
												<input id="salesubs[${status.index}].goodid"
													name="salesubs[${status.index}].goodid" type="hidden"
													value="${salesub.goodid}" /> <select class="form-control"
													id="choose${status.index}"
													onchange="selectInput(this,'salesubs[${status.index}].goodid')">
													<option value="">必选</option>
													<c:forEach var="product" items="${products}">
														<c:choose>
															<c:when test="${product.id eq salesub.goodid}">
																<option value="${product.id}" selected="selected">${product.goodname}</option>
															</c:when>
															<c:otherwise>
																<option value="${product.id}">${product.goodname}</option>
															</c:otherwise>
														</c:choose>
													</c:forEach>
												</select>
											</div>
											<div class="col-${screen}-4">
												<input type="date" class="form-control"
													id="salesubs[${status.index}].opertime"
													name="salesubs[${status.index}].opertime"
													placeholder="送货时间"
													value=<fmt:formatDate value="${salesub.opertime}" pattern="yyyy-MM-dd" />>
											</div>
										</div>
										<div class="form-group">
											<div class="col-${screen}-4 ">
												<input type="text" class="form-control"
													id="salesubs[${status.index}].price"
													name="salesubs[${status.index}].price" placeholder="价格"
													value="${salesub.price}" onchange="getTotal();">
											</div>
											<div class="col-${screen}-4">
												<input type="text" class="form-control"
													id="salesubs[${status.index}].goodqty"
													name="salesubs[${status.index}].goodqty"
													placeholder="数量（吨）" value="${salesub.goodqty}">
											</div>
											<div class="col-${screen}-4">
												<a onclick="clearsalesub(${status.index});"
													class="btn btn-danger">删除</a>
											</div>
										</div>
									</div>
								</c:forEach>
							</div>
							<div class="form-group">
								<div class="col-${screen}-4 ">
									<a onclick="addsalesub();" class="btn btn-success">添加商品</a>
								</div>
								<div class="col-${screen}-4 ">
									<a onclick="closeModal();" class="btn btn-default">返回</a>
									<button type="submit" class="btn btn-primary">更新</button>
								</div>
							</div>
						</div>
					</div>
				</fieldset>
			</form>
		</div>
	</div>
</body>
</html>