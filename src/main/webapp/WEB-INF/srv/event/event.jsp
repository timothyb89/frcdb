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
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/tlds/js.tld" prefix="js" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="/WEB-INF/tlds/cewolf.tld" prefix="cewolf" %>
<%@taglib uri="http://frcdb.net/taglibs/utils" prefix="utils" %>
<%@taglib uri="http://frcdb.net/taglibs/permission" prefix="permission" %>

<%
Event e = (Event) request.getAttribute("event");
pageContext.setAttribute("name", e.getName());
pageContext.setAttribute("shortName", e.getShortName());

Game g = (Game) request.getAttribute("eventGame");
pageContext.setAttribute("game", g);

pageContext.setAttribute("year", g.getGameYear());
pageContext.setAttribute("gameYear", g.getGameYear());
pageContext.setAttribute("gameName", g.getGameName());
%>

<tiles:insertDefinition name="layout-default">
	<tiles:putAttribute name="title" value="Event: ${name}"/>
	<tiles:putAttribute name="body">
		<h1>Event: ${name} (${year})</h1>
		<p class="breadcrumbs">
			<a href="/event/${shortName}/${gameYear}/standings">Standings</a>,
			<a href="/event/${shortName}/wiki">Wiki</a>
		</p>
		
		Viewing results for ${gameYear}. Years available:<br>
		<ul>
			<c:forEach items="${event.gamesSorted}" var="g">
				<li>
					<a href="/event/${event.shortName}/${g.gameYear}">
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
					<td>${name}</td>
				</tr>
				<tr>
					<td>Location</td>
					<td>${event.city}, ${event.state}</td>
				</tr>
				<tr>
					<td>Date</td>
					<td>${utils:date(game.startDate, 'MMMM dd yyyy')} - 
						${utils:date(game.endDate, 'MMMM dd yyyy')}</td>
				</tr>
				<% if (g.hasGameProperty(Game.PROP_GAME_OPR)) { %>
					<% GameOPRProvider o = (GameOPRProvider) g; %>
					
					<tr>
						<td>Average OPR</td>
						<td><%= String.format("%.2f", o.getAverageOPR()) %></td>
					
					<% if (o.getHighestOPRTeam() != null) { %>
						<%
						TeamEntry hTeam = o.getHighestOPRTeam();
						
						String url = "/event/"
								+ e.getShortName() + "/"
								+ g.getGameYear()
								+ "/team/"
								 + hTeam.getTeamNumber();
						
						String title = "#" + hTeam.getTeamNumber() + ": "
								+ hTeam.getTeamNickname();
						
						double hOPR = o.getHighestOPR();
						%>
						<tr>
							<td>Highest OPR</td>
							<td>
								<a href="<%= url %>" title="<%= title %>">
									<%= String.format("%.2f", hOPR) %>
								</a>
							</td>
						</tr>
					<% } %>
				<% } %>
			</tbody>
		</table>
		
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
		<% } %>
		
		<h2 style="padding-top: 15px;">Teams Attending</h2>
		<js:table id="event_teams" width="90%" align="center">
			<thead>
				<tr>
					<th>Number</th>
					<th>Nickname</th>
					<th>Rank</th>
					<% if (g.hasTeamProperty(Game.PROP_TEAM_OPR)) { %>
						<th>OPR</th>
						<th>DPR</th>
					<% } %>
					<th>
						Robot Info
					</th>
				</tr>
			</thead>
			<tbody>
				<% for (TeamEntry t : g.getTeams()) { %>
					<%
					String url =
							"/event/"
							+ e.getShortName() + "/"
							+ g.getGameYear() 
							+ "/team/"
							+ t.getTeamNumber();
					%>
					<tr>
						<td><%= t.getTeamNumber() %></td>
						<td>
							<a href="<%= url %>">
								<%=	t.getTeamNickname() %>
							</a>
						</td>
						<td><%= t.getRank() %></td>
						<% if (g.hasTeamProperty(Game.PROP_TEAM_OPR)) { %>
							<% OPRProvider opr = (OPRProvider) t; %>
							<td><%= String.format("%.2f", opr.getOPR()) %></td>
							<td><%= String.format("%.2f", opr.getDPR()) %></td>
						<% } %>
						<td>
							<a href="/team/<%= t.getTeamNumber() %>/robot/${gameYear}">
								Info
							</a>
						</td>
					</tr>
				<% } %>
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
				<% for (Match m : g.getMatches(MatchType.QUALIFICATION)) { %>
					<%
					String url =
							"/event/"
							+ e.getShortName() + "/"
							+ g.getGameYear()
							+ "/match/"
							+ m.getNumber();
					%>
					<tr>
						<td><%= m.getNumber() %></td>
						<% for (int t : m.getRedTeams()) { %>
							<td>
								<%= t %>
							</td>
						<% } %>
						<% for (int t : m.getBlueTeams()) { %>
							<td>
								<%= t %>
							</td>
						<% } %>
						<td><%= m.getRedScore() %></td>
						<td><%= m.getBlueScore() %></td>
						<td><a href="<%= url %>">Info</a></td>
					</tr>
				<% } %>
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
				<% for (Match m : g.getMatches(MatchType.QUARTERFINAL)) { %>
					<%
					String url =
							"/event/" 
							+ e.getShortName() + "/"
							+ g.getGameYear()
							+ "/match/"
							+ "q" + m.getNumber();
					%>
					<tr>
						<td><%= m.getNumber() %></td>
						<% for (int t : m.getRedTeams()) { %>
							<td>
								<%= t %>
							</td>
						<% } %>
						<% for (int t : m.getBlueTeams()) { %>
							<td>
								<%= t %>
							</td>
						<% } %>
						<td><%= m.getRedScore() %></td>
						<td><%= m.getBlueScore() %></td>
						<td><a href="<%= url %>">Info</a></td>
					</tr>
				<% } %>
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
				<% for (Match m : g.getMatches(MatchType.SEMIFINAL)) { %>
					<%
					String url =
							"/event/"
							+ e.getShortName() + "/"
							+ g.getGameYear()
							+ "/match/"
							+ "s" + m.getNumber();
					%>
					<tr>
						<td><%= m.getNumber() %></td>
						<% for (int t : m.getRedTeams()) { %>
							<td>
								<%= t %>
							</td>
						<% } %>
						<% for (int t : m.getBlueTeams()) { %>
							<td>
								<%= t %>
							</td>
						<% } %>
						<td><%= m.getRedScore() %></td>
						<td><%= m.getBlueScore() %></td>
						<td><a href="<%= url %>">Info</a></td>
					</tr>
				<% } %>
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
				<% for (Match m : g.getMatches(MatchType.FINAL)) { %>
					<%
					String url =
							"/event/"
							+ e.getShortName() + "/"
							+ g.getGameYear()
							+ "/match/"
							+ "f" + m.getNumber();
					%>
					<tr>
						<td><%= m.getNumber() %></td>
						<% for (int t : m.getRedTeams()) { %>
							<td>
								<%= t %>
							</td>
						<% } %>
						<% for (int t : m.getBlueTeams()) { %>
							<td>
								<%= t %>
							</td>
						<% } %>
						<td><%= m.getRedScore() %></td>
						<td><%= m.getBlueScore() %></td>
						<td><a href="<%= url %>">Info</a></td>
					</tr>
				<% } %>
			</tbody>
		</js:table>
		
		<permission:if name="root.task.add">
			<h2>Scheduled Updates</h2>
			<js:schedule id="eventScheduler"
						type="eventUpdate"
						eventName="${shortName}"
						gameYear="${gameYear}"/>
		</permission:if>
	</tiles:putAttribute>
</tiles:insertDefinition>
