

var express = require('express');
var mysql = require('mysql');
router = express.Router();

var connection = require('../mydb/db')();

//세션 사용 준비 
var session = require('express-session');
var MySQLStore = require('express-mysql-session')(session);
router.use(session({
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



router.get('/welcome', function(req, res, next){
    //if(req.session.username){var username=req.session.username;
    if(req.user&&req.user.username){
        var username = req.user.username;
       // var password = req.user.password;
        console.log('username :' +  username);
        res.render('users/welcome', {username : username});
    }else {
        res.send(`
        <h1>Welcome</h1>
        <a href="/users/login">Login</a>`);
    }
});


module.exports = router;