function post(url, data, callback, errorCallback) {
	var settings = {
		contentType: "application/json",
		type: "POST",
		data: JSON.stringify(data)
	};
	$.ajax(url, settings).done(function(data) {
		if (data.message != "OK") {
			if (errorCallback != null) {
				errorCallback(data.code + ": " + data.message);
			} else {
				console.error(data.code, data.message, data);
			}
		} else {
			callback(data);
		}
	  })
	  .fail(function() {
	    console.error("login");
	  });
}

function post2(url, data, callback, errorCallback) {
	var settings = {
		contentType: "application/json",
		type: "POST",
		data: JSON.stringify(data)
	};
	$.ajax(url, settings).done(function(data) {
		callback(data);
	  })
	  .fail(function() {
	    console.error("login");
	  });
}