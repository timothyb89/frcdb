<%-- 
    Document   : match
    Created on : Jul 25, 2010, 6:27:24 PM
    Author     : tim
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/tlds/js.tld" prefix="js" %>
<%@taglib uri="http://frcdb.net/taglibs/utils" prefix="utils" %>
<%@taglib uri="http://frcdb.net/taglibs/content" prefix="content" %>

<tiles:insertDefinition name="layout-default">
	<tiles:putAttribute name="title" value="Event: ${data.event.name}, Match #${data.match.number}"/>
	<tiles:putAttribute name="body">
		<h1>Event: ${data.event.name}, ${data.match.type.text} Match #${data.match.number}</h1>
		<p class="breadcrumbs">
			<a href="/event/${data.event.shortName}/${data.game.gameYear}">Event Page</a>
		</p>
		
		<h2>Scores</h2>
		<table border="1" style="width: 90%; margin: auto;">
			<tbody>
				<tr>
					<td>Red Score</td>
					<td>${data.match.redScore}</td>
				</tr>
				<tr>
					<td>Blue Score</td>
					<td>${data.match.blueScore}</td>
				</tr>
				
				<c:if test="${utils:hasProperty(data.game, 'highestOPR')}">
					<tr>
						<td>Predicted Red Score*</td>
						<td>${utils:format('%.3f', data.redPrediction)}</td>
					</tr>
					<tr>
						<td>Predicted Blue Score</td>
						<td>${utils:format('%.3f', data.bluePrediction)}</td>
					</tr>
				</c:if>
			</tbody>
		</table>
		
		<h2 style="padding-top: 15px;">Red Alliance</h2>
		<js:table id="event_matchTeamsRed" width="90%" align="center">
			<thead>
				<tr>
					<th>Number</th>
					<c:if test="${utils:hasProperty(data.game, 'highestOPR')}">
						<th>OPR</th>
						<th>DPR</th>
					</c:if>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${data.redTeams}" var="team">
					<tr>
						<td>${team.team.number}</td>
						<c:if test="${utils:hasProperty(data.game, 'highestOPR')}">
							<td>${utils:format('%.3f', team.OPR)}</td>
							<td>${utils:format('%.3f', team.DPR)}</td>
						</c:if>
					</tr>
				</c:forEach>
			</tbody>
		</js:table>
		
		<h2 style="padding-top: 15px;">Blue Alliance</h2>
		<js:table id="event_matchTeamsBlue" width="90%" align="center">
			<thead>
				<tr>
					<th>Number</th>
					<c:if test="${utils:hasProperty(data.game, 'highestOPR')}">
						<th>OPR</th>
						<th>DPR</th>
					</c:if>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${data.blueTeams}" var="team">
					<tr>
						<td>${team.team.number}</td>
						<c:if test="${utils:hasProperty(data.game, 'highestOPR')}">
							<td>${utils:format('%.3f', team.OPR)}</td>
							<td>${utils:format('%.3f', team.DPR)}</td>
						</c:if>
					</tr>
				</c:forEach>
			</tbody>
		</js:table>

		<%-- fixme
		<h2>Media for This Match</h2>
		<content:show id="match" provider="${data.match}" /> --%>
		
		<p>
		<a href="/event/${data.event.name}/${data.game.gameYear}">
			Back to Event Page
		</a>
		<p>
		<hr>
		<b>* Note:</b> For entertainment purposes only, equal to sum of alliance 
		OPRs.
	</tiles:putAttribute>
</tiles:insertDefinition>