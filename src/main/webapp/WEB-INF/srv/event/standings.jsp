<%-- 
    Document   : standings
    Created on : Jul 27, 2010, 10:15:35 PM
    Author     : tim
--%>

<%@page import="net.frcdb.api.game.team.element.Seeding2010Provider"%>
<%@page import="net.frcdb.api.game.team.element.Seeding2007Provider"%>
<%@page import="net.frcdb.api.game.team.element.HangingProvider"%>
<%@page import="net.frcdb.api.game.standing.Standing"%>
<%@page import="net.frcdb.api.game.team.element.MatchPointsProvider"%>
<%@page import="net.frcdb.api.game.event.Game"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.List" %>
<%@page import="net.frcdb.api.team.Team" %>
<%@page import="net.frcdb.api.event.Event" %>
<%@page import="net.frcdb.db.Database" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/tlds/js.tld" prefix="js" %>

<%
Event e = (Event) request.getAttribute("event");
pageContext.setAttribute("name", e.getName());
pageContext.setAttribute("shortName", e.getShortName());

Game g = (Game) request.getAttribute("eventGame");
pageContext.setAttribute("game", g);
request.removeAttribute("eventGame");

pageContext.setAttribute("gameYear", g.getGameYear());
pageContext.setAttribute("gameName", g.getGameName());
%>

<tiles:insertDefinition name="layout-default">
	<tiles:putAttribute name="title" value="Event Standings: ${name}"/>
	<tiles:putAttribute name="body">
		<h1>Event Standings: ${name}</h1>
		<p class="breadcrumbs">
			<a href="/event/${shortName}/${gameYear}">Event Page</a>
		</p>
		
		<% List<Standing> standings = g.getStandings(); %>
		
		<% if (standings == null) { %>
			No standings data found!
		<% } else { %>
			<js:table id="event_standings" width="90%" align="center">
				<thead>
					<tr>
						<th>Rank</th>
						<th>Team</th>
						<th>Matches</th>
						<% if (g.hasStandingProperty(Game.PROP_TEAM_SEEDING_2007)) { %>
							<th>Wins</th>
							<th>Losses</th>
							<th>Ties</th>
							<th>QS</th>
							<th>RS</th>
						<% } %>
						
						<% if (g.hasStandingProperty(Game.PROP_TEAM_SEEDING_2010)) { %>
							<th>Seeding Score</th>
							<th>Co-op. Bonus</th>
						<% } %>
						
						<% if (g.hasStandingProperty(Game.PROP_TEAM_HANGING)) { %> 
							<th>Hanging Pts.</th>
						<% } %>
						
						<% if (g.hasStandingProperty(Game.PROP_TEAM_MATCH_POINTS)) { %> 
							<th>Match Points</th>
						<% } %>
					</tr>
				</thead>
				<tbody>
					<% Collections.sort(standings); %>
					<% for (Standing s : standings) { %>
						<%
						String url = "/event/" + e.getShortName()
								+ "/team/" + s.getTeamNumber();
						%>
						<tr>
							<td><%= s.getRank() %></td>
							<td>
								<a href="<%= url %>"
								   title="<%= s.getTeamNickname() %>">
									<%= s.getTeamNumber() %>
								</a>
							</td>
							<td><%= s.getMatchesPlayed() %></td>
							
							<% if (s instanceof Seeding2010Provider) { %>
								<% Seeding2010Provider sp = (Seeding2010Provider) s; %>
								<td><%= sp.getSeedingScore() %></td>
								<td><%= sp.getCoopertitionBonus() %></td>
							<% } %>
							
							<% if (s instanceof HangingProvider) { %>
								<% HangingProvider hp = (HangingProvider) s; %>
								<td><%= hp.getHangingPoints() %></td>
							<% } %>
							
							<% if (s instanceof Seeding2007Provider) { %>
								<% Seeding2007Provider sp = (Seeding2007Provider) s; %>
								<td><%= sp.getWins() %></td>
								<td><%= sp.getLosses() %></td>
								<td><%= sp.getTies() %></td>
								<td><%= sp.getQualificationScore() %></td>
								<td><%= sp.getRankingScore() %></td>
							<% } %>
							
							<% if (s instanceof MatchPointsProvider) { %> 
								<% MatchPointsProvider mpp = (MatchPointsProvider) s; %>
								<td><%= mpp.getMatchPoints() %></td>
							<% } %>
						</tr>
					<% } %>
				</tbody>
			</js:table>
		<% } %>

		Back to <a href="/event/${shortName}">Event: ${name}</a>
	</tiles:putAttribute>
</tiles:insertDefinition>