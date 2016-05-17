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

<title>Header</title>

<c:set value="${pageContext.request.contextPath}" var="path"
	scope="page" />
<script type="text/javascript">
	function genLatestReportLink() {
		var today = new Date();
		var year = today.getFullYear();
		var month = today.getMonth();
		if (month == 0) {
			year = year - 1;
			month = 12;
		}
		if (month < 10) {
			month = "0" + month;
		}
		document.getElementById("latestReport").innerHTML = "<a href='${path}/queryReportProcess.htm?time="
				+ year
				+ month
				+ "' target='contentFrame' onclick='foldMenu()'>查看最新报表"
				+ year
				+ month + "</a>";
	}
</script>
</head>
<body style="background-color: #eeeeee" onload="genLatestReportLink()"
	style="overflow:hidden">
	<div class="navbar navbar-default navbar-fixed-top">
		<div class="container-fluid">

			<div class="navbar-header">
				<a href="${path}/view/content.jsp" target="contentFrame"
					class="navbar-brand"><b>你好, ${user.nickname}</b></a>
				<button class="navbar-toggle" type="button" data-toggle="collapse"
					data-target="#navbar-main">
					<span class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
			</div>
			<div class="navbar-collapse collapse" id="navbar-main">
				<ul class="nav navbar-nav ">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown" aria-expanded="false"> <strong>采购模块</strong>
							<span class="caret"></span>
					</a>
						<ul class="dropdown-menu">
							<li><a onclick="getSearchMenuSubFields('Purchase')">查询订单</a></li>
							<li><a href="${path}/createPurchase.htm"
								target="contentFrame" onclick="foldMenu()">新增订单</a></li>
							<li><a href="${path}/queryGoods.htm?id=&key=&page=1&size=10"
								target="contentFrame" onclick="foldMenu()">查询商品</a></li>
							<li><a href="${path}/createGood.htm" target="contentFrame"
								onclick="foldMenu()">新增商品</a></li>
							<li><a
								href="${path}/querySuppliers.htm?id=&key=&page=1&size=10"
								target="contentFrame" onclick="foldMenu()">查询供应商</a></li>
							<li><a href="${path}/createSupplier.htm"
								target="contentFrame" onclick="foldMenu()">新增供应商</a></li>
						</ul></li>

					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown" aria-expanded="false"> <strong>销售模块</strong>
							<span class="caret"></span>
					</a>
						<ul class="dropdown-menu">
							<li><a onclick="getSearchMenuSubFields('Sale')">查询销售单</a></li>
							<li><a href="${path}/createSale.htm" target="contentFrame"
								onclick="foldMenu()">新增销售单</a></li>
							<li><a
								href="${path}/queryCustomers.htm?id=&key=&page=1&size=10"
								target="contentFrame" onclick="foldMenu()">查询客户</a></li>
							<li><a href="${path}/createCustomer.htm"
								target="contentFrame" onclick="foldMenu()">新增客户</a></li>
						</ul></li>

					<c:if test="${user.privileges[4].privilege ne ''}">
						<li class="dropdown"><a href="#" class="dropdown-toggle"
							data-toggle="dropdown" aria-expanded="false"> <strong>库存模块</strong>
								<span class="caret"></span>
						</a>
							<ul class="dropdown-menu">
								<li><a
									href="${path}/querySummaryInventorys.htm?id=&key=&page=1&size=10"
									target="contentFrame" onclick="foldMenu()">查询库存</a></li>
								<li><a onclick="getSearchMenuSubFields('Inventory')">管理库存</a></li>
								<li><a href="${path}/createInventory.htm"
									target="contentFrame" onclick="foldMenu()">新增库存</a></li>
								<li><a
									href="${path}/queryProducts.htm?id=&key=&page=1&size=10"
									target="contentFrame" onclick="foldMenu()">查询商品种类</a></li>
								<li><a href="${path}/createProduct.htm"
									target="contentFrame" onclick="foldMenu()">新增商品种类</a></li>
							</ul></li>
					</c:if>
					<c:if test="${user.privileges[0].privilege ne ''}">
						<li class="dropdown"><a href="#" class="dropdown-toggle"
							data-toggle="dropdown" aria-expanded="false"> <strong>财务模块</strong>
								<span class="caret"></span>
						</a>
							<ul class="dropdown-menu">
								<li><a
									href="${path}/querySummaryCurrentFinanceReceivables.htm?id=&key=&page=1&size=10"
									target="contentFrame" onclick="foldMenu()">查看待收情况</a></li>
								<li><a onclick="getSearchMenuSubFields('Finance')">查询财务记录</a></li>
								<li><a href="${path}/createLedger.htm"
									target="contentFrame" onclick="foldMenu()">新增财务记录</a></li>
								<li><a
									href="${path}/queryFinancetypes.htm?id=&key=&page=1&size=10"
									target="contentFrame" onclick="foldMenu()">查询财务条目种类</a></li>
								<li><a href="${path}/createFinancetype.htm"
									target="contentFrame" onclick="foldMenu()">新增财务条目</a></li>
								<li><a
									href="${path}/queryPaymentaccounts.htm?id=&key=&page=1&size=10"
									target="contentFrame" onclick="foldMenu()">查询公司交易账号</a></li>
								<li><a href="${path}/createPaymentaccount.htm"
									target="contentFrame" onclick="foldMenu()">新增公司交易账号</a></li>
							</ul></li>
					</c:if>

					<c:if test="${user.privileges[5].privilege ne ''}">
						<li class="dropdown"><a href="#" class="dropdown-toggle"
							data-toggle="dropdown" aria-expanded="false"> <strong>报表模块</strong>
								<span class="caret"></span>
						</a>
							<ul class="dropdown-menu">
								<li id="latestReport"></li>
								<li><a onclick="getSearchMenuSubFields('Report')">查询历史报表</a></li>
								<li><a href="${path}/createReportProcess.htm?time="
									target="contentFrame" onclick="foldMenu()">生成上月报表</a></li>
							</ul></li>
					</c:if>

					<c:if test="${user.privileges[1].privilege ne ''}">
						<li class="dropdown"><a href="" class="dropdown-toggle"
							data-toggle="dropdown" aria-expanded="false"> <strong>用户管理</strong>
								<span class="caret"></span>
						</a>
							<ul class="dropdown-menu">
								<li><a onclick="getSearchMenuSubFields('Users')">查询用户</a></li>
								<li><a href="${path}/createUser.htm" target="contentFrame"
									onclick="foldMenu()">新增用户</a></li>
							</ul></li>
					</c:if>
				</ul>
				<ul class="nav navbar-nav navbar-right">
					<li><a onclick="showSearchMenu()">搜索</a></li>
					<li><a href="${path}/userLogout.htm" target="_parent">退出</a></li>
				</ul>
			</div>
		</div>
	</div>
</body>
</html>