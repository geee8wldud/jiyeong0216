

var express = require('express');

router = express.Router();
var connection = require('../mydb/db')();


router.get('/write2',function (req,res,next) {
    res.render('write', {title:'글 쓰기 페이지'});
  });


  // POST 방식의 요청이 들어왔을 때 데이터를 DB에 저장하고 해당하는 DB의 IDX값을
// 가지고 온 후 Read 페이지로 이동
router.post('/write2',function (req,res,next) {
    //form 에서 정보를 받아오는것. 
    var body = req.body;
    //var writer = req.body.writer;
    var writer=req.body.username;
    var title = req.body.title;
    var content = req.body.content;
    var password = req.body.password;
    console.log('HiHello '+title);

    connection.beginTransaction(function(err) {
      if(err) console.log(err);
      connection.query('insert into board(title,writer,content,password) values(?,?,?,?)'
          ,[title,writer,content,password]
          ,function (err) {
            if(err) {
              /* 이 쿼리문에서 에러가 발생했을때는 쿼리문의 수행을 취소하고 롤백합니다.*/
              console.log(err);
              connection.rollback(function () {
                console.error('rollback error1');
              })
            }
            connection.query('SELECT LAST_INSERT_ID() as idx',function (err,rows) {
              if(err) {
                /* 이 쿼리문에서 에러가 발생했을때는 쿼리문의 수행을 취소하고 롤백합니다.*/
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
                  //res.render('/board/read/'+idx);
                });
              }
            });
      });
    });
    
  });
  
  module.exports=router;