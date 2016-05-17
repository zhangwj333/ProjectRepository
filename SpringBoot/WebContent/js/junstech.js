var path = "";
var flat = 0;
var salesubcount = 0;
function showSearchMenu() {
	if (flat == 0) {
		flat = 1;
		document.getElementById("searchFrame").innerHTML = "<iframe id='searchiFrame' src='"
				+ path
				+ "/view/search.jsp' frameBorder=0 scrolling='no' width='100%' onLoad='iFrameHeight(this)'></iframe>";
	} else {
		flat = 0;
		document.getElementById("searchFrame").innerHTML = "";
	}
	foldMenu();

}

function foldMenu() {
	document.getElementById("navbar-main").setAttribute("class",
			"navbar-collapse collapse");

}

function iFrameHeight(frame) {
	var ifm = document.getElementById(frame.id);
	var subWeb = document.frames ? document.frames[frame.id].document
			: ifm.contentDocument;
	if (ifm != null && subWeb != null) {
		ifm.height = subWeb.body.scrollHeight;
	}
}

function selectSearchModule(choose) {
	if (choose.options[choose.selectedIndex].value == "") {
		document.getElementById("module").setAttribute("action", "");
		document.getElementById("searchSubmit").setAttribute("class",
				"btn btn-primary disabled");
		document.getElementById("searchSubmit").setAttribute("type", "reset");
		getSearchMenuSubFields("");
	} else {
		getSearchMenuSubFields(choose.options[choose.selectedIndex].value);
	}

}

function selectInput(choose, id) {
	document.getElementById(id).value = choose.options[choose.selectedIndex].value;
}

function goback() {
	history.back();
}

// get search menu subField
function getSearchMenuSubFields(module) {
	flat = 1;
	window.parent.document.getElementById("searchFrame").innerHTML = "<iframe id='searchiFrame' src='"
			+ path
			+ "/view/search"
			+ module
			+ ".jsp' frameBorder=0 scrolling='no' width='100%' onLoad='iFrameHeight(this)'></iframe>";
	foldMenu();
}
