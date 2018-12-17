

var express = require('express');
var mysql = require('mysql');
router = express.Router();
var connection = require('../mydb/db')();


// POST 방식의 요청이 들어왔을 때 데이터를 DB에 저장하고 해당하는 DB의 IDX값을
// 가지고 온 후 Read 페이지로 이동합니다.
router.post('/modify2', function (req,res,next) {
    //form 에서 정보를 받아오는것. 
    //var body = req.body;
    var idx = req.body.idx;
    var title = req.body.title;
    var content = req.body.content;
    var username=req.body.username;


    connection.beginTransaction(function(err){
        if(err) console.log(err);
        var sql='UPDATE board SET title=?, content=?, moddate=DATE_FORMAT(moddate, "%Y/%m/%d %T") where idx=?';
        connection.query(sql, [title, content, idx], function(err, result, field){
            if(err){
                console.log(err);
                connection.rollback(function(){
                console.error('rollback error1');
                });
            }
            
            else {
                connection.query('select idx,title,content,writer,hit,DATE_FORMAT(moddate, "%Y/%m/%d %T")' +
                ' as moddate,DATE_FORMAT(regdate, "%Y/%m/%d %T") as regdate from board where idx=?',[idx],function(err,rows)
            {
              if(err) {
                /* 이 쿼리문에서 에러가 발생했을때는 쿼리문의 수행을 취소하고 롤백합니다.*/
                console.log(err);
                connection.rollback(function () {
                  console.error('rollback error2');
                })
              }
              else {
                connection.commit(function (err) {
                  if(err) console.log(err);
                  console.log("row : " + rows);
                  res.render('read',{title:rows[0].title ,username:username,  rows : rows});
                });
              }
            });
            }
        });
    });
});
    

    
  module.exports = router;