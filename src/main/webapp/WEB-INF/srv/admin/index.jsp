<%-- 
    Document   : index
    Created on : Dec 27, 2012, 4:51:42 PM
    Author     : tim
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://frcdb.net/taglibs/utils" prefix="utils" %>

<tiles:insertDefinition name="layout-default">
	<tiles:putAttribute name="title" value="Admin"/>
	<tiles:putAttribute name="body">
		<h2>Administration</h2>
		<h3>Team Actions</h3>
		<button id="teamCreate">Create Team</button>
		<button id="teamModify">Modify Team</button>
		<button id="teamsImport">Import Team Data</button>
		
		<h3>Event Actions</h3>
		<button id="eventCreate">Create Event</button>
		<button id="eventsImport">Import Event Data</button>
		
		<h3>Game Actions</h3>
		<button id="gamesImport">Import Games (.zip)</button>
		
		<h3>Statistics</h3>
		<button id="executeStatistic">Execute statistic</button>
		
		<script type="text/javascript">
			var teamImportTemplate = {
				"Teams JSON": { name: "file", type: "file" }
			};
			
			var eventImportTemplate = {
				"Events JSON": { name: "file", type: "file" }
			};
			
			var gamesImportTemplate = {
				"Games .zip": { name: "file", type: "file" }
			};
		
			var executeStatisticTemplate = {
				"Name":       { name: "name", type: "text" },
				"Event name": { name: "event", type: "text" },
				"Year":       { name: "year", type: "number" }
			};
			
			$("#teamCreate").button().click(function() {
				$.dialogform({
					title: "Create Team",
					fields: EditorTemplates.team,
					url: "/json/admin/team/create",
					success: function(response) {
						console.log(response);
						alert("Error: " + response.message);
					}
				});
			});
			
			$("#teamModify").button().click(function() {
				var template = $.extend({
					"Number": {
						disabled: true
					}
				}, teamTemplate);
				
			});
		
			$("#teamsImport").button().click(function() {
				$.dialogform({
					title: "Import Teams",
					fields: teamImportTemplate,
					url: "/json/admin/team/import",
					contentType: "multipart/form-data",
					success: function(response) {
						console.log(response);
						alert(response.message);
					}
				});
			});
			
			$("#eventCreate").button().click(function() {
				$.dialogform({
					title: "Create Event",
					fields: EditorTemplates.event,
					url: "/json/admin/event/create",
					success: function(response) {
						console.log(response);
						alert(response.message);
					}
				});
			});
			
			$("#eventsImport").button().click(function() {
				$.dialogform({
					title: "Import Events",
					fields: eventImportTemplate,
					url: "/json/admin/event/import",
					contentType: "multipart/form-data",
					success: function(response) {
						console.log(response);
						alert(response.message);
					}
				});
			});
			
			$("#gamesImport").button().click(function() {
				$.dialogform({
					title: "Import Games",
					fields: gamesImportTemplate,
					url: "/json/admin/game/import",
					contentType: "multipart/form-data",
					success: function(response) {
						console.log(response);
						alert(response.message);
					}
				});
			});
			
			$("#executeStatistic").button().click(function() {
				$.dialogform({
					title: "Execute Statistic",
					fields: executeStatisticTemplate,
					url: "/json/admin/stats/execute",
					success: function(response) {
						console.log(response);
						alert(response.message);
					}
				});
			});
		</script>
		
	</tiles:putAttribute>
</tiles:insertDefinition>