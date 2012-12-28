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
		
		<script type="text/javascript">
			var teamTemplate = {
				"Name": {
					name: "name",
					type: "text",
				},
				"Nickname": {
					name: "nickname",
					type: "text"
				},
				"Number": {
					name: "number",
					type: "number",
					disabled: true
				},
				"Country": {
					name: "country",
					type: "text"
				},
				"State": {
					name: "state",
					type: "text"
				},
				"City": {
					name: "city",
					type: "text"
				},
				"Rookie Season": {
					name: "rookieSeason",
					type: "number"
				},
				"Motto": {
					name: "motto",
					type: "text"
				},
				"Website": {
					name: "website",
					type: "text"
				}
			}

			var eventTemplate = {
				"Name": {
					name: "name",
					type: "text"
				},
				"Short Name": {
					name: "shortName",
					type: "text",
					disabled: true
				},
				"Venue": {
					name: "venue",
					type: "text"
				},
				"City": {
					name: "city",
					type: "text"
				},
				"State": {
					name: "State",
					type: "text"
				},
				"Country": {
					name: "country",
					type: "text"
				}
			}
		</script>
		
	</tiles:putAttribute>
</tiles:insertDefinition>