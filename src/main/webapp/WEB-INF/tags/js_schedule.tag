<%-- 
    Document   : js_schedule
    Created on : Mar 13, 2012, 12:41:56 AM
    Author     : tim
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- assumed compiler breakage
<%@tag body-content="empty"
	   description="put the tag description here"
	   pageEncoding="UTF-8"%> --%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%--
<%@attribute name="type" type="java.lang.String"%>
<%@attribute name="id" type="java.lang.String"%>
<%@attribute name="eventName" type="java.lang.String"%>
<%@attribute name="gameYear" type="java.lang.String"%>--%>

<div id="${id}">
	JS must be enabled.
</div>
<button id="${id}-button">Schedule Update</button>
<script type="text/javascript">
	var dialog = $("#${id}");
	dialog.html("");

	var list = $("<ul>");
	dialog.append(list);

	var now = new Date();
	

	var dateInput = $('<input type="date">');
	dateInput.val((now.getMonth() + 1) + "/" + now.getDate() + "/" + now.getFullYear());
	dialog.append(dateInput);
	dateInput.datepicker();

	var hourInput = $('<input type="number">');
	hourInput.val(now.getHours());
	var minuteInput = $('<input type="number">');
	minuteInput.val(now.getMinutes());

	list.append(
			$("<li>").append($("<span><b>Date: </b>")).append(dateInput)
	);

	list.append(
			$("<li>").append($("<span><b>Hour: </b>")).append(hourInput)
	);

	list.append(
			$("<li>").append($("<span><b>Minute: </b>")).append(minuteInput)
	);

	dialog.dialog({
		title: "Schedule Update",
		autoOpen: false,
		height: 300,
		width: 400,
		modal: true,
		buttons: {
			"Add": function() {
				var postData = {
					"type": "${type}",
					"date": dateInput.val(),
					"hour": hourInput.val(),
					"minute": minuteInput.val(),
					"eventName": "${eventName}", <%-- TODO: clean this up! --%>
					"gameYear": "${gameYear}"
				}

				$.post("/scheduler/add/json", postData, function(data) {
					if (data.error) {
						alert("Error: " + data.error);
						dialog.dialog("close");
					} else {
						alert("Update scheduled.");
						dialog.dialog("close");
					}
				});
			},
			Cancel: function() {
				dialog.dialog("close");
			}
		}
	});

	$("#${id}-button").button().click(function() {
		dialog.dialog("open");
	});
</script>
