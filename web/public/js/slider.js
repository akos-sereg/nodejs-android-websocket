var Slider = function() {
  this.SLIDER_WIDTH = 100;
  this.SLIDER_ACCEPTABLE_THRESHOLD = 20;
  this.speed = 0;
  this.leftPosition = 20;
}

Slider.prototype.getView = function() {
  return document.getElementById('slider');
}

Slider.prototype.moveSlider = function() {

  var MAX_LEFT = 1200 - 200 - 10;
  var MIN_LEFT = 10;

  var slider = this.getView();
  this.leftPosition += this.speed;

  if (this.leftPosition > MAX_LEFT) this.leftPosition = MAX_LEFT;
  if (this.leftPosition < MIN_LEFT) this.leftPosition = MIN_LEFT;

  slider.style.position = 'absolute';
  slider.style.left = this.leftPosition + 'px';

  setTimeout('slider.moveSlider();', 50);
}