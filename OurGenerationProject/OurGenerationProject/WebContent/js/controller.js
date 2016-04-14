var curPageFirstQuestionNo = 0;
var myAnswers = new Array();
var title = new Array();
var options = new Array();
var questionPerPage = 1;
var curPageQuestionNo = 1;

function setButtons() {
	if ((parseInt(curPageFirstQuestionNo, 10) + parseInt(questionPerPage, 10)) >= title.length) {
		document.getElementById("submitButton").innerHTML = "<a id='next' href='javascript:;' class='weui_btn weui_btn_primary' onclick='submitAnswer()'>提交</a>";
	} else {
		document.getElementById("submitButton").innerHTML = "<a id='next' href='javascript:;' class='weui_btn weui_btn_primary' onclick='next()'>下一条</a>";
	}
}

function getCurPageQuestionNo() {
	if ((parseInt(title.length, 10) - parseInt(curPageFirstQuestionNo, 10)) >= questionPerPage) {
		curPageQuestionNo = questionPerPage;
	} else {
		curPageQuestionNo = parseInt(title.length, 10)
				- parseInt(curPageFirstQuestionNo, 10);
	}
}

function submitAnswer() {
	var temp = getSelection();
	if (temp != "") {
		for (var i = 0; i < curPageQuestionNo; i++) {
			myAnswers[parseInt(curPageFirstQuestionNo, 10) + i] = temp[i];
		}
		document.getElementById("answer").value = myAnswers;
		document.getElementById("GetResult").submit();
	} else {
		alert("请选答案");
	}
}

function getSelection() {
	var temp = new Array();
	for (var i = 0; i < curPageQuestionNo; i++) {
		var radionum = document.getElementsByName("radio1" + i);
		for (var j = 0; j < radionum.length; j++) {
			if (radionum[j].checked) {
				temp[i] = radionum[j].value
			}
		}
	}
	return temp;
}

function next() {

	var temp = getSelection();
	if (temp != "" && (curPageQuestionNo == temp.length)) {
		for (var i = 0; i < curPageQuestionNo; i++) {
			myAnswers[curPageFirstQuestionNo + i] = temp[i];
		}
		document.getElementById("answer").value = myAnswers;
		curPageFirstQuestionNo = parseInt(curPageFirstQuestionNo, 10)
				+ parseInt(questionPerPage, 10);
		setQuestion();
	} else {
		alert("请选答案");
	}
}
function prev() {
	if (curPageFirstQuestionNo > 0) {
		var temp = getSelection();
		for (var i = 0; i < curPageQuestionNo; i++) {
			myAnswers[curPageFirstQuestionNo + i] = temp[i];
		}
		document.getElementById("answer").value = myAnswers;
		curPageFirstQuestionNo = parseInt(curPageFirstQuestionNo, 10)
				- parseInt(questionPerPage, 10);
		document.getElementById("submitButton").innerHTML = "<a id='next' href='javascript:;' class='weui_btn weui_btn_primary' onclick='next()'>下一条</a>";
		setQuestion();
	}
}
function setQuestion() {
	getCurPageQuestionNo();
	var preSetValueHtml = "";
	for (var i = 0; i < curPageQuestionNo; i++) {
		preSetValueHtml = preSetValueHtml
				+ "<div class='weui_cells_title' id='title"
				+ i
				+ "'>单选列表项</div>"
				+ "<div class='weui_cells weui_cells_radio' id='choice"
				+ i
				+ "'>"
				+ "<label class='weui_cell weui_check_label' for='optionsRadios"
				+ i
				+ "'>"
				+ "<div class='weui_cell_bd weui_cell_primary'>"
				+ "<p>cell standard</p>"
				+ "</div>"
				+ "<div class='weui_cell_ft'>"
				+ "<input type='radio' class='weui_check' name='radio1' id='optionsRadios"
				+ i + "'value=''>" + "<span class='weui_icon_checked'></span>"
				+ "</div>" + "</label>" + "</div>";
	}
	document.getElementById("questions").innerHTML = preSetValueHtml;
	for (var i = 0; i < curPageQuestionNo; i++) {
		document.getElementById("title" + i).innerHTML = parseInt(
				curPageFirstQuestionNo + 1 + i, 10)
				+ ". " + title[curPageFirstQuestionNo + i];
		var html = "";
		for (var j = 0; j < options[curPageFirstQuestionNo + i].length; j++) {
			var checked = "";
			if (myAnswers[curPageFirstQuestionNo + i] == options[curPageFirstQuestionNo
					+ i][j][1]) {
				checked = "checked = 'checked'";
			}
			html = html
					+ "<label class='weui_cell weui_check_label' for='optionsRadios"
					+ i + j + "'>"
					+ "<div class='weui_cell_bd weui_cell_primary'>" + "<p>"
					+ options[curPageFirstQuestionNo + i][j][0] + "</p>"
					+ "</div>" + "<div class='weui_cell_ft'>"
					+ "<input type='radio' class='weui_check' name='radio1" + i
					+ "' id='optionsRadios" + i + j + "' " + checked
					+ " value='" + options[curPageFirstQuestionNo + i][j][1]
					+ "'>" + "<span class='weui_icon_checked'></span>"
					+ "</div>" + "</label>"
		}
		document.getElementById("choice" + i).innerHTML = html;
	}
	setButtons();
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
	var questionLengthPerPage = document.getElementById("questionPerPage").value
	if (!isNaN(questionLengthPerPage)) {
		questionPerPage = questionLengthPerPage;
	}
	var data = document.getElementById("dataSet").value;
	var obj = eval('(' + data + ')');
	init(obj);
}