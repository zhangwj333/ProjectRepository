<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="st" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE HTML>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>PersonalityTestGame</title>
<c:set value="${pageContext.request.contextPath}" var="path"
	scope="page" />

<link rel="stylesheet" href="${path}/css/bootstrap.css">
<link rel="stylesheet" href="${path}/css/bootswatch.min.css">

<script src="${path}/js/jquery-1.10.2.min.js"></script>
<script src="${path}/js/bootstrap.min.js"></script>
<script src="${path}/js/bootswatch.js"></script>
<script type="text/javascript">
	var curQuestion = 0;
	var myAnswers = new Array();
	var title = new Array();
	var options = new Array();

	function submitAnswer() {
		var temp = getSelection();
		if (temp != "") {
			myAnswers[curQuestion] = temp;
			document.getElementById("answer").value = myAnswers;
			document.getElementById("GetResult").submit();
		} else {
			alert("请选答案");
		}
	}

	function getSelection() {
		var temp = "";
		var radionum = document.getElementsByName("optionsRadios");
		for (var i = 0; i < radionum.length; i++) {
			if (radionum[i].checked) {
				temp = radionum[i].value
			}
		}
		return temp;
	}

	function next() {

		var temp = getSelection();
		if (temp != "") {
			myAnswers[curQuestion] = temp;
			document.getElementById("answer").value = myAnswers;
			curQuestion++;
			setQuestion();
			if ((curQuestion + 1) == title.length) {
				document.getElementById("submitButton").innerHTML = "<a id='next' class='btn btn-success' onclick='submitAnswer()'>提交</a>";
			} else {
				document.getElementById("submitButton").innerHTML = "<a id='next' class='btn btn-primary' onclick='next()'>下一条</a>";
			}
		} else {
			alert("请选答案");
		}
	}
	function prev() {
		if (curQuestion > 0) {
			var temp = getSelection();
			myAnswers[curQuestion] = temp;
			document.getElementById("answer").value = myAnswers;
			curQuestion--;
			document.getElementById("submitButton").innerHTML = "<a id='next' class='btn btn-primary' onclick='next()'>下一条</a>";
			setQuestion();
		}
	}
	function setQuestion() {
		document.getElementById("title").innerHTML = title[curQuestion];
		var html = "";
		for (var i = 0; i < options[curQuestion].length; i++) {
			var checked = "";
			if (myAnswers[curQuestion] == options[curQuestion][i][1]) {
				checked = "checked = 'checked'";
			}
			html = html
					+ "<label> <input type='radio' name='optionsRadios' id='optionsRadios' "+checked+" value='"+options[curQuestion][i][1]+"' >"
					+ options[curQuestion][i][0] + "</label><br/>";
		}
		document.getElementById("choice").innerHTML = html;
	}

	function getQuestions() {
		$.ajax({
			type : "POST",
			url : "MultipleChoiceGame.htm",
			data : {
				"type" : "${type}"
			},
			dataType : "json",
			success : function(data) {
				var obj = eval('('
						+ eval('(' + JSON.stringify(data) + ')').output + ')');
				init(obj);
			},
			error : function() {
				alert("error");
			}
		});
	}

	function init(obj) {
		for (var i = 0; i < obj.questions.length; i++) {
			title[i] = obj.questions[i].title;
			options[i] = new Array();
			for (var j = 0; j < obj.questions[i].options.length; j++) {
				options[i][j] = new Array();
				options[i][j][0] = obj.questions[i].options[j].description;
				options[i][j][1] = obj.questions[i].options[j].value;
			}
		}
		setQuestion();
	}

	function getDataSet() {
		var data = document.getElementById("dataSet").value;
		var obj = eval('(' + document.getElementById("dataSet").value + ')');
		init(obj);
	}
</script>
</head>
<body style="background-color: #eeeeee" onload="getDataSet()">
	<input id="dataSet" name="dataSet" type="hidden" value='${dataSet}' />
	<form:form class="form-horizontal" id="GetResult" name="GetResult"
		method="post" action="${path}/GetResult.htm">
		<fieldset>
			<div class="container">
				<div class="row">
					<div class="span12"></div>
				</div>

				<div class="row">
					<div class="info">
						<blockquote>
							<h3 id="title">${questions[0].title}</h3>
						</blockquote>
					</div>
					<div class="well">
						<div id="selections" class="form-group">
							<div id="choice" class="radio">
								<label> <input type="radio" name="optionsRadios"
									id="optionsRadios" value="option"> option
								</label>
							</div>

						</div>
					</div>
					<div class="row">
						<div class="col-sm-4">
							<a id="prev" class="btn btn-default" onclick="prev()">上一条</a>

						</div>
						<div class="col-sm-4"></div>
						<div id="submitButton" class="col-sm-4">
							<a id="next" class="btn btn-primary" onclick="next()">下一条</a>
						</div>
					</div>
				</div>

				<input id="type" name="type" type="hidden" value="${type}" /> <input
					id="answer" name="answer" type="hidden" value="" />
		</fieldset>
	</form:form>
	<div class="row">
		<div class="span12"></div>
	</div>
</body>