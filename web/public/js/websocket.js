var PingSquashWebsocketClient = function(url, slider, series) {
  this.url = url;
  this.slider = slider;
  this.series = series;
  this.ws = null;
  this.messageCount = 0;
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
    this.messageCount++;
    document.getElementById('connectionStatus').className = 'connected';
    document.getElementById('connectionStatus').innerHTML = this.messageCount + ' messages received';

    var data = JSON.parse(JSON.parse(evt.data).utf8Data);

    var axisX = parseFloat(data.axis_x.replace(',', '.'));
    var axisY = parseFloat(data.axis_y.replace(',', '.'));
    var axisZ = parseFloat(data.axis_z.replace(',', '.'));
    
    var orientationX = parseFloat(data.orientation_x.replace(',', '.'));
    var orientationY = parseFloat(data.orientation_y.replace(',', '.'));
    var orientationZ = parseFloat(data.orientation_z.replace(',', '.'));

    /*if (axisZ < -0.3) slider.speed = 40;
    else if (axisZ > 0.3) slider.speed = -40;
    else slider.speed = 0;*/

    if (orientationY < -0.2) slider.speed = -40;
    else if (orientationY > 0.2) slider.speed = 40;
    else slider.speed = 0;

    series.append(new Date().getTime(), orientationY);
  };

  this.ws.onclose = function()
  {
    document.getElementById('connectionStatus').className = 'disconnected';
    document.getElementById('connectionStatus').innerHTML = 'Disconnected';
  };
};