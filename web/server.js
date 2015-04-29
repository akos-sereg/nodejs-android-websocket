var express     = require('express');
var path        = require('path');
var app         = express();
var bodyParser  = require('body-parser');

app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
app.use(express.static(path.join(__dirname, 'public')));

var port = process.env.PORT || 3005;

app.listen(port);
console.log('Web server listening on port ' + port);