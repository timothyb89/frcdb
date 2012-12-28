<%-- 
    Document   : teams
    Created on : Dec 27, 2012, 4:50:17 PM
    Author     : tim
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://frcdb.net/taglibs/utils" prefix="utils" %>

<tiles:insertDefinition name="layout-default">
	<tiles:putAttribute name="title" value="Admin - Teams"/>
	<tiles:putAttribute name="body">
		<h2>Team Management</h2>
		
		<h3>Update Teams</h3>
		Perform an automatic update of teams from FIRST's directory.
		
		<h3>Create Team</h3>
		Create a new team.
		<form action="">
			Name: <input type="text" name="name" required="true">
			Nickname: <input type="text" name="nickname" required="true">
			Number: <input type="number" name="number" required="true">
			City: <input type="text" name="city" required="true">
			State: <input type="text" name="city" required="true">
			Country: <input type="text" name="city" required="true">
			
			
			<input type="hidden" name="action" value="create">
			<input type="submit" value="Create">
		</form>
		
	</tiles:putAttribute>
</tiles:insertDefinition>