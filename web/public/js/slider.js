var Slider = function(boardDimensions) {
  this.SLIDER_WIDTH = 140;
  this.SLIDER_HEIGHT = 12;
  this.SLIDER_ACCEPTABLE_THRESHOLD = 30;
  this.DEFAULT_SPEED = 2;
  this.speed = 0;
  this.leftPosition = 0;
  this.boardDimensions = boardDimensions;

  // Allow user to control slider with keyboard (left, down, right arrows)
  document.onkeydown = function(evt) {
    evt = evt || window.event;
    switch (evt.keyCode) {
        // Left arrow
        case 37:
            slider.speed = -slider.DEFAULT_SPEED;
            break;
        // Right arrow
        case 39:
            slider.speed = slider.DEFAULT_SPEED;
            break;
        case 40:
            slider.speed = 0;
            break;
    }
  };
}

Slider.prototype.getView = function() {
  return document.getElementById('slider');
}

Slider.prototype.moveSlider = function() {

  var MAX_LEFT = this.boardDimensions.width - this.SLIDER_WIDTH - this.boardDimensions.padding;
  var MIN_LEFT = this.boardDimensions.padding;

  var slider = this.getView();
  this.leftPosition += this.speed;

  if (this.leftPosition > MAX_LEFT) this.leftPosition = MAX_LEFT;
  if (this.leftPosition < MIN_LEFT) this.leftPosition = MIN_LEFT;

  slider.style.left = this.leftPosition + 'px';

  setTimeout('slider.moveSlider();', 1);
}