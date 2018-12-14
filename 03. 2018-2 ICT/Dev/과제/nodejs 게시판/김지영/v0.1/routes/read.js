
var express = require('express');
var mysql = require('mysql');
router = express.Router();
var connection = require('../mydb/db')();

  router.get('/read/:idx',function (req,res,next) {
    var idx = req.params.idx;
    console.log("idx : "+idx);
  
        connection.beginTransaction(function(err){
          if(err) console.log(err);
          connection.query('update board set hit=hit+1 where idx=?', [idx], function (err) {
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
                })
              }
              else {
                connection.commit(function (err) {
                  if(err) console.log(err);
                  console.log("row : " + rows);
                  var username=rows[0].writer;
                  res.render('read',{title:rows[0].title ,username : username, rows : rows});
                });
              }
            });
        });
    });
  });


  router.post('/read/:idx',function (req,res,next) {
    var idx = req.params.idx;
    var username=req.body.username;
    console.log("idx : "+idx);
  
        connection.beginTransaction(function(err){
          if(err) console.log(err);
          connection.query('update board set hit=hit+1 where idx=?', [idx], function (err) {
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
                })
              }
              else {
                connection.commit(function (err) {
                  if(err) console.log(err);

                  //////////////
                  else{
                    console.log("row : " + rows);
                    if(username==rows[0].writer){
                      res.render('read',{title:rows[0].title ,username : username, rows : rows});
                    }else{
                      res.render('read2',{title:rows[0].title ,username : username, rows : rows});
                    }
                    
                  }
                  
                });
              }
            });
        });
    });
  });


  module.exports = router;