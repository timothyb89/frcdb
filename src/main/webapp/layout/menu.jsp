<%-- 
    Document   : menu
    Created on : Jul 12, 2009, 9:28:58 PM
    Author     : tim
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/tlds/js" prefix="js" %>
<%@ taglib uri="http://frcdb.net/taglibs/utils" prefix="utils" %>

<ul>
	<li>
		<h2>Navigation</h2>
		<ul>
			<li>
				<a href="/">
					Home
				</a>
			</li>
			<li>
				<a href="/search">
					Search
				</a>
			</li>
			<li>
				<a href="/about">
					About
				</a>
			</li>
			<li>
				<a href="http://dev.frcdb.net/projects/main">
					Dev Site
				</a>
			</li>
		</ul>
	</li>
	<li>
		<h2>Search</h2>
		<form action="/search" method="GET">
			<js:input name="q" type="text" value="Query..." size="15"/>
			<br>
			<label><input type="radio"
					name="type"
					value="teams"
					id="teamsBoxQ"
					checked="checked"> Teams</label>, 
			<label><input type="radio"
					name="type"
					value="events"
					id="eventsBoxQ"> Events</label>
			<br>
			<p><input type="submit" value="Search" /></p>
		</form>
	</li>
	<li>
		<c:choose>
			<c:when test="${empty user}">
				<h2>Login</h2>
				Login with a <utils:login text="Google Account"/>.
			</c:when>
			<c:otherwise>
				<h2>User</h2>
				Welcome, ${user.nickname}
				<utils:logout text="Logout?"/>
				<c:if test="${admin}">
					<br>
					You are an admin!
				</c:if>
			</c:otherwise>
		</c:choose>
	</li>
</ul>
