

var express = require('express');
var mysql = require('mysql');
router = express.Router();
var connection = require('../mydb/db')();

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

router.get('/list', function(req, res, next){
    var username=req.body.username;
    req.session.username=username;
    var query = connection.query('select idx,title,writer,hit,DATE_FORMAT(moddate, "%Y/%m/%d %T") as moddate from board',function(err,rows){
        if(err) {console.log(err)}        // 만약 에러값이 존재한다면 로그에 표시합니다.
        else {
            console.log('rows :' +  rows);
            res.render('list', { username: username, title:'Board List',rows: rows }); // view 디렉토리에 있는 list 파일로 이동합니다.
        }
      });
});

router.post('/list', function(req, res, next){
    //var body = req.body;
    var username = req.body.username;
    var query = connection.query('select idx,title,writer,hit,DATE_FORMAT(moddate, "%Y/%m/%d %T") as moddate from board',function(err,rows){
        if(err) {console.log(err)}        // 만약 에러값이 존재한다면 로그에 표시합니다.
        else {
            //console.log('rows :' +  rows);
            //console.log('Hi Hello '+username);
            res.render('list', { title:'Board List', username: username, rows: rows }); // view 디렉토리에 있는 list 파일로 이동합니다.
        }
      });
});


module.exports = router;