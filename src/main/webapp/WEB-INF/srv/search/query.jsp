<%-- 
	Document: search
	Created on: Jul 23, 2010, 7:39:16 PM
	Author: tim
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="net.frcdb.util.StringUtil"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/tlds/js.tld" prefix="js" %>
<%@taglib uri="http://frcdb.net/taglibs/utils" prefix="utils" %>

<%
String query = "";
if (request.getParameter("q") != null) {
	query = request.getParameter("q");
}
pageContext.setAttribute("query", query);

String tChecked = "checked=\"checked\"";
String eChecked = "";

if (request.getParameter("type") != null) {
	String t = request.getParameter("type").toLowerCase();

	if (t.equals("teams")) {
		tChecked = "checked=\"checked\"";
		eChecked = "";
	} else if (t.equals("events")) {
		tChecked = "";
		eChecked = "checked=\"checked\"";
	} else {
		tChecked = "";
		eChecked = "";
	}
}
pageContext.setAttribute("tChecked", tChecked);
pageContext.setAttribute("eChecked", eChecked);
%>

<tiles:insertDefinition name="layout-default">
	<tiles:putAttribute name="title" value="Search"/>
	<tiles:putAttribute name="body">
		<h2>Search</h2>

		<form action="/search" method="GET">
			<table border="0">
				<tr>
					<td>Query:</td>
					<td><input type="text" name="q" size="30" value="${query}"></td>
				</tr>
				<tr>
					<td>Search in:</td>
					<td>
						<input type="radio"
							   name="type"
							   value="teams"
							   id="teamsBoxQ"
							   ${tChecked}><label for="teamsBoxQ">Teams</label>
					</td>
				</tr>
				<tr>
					<td></td>
					<td>
						<input type="radio"
							   name="type"
							   value="events"
							   id="eventsBoxQ"
							   ${eChecked}><label for="eventsBoxQ">Events</label>
					</td>
				</tr>
			</table>

			<p><input type="submit" value="Submit" /></p>
		</form>

		<c:if test="${not empty teamSearchResults}">
			<h2>Team Search Results</h2>
			<js:table id="teamSearchResults" width="90%" align="center">
				<thead>
					<tr>
						<th>Number</th>
						<th>Nickname</th>
						<th>Location</th>
						<th>Rookie Season</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${teamSearchResults}" var="team">
						<tr>
							<td>${team.number}</td>
							<td>
								<a href="/team/${team.number}">
									${utils:escape(team.nickname)}
								</a>
							</td>
							<td>${team.city}, ${team.state}</td>
							<td>${team.rookieSeason}</td>
						</tr>
					</c:forEach>
				</tbody>
			</js:table>
		</c:if>

		<c:if test="${not empty eventSearchResults}">
			<h2>Event Search Results</h2>
			<js:table id="eventSearchResults" width="95%" align="center">
				<thead>
					<tr>
						<th>Name</th>
						<th>City</th>
						<th>State</th>
						<th>Country</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${eventSearchResults}" var="e">
						<tr>
							<td>
								<a href="/event/${e.shortName}/">
									${utils:escape(e.name)}
								</a>
							</td>
							<td>${e.city}</td>
							<td>${e.state}</td>
							<td>${e.country}</td>
						</tr>
					</c:forEach>
				</tbody>
			</js:table>
		</c:if>
	</tiles:putAttribute>
</tiles:insertDefinition>