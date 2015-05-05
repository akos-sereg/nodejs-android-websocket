var Ball = function() {
  this.BALL_SIZE = 32;
  this.reset();
  this.board = null;
  this.slider = null;
};

Ball.prototype.getView = function() {
  return document.getElementById('ball');
}

Ball.prototype.moveBall = function() {

  var ball = this.getView();

  this.position[0] += this.direction[0] * this.speed;
  this.position[1] += this.direction[1] * this.speed;

  ball.style.left = this.position[0] + 'px';
  ball.style.top = this.position[1] + 'px';

  this.checkBallBoundaries();

  // Prepare next iteration of moving
  if (!this.stopped) {
    setTimeout('ball.moveBall()', 1);
  }
}

Ball.prototype.checkBallBoundaries = function() {

  if (this.board == undefined) {
    console.log('Ball instance has no reference on Board. Unable to check board boundaries.');
    return;
  }

  if (this.position[0] > this.board.MAX_WIDTH_OF_BOARD - this.BALL_SIZE) { this.direction[0] = -this.direction[0]; this.position[0] = board.MAX_WIDTH_OF_BOARD - this.BALL_SIZE; }
  if (this.position[1] > this.board.MAX_HEIGHT_OF_BOARD - this.slider.SLIDER_HEIGHT) { 
    // Check if slider covers the ball
    if (this.position[0] < this.slider.leftPosition - this.slider.SLIDER_ACCEPTABLE_THRESHOLD) this.board.failGame();
    if (this.position[0] > this.slider.leftPosition + this.slider.SLIDER_WIDTH + this.slider.SLIDER_ACCEPTABLE_THRESHOLD) this.board.failGame();

    this.direction[1] = -this.direction[1]; 
    this.position[1] = this.board.MAX_HEIGHT_OF_BOARD - this.slider.SLIDER_HEIGHT; 
  }

  if (this.position[0] < 0) { this.direction[0] = -this.direction[0]; this.position[0] = 0; }
  if (this.position[1] < 0) { this.direction[1] = -this.direction[1]; this.position[1] = 0; }
}

Ball.prototype.reset = function() {
  this.speed = 1;
  this.direction = [ 0.5, 0.5 ];
  this.position = [ 0, 0 ];
  this.stopped = false;
}