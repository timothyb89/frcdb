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

		<h2>Beta warning!</h2>
		<p>
			We're just wrapping up a port to a new host and there's still bound
			to be a large number of issues. Please bear with us while we get
			things sorted, and sorry for the inconvenience!
		</p>
		<p>
			Features currently disabled or broken:
		</p>
		<ul>
			<li>Media uploads</li>
			<li>Searching, except by exact team number / event shortname</li>
			<li>Standings pages (full event view is fine, however)</li>
			<li>Lots of other things</li>
		</ul>
		<p>
			A lot of data may be missing or otherwise unavailable at the moment
			as we can only import a small amount of data at a time. As such,
			it may take several days for everything to get back to normal.
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
		<%--<p>
			Do you post match videos to YouTube? Make an account and link them
			to our match pages - see the "Add Media" button at the bottom of
			every match page!
			(<a href="http://frcdb.net/event/colorado/2011/match/f20">example</a>)
		</p>--%>
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
				<b>January 8th, 2012:</b> We've back! The host switch is
				wrapping up and we're live again. Existing data will be imported
				over the next few days, and 2013 data will be coming soon.
			</li>
		</ul>
	</tiles:putAttribute>
</tiles:insertDefinition>
