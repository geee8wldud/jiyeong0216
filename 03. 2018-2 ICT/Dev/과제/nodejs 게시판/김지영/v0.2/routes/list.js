

var express = require('express');
router = express.Router();

var connection = require('../mydb/db')();

//게시판 조회해서 표로 나타내기 
router.get('/list', function(req, res, next){
    var query = connection.query('select idx,title,writer,hit,DATE_FORMAT(moddate, "%Y/%m/%d %T") as moddate from board'
    ,function(err,rows){
         //json으로 결과값을 보기위해서 이 코드를 이용한다
        var result = returnResult(err, res);
        if(err) console.log(err)        // 만약 에러값이 존재한다면 로그에 표시합니다.
        console.log('rows :' +  rows);
      res.render('list', { title:'Board List',rows: rows }); // view 디렉토리에 있는 list 파일로 이동합니다.
         //json으로 결과값을 보기위해서 이 코드를 이용한다. --> res.render코드 지워야함. 
            // result.message = rows;
            // result.status = res.statusCode;
            // res.send(result);
      });
});


//json으로 결과를 보기위한 함수처리 
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