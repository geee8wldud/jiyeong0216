var express = require('express');
router = express.Router();
var connection = require('../mydb/db')();

  router.get('/write',function (req,res,next) {
    res.render('write', {title:'글 쓰기 페이지'});
  });


//사용자가 입력한 정보들을 이용해 새로운 게시글을 생성한다. 
router.post('/write',function (req,res,next) {
   
    //form 에서 정보를 받아오는것. 
    var writer = req.body.writer;
    var title = req.body.title;
    var content = req.body.content;
    var password = req.body.password;


      
    console.log("Hi hello"+title);
  
    connection.beginTransaction(function(err) {
       //json으로 결과값을 보기위해서 이 코드를 이용한다
      var result = returnResult(err, res);
      if(err) console.log(err);
      connection.query('insert into board(title,writer,content,password) values(?,?,?,?)'
          ,[title,writer,content,password]
          ,function (err) {
            if(err) {
              console.log(err);
              connection.rollback(function () {
                console.error('rollback error1');
              })
            }
            connection.query('SELECT LAST_INSERT_ID() as idx',function (err,rows) {
              if(err) {
                console.log(err);
                connection.rollback(function () {
                  console.error('rollback error1');
                })
              }
              else
              {
                connection.commit(function (err) {
                  if(err) console.log(err);
                  console.log("row : " + rows);
                  var idx = rows[0].idx;
                  res.redirect('/board/read/'+idx);
                   //json으로 결과값을 보기위해서 이 코드를 이용한다. --> res.render코드 지워야함. 
                  // result.message = rows;
                  // result.status = res.statusCode;
                  // res.send(result);
                });
              }
            });
      });
    });
  });
  
  

    //json으로 결과값을 보기위한 함수
    var returnResult = function(err, res) {
      // 결과를 눈으로 보기 쉽게하기 위해 result 객체 생성
      var result = {};
      if (err) {
          res.status(400);
          result.message = err.stack;
      } else {
          res.status(200);
          result.message = "Success";
      }
      return result;
  }

  module.exports = router;