function textField_clear(field) {
    if (field.defaultValue == field.value) {
		field.value = '';
	} else if (field.value == '') {
		field.value = field.defaultValue;
	}
}

Array.prototype.remove = function(s) {
	for (var i = 0; i <  this.length; i++) {
		if(s == this[i]) {
			this.splice(i, 1);
		}
	}
}
