<%-- 
    Document   : stats
    Created on : Jan 25, 2013, 11:53:25 PM
    Author     : tim
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://frcdb.net/taglibs/utils" prefix="utils" %>
<%@ taglib uri="/WEB-INF/tlds/js" prefix="js" %>

<tiles:insertDefinition name="layout-default">
	<tiles:putAttribute name="title" value="Statistics"/>
	<tiles:putAttribute name="head-extra">
		<script type="text/javascript">
			google.load("visualization", "1", { packages: ["corechart", "geochart"] });
		</script>
	</tiles:putAttribute>
	<tiles:putAttribute name="body">
		<h1>Charts and Statistics</h1>
		
		<p>(This is just a charts API test at the moment)</p>
		
		<c:forEach items="${data.charts}" var="chart">
			<h2>${chart.displayName}</h2>
			<js:chart chartId="${chart.id}"
					  style="width: 90%; height: 400px; margin: 0 auto"/>
			<c:if test="${utils:isUserAdmin()}">
				<a class="generate-link" href="#" data-chartId="${chart.id}">
					Generate
				</a>
			</c:if>
		</c:forEach>
		
		<script type="text/javascript">
			$(".generate-link").click(function() {
				var chartId = $(this).attr("data-chartId");
				
				$.ajax({
					type: "POST",
					url: "/json/admin/chart/generate",
					data: { id: chartId },
					success: function(response) {
						console.log(response);
						alert(response.message);
					}
				});
			});
		</script>
		
	</tiles:putAttribute>
</tiles:insertDefinition>
