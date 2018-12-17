
var express = require('express');
router = express.Router();

var connection = require('../mydb/db')();



//idx번째 게시글을 삭제한 후 list화면으로 redirect한다. 
router.delete('/delete', function(req, res, next){
    var idx=req.body.idx;
    console.log(idx);
    connection.beginTransaction(function(err) {
        //json으로 결과값을 보기위해서 이 코드를 이용한다
            var result = returnResult(err, res);
        if(err) console.log(err);
          connection.query('delete from board where idx=?',[idx], function(err){
            if(err){
                console.log(err);
                connection.rollback(function(){
                    console.error('rollback error1');
                });
            } else
            {
              connection.commit(function (err) {
                if(err) console.log(err);
                res.redirect('/board/list');
                //json으로 결과값을 보기위해서 이 코드를 이용한다. --> res.render코드 지워야함. 
                 // console.log("row : " + rows);
                //   result.message = "Delete success";
                //   result.status = res.statusCode;
                //   res.send(result);
              });
            }
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