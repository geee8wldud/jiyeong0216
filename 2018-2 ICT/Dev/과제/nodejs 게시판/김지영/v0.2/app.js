var express = require('express');
var path = require('path');
var bodyParser=require('body-parser');
var methodOverride = require('method-override');

var app = express();



// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended : false }))
app.use(methodOverride('_method'));

var list = require('./routes/list');
var read = require('./routes/read');
var write = require('./routes/write');
var modify = require('./routes/modify');
var delete1 = require('./routes/delete');


  
app.use('/board', list);
app.use('/board', read);
app.use('/board', write);
app.use('/board', modify);
app.use('/board', delete1);


app.listen('3000', function(){
    console.log("Server is starting...");
});