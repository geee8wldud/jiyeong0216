

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


router.get('/logout', function(req, res, next){
    //세션삭제 
   // delete req.session.username;
   //passsport 사용해서 삭제
    req.logout();
    req.session.save(function(){
        res.render('users/logout');
    });
});



module.exports = router;