var express = require('express');
var router = express.Router();
var mysql = require('mysql'); //mysql 모듈을 로딩.


//////////////db연결
connection.connect(function(err) {
    if (err) {
        console.error('mysql connection error');
        console.error(err);
        throw err;
    }else{
        console.log("연결에 성공하였습니다.");
    }
});

/* GET List Page. */
/*
router.get('/list',function (req,res,next) {
  res.redirect('/board/1')// /board로 접속요청이 들어왔을 때 1페이지로 자동으로 이동하도록 리다이렉트 해줍니다.
})
router.get('/list/:page', function(req, res, next) {

  var query = connection.query('select idx,title,writer,hit,DATE_FORMAT(moddate, "%Y/%m/%d %T") as moddate from board',function(err,rows){
    if(err) console.log(err)        // 만약 에러값이 존재한다면 로그에 표시합니다.
    console.log('rows :' +  rows);
    res.render('list', { title:'Board List',rows: rows }); // view 디렉토리에 있는 list 파일로 이동합니다.
  });
});
*/

router.get('/list', function(req, res, next) {

    var query = connection.query('select idx,title,writer,hit,DATE_FORMAT(moddate, "%Y/%m/%d %T") as moddate from board',function(err,rows){
      if(err) console.log(err)        // 만약 에러값이 존재한다면 로그에 표시합니다.
      console.log('rows :' +  rows);
      res.render('list', { title:'Board List',rows: rows }); // view 디렉토리에 있는 list 파일로 이동합니다.
    });
  });
  
//module.exports = router;