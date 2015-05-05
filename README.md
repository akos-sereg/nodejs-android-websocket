# nodejs-android-websocket

Project template for WebSocket based applications - streaming messages from Android client to NodeJS WebSocket server, 
displaying live data on web.

This lightweight project consists of 
 - an *android-client* that sends mobile phone's gyroscope data to web socket server
 - a *ws-server* (NodeJS based WebSocket server) that receives the gyroscope data
 - a *web* (NodeJS based web server) project that shows you the gyroscope's status - live update from *ws-server*
 
 
# Install and Configure

1. Download project
2. Android Client: open MainActivity.java, update WebSocket server's URI to use your IP address, then create APK and install on your phone.
3. Enter ws-server folder, run "npm install" command, then run web socket server with "nodejs server.js".
4. Enter web folder, run "npm install" command, then run web server with "nodejs server.js".

Open http://<your hostname>:3005/index.html in browser
Launch the android application, and start shaking your phone. You should see the live-updating chart changing on the website.

# Screenshot

![Screenshot](https://raw.githubusercontent.com/akos-sereg/nodejs-android-websocket/master/web/screenshots/screenshot.png "Screenshot")

# Branches you might be interested in

**Ping Squash** - *experimental project*: basic ping squash game, you can control the game remotely by rotating or sliding your phone.
https://github.com/akos-sereg/nodejs-android-websocket/tree/ping-squash
