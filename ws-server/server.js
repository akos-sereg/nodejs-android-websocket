var WebSocketServer = require('websocket').server;
var http = require('http');
var clients = [];

var server = http.createServer(function(request, response) {
    // process http request
});

server.listen(3000, function() { });

var wsServer = new WebSocketServer({
    httpServer: server
});

var index = 0;
wsServer.on('request', function(request) {
    var connection = request.accept(null, request.origin);
    
    if (index > 0) {
	clients.push(connection);
    }

    index++;
    connection.on('message', function(message) {
	console.log('message recv.: ' + JSON.stringify(message));
	for (var i=0; i!=clients.length; i++) {
	    console.log('deliver...');
	    clients[i].send(JSON.stringify(message));
	}
    });
});