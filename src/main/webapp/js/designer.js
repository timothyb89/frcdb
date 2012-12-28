Array.prototype.remove = function(s) {
	for (var i = 0; i <  this.length; i++) {
		if(s == this[i]) {
			this.splice(i, 1);
		}
	}
}

var Designer = {
	getPage: function(callback) {
		var postData = {
			'action': null
		};
		
		$.getJSON("/designer/api"), JSON.stringify(postData), function(data) {
			
		};
	}
}
