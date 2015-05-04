var PingSquashWebsocketClient = function(url, slider, series) {

  this.url = url;
  this.slider = slider;
  this.series = series;
  this.ws = null;
  this.messageCount = 0;

  this.sensorHandler = new OrientationSensorHandler();
}

PingSquashWebsocketClient.prototype.connect = function() {

  this.ws = new WebSocket(this.url);

  this.ws.onopen = function()
  {
    document.getElementById('connectionStatus').className = 'connected';
    document.getElementById('connectionStatus').innerHTML = 'Connected';
  };

  this.ws.onmessage = function (evt)
  {
    wsClient.messageCount++;
    document.getElementById('connectionStatus').className = 'connected';
    document.getElementById('connectionStatus').innerHTML = wsClient.messageCount + ' messages received';

    var data = JSON.parse(JSON.parse(evt.data).utf8Data);

    // Extract acceleration data
    var axisX = parseFloat(data.axis_x.replace(',', '.'));
    var axisY = parseFloat(data.axis_y.replace(',', '.'));
    var axisZ = parseFloat(data.axis_z.replace(',', '.'));
    
    // Extract orientation data
    var orientationX = parseFloat(data.orientation_x.replace(',', '.'));
    var orientationY = parseFloat(data.orientation_y.replace(',', '.'));
    var orientationZ = parseFloat(data.orientation_z.replace(',', '.'));

    slider.speed = wsClient.sensorHandler.getMotionSignum(axisZ, orientationY);
    series.append(new Date().getTime(), wsClient.sensorHandler.getChartData(axisZ, orientationY));
  };

  this.ws.onclose = function()
  {
    document.getElementById('connectionStatus').className = 'disconnected';
    document.getElementById('connectionStatus').innerHTML = 'Disconnected';
  };
};

// Data providers
var OrientationSensorHandler = function() {
}

OrientationSensorHandler.prototype.getChartData = function(accelerationZ, orientationY) {
  return orientationY;
}

OrientationSensorHandler.prototype.getMotionSignum = function(accelerationZ, orientationY) {
  if (orientationY < -0.2) return -40;
  else if (orientationY > 0.2) return 40;
  else return 0;
}

var AccelerometerSensorHandler = function() {
}

AccelerometerSensorHandler.prototype.getChartData = function(accelerationZ, orientationY) {
  return accelerationZ;
}

AccelerometerSensorHandler.prototype.getMotionSignum = function(accelerationZ, orientationY) {
  if (accelerationZ < -0.3) return 40;
  else if (accelerationZ > 0.3) return -40;
  else return 0;
}