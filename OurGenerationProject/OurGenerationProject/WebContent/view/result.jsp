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
<title>PersonalityTestGame</title>
<c:set value="${pageContext.request.contextPath}" var="path" scope="page" />

<link rel="stylesheet" href="${path}/css/bootstrap.css">
<link rel="stylesheet" href="${path}/css/bootswatch.min.css">

<script src="${path}/js/jquery-1.10.2.min.js"></script>
<script src="${path}/js/bootstrap.min.js"></script>
<script src="${path}/js/bootswatch.js"></script>
<link rel="stylesheet" href="${path}/js/jqwidgets/styles/jqx.base.css" type="text/css" />
<script type="text/javascript" src="${path}/js/jqwidgets/jqxcore.js"></script>
<script type="text/javascript" src="${path}/js/jqwidgets/jqxdata.js"></script>
<script type="text/javascript" src="${path}/js/jqwidgets/jqxdraw.js"></script>
<script type="text/javascript" src="${path}/js/jqwidgets/jqxchart.core.js"></script>
<script type="text/javascript" src="${path}/js/jqwidgets/jqxslider.js"></script>
<script type="text/javascript" src="${path}/js/jqwidgets/jqxbuttons.js"></script>
<script type="text/javascript" src="${path}/js/jqwidgets/jqxlistbox.js"></script>
<script type="text/javascript" src="${path}/js/jqwidgets/jqxscrollbar.js"></script>
<script type="text/javascript" src="${path}/js/jqwidgets/jqxdropdownlist.js"></script>
<script type="text/javascript" src="${path}/js/jqwidgets/base.js"></script>
<script type="text/javascript">
		var result;
function 
	showResult() {
		// prepare jqxChart settings
		var settings = {
			title : "Testing Result",
			description : "Source: http://www.junstech.com/",
			enableAnimations : true,
			showLegend : true,
			padding : {
				left : 5,
				top : 5,
				right : 5,
				bottom : 5
			},
			titlePadding : {
				left : 0,
				top : 0,
				right : 0,
				bottom : 5
			},
			colorScheme : 'scheme01',
			source : result,
			xAxis : {
				dataField : "key",
				valuesOnTicks : true,
				labels : {
					autoRotate : true
				}
			},
			valueAxis : {
				labels : {
					formatSettings : {
						decimalPlaces : 0
					},
					autoRotate : true
				}
			},

			seriesGroups : [ {
				spider : true,
				startAngle : 0,
				endAngle : 360,
				radius : 90,
				type : 'line',

				type : 'splinearea',
				valueAxis : {
					labels : {
						formatSettings : {
							decimalPlaces : 0
						},
						autoRotate : true
					}
				},
				series : [ {
					dataField : "value",
					displayText : 'Your Rating',
					opacity : 0.7,
					lineWidth : 1,
					radius : 1,
					lineWidth : 1,
					symbolType : 'square'
				} ]
			} ]
		};

		// create the chart
		$('#chartContainer').jqxChart(settings);

		// get the chart's instance
		var chart = $('#chartContainer').jqxChart('getInstance');

		// start angle slider
		$('#sliderStartAngle').jqxSlider({
			width : 240,
			min : 0,
			max : 360,
			step : 1,
			ticksFrequency : 20,
			mode : 'fixed'
		});

		$('#sliderStartAngle').on('change', function(event) {
			var value = event.args.value;
			chart.seriesGroups[0].startAngle = value;
			chart.seriesGroups[0].endAngle = value + 360;
			chart.update();
		});

		// radius slider
		$('#sliderRadius').jqxSlider({
			width : 240,
			min : 80,
			max : 140,
			value : 120,
			step : 1,
			ticksFrequency : 20,
			mode : 'fixed'
		});

		$('#sliderRadius').on('change', function(event) {
			var value = event.args.value;
			chart.seriesGroups[0].radius = value;
			chart.update();
		});

		// color scheme drop down
		var colorsSchemesList = [ "scheme01", "scheme02", "scheme03",
				"scheme04", "scheme05", "scheme06", "scheme07", "scheme08" ];
		$("#dropDownColors").jqxDropDownList({
			source : colorsSchemesList,
			selectedIndex : 2,
			width : '200',
			height : '25',
			dropDownHeight : 100
		});

		$('#dropDownColors').on('change', function(event) {
			var value = event.args.item.value;
			chart.colorScheme = value;
			chart.update();
		});

		// series type drop down
		var seriesList = [ "splinearea", "spline", "column", "scatter",
				"stackedcolumn", "stackedsplinearea", "stackedspline" ];
		$("#dropDownSeries").jqxDropDownList({
			source : seriesList,
			selectedIndex : 0,
			width : '200',
			height : '25',
			dropDownHeight : 100
		});

		$('#dropDownSeries').on('select', function(event) {
			var args = event.args;
			if (args) {
				var value = args.item.value;

				chart.seriesGroups[0].type = value;
				chart.update();
			}
		});
	}

	function getDataSet() {
		var data = document.getElementById("dataSet").value;
		var resultFromServer = eval('('
				+ document.getElementById("dataSet").value + ')');
		result = resultFromServer.result;
		showResult();
	}
</script>
</head>
<body style="background-color: #eeeeee" onload="getDataSet()">
	<input id="dataSet" name="dataSet" type="hidden" value='${dataSet}' />
	<div id='chartContainer' style="width: 400px; height: 400px">
    </div>


	<div class="container">
		<div class="row">
			<div class="span12"></div>
		</div>
		<div class="row">
			<div class="col-md-3"></div>
			<div class="col-md-5">
			<c:forEach var="factor" items="${result}" varStatus="status">
				<strong>type: ${factor.key}</strong>
				<span> - </span>
				<strong>score: ${factor.value}</strong>
				<br/>
			</c:forEach>
			</div>
			<div class="col-md-4"></div>
		</div>
	</div>
	<div class="row">
		<div class="span12">
		</div>
	</div>
</body>