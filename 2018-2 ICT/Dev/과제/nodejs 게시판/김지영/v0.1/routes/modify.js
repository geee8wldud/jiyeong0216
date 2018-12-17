

var express = require('express');
var mysql = require('mysql');
router = express.Router();
var connection = require('../mydb/db')();

/*
var connection = mysql.createConnection({
    host: 'localhost', // DB가 위치한 IP주소
    port: 3306,          // DB와 연결할 포트번호
    user: 'root',        // 계정이름
    password: '111111',    // 계정 비밀번호
    database: 'board01'    // 데이터베이스 이름
  });
*/

  router.get('/modify/:idx',function (req,res,next) {
    var idx = req.params.idx;
    console.log("idx : "+idx);
    
        connection.beginTransaction(function(err){
         // if(err) console.log(err);
         // connection.query('update board set hit=hit+1 where idx=?', [idx], function (err) {
            if(err) {
              /* 이 쿼리문에서 에러가 발생했을때는 쿼리문의 수행을 취소하고 롤백합니다.*/
              console.log(err);
              connection.rollback(function () {
                console.error('rollback error1');
              })
            }
            connection.query('select idx,title,content,writer,hit,DATE_FORMAT(moddate, "%Y/%m/%d %T")' +
                ' as moddate,DATE_FORMAT(regdate, "%Y/%m/%d %T") as regdate from board where idx=?',[idx],function(err,rows)
            {
              if(err) {
                /* 이 쿼리문에서 에러가 발생했을때는 쿼리문의 수행을 취소하고 롤백합니다.*/
                console.log(err);
                connection.rollback(function () {
                  console.error('rollback error2');
                });
              }
              else {
                connection.commit(function (err) {
                  if(err) console.log(err);
                  console.log("row : " + rows);
                  res.render('modify',{title:rows[0].title , rows : rows});
                });
              }
            });
       // });
    });
  });

  router.post('/modify/:idx', function(req, res, next){
    var username = req.body.username;
    var idx = req.params.idx;
    console.log("idx : "+idx);
    
        connection.beginTransaction(function(err){
         // if(err) console.log(err);
         // connection.query('update board set hit=hit+1 where idx=?', [idx], function (err) {
            if(err) {
              console.log(err);
              connection.rollback(function () {
                console.error('rollback error1');
              })
            }
            connection.query('select idx,title,content,writer,hit,DATE_FORMAT(moddate, "%Y/%m/%d %T")' +
                ' as moddate,DATE_FORMAT(regdate, "%Y/%m/%d %T") as regdate from board where idx=?',[idx],function(err,rows)
            {
              if(err) {
                console.log(err);
                connection.rollback(function () {
                  console.error('rollback error2');
                });
              }
              else {
                connection.commit(function (err) {
                  if(err) console.log(err);
                  console.log("row : " + rows);
                  res.render('modify',{title:rows[0].title , username : username, rows : rows});
                });
              }
            });
       // });
    });
  });

//  // POST 방식의 요청이 들어왔을 때 데이터를 DB에 저장하고 해당하는 DB의 IDX값을
// // 가지고 온 후 Read 페이지로 이동합니다.
// router.post('/modify', function (req,res,next) {
//     //form 에서 정보를 받아오는것. 
//     //var body = req.body;
//     var idx = req.body.idx;
//     var title = req.body.title;
//     var content = req.body.content;


//     connection.beginTransaction(function(err){
//         if(err) console.log(err);
//         var sql='UPDATE board SET title=?, content=?, moddate=DATE_FORMAT(moddate, "%Y/%m/%d %T") where idx=?';
//         connection.query(sql, [title, content, idx], function(err, result, field){
//             if(err){
//                 console.log(err);
//                 connection.rollback(function(){
//                 console.error('rollback error1');
//                 });
//             }
            
//             else {
//                 connection.query('select idx,title,content,writer,hit,DATE_FORMAT(moddate, "%Y/%m/%d %T")' +
//                 ' as moddate,DATE_FORMAT(regdate, "%Y/%m/%d %T") as regdate from board where idx=?',[idx],function(err,rows)
//             {
//               if(err) {
//                 /* 이 쿼리문에서 에러가 발생했을때는 쿼리문의 수행을 취소하고 롤백합니다.*/
//                 console.log(err);
//                 connection.rollback(function () {
//                   console.error('rollback error2');
//                 })
//               }
//               else {
//                 connection.commit(function (err) {
//                   if(err) console.log(err);
//                   console.log("row : " + rows);
//                   res.render('read',{title:rows[0].title , rows : rows});
//                 });
//               }
//             });
//             }
//         });
//     });
// });
    

    
  module.exports = router;