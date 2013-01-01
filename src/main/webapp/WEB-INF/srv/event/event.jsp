
<%-- 
    Document   : event
    Created on : Jul 25, 2010, 6:27:17 PM
    Author     : tim
--%>

<%@page import="net.frcdb.api.game.team.element.OPRProvider"%>
<%@page import="net.frcdb.api.game.event.element.GameOPRProvider"%>
<%@page import="net.frcdb.api.game.match.MatchType"%>
<%@page import="net.frcdb.api.game.match.Match"%>
<%@page import="net.frcdb.api.game.team.TeamEntry"%>
<%@page import="net.frcdb.api.game.event.Game"%>
<%@page import="net.frcdb.api.award.Award"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.List" %>
<%@page import="net.frcdb.api.team.HistoryEntry"%>
<%@page import="net.frcdb.api.team.Team" %>
<%@page import="net.frcdb.api.event.Event" %>
<%@page import="net.frcdb.db.Database" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@taglib uri="/WEB-INF/tlds/js.tld" prefix="js" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://frcdb.net/taglibs/utils" prefix="utils" %>

<tiles:insertDefinition name="layout-default">
	<tiles:putAttribute name="title" value="Event: ${name}"/>
	<tiles:putAttribute name="body">
		<h1>Event: ${data.event.name} (${data.game.year})</h1>
		<p class="breadcrumbs">
			<a href="/event/${data.event.shortName}/${data.game.gameYear}/standings">Standings</a>
		</p>
		
		Viewing results for ${data.game.gameYear}. Years available:<br>
		<ul>
			<c:forEach items="${data.games}" var="g">
				<li>
					<a href="/event/${data.event.shortName}/${g.gameYear}">
						${g.gameYear} - ${g.gameName}
					</a>
				</li>
			</c:forEach>
		</ul>
		
		<h2>Event Info</h2>
		<table width="90%" border="1" style="margin: auto; margin-bottom: 15px;">
			<tbody>
				<tr>
					<td>Name</td>
					<td>${data.event.name}</td>
				</tr>
				<tr>
					<td>Location</td>
					<td>${data.event.city}, ${data.event.state}</td>
				</tr>
				<tr>
					<td>Date</td>
					<td>${utils:date(data.game.startDate, 'MMMM dd yyyy')} - 
						${utils:date(data.game.endDate, 'MMMM dd yyyy')}</td>
				</tr>
				<c:if test="${utils:hasProperty(data.game, 'averageOPR')}">
					<tr>
						<td>Average OPR</td>
						<td>${utils:format("%.2f", data.game.averageOPR)}</td>
					</tr>
					<c:if test="${not empty data.game.highestOPRTeam}">
						<c:set property="hoprTeam" value="${data.game.highestOPRTeam.team}"/>
						<tr>
							<td>Highest OPR</td>
							<td>
								<a href="/event/${data.event.shortName}/${data.game.gameYear}/team/${hoprTeam.number}"
								   title="#${hoprTeam.number}: ${hoprTeam.nickname}">
									${utils:format("%.2f", data.game.highestOPR)}
								</a>
							</td>
						</tr>
					</c:if>
					
				</c:if>
			</tbody>
		</table>
		
		<%-- TODO: rewrite charts with Google Charts API
		<% if (g.hasTeamProperty(Game.PROP_TEAM_OPR)
				&& g.hasGameProperty(Game.PROP_GAME_OPR)) { %>
				<jsp:useBean id="gameOPR" 
							 class="net.frcdb.servlet.event.GameOPRPie"/>
				<jsp:setProperty name="gameOPR" 
								 property="entry" 
								 value="${game}"/>

				<cewolf:chart id="oprPie"
							  type="pie3d">
					<cewolf:data>
						<cewolf:producer id="gameOPR"/>
					</cewolf:data>
				</cewolf:chart>
				
				<h2>OPR Distribution</h2>
				<table style="margin: 0 auto;" border="1">
					<tbody>
						<tr><td>
							<cewolf:img chartid="oprPie" 
										renderer="/cewolf" 
										width="350"
										height="200">
								<cewolf:map tooltipgeneratorid="gameOPR"
											linkgeneratorid="gameOPR"/>
							</cewolf:img>
						</td></tr>
					</tbody>
				</table>
		<% } %>--%>
		
		<h2 style="padding-top: 15px;">Teams Attending</h2>
		<js:table id="event_teams" width="90%" align="center">
			<thead>
				<tr>
					<th>Number</th>
					<th>Nickname</th>
					<th>Rank</th>
					<c:if test="${utils:hasProperty(data.game, 'averageOPR')}">
						<th>OPR</th>
						<th>DPR</th>
					</c:if>
					<%--<th> TODO: content APIs
						Robot Info
					</th>--%>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${data.teams}" var="t">
					<tr>
						<td>${t.team.number}</td>
						<td>
							<a href="/event/${data.event.shortName}/${data.game.gameYear}/team/${t.team.number}">
								${t.team.nickname}
							</a>
						</td>
						<td>${t.rank}</td>
						<c:if test="${utils:hasProperty(t, 'OPR')}">
							<td>${utils:format("%.2f", t.OPR)}</td>
							<td>${utils.format("%.2f", t.DPR)}</td>
						</c:if>
						<td>
							<a href="/team/${t.team.number}/robot/${gameYear}">
								Info
							</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</js:table>
		
		<h2 style="padding-top: 15px;">Qualification Matches</h2>
		<js:table id="event_qualificationResults" width="90%" align="center">
			<thead>
				<tr>
					<th>Number</th>
					<th>Red 1</th>
					<th>Red 2</th>
					<th>Red 3</th>
					<th>Blue 1</th>
					<th>Blue 2</th>
					<th>Blue 3</th>
					<th>Red Score</th>
					<th>Blue Score</th>
					<th>Link</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${data.qualificationMatches}" var="m">
					<tr>
						<td>${m.number}</td>
						<c:forEach items="${m.redTeams}" var="t">
							<td>${t.number}</td>
						</c:forEach>
						<c:forEach items="${m.blueTeams}" var="t">
							<td>${t.number}</td>
						</c:forEach>
						<td>${m.redScore}</td>
						<td>${m.blueScore}</td>
						<td>
							<a href="/event/${data.event.shortName}/${data.game.gameYear}/match/${m.number}">
								Info
							</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</js:table>
		
		<h2 style="padding-top: 15px;">Quaterfinals Matches</h2>
		<js:table id="event_quarterfinalsResults" width="90%" align="center">
			<thead>
				<tr>
					<th>Number</th>
					<th>Red 1</th>
					<th>Red 2</th>
					<th>Red 3</th>
					<th>Blue 1</th>
					<th>Blue 2</th>
					<th>Blue 3</th>
					<th>Red Score</th>
					<th>Blue Score</th>
					<th>Link</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${data.quarterfinalMatches}" var="m">
					<tr>
						<td>${m.number}</td>
						<c:forEach items="${m.redTeams}" var="t">
							<td>${t.number}</td>
						</c:forEach>
						<c:forEach items="${m.blueTeams}" var="t">
							<td>${t.number}</td>
						</c:forEach>
						<td>${m.redScore}</td>
						<td>${m.blueScore}</td>
						<td>
							<a href="/event/${data.event.shortName}/${data.game.gameYear}/match/q${m.number}">
								Info
							</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</js:table>
		
		<h2 style="padding-top: 15px;">Semifinals Matches</h2>
		<js:table id="event_semifinalsResults" width="90%" align="center">
			<thead>
				<tr>
					<th>Number</th>
					<th>Red 1</th>
					<th>Red 2</th>
					<th>Red 3</th>
					<th>Blue 1</th>
					<th>Blue 2</th>
					<th>Blue 3</th>
					<th>Red Score</th>
					<th>Blue Score</th>
					<th>Link</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${data.semifinalMatches}" var="m">
					<tr>
						<td>${m.number}</td>
						<c:forEach items="${m.redTeams}" var="t">
							<td>${t.number}</td>
						</c:forEach>
						<c:forEach items="${m.blueTeams}" var="t">
							<td>${t.number}</td>
						</c:forEach>
						<td>${m.redScore}</td>
						<td>${m.blueScore}</td>
						<td>
							<a href="/event/${data.event.shortName}/${data.game.gameYear}/match/s${m.number}">
								Info
							</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</js:table>
		
		<h2 style="padding-top: 15px;">Finals Matches</h2>
		<js:table id="event_finalsResults" width="90%" align="center">
			<thead>
				<tr>
					<th>Number</th>
					<th>Red 1</th>
					<th>Red 2</th>
					<th>Red 3</th>
					<th>Blue 1</th>
					<th>Blue 2</th>
					<th>Blue 3</th>
					<th>Red Score</th>
					<th>Blue Score</th>
					<th>Link</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${data.finalMatches}" var="m">
					<tr>
						<td>${m.number}</td>
						<c:forEach items="${m.redTeams}" var="t">
							<td>${t.number}</td>
						</c:forEach>
						<c:forEach items="${m.blueTeams}" var="t">
							<td>${t.number}</td>
						</c:forEach>
						<td>${m.redScore}</td>
						<td>${m.blueScore}</td>
						<td>
							<a href="/event/${data.event.shortName}/${data.game.gameYear}/match/f${m.number}">
								Info
							</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</js:table>

		<c:if test="${admin}">
			<h2>Scheduled Updates</h2>
			<js:schedule id="eventScheduler"
						 type="eventUpdate"
						 eventName="${shortName}"
						 gameYear="${gameYear}"/>
		</c:if>
	</tiles:putAttribute>
</tiles:insertDefinition>
