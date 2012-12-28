<%-- 
    Document   : error
    Created on : Jul 25, 2010, 6:28:16 PM
    Author     : tim
--%>

<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@page isErrorPage="true"%>

<tiles:insertDefinition name="layout-default">
	<tiles:putAttribute name="title" value="Event: Error"/>
	<tiles:putAttribute name="body">
		<h1>Error</h1>
		There was an error retrieving the requested data.
		<a href="javascript:history.go(-1)">Go back?</a>
	</tiles:putAttribute>
</tiles:insertDefinition>