function post(url, data, callback, errorCallback) {
	var settings = {
		contentType: "application/json",
		type: "POST",
		data: JSON.stringify(data)
	};
	$.ajax(url, settings).done(function(data) {
		console.log(data);
		if (data.message != "OK") {
			errorCallback(data.code + ": " + data.message);
		} else {
			callback(data);
		}
	  })
	  .fail(function() {
	    console.error("login");
	  });
}
