<%-- 
    Document   : about
    Created on : Jul 28, 2010, 2:33:47 AM
    Author     : tim
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<%
pageContext.setAttribute("srcPrefix",
		"http://dev.frcdb.net/projects/main/repository/show/src/main/java/net"
		+ "/frcdb/stats/calc");
%>

<tiles:insertDefinition name="layout-default">
	<tiles:putAttribute name="title" value="Home"/>
	<tiles:putAttribute name="body">
		<h1>About</h1>

		<h2>Background Information</h2>
		<p>
			FRC-DB is powered by a <span style="text-decoration: line-through">
			series of tubes</span> over 30,000 lines of Java  and HTML code 
			utilizing a multitude of helpful libraries to get the job done. The 
			database itself is intended to be completely autonomous: all data is
			is extracted from the FIRST website by several specialized crawlers.
			For more information, see the
			<a href="http://dev.frcdb.net/">dev site</a>.
		</p>

		<a name="faqs"></a>
		<h2>FAQs</h2>
		<div id="faqs">
			<a name="faq-browsers"></a>
			<h3>What browsers is FRC-DB compatible with?</h3>
			<p>
				FRC-DB works well with most (if not all) standards compliant
				browsers (Firefox, Chrome, Opera), though recent versions with
				better HTML5 support will probably give the best results. Internet
				Explorer should also work if using IE8 or later, though only IE9 and
				up support some visual-only enhancements. IE7 doesn't support many
				of the extra JavaScript features (interactive tables, etc).
			</p>

			<a name="faq-content"></a>
			<h3>What content can I submit?</h3>
			<p>
				We accept both photos and YouTube videos. However, all submitted
				content will be placed in the
				<a href="/moderator">moderation queue</a> before being displayed
				by default.
			</p>
			
			<a name="faq-updates"></a>
			<h3>When is your data updated?</h3>
			<p>
				We update our data on an as-needed basis. Team data is usually
				retrieved from FIRST at the beginning of each season along with
				event team lists, while event data, including match scores,
				standings, and OPR/DPR, is (as of 2012) updated automatically
				at 10:00. 12:00, 14:00, and 17:00 MST on competition days for
				individual events. However, please
				<a href="http://dev.frcdb.net/projects/main/issues/new">file a bug report</a>
				if you find data that is out-of-date.
			</p>
			
			<a name="faq-data"></a>
			<h3>Is your data publicly available?</h3>
			<p>
				For the most part, yes. We offer JSON-formatted data through our
				API. For more information on this, see
				<a href="http://dev.frcdb.net/projects/main/wiki/API">the API documentation</a>.
			</p>
		</div>
		
		<a name="stats"></a>
		<h2>Different Statistics</h2>
		<p>
			The vast majority of these statistics have been created by the FIRST
			community and are not our creation. Though accuracy is one of our
			top priorities, keep in mind that our calculations are done in mass,
			and we haven't checked each individual team.
		</p>

		<a name="stats-opr"></a>
		<h3>OPR</h3>
		<p>
			OPR (or 'Offensive Power Rating') is an event/team-specific average
			of points a team contributes to their alliance score during the
			course of an event. A higher OPR means a team scores more points,
			on average, during a given match. Keep in mind that OPR (unlike
			BBQ/Sauce, which use their own scale) uses the same scale as the
			FIRST game. That is, 1 OPR point is equal to 1 game point. Teams
			with a higher OPR are often more successful at offense.
		</p>
		<ul>
			<li>
				<a href="${srcPrefix}/OPRDPRCalc.java">
					Implementation Source
				</a>,
				<a href="http://www.chiefdelphi.com/forums/showpost.php?p=484220&amp;postcount=19">
					Algorithm Source
				</a>
			</li>
		</ul>

		<a name="stats-aopr"></a>
		<h3>AOPR</h3>
		<p>
			AOPR (or 'Average OPR') is simply an average of the OPR statics for
			a given team across all of the team's attended events.
		</p>
		<ul>
			<li>
				<a href="${srcPrefix}/AOPRADPRCalc.java">
					Implementation Source
				</a>
			</li>
		</ul>

		<a name="stats-dpr"></a>
		<h3>DPR</h3>
		<p>
			DPR (or 'Defensive Power Rating') is an event/team-specific average
			of points scored against a team's alliance during the course of an
			event. A higher DPR means a more points are scored against a team,
			on average, during a given match. A lower DPR, on the other hand,
			likely means the given team is better at defense. As with OPR, this
			statistic uses the game scale (1 DPR point is equal to 1 game
			point).
		</p>
		<ul>
			<li>
				<a href="${srcPrefix}/OPRDPRCalc.java">
					Implementation Source
				</a>,
				<a href="http://www.chiefdelphi.com/forums/showpost.php?p=484220&amp;postcount=19">
					Algorithm Source
				</a>
			</li>
		</ul>

		<a name="stats-adpr"></a>
		<h3>ADPR</h3>
		<p>
			ADPR (or 'Average DPR') is simply an average of the DPR statics for
			a given team across all of the team's attended events.
		</p>
		<ul>
			<li>
				<a href="${srcPrefix}/AOPRADPRCalc.java">
					Implementation Source
				</a>
			</li>
		</ul>
					
		<h2>Who makes FRC-DB?</h2>
		<p>
			FRC-DB is currently made via a combined effort from the members of
			<a href="http://robotics.lovelandhs.org/">FIRST Team 1977</a>. 
		</p>
	</tiles:putAttribute>
</tiles:insertDefinition>