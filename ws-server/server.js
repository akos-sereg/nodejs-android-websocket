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

wsServer.on('request', function(request) {
    var connection = request.accept(null, request.origin);
    var clientIndex = clients.push(connection) - 1;
    var remoteAddress = connection.remoteAddress;
    console.log('Client connected from address ' + remoteAddress);

    // Message received from a client
    connection.on('message', function(message) {
    	console.log('Message Received: ' + JSON.stringify(message));
        console.log('Delivering to clients (' + clients.length + ')');

        // Broadcast the message
    	for (var i=0; i!=clients.length; i++) {
    	    clients[i].send(JSON.stringify(message));
    	}
    });

    // Client disconnects, removing from list
    connection.on('close', function(connection) {
        console.log('Peer ' + remoteAddress + ' disconnected.');
        clients.splice(clientIndex, 1);
    });
});