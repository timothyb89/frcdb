<%-- 
    Document   : index
    Created on : Mar 6, 2010, 7:11:29 PM
    Author     : tim
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://frcdb.net/taglibs/utils" prefix="utils" %>

<tiles:insertDefinition name="layout-default">
	<tiles:putAttribute name="title" value="Home"/>
	<tiles:putAttribute name="body">
		<h1>Homepage</h1>
		
		<p>
			FRC-DB is a database of events, teams, and robots that can be used
			as both a scouting and historical reference.
		</p>

		<h2>Statistics</h2>
		<p>
			We know about
			<a href="/search?q=&amp;type=teams">${data.teamCount} teams</a>,
			<a href="/search?q=&amp;type=events">${data.eventCount} events</a>,
			and ${data.gameCount} event-years.
		</p>
		
		<div class="stats">
			<c:if test="${not utils:isEmpty(data.currentGames)}">
				<div class="stats-item-nofloat" style="width: 75%; margin: auto">
					<div class="stats-header">
						<h3>Events Currently In Progress</h3>
					</div>
					<ul>
						<c:forEach items="${data.currentGames}" var="game">
							<li>
								<a href="/event/${game.event.shortName}/${game.gameYear}">
									${game.event.name}
								</a>
							</li>
						</c:forEach>
					</ul>
				</div>
				<br class="clear"/>
			</c:if>
			
			<div class="stats-item">
				<div class="stats-header">
					<h3>Top Events</h3>
				</div>
				<ul>
					<c:forEach items="${data.topEvents}" var="event">
						<li>
							<a href="/event/${event.shortName}">
								${event.name} (${event.hits})
							</a>
						</li>
					</c:forEach>
				</ul>
				<div class="stats-footer">
					<a href="/search?q=&amp;type=events">View All</a>
				</div>
			</div>

			<div class="stats-item">
				<div class="stats-header">
					<h3>Top Teams</h3>
				</div>
				<ul>
					<c:forEach items="${data.topTeams}" var="team">
						<li>
							<a href="/team/${team.number}">
								${team.nickname} (${team.hits})
							</a>
						</li>
					</c:forEach>
					
				</ul>
				<div class="stats-footer">
					<a href="/search?q=&amp;type=teams">View All</a>
				</div>
			</div>
			
			<br class="clear">
			
			<div class="stats-item">
				<div class="stats-header">
					<h3>Latest Games</h3>
				</div>
				<ul>
					<c:forEach items="${data.latestGames}" var="game">
						<li>
							<a href="/event/${game.event.shortName}/${game.gameYear}">
								<%-- possibly slow --%>
								${game.event.name} (${utils:date(game.startDate, "MMMM dd")})
							</a>
						</li>
					</c:forEach>
					
				</ul>
			</div>
			
			<c:if test="${not utils:isEmpty(data.upcomingGames)}">
				<div class="stats-item">
					<div class="stats-header">
						<h3>Upcoming Games</h3>
					</div>
					<ul>
						<c:forEach items="${data.upcomingGames}" var="game">
							<li>
								<a href="/event/${game.event.shortName}/${game.gameYear}">
									<%-- possibly slow --%>
									${game.event.name} (${utils:date(game.startDate, "MMMM dd")})
								</a>
							</li>
						</c:forEach>
					</ul>
				</div>
			</c:if>
		</div>
		
		<br class="clear">
		
		<h2>Other Info</h2>
		<p>
			Get started by searching for a team or viewing event list
			via the appropriate links on the left menu. Event and team
			data is linked together to allow for easy browsing data.
			Clicking on table headers with arrows should sort the data
			in the given column to help identify teams and events that
			stick out. Although <b>registration is not required</b>, 
			it has many benefits, including:
		</p>
		<ul>
			<li>
				<b>Easy Editing</b> - Post and edit information without any
				other authentication
			</li>
			<li>
				<b>Media Uploads</b> - Post robot photos and match videos!
			</li>
		</ul>

		<h2>Other Odds and Ends</h2>
		<p>
			Do you post match videos to YouTube? Make an account and link them
			to our match pages - see the "Add Media" button at the bottom of
			every match page!
			(<a href="http://frcdb.net/event/colorado/2011/match/f20">example</a>)
		</p>
		<p>
			To learn more about FRC-DB, click on the 'Dev Site' link
			to view the wiki and trackers, where you can submit bug reports
			and feature requests in addition to browsing technical info
			about the database.
		</p>

		<p>
			Hope you enjoy this resource!<br>
			<i>-FRC Team #1977</i>
		</p>

		<h1>News</h1>
		<ul>
			<li>
				<b>March 13th, 2012:</b> We've just added a scheduler to
				auto-update event data - in the next few days we'll hopefully
				have data updating in near-real-time during events!
			</li>
			<li>
				<b>February 21st, 2012:</b> FRC-DB 2.0 is ready - be sure to
				take it for a spin! Make sure to post questions and comments
				<a href="http://dev.frcdb.net/projects/main/boards">in the forums</a>,
				and tell us about any issues you find
				<a href="http://dev.frcdb.net/projects/main/issues">in the bugtracker</a>.
			</li>
		</ul>
		
		<button id="testButton">Push Me</button>
		
		<script type="text/javascript">
			var template = {
				"Name": {
					name: "name",
					type: "text"
				},
				"Number": {
					name: "number",
					type: "number"
				}
			};
		
			$(document).ready(function() {
				$("#testButton").button();
				$("#testButton").click(function() {
					$.dialogform({
						title: "Test Form",
						url: "/json/test/",
						fields: template,
						success: function(data) {
							console.log(data);
						}
					});
				});
			});
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>
