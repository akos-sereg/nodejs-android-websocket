var Board = function(ball, slider) {
  this.ball = ball;
  this.slider = slider;

  this.MAX_HEIGHT_OF_BOARD = 620;
  this.MAX_WIDTH_OF_BOARD = 1150;

  // Allow user to control slider with keyboard (left, down, right arrows)
  document.onkeydown = function(evt) {
    evt = evt || window.event;
    switch (evt.keyCode) {
        // Left arrow
        case 37:
            slider.speed = -40;
            break;
        // Right arrow
        case 39:
            slider.speed = 40;
            break;
        case 40:
            slider.speed = 0;
            break;
    }
  };
}

Board.prototype.getView = function() {
  return document.getElementById('board');
}

Board.prototype.resetBoard = function() {
  this.ball.reset();
  this.ball.stopped = false;
  this.ball.moveBall();
  this.getView().style.backgroundColor = '#eeeeee';
}

Board.prototype.failGame = function() {
  this.ball.stopped = true;
  this.getView().style.backgroundColor = '#FF8787';
  alert('Ball position: ' + this.ball.position[0] + ', Slider Position: ' + this.slider.leftPosition);
}
