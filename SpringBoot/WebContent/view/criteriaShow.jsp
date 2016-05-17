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
	<div class="row-fluid">
		<div class="span12">
				<table id="table" class="table table-striped table-hover ">
					<tbody>

						<c:forEach var="tableproperty" items="${tablepropertys}"
							varStatus="status">
							<tr>
								<td><label class="control-label pull-right">${tableproperty.value}</label></td>
								<c:choose>
								<c:when test="${tableproperty.key eq 'goodid'}">
									<td>${tableline.good.goodname}</td>
								</c:when>
								<c:when test="${tableproperty.key eq 'goodsortid'}">
									<td>${tableline.product.goodname}</td>
								</c:when>
								<c:when test="${tableproperty.key eq 'customerid'}">
									<td>${tableline.customer.name}</td>
								</c:when>
								<c:when test="${tableproperty.key eq 'total'}">
									<td><fmt:setLocale value="en_CN" /> <fmt:formatNumber
											value="${tableline[tableproperty.key]}" type="currency" /></td>
								</c:when>
								<c:when test="${tableproperty.key eq 'price'}">
									<td><fmt:setLocale value="en_CN" /> <fmt:formatNumber
											value="${tableline[tableproperty.key]}" type="currency" /></td>
								</c:when>
								<c:when
									test="${tableproperty.key eq 'saletime' or tableproperty.key eq 'purchasedate' or tableproperty.key eq 'create_time' or tableproperty.key eq 'lastlogintime' or tableproperty.key eq 'createTime'}">
									<td><fmt:formatDate
											value="${tableline[tableproperty.key]}" type="date"
											dateStyle="full" /></td>
								</c:when>
								<c:otherwise>
								<td>${tableline[tableproperty.key]}</td>
								</c:otherwise>
								</c:choose>
							</tr>
						</c:forEach>
					</tbody>
				</table>
		</div>
	</div>
</body>
</html>