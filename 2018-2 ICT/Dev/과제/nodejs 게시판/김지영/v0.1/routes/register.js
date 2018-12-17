
var express = require('express');
var mysql = require('mysql');
router = express.Router();
var passport = require('passport')
, LocalStrategy = require('passport-local').Strategy;

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

var bkfd2Password = require("pbkdf2-password");
var hasher = bkfd2Password();


router.use(passport.initialize());
router.use(passport.session());

router.get('/register', function(req, res, next){
    res.render('users/register');
});

router.get('/register2', function(req, res, next){
    res.render('users/register2');
})

router.post('/register', function(req, res){
    hasher({ password:req.body.password}, function(err, pass, salt, hash){
        //사용자에게 받아온 정보를 이용해서 user 객체 생성. 
        var user = {
            authId : 'local:'+req.body.username,
            username : req.body.username,
            password : hash,
            salt : salt,
            email : req.body.userEmail
        };
        var sql1='SELECT username FROM users';
        connection.query(sql1, function(err, results){
            if(err){
                console.log(err);
                res.status(500);
            }else{
                console.log(results);
                
                ///////
                for(i=0;i<results.length;i++){
                    var uname=results[i].username;
                    console.log(results[i].username);
                    if(uname==user.username){
                        res.redirect('/users/register2');
                    }else{
                        var sql='INSERT INTO users SET ?';
                        connection.query(sql, user, function(err, results){
                            if(err){
                                console.log(err);
                                 res.status(500);
                            }else {
                                 var username=user.username;
                                 req.login(user, function(err){
                                 req.session.save(function(){
                                //res.redirect('/users/welcome', {username : username});//?오류?
                                 res.render('users/welcome', {username : username});
                        });
                }); 
            }
        });
                    }
                }
            }
        });
        
    });
});


module.exports = router;