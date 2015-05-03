var PingSquashWebsocketClient = function(url, slider, series) {
  this.url = url;
  this.slider = slider;
  this.series = series;
}

PingSquashWebsocketClient.prototype.connect = function() {

  var ws = new WebSocket(this.url);

  ws.onopen = function()
  {
  };

  ws.onmessage = function (evt)
  {
    var data = JSON.parse(JSON.parse(evt.data).utf8Data);

    var axisX = parseFloat(data.axis_x.replace(',', '.'));
    var axisY = parseFloat(data.axis_y.replace(',', '.'));
    var axisZ = parseFloat(data.axis_z.replace(',', '.'));

    if (axisZ < -0.2) slider.speed = 40;
    else if (axisZ > 0.2) slider.speed = -40;
    else slider.speed = 0;

    series.append(new Date().getTime(), axisZ);
  };

  ws.onclose = function()
  {
  };
};