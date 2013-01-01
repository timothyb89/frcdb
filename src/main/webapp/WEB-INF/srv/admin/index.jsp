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
		
		<script type="text/javascript">
			var teamTemplate = {
				"Name":          { name: "name", type: "text" },
				"Nickname":      { name: "nickname", type: "text" },
				"Number":	     { name: "number", type: "number" },
				"Country":       { name: "country", type: "text" },
				"State":         { name: "state", type: "text" },
				"City":          { name: "city", type: "text" },
				"Rookie Season": { name: "rookieSeason", type: "number" },
				"Motto":         { name: "motto", type: "text" },
				"Website":       { name: "website", type: "text" }
			};
		
			var teamImportTemplate = {
				"Teams JSON": { name: "file", type: "file" }
			};

			var eventTemplate = {
				"Name":       { name: "name", type: "text" },
				"Short Name": { name: "shortName", type: "text" /*, disabled: true */},
				"Identifier": { name: "identifier", type: "text" },
				"Venue":      { name: "venue", type: "text" },
				"City":       { name: "city", type: "text" },
				"State":      { name: "State", type: "text" },
				"Country":    { name: "country", type: "text" }
			};
			
			var eventImportTemplate = {
				"Events JSON": { name: "file", type: "file" }
			};
			
			$("#teamCreate").button().click(function() {
				$.dialogform({
					title: "Create Team",
					fields: teamTemplate,
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
					fields: eventTemplate,
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
		</script>
		
	</tiles:putAttribute>
</tiles:insertDefinition>