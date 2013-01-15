/*
 * Creates and displays a form dialog based on some form model. Attach this
 * to an event (e.g. button press, link click) to have a modal form pop up.
 *
 * All parameters of fields are applied directly to <input> tags, and the name
 * for each field is used as the <label>
 *
 * Sample 'fields':
 * var fields = {
 *     "Name": {
 *         name: "namefield",
 *         type: "text"
 *     },
 *     "Number": {
 *         name: "numberfield",
 *         type: "number"
 *     }
 * }
 * 
 * You can also create templates and apply individual properties to them later.
 * This could be used to show an empty form for creating a new user, versus a
 * populated form for modifying a user:
 * 
 * var values = $.extend(true, {
 *     "Name": {
 *         value: "John Smith"
 *     },
 *     "Number": {
 *         value: 1234
 *     }
 * }, fields);
 * 
 * This will merge the field definitions into the new `values` object, which
 * can then be passed to the dialogform function.
 * 
 * Note that setting the contentType attribute to `multipart/form-data` will
 * automatically wrap the form in a FormData object rather than using jQuery's
 * form.serialize().
 * 
 * This file also includes a utility for pushing a JSON object's values into an
 * existing template, adjusting the 'value' field. Note that this only works 
 * with first-level values, and requires the 'name' field to be the same as
 * the JSON field name. The original template will not be modified and the
 * merged field list will be returned.
 * See $.pushJsonToTemplate() below for more.
 */
(function($) {
	$.dialogform = function(options) {
		var settings = $.extend({
			'title': "Form",		// the dialog title
			'fields': {},			// the list of form fields
			'submitText': 'Submit',	// text for the submit button
			'cancelEnabled': true,	// turn on/off the cancel button
			'cancelText': 'Cancel',	// cancel button text
			'width': 350,			// initial dialog width
			'height': 400,			// initial dialog height
			'url': null,			// submit url
			'method': 'POST',		// form submit method
			'contentType': null,    // content type for form submission
			'success': null,		// ajax success callback
			'error': null,			// ajax error callback
			'cancel': null			// user cancel call back
		}, options);

		// init form and inputs
		var dialog = $("<div>");
		var form = $("<form>");
		if (settings.contentType === "multipart/form-data") {
			form.attr("enctype", "multipart/form-data");
		}
		
		for (var labelName in settings.fields) {
			if (!settings.fields.hasOwnProperty(labelName)) {
				// skip inherited members
				continue;
			}
		
			var label = $("<label>");
			label.html(labelName);
			label.css("display", "block");
			
			var options = settings.fields[labelName];
			if (options.name) {
				label.attr("for", options.name);
			}

			form.append(label);

			var input = $("<input>");
			input.addClass("ui-corner-all");
			input.addClass("ui-widget-content");
			input.css("width", "95%");
			for (var attr in options) {
				if (options.hasOwnProperty(attr)) {
					input.attr(attr, options[attr]);
				}
			}
			form.append(input);
		}
	
		dialog.append(form);

		// create buttons
		var btns = new Object();
		btns[settings.submitText] = function() {
			if (settings.url) {
				var processDataWrapper = true;
				var contentTypeWrapper = settings.contentType;
				var dataWrapper;
				
				// fix upload for multipart data
				if (contentTypeWrapper === "multipart/form-data") {
					console.log("Using HTML5 FormData")
					processDataWrapper = false;
					
					// content type must be false to prevent jquery from
					// adding the wrong one "automatically"
					contentTypeWrapper = false;
					dataWrapper = new FormData(form[0]);
				} else {
					contentTypeWrapper = "'application/x-www-form-urlencoded";
					dataWrapper = form.serialize();
				}
				
				$.ajax({
					type: settings.method,
					url: settings.url,
					data: dataWrapper,
					contentType: contentTypeWrapper,
					processData: processDataWrapper,
					success: function(data, textStatus, jqXHR) {
						// close the dialog and forward the callback
						dialog.dialog("close");
						
						if (settings.success) {
							settings.success(data, textStatus, jqXHR);
						}
					},
					error: settings.error
				});
			} else {
				console.log("No URL defined, simulating...");
				
				if (settings.success) {
					settings.success(null);
				}
				
				dialog.dialog("close");
			}
		};

		// add cancel button if configured
		if (settings.cancelEnabled) {
			btns[settings.cancelText] = function() {
				// close and remove from the dom
				dialog.dialog("close");
				
				if (settings.cancel) {
					settings.cancel();
				}
			};
		}

		$("body").append(dialog);

		dialog.dialog({
			title: settings.title,
			autoOpen: true,
			width: settings.width,
			height: settings.height,
			buttons: btns,
			close: function() {
				dialog.remove(); // remove from dom
			}
		});
	};

	$.pushJsonToTemplate = function(object, template) {
		// deep copy template
		var copy = $.extend({}, template);
		
		for (var itemName in copy) {
			if (!template.hasOwnProperty(itemName)) {
				continue;
			}
		
			var item = copy[itemName];
			
			if (!item.name) {
				// item is missing a name attribute, skip
				continue;
			}
			
			var value = object[item.name];
			if (!value) {
				// this doesn't exist in json object
				continue;
			}
		
			item.value = value;
		}
	
		return copy;
	};
	
	/**
	 * Merges properties in props with the given template. A new template with
	 * the merged properties will be returned. "Parent" properties in the
	 * original template will always be preserved, but "child" properties will
	 * be overwritten by those contained in props.
	 * Pretty much equivalent to $.extend(true, newProps, template), with the
	 * main difference of $.extend() probably handling copying better
	 */
	$.templateMerge = function(template, props) {
		var copy = $.extend({}, template); // deep copy
		
		for (var parentName in props) {
			if (!props.hasOwnProperty(parentName)) {
				continue;
			}
			
			// check template for parent property
			var p = copy[parentName];
			if (!p) {
				// template doesn't have the parent property
				// for now, just assume incomplete and skip
				continue;
			}
			
			var prop = props[parentName];
			// push all children of the diff property to the template
			for (var childName in prop) {
				if (!prop.hasOwnProperty(childName)) {
					continue;
				}
				
				// push
				p[childName] = prop[childName];
			}
		}
		
		return copy;
	};
})(jQuery);

