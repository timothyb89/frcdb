<%-- 
    Document   : default
    Created on : Jul 11, 2009, 2:19:32 AM
    Author     : tim
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://timothyb89.org/tlds/dialog" prefix="dialog" %>
<%@ taglib uri="http://timothyb89.org/tlds/js" prefix="js" %>

<%
session.setAttribute("page", request.getRequestURI());

if (request.getParameter("error") != null) {
	session.setAttribute("error", request.getParameter("error"));
}

session.setMaxInactiveInterval(15 * 60);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
	"http://www.w3.org/TR/html4/loose.dtd">

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>FRC-DB: <tiles:getAsString name="title"/></title>

		<link rel="stylesheet" type="text/css" href="/layout/default/res/css/default.css"/>
		<link rel="stylesheet" type="text/css" href="/layout/dialog/dialog.css"/>

		<script type="text/javascript" src="/js/functions.js"></script>
		<script type="text/javascript" src="/js/jquery/jquery.min.js"></script>
		<!--<script type='text/javascript' src='/js/jquery/jquery.autocomplete.pack.js'></script>-->
		<script type='text/javascript' src='/js/jquery/jquery.corner.js'></script>
		<script type='text/javascript' src='/js/jquery/jquery.dataTables.min.js'></script>
		<link rel="stylesheet" type="text/css" 
			  href="/js/jquery/jquery.autocomplete.css">
		<link rel="stylesheet" type="text/css"
			  href="/js/jquery/themes/ui-darkness/jquery-ui-1.8.2.custom.css">
		<link rel="stylesheet" type="text/css"
			  href="/js/jquery/dataTables-jui.css">
	</head>
	<body>
		<table id="main" border="0" style="width: 100%;">
			<tr><td colspan="4">
				<div id="top">
					<table border="0" style="width: 100%; height: 100%;">
						<tr>
							<td valign="center">
								<img id="logo" src="/res/logo-med.png" alt="logo">
							</td>
							<td>
								<span class="title">FRC-DB: A scouting database for FRC</span>
							</td>
						</tr>
					</table>
				</div>
			</td></tr>
			<tr>
				<td width="25px"></td>
				<td width="200px" valign="top">
						<tiles:insertAttribute name="menu"/>
				</td>
				<td>
					<div id="main">
							<div id="content">
								<% if (session.getAttribute("error") != null) { %>
									<div class="error">
										<table border="0"><tr>
											<td>
												<img src="/layout/default/res/gfx/error.png" alt="error">
											</td>
											<td style="color: black;">
												<b>Error:</b>
												<% out.println(session.getAttribute("error")); %>
											</td>
										</tr></table>
									</div>
									<% session.removeAttribute("error"); %>
								<% } %>

								<tiles:insertAttribute name="body"/>
							</div>
					</div>
				</td>
				<td width="25px"></td>
			</tr>
		</table>
	</body>
</html>
