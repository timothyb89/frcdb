<%-- 
    Document   : robot-edit
    Created on : Apr 7, 2011, 3:06:52 PM
    Author     : Tim
--%>

<%@page import="net.tanesha.recaptcha.ReCaptchaFactory"%>
<%@page import="net.tanesha.recaptcha.ReCaptcha"%>
<%@page import="net.frcdb.servlet.RegisterServlet"%>
<%@page import="java.util.List"%>
<%@page import="net.frcdb.auth.User"%>
<%@page import="net.frcdb.api.team.Team"%>
<%@page import="net.frcdb.db.Database"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@taglib uri="/WEB-INF/tlds/js" prefix="js" %>

<%
Team t = (Team) session.getAttribute("team");

String number = t == null ? "?" : String.valueOf(t.getNumber());
String name = t == null ? "Unknown" : t.getNickname();
pageContext.setAttribute("number", number);
pageContext.setAttribute("name", name);

RobotInfo info = (RobotInfo) request.getAttribute("robotInfo");
pageContext.setAttribute("infoYear", request.getAttribute("robotInfoYear"));

boolean canEdit = false;
User u = (User) request.getAttribute("user");
if (u != null && u.hasPermission("root.team.modify.robotinfo")) {
	canEdit = true;
}

info.checkInput(request);
%>

<tiles:insertDefinition name="layout-default">
	<tiles:putAttribute name="title" value="Team: ${number}"/>
	<tiles:putAttribute name="body">
		<h1>Editing Info for Team ${number}: ${infoYear}</h1>
		<p class="breadcrumbs">
			<% if (canEdit) { %>
				<a href="/team/${number}/robot/${infoYear}">Back</a>, 
			<% } %>
			<a href="/team/${number}">Team Page</a>
		</p>
		
		Editing is currently broken and has been disabled, please check back
		later! Sorry for the inconvenience.
		<%--<%= info.displayEditor(request) %>--%>
	</tiles:putAttribute>
</tiles:insertDefinition>