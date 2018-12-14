
var express = require('express');
var path = require('path');
var bodyParser=require('body-parser');
// var passport = require('passport')
// , LocalStrategy = require('passport-local').Strategy;


var app = express();

var session = require('express-session');
var MySQLStore = require('express-mysql-session')(session);
//세션 사용 준비 
app.use(session({
    secret : '1234DSFs@adf1234!@#$asd',
    resave : false,
    saveUninitialized : true, 
    store:new MySQLStore({
        host:'localhost',
        port:3306,
        user:'root',
        password:'111111',
        database:'board01'
      })
    
}));





app.use(bodyParser.urlencoded({ extended : false }))
// app.use(passport.initialize());
// app.use(passport.session());

app.get('/count' , function(req, res){
    if(req.session.count){
        req.session.count++;
    }else{
        req.session.count=1;
    }
    res.send('hi session'+req.session.count);
})

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

app.get('/', function(req, res){
   res.render('users/index');
});

var list = require('./routes/list');
var read = require('./routes/read');
var write = require('./routes/write');
var modify = require('./routes/modify');
var delete1 = require('./routes/delete1');
var write2=require('./routes/write2');
var modify2=require('./routes/modify2');

var welcome1 = require('./routes/welcome');
var register1 = require('./routes/register');
var login = require('./routes/login');
var logout= require('./routes/logout');



app.use('/board', list);
app.use('/board', read);
app.use('/board', write);
app.use('/board', modify);
app.use('/board', delete1);
app.use('/board', write2);
app.use('/board', modify2);

app.use('/users', register1);
app.use('/users', login);
app.use('/users', welcome1);
app.use('/users', logout);



app.listen('3000', function(){
    console.log("Server is starting...");
});