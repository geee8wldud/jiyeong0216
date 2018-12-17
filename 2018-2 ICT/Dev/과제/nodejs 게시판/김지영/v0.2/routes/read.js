
var express = require('express');
router = express.Router();
var connection = require('../mydb/db')();


//조회수 업데이트 후 조회
  router.get('/read/:idx',function (req,res,next) {

    var idx = req.params.idx;
    console.log("idx : "+idx);
        connection.beginTransaction(function(err){
          //json으로 결과값을 보기위해서 이 코드를 이용한다
            var result = returnResult(err, res);
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
                  res.render('read',{title:rows[0].title , rows : rows});
                  //json으로 결과값을 보기위해서 이 코드를 이용한다. --> res.render코드 지워야함. 
                //  result.message = rows;
                //  result.status = res.statusCode;
                //   res.send(result);
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