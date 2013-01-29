/*
 * A small utility to use the Google Chart API to display a chart with data from
 * a JSON request to /chart/
 */
(function($) {
	$.chart = function(options) {
		var stringToFunction = function(str) {
			var arr = str.split(".");

			var fn = (window || this);
			for (var i = 0, len = arr.length; i < len; i++) {
				fn = fn[arr[i]];
			}

			if (typeof fn !== "function") {
				throw new Error("function not found");
			}

			return  fn;
		};
		
		$.getJSON("/chart/" + options.chartId, function(data) {
			var table = new google.visualization.DataTable();
			
			// dates can't be sent via json
			var dateCols = [];
			
			console.log("adding cols...");
			
			// add cols
			for (var i = 0; i < data.columns.length; i++) {
				var column = data.columns[i];
				
				if (column.type == "date") {
					dateCols.push(i);
				}
				
				table.addColumn(column);
			}
			
			console.log("fixing dates...");
			
			// fix date rows
			data.rows.forEach(function(row) {
				dateCols.forEach(function(col) {
					var millisDate = row[col];
					row[col] = new Date(millisDate);
				});
			});
			
			console.log(data.rows);
			
			console.log("adding rows...");
			// add rows
			table.addRows(data.rows);
			
			console.log("creating chart...");
			
			// instantiate chart class
			var className = "google.visualization." + data.chartType;
			var chartClass = stringToFunction(className);
			var chart = new chartClass(
					document.getElementById(options.containerId));
			
			console.log("drawing chart...");
			
			if (data.options) {
				chart.draw(table, data.options);
			} else {
				chart.draw(table, data);
			}
		});
	}
})(jQuery);
