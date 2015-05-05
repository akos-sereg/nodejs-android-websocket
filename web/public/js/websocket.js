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
    var gyroscope = [ parseFloat(data.axis_x.replace(',', '.')), parseFloat(data.axis_y.replace(',', '.')), parseFloat(data.axis_z.replace(',', '.')) ];
    var orientation = [ parseFloat(data.orientation_x.replace(',', '.')), parseFloat(data.orientation_y.replace(',', '.')), parseFloat(data.orientation_z.replace(',', '.')) ];

    slider.speed = wsClient.sensorHandler.getMotionSignum(gyroscope[2], orientation[1]);
    series.append(new Date().getTime(), wsClient.sensorHandler.getChartData(gyroscope[2], orientation[1]));
  };

  this.ws.onclose = function()
  {
    document.getElementById('connectionStatus').className = 'disconnected';
    document.getElementById('connectionStatus').innerHTML = 'Disconnected';
  };
};


PingSquashWebsocketClient.prototype.changeDataProvider = function(dataProviderType) {
    switch(dataProviderType) {
      case 1:
        this.sensorHandler = new GyroscopeSensorHandler();
        break;
      case 2:
        this.sensorHandler = new OrientationSensorHandler();
        break;
    }
  }

// Sensor Handlers
// -------------------------------------------------------------------------------------

// Orientation
var OrientationSensorHandler = function() { }

OrientationSensorHandler.prototype.getChartData = function(gyroscopeZ, orientationY) {
  return orientationY;
}

OrientationSensorHandler.prototype.getMotionSignum = function(gyroscopeZ, orientationY) {
  if (orientationY < -0.2) return -slider.DEFAULT_SPEED;
  else if (orientationY > 0.2) return slider.DEFAULT_SPEED;
  else return 0;
}

// Gyroscope
var GyroscopeSensorHandler = function() {}

GyroscopeSensorHandler.prototype.getChartData = function(gyroscopeZ, orientationY) {
  return gyroscopeZ;
}

GyroscopeSensorHandler.prototype.getMotionSignum = function(gyroscopeZ, orientationY) {
  if (gyroscopeZ < -0.3) return slider.DEFAULT_SPEED;
  else if (gyroscopeZ > 0.3) return -slider.DEFAULT_SPEED;
  else return 0;
}