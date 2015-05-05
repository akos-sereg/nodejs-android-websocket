var Board = function(ball, slider) {
  this.ball = ball;
  this.slider = slider;

  this.MAX_HEIGHT_OF_BOARD = 510;
  this.MAX_WIDTH_OF_BOARD = 1190;
}

Board.prototype.getView = function() {
  return document.getElementById('board');
}

Board.prototype.resetBoard = function() {
  this.ball.reset();
  this.ball.stopped = false;
  this.ball.moveBall();
  this.getView().style.backgroundColor = '#F4F3E5';
}

Board.prototype.failGame = function() {
  this.ball.stopped = true;
  this.ball.setImage('fail');
}
