const express = require('express');
const path = require('path');
const bodyParser=require('body-parser');
const methodOverride = require('method-override');

const app = express();



// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended : false }))
app.use(methodOverride('_method'));

let list = require('./routes/list');
let read = require('./routes/read');
let write = require('./routes/write');
let modify = require('./routes/modify');
let delete1 = require('./routes/delete');


  
app.use('/board', list);
app.use('/board', read);
app.use('/board', write);
app.use('/board', modify);
app.use('/board', delete1);


app.listen('3000', function(){
    console.log("Server is starting...");
});