
(function($) {
	$.fn.uploader = function(method) {
		
		var methods = {
			init: function(options) {
				var settings = $.extend({
					'images': true,
					'videos': true,
					'basePath': "bogus", // needs to be set at runtime
					'imagePath': "/content/aupload/image",
					'videoPath': "/content/aupload/video",
					'addPath': "/content/add",
					'contentPath': "/content",
					'cpid': 'bogus',
					'type': 'bogus'
				}, options);
				
				return this.each(function() {
					var $this = $(this);
					
					var data = {
						cids: [] // content ids to be added (in the queue)
					};
					$this.data('uploaderData', data);
					
					// create the queue var early so it can be referenced,
					// but don't add it yet
					var queue = $('<ul class="upload-image-queue">');
					
					// helper function for queueing elements
					var queueAdd = function(cid) {
						var src = settings.basePath + settings.contentPath
								+ "/" + cid + "/thumbnail";
						var image = $('<image src="' + src + '">');
						var li = $('<li>');
						li.attr("cid", cid); // save the cid with the li
						li.css("float", "left").css("padding", "5px");
						li.append(image);
						
						queue.append(li);
						
						image.click(function() {
							if (confirm("Really remove?")) {
								queueRemove(li);
							}
						});
						
						data.cids.push(cid);
					}
					
					var queueRemove = function(element) {
						element.remove();
						data.cids.remove(element.attr("cid"));
					}
					
					// create the upload dialog
					var dialog = $("<div>");
					
					// create the file selector
					var selector = $("<div>");
					selector.append($("<h3>File Selector</h3>"))
					
					if (settings.images) {
						var imageButton = $("<button>Image</button>");
						imageButton.button(); // jqUI
						selector.append(imageButton);
						
						var imageDialog = $("<div>");
						imageDialog.append($("<h3>Upload an Image</h3>"));
						imageDialog.append($("<p>Select a file below:</p>"));
						
						var field = $('<input type="file" name="file">');
						imageDialog.append(field);
						
						imageDialog.fileupload({
							dataType: 'json',
							url: settings.basePath + settings.imagePath,
							multipart: false,
							singleFileUploads: true,
							done: function(e, data) {
								// object is in data.result
								if (data.result.success) {
									var cid = data.result.cids[0];
									queueAdd(cid);
								} else if (data.result.error) {
									alert("Image upload failed: "
											+ data.result.error);
								} else {
									alert("Unknown upload error, check js " +
											"console!");
								}
								imageDialog.dialog("close");
							}
						});
						
						$this.after(imageDialog);
						
						imageDialog.dialog({
							title: "Upload Image",
							autoOpen: false,
							height: 300,
							width: 400,
							modal: true,
							buttons: {
								Cancel: function() {
									$(this).dialog("close");
								}
							}
						});
						
						imageButton.click(function() {
							imageDialog.dialog("open");
						})
					}
					
					if (settings.videos) {
						var videoButton = $("<button>Video</button>");
						videoButton.button();
						selector.append(videoButton);
						
						// add the video dialog
						var videoDialog = $("<div>");
						videoDialog.append($("<h3>Add a YouTube Video</h3>"));
						
						videoDialog.append($("<p>Paste a link to a YouTube video:</p>"));
						
						var videoUrl = $('<input type="text" size="45">');
						videoDialog.append(videoUrl);
						
						$this.after(videoDialog);
						
						videoDialog.dialog({
							title: "Add Video",
							autoOpen: false,
							height: 300,
							width: 400,
							modal: true,
							buttons: {
								"Add": function() {
									// submit video url to server
									
									var postData = {
										'vid': videoUrl.val()
									}
									
									var url = settings.basePath + settings.videoPath;
									$.post(url, postData, function(data) {
										if (data.success) {
											videoDialog.dialog("close");
											
											var cid = data.cids[0];
											queueAdd(cid);
										} else if (data.error) {
											alert("Couldn't add video: " + data.error);
											videoDialog.dialog("close");
										}
									});
								},
								Cancel: function() {
									$(this).dialog("close");
								}
							},
							close: function() {
								videoUrl.val("");
							}
						});
						
						videoButton.click(function() {
							videoDialog.dialog("open");
						})
					}
					
					dialog.append(selector);
					
					data.selector = selector;
					
					// create the queue
					var queueContainer = $("<div>");
					queueContainer.append($("<h3>Queue: (click to remove)</h3>"));
					
					// var queue defined earlier
					data.queue = queue;
					queueContainer.append(queue);
					data.queueContainer = queueContainer;
					dialog.append(queueContainer);
					
					dialog.append($('<br style="clear: both"/>'));
					
					var textDiv = $("<div>");
					// add the dialog text
					if (!settings.dialogText) {
						// some default dialog text
						textDiv.append($('<h3>Notes</h3>'));
						
						var ul = $("<ul>");
						ul.append($("<li>You may only upload images or add YouTube "
								+ "videos.</li>"));
						ul.append($("<li>Images larger than 1024x768 will be "
								+ "resized.</li>"));
						ul.append($("<li>You must own the rights to any pictures "
								+ "you upload.</li>"));
						ul.append($("<li>All uploaded and linked content will be "
								+ "queued for moderation.</li>"));
						
						textDiv.append(ul);
					} else {
						textDiv.html(settings.dialogText);
					}
					dialog.append(textDiv); // add the text to the dialog
					
					// add the dialog after this element
					$this.after(dialog);
					
					dialog.dialog({
						title: "Uploader",
						autoOpen: false,
						height: 450,
						width: 550,
						modal: true,
						buttons: {
							"Add": function() {
								var postData = {
									'cpid': settings.cpid,
									'type': settings.type
								}
								
								// put all of the cids in an array
								data.cids.forEach(function(cid) {
									postData['cid-' + data.cids.indexOf(cid)] = cid;
								});
								
								var path = settings.basePath + settings.addPath;
								$.post(path, postData, function(data) {
									if (data.error) {
										alert("Could not add content: " + data.error);
									}
									dialog.dialog("close");
								});
							},
							Cancel: function() {
								dialog.dialog("close");
							}
						},
						close: function() {
							// clear the queue
							queue.empty();
							data.cids = [];
						}
					});
					
					$this.button(); // make this a button
					$this.click(function() {
						// open on click
						dialog.dialog("open");
					})
					
					// save for later
					data.dialog = dialog;
				});
			}
		}
		
		if ( methods[method] ) {
			return methods[ method ].apply( this, Array.prototype.slice.call( arguments, 1 ));
		} else if ( typeof method === 'object' || ! method ) {
			return methods.init.apply( this, arguments );
		} else {
			$.error( 'Method ' +  method + ' does not exist on jQuery.uploader' );
			return null;
		}
	}
})(jQuery);

