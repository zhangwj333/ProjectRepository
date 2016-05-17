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
<script type="text/javascript">
	function getCurrentYear() {
		var today = new Date();
		var years;
		for (yyyy = today.getFullYear(); yyyy > 2013; yyyy--) {
				years = years + "<option value='"+yyyy+"'>"+yyyy+"</option>";
		}
		document.getElementById("year").innerHTML=	"<option value='' selected='selected'>选择年份</option>" +years;
	}

	function selectInput(choose, id) {
		document.getElementById(id).value = choose.options[choose.selectedIndex].value;	
		if(id == "year"){
			var today = new Date();
			var curMonth = 13;
			if(document.getElementById(id).value == today.getFullYear()){
				curMonth = today.getMonth()+1;
			}
			
			var months;
			for(var i=1; i<curMonth;i++){
				months = months + "<option value='"+i+"'>"+i+"</option>";
			}
			document.getElementById("month").innerHTML=	"<option value='' selected='selected'>选择月份</option>" + months;
		}
	}
</script>

</head>
<body style="background-color: #eeeeee" onload=getCurrentYear()>
	<div class="row-fluid">
		<div class="span12">
			<form:form class="form-horizontal" id="module" name="module"
				method="post" action="${path}/queryHistoryReportProcess.htm" target="contentFrame">
				<legend>
					<strong>报表查询</strong>
				</legend>
				<fieldset>
					<div class="form-group">
						<label class="col-md-2 control-label">模块</label>
						<div class="col-md-4">
							<select class="form-control" id="select"
								onchange="selectSearchModule(this, module)">
								<option value="Report" selected="selected">报表查询</option>
							</select>
						</div>
					</div>
					<div class="span12">
						<div class="form-group">
							<label class="col-md-2 control-label">年份</label>
							<div class="col-md-4">
								<select class="form-control" id="year" name="year"
									onchange="selectInput(this, 'year')">
									<option value="" selected="selected">选择年份</option>
										<option value="${year}">${year}</option>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label">月份</label>
							<div class="col-md-4">
								<select class="form-control" id="month" name="month"
									onchange="selectInput(this, 'month')">
									<option value="" selected="selected">选择月份</option>
									<c:forEach var="month" begin="1" end="12">
										<option value="${month}">${month}</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-md-4 col-md-offset-2">
							<button id="searchSubmit" type="submit" class="btn btn-primary">查询</button>
						</div>
					</div>
				</fieldset>
			</form:form>
		</div>
	</div>
</body>
</html>