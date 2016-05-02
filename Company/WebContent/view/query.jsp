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
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
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
function setPageNum(page){
	document.getElementById("page").value = page;
	document.getElementById("searchModule").submit();
}
function setSizeNum(size){
	document.getElementById("size").value = size;
	document.getElementById("searchModule").submit();
}

function submitForNextStep(actionHtm){
	if(actionHtm.indexOf("delete")>=0){
	 	if(!confirm("确定删除当前记录？")){
		 	return;
	 	}
	}else if(actionHtm.indexOf("submit")>=0){
		if(!confirm("确定核实当前记录？")){
			return;
		 }	
	}
	document.getElementById("searchModule").action = actionHtm;
	document.getElementById("searchModule").submit();
}

function showQueryModel(screen, title, pagelink){
	document.getElementById("mydalscreen").className = "modal-"+screen;
	document.getElementById("showViewTitle").innerHTML = title;
	document.getElementById("showViewContent").innerHTML = "<iframe src=\""+pagelink+"\" id=\"modalFrame\" name=\"modalFrame\" frameBorder=0 scrolling=yes width=\"100%\" onLoad=\"iFrameHeight(this)\"></iframe>";
}

$(function () 
	      { $("[data-toggle='popover']").popover();
	      });

</script>
</head>
<body style="background-color: #eeeeee">
	<form:form class="form-horizontal" id="searchModule" name="module"
		method="post" action="${path}/query${criteria}s.htm"
		target="contentFrame">
		<ul class="pagination">
			<c:choose>
				<c:when test="${page == '1'}">
					<li class="disabled"><a>上一页</a></li>
				</c:when>

				<c:when test="${page != '1'}">
					<li><a onclick="setPageNum(${page-1})">上一页</a></li>
				</c:when>
			</c:choose>
			<li class="disabled"><a>${page}</a></li>
			<c:choose>
				<c:when test="${lastpage == page}">
					<li class="disabled"><a>下一页</a></li>
				</c:when>
				<c:otherwise>
					<li><a onclick="setPageNum(${page+1})">下一页</a></li>
				</c:otherwise>
			</c:choose>
		</ul>

		<div class="dropdown">
			<a href="#" class="btn btn-primary btn-sm dropdown-toggle"
				data-toggle="dropdown" aria-expanded="false"> <strong>每页显示记录数</strong>
				<span class="caret"></span>
			</a>
			<ul class="dropdown-menu">
				<li><a onclick="setSizeNum(10)">10</a></li>
				<li><a onclick="setSizeNum(20)">20</a></li>
				<li><a onclick="setSizeNum(50)">50</a></li>
			</ul>
		</div>

		<table id="table" class="table table-striped table-hover ">
			<thead>
				<tr>
					<th>#</th>
					<c:forEach var="tableproperty" items="${tablepropertys}"
						varStatus="status">
						<th>${tableproperty.value}</th>
					</c:forEach>
					<c:if test="${criteria eq 'Good'}">
						<th>供应商</th>
					</c:if>
					<c:choose>
						<c:when test="${showoper eq 'no'}">
						</c:when>
						<c:otherwise>
							<th>操作</th>
						</c:otherwise>
					</c:choose>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="tableline" items="${tablelines}" varStatus="status">
					<tr>
						<td>${status.count}</td>
						<c:forEach var="tableproperty" items="${tablepropertys}"
							varStatus="status">
							<c:choose>
								<c:when test="${tableproperty.key eq 'companyid'}">
									<c:choose>
										<c:when
											test="${criteria eq 'SummaryCurrentFinanceReceivable'}">
											<td>${tableline.customer.name}</td>
										</c:when>
										<c:otherwise>
											<c:if test="${tableline.companytype eq 'supplier'}">
												<td>${tableline.supplier.supplier}</td>
											</c:if>
											<c:if test="${tableline.companytype eq 'customer'}">
												<td>${tableline.customer.name}</td>
											</c:if>
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:when test="${tableproperty.key eq 'needpay'}">
									<td><fmt:setLocale value="en_CN" /> <fmt:formatNumber
											value="${tableline.totalamount - tableline.nowpay}"
											type="currency" /></td>
								</c:when>
								<c:when test="${tableproperty.key eq 'financetype'}">
									<td>${tableline.financetypeinfo.name}</td>
								</c:when>
								<c:when test="${tableproperty.key eq 'goodid'}">
									<td>${tableline.good.goodname}</td>
								</c:when>
								<c:when test="${tableproperty.key eq 'goodsortid'}">
									<td>${tableline.product.goodname}</td>
								</c:when>
								<c:when test="${tableproperty.key eq 'customerid'}">
									<td>${tableline.customer.name}</td>
								</c:when>
								<c:when
									test="${tableproperty.key eq 'total' or tableproperty.key eq 'price' or tableproperty.key eq 'amount' or tableproperty.key eq 'totalamount' or tableproperty.key eq 'nowpay'}">
									<td><c:if test="${tableline[tableproperty.key] < 0 }">
											<font color="red">
										</c:if> <fmt:setLocale value="en_CN" /> <fmt:formatNumber
											value="${tableline[tableproperty.key]}" type="currency" /> <c:if
											test="${tableline[tableproperty.key] < 0 }">
											</font>
										</c:if></td>
								</c:when>
								<c:when
									test="${tableproperty.key eq 'saletime' or tableproperty.key eq 'purchasedate' or tableproperty.key eq 'create_time' or tableproperty.key eq 'lastlogintime' or tableproperty.key eq 'createTime' or tableproperty.key eq 'executedate' or tableproperty.key eq 'paydate'}">
									<td><fmt:formatDate
											value="${tableline[tableproperty.key]}" type="date"
											dateStyle="full" /></td>
								</c:when>
								<c:when test="${tableproperty.key eq 'note'}">
									<td><a class="btn btn-primary" data-container="body"
										data-toggle="popover" data-placement="right" data-html="true"
										data-content="${tableline[tableproperty.key]}">查看说明</a></td>
								</c:when>
								<c:otherwise>
									<td>${tableline[tableproperty.key]}</td>
								</c:otherwise>
							</c:choose>
						</c:forEach>
						<c:if test="${criteria eq 'Good'}">
							<td>${tableline.supplier.supplier}</td>
						</c:if>
						<c:choose>
							<c:when test="${showoper eq 'no'}">
							</c:when>
							<c:otherwise>
								<td><c:if
										test="${user.superuser ne 'R' and (criteria eq 'Purchase' or criteria eq 'Sale')}">
										<c:if
											test="${tableline.status eq '已拒绝' or tableline.status eq '新开单'}">
											<a
												onClick="submitForNextStep('submit${criteria}.htm?id=${tableline.id}');"
												target="contentFrame" class="btn btn-primary">提交</a>
											<c:if test="${criteria eq 'Purchase'}">
												<a
													onclick="showQueryModel('dialog','${title}', '${path}/editPurchaseLedger.htm?id=${tableline.id}');"
													data-toggle="modal" data-target="#myModal"
													class="btn btn-primary">采购入账记录</a>
											</c:if>
											<c:if test="${criteria eq 'Sale'}">
												<a
													onclick="showQueryModel('dialog','${title}', '${path}/editSaleLedger.htm?id=${tableline.id}');"
													data-toggle="modal" data-target="#myModal"
													class="btn btn-primary">销售入账记录</a>
											</c:if>
										</c:if>
									</c:if> <c:if
										test="${user.superuser ne 'R' and criteria eq 'Inventory'}">
										<c:if test="${tableline.status eq '运送中'}">
											<a
												onClick="submitForNextStep('submit${criteria}.htm?id=${tableline.id}');"
												target="contentFrame" class="btn btn-primary">确认入库</a>
										</c:if>
										<c:if test="${tableline.status eq '待出货'}">
											<a
												onClick="submitForNextStep('submit${criteria}.htm?id=${tableline.id}');"
												target="contentFrame" class="btn btn-primary">确认出货</a>
										</c:if>
									</c:if> <c:if
										test="${user.superuser ne 'R' and ( criteria eq 'Ledger' or criteria eq 'Inventory')}">
										<a
											onclick="showQueryModel('dialog', '${title}', '${path}/edit${criteria}Proof.htm?id=${tableline.id}');"
											data-toggle="modal" data-target="#myModal"
											class="btn btn-primary">更改票据</a>
									</c:if> <c:if
										test="${criteria eq 'Ledger' or criteria eq 'Inventory'}">
										<a
											onclick="showQueryModel('${screen}', '${title}', '${path}/query${criteria}Proof.htm?id=${tableline.id}');"
											data-toggle="modal" data-target="#myModal"
											class="btn btn-success">查看单据</a>
									</c:if> <c:if test="${criteria ne 'Inventory'}">
										<c:choose>
											<c:when test="${user.superuser eq 'R'}">
											</c:when>
											<c:when
												test="${criteria eq 'Purchase' or criteria eq 'Sale'}">
												<c:if
													test="${tableline.status eq '已拒绝' or tableline.status eq '新开单' or tableline.status eq ''}">
													<a
														onclick="showQueryModel('dialog','${title}', '${path}/edit${criteria}.htm?id=${tableline.id}');"
														data-toggle="modal" data-target="#myModal"
														class="btn btn-primary">修改</a>
													<a
														onclick="submitForNextStep('delete${criteria}.htm?id=${tableline.id}');"
														target="contentFrame" class="btn btn-danger">删除</a>
												</c:if>
											</c:when>
											<c:otherwise>
												<a
													onclick="showQueryModel('dialog','${title}', '${path}/edit${criteria}.htm?id=${tableline.id}');"
													data-toggle="modal" data-target="#myModal"
													class="btn btn-primary">修改</a>
												<a
													onclick="submitForNextStep('delete${criteria}.htm?id=${tableline.id}');"
													target="contentFrame" class="btn btn-danger">删除</a>
											</c:otherwise>
										</c:choose>

									</c:if></td>
							</c:otherwise>
						</c:choose>
					</tr>
				</c:forEach>
			</tbody>
		</table>

		<ul class="pagination">
			<c:choose>
				<c:when test="${page == '1'}">
					<li class="disabled"><a>上一页</a></li>
				</c:when>

				<c:when test="${page != '1'}">
					<li><a onclick="setPageNum(${page-1})">上一页</a></li>
				</c:when>
			</c:choose>
			<li class="disabled"><a>${page}</a></li>
			<c:choose>
				<c:when test="${lastpage == page}">
					<li class="disabled"><a>下一页</a></li>
				</c:when>
				<c:otherwise>
					<li><a onclick="setPageNum(${page+1})" type="submit">下一页</a></li>
				</c:otherwise>
			</c:choose>
		</ul>
		<c:forEach var="searchFactor" items="${searchFactors}"
			varStatus="status">
			<input type="hidden" id="${searchFactor.key}"
				name="${searchFactor.key}" value="${searchFactor.value}" />
		</c:forEach>
	</form:form>

	<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-${screen}" id="mydalscreen">
			<div class="modal-content" style="background-color: #eeeeee">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="showViewTitle"></h4>
				</div>
				<div class="modal-body" id="showViewContent"></div>
				<div class="modal-footer">
					<button id="cancelModal" type="button" class="btn btn-default"
						data-dismiss="modal">返回</button>
				</div>
			</div>
		</div>
	</div>

</body>
</html>