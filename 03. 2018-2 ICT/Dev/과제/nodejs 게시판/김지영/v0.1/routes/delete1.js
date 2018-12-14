
var express = require('express');
var mysql = require('mysql');
router = express.Router();

var connection = require('../mydb/db')();


  router.post('/delete1', function(req, res, next){
    var body = req.body;
    var idx=req.body.idx;
    var username=req.body.username;

    connection.beginTransaction(function(err) {
        if(err) console.log(err);
          connection.query('delete from board where idx=?',[idx], function(err){
            if(err){
                console.log(err);
                connection.rollback(function(){
                    console.error('rollback error1');
                });
            } else
            {
              // connection.query('select idx,title,content,writer,hit,DATE_FORMAT(moddate, "%Y/%m/%d %T")' +
              //   ' as moddate,DATE_FORMAT(regdate, "%Y/%m/%d %T") as regdate from board where idx=?',[idx],function(err,rows)
                connection.query('select idx,title,content,writer,hit,DATE_FORMAT(moddate, "%Y/%m/%d %T")' +
                ' as moddate,DATE_FORMAT(regdate, "%Y/%m/%d %T") as regdate from board', function(err,rows)
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
                 // console.log("row : " + rows);
                  //res.redirect('/board/list');
                  res.render('list',{title:'Board List', username : username, rows: rows});
                });
              }
            });

              // connection.commit(function (err) {
              //   if(err) console.log(err);
              //  // console.log("row : " + rows);
              //   //res.redirect('/board/list');
              //   res.redirect('list');
              // });
            }
        });
      });

  });

  module.exports = router;