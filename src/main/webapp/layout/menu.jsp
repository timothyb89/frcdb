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
				<a href="http://code.google.com/p/frcdb/">
					Dev Site
				</a>
			</li>
		</ul>
	</li>
	<li>
		<h2>Search</h2>
		<form action="/search" method="GET">
			<%--<js:input name="q" type="text" value="Query..." size="15"/>
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
			<p><input type="submit" value="Search" /></p>--%>
			<script>
				(function() {
					var cx = '015151542149676618601:yp-wr_3klpm';
					var gcse = document.createElement('script'); gcse.type = 'text/javascript'; gcse.async = true;
					gcse.src = (document.location.protocol == 'https:' ? 'https:' : 'http:') +
						'//www.google.com/cse/cse.js?cx=' + cx;
					var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(gcse, s);
				})();
			</script>
			<!-- Put the following javascript before the closing </head> tag. -->
  
<script>
  (function() {
    var cx = '015151542149676618601:yp-wr_3klpm';
    var gcse = document.createElement('script'); gcse.type = 'text/javascript'; gcse.async = true;
    gcse.src = (document.location.protocol == 'https:' ? 'https:' : 'http:') +
        '//www.google.com/cse/cse.js?cx=' + cx;
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(gcse, s);
  })();
</script>
<!-- Place this tag where you want the search box to render -->
<gcse:searchbox-only></gcse:searchbox-only>
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
					<br>You are an admin!
					<br>View the <a href="/admin">admin panel</a>.
				</c:if>
			</c:otherwise>
		</c:choose>
	</li>
</ul>
