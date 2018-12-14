
var express = require('express');
var router = express.Router();
var mysql = require('mysql');

///////
var path = require('path');

 
/*
var index = require('./routes/index');
var users = require('./routes/users');
var board = require('./routes/board');    //board 파일 등록
*/

/////////////
var connection = mysql.createConnection({
    host    :'localhost', //db ip address
    user : 'root', //db id
    password : '111111', //db password
    database:'board01' //db schema name
});


var app = express();

//데이터베이스 연결
connection.connect(function(err) {
    if (err) {
        console.error('mysql connection error');
        console.error(err);
        throw err;
    }else{
        console.log("연결에 성공하였습니다.");
    }
});


app.locals.pretty = true;
//템플릿엔진 ejs로 설정.
app.set('view engine', 'ejs');
app.set('views', path.join(__dirname, 'views'));

/*////////////
app.use('/', index);
app.use('/users', users);
app.use('/board',board);  
//////////////*/

//post 방식으로 데이터 받기위해 body-parser 모듈 추가
var bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({ extended: false }));


/////추가
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

//app.get('/list',function (req,res,next) {
  //  res.redirect('list')// /board로 접속요청이 들어왔을 때 1페이지로 자동으로 이동하도록 리다이렉트 해줍니다.
  //})

  app.get('/list', function(req, res, next) {
  
    var query = connection.query('select idx,title,writer,hit,DATE_FORMAT(moddate, "%Y/%m/%d %T") as moddate from board',function(err,rows){
      if(err) console.log(err)        // 만약 에러값이 존재한다면 로그에 표시합니다.
      console.log('rows :' +  rows);
      res.render('list', { title:'Board List',rows: rows }); // view 디렉토리에 있는 list 파일로 이동합니다.
    });
  });
  /*
  app.get('/list/:page', function(req, res, next) {
  
    var query = connection.query('select idx,title,writer,hit,DATE_FORMAT(moddate, "%Y/%m/%d %T") as moddate from board',function(err,rows){
      if(err) console.log(err)        // 만약 에러값이 존재한다면 로그에 표시합니다.
      console.log('rows :' +  rows);
      res.render('list', { title:'Board List',rows: rows }); // view 디렉토리에 있는 list 파일로 이동합니다.
    });
  });

*/
  /////////////////////////////////read
app.get('/read1/:idx',function (req,res,next) {
    /* GET 방식의 연결이므로 read 페이지 조회에 필요한 idx 값이 url 주소에 포함되어 전송됩니다.
     이 idx값을 참조하여 DB에서 해당하는 정보를 가지고 옵니다.
    * url에서 idx 값을 가져오기 위해 request 객체의 params 객체를 통해 idx값을 가지고 옵니다.*/
    var idx = req.params.idx;
    console.log("idx : "+idx);
    /*
    * Node는 JSP에서 JDBC의 sql문 PreparedStatement 처리에서와 같이 sql문을 작성할 때
    * ? 를 활용한 편리한 쿼리문 작성을 지원합니다.
    * Node에서 참조해야할 인자값이 있을 때 ? 로 처리하고
    * []를 통해 리스트 객체를 만든 후 ? 의 순서대로 입력해주시면 자동으로 쿼리문에 삽입됩니다.
    * 아래에는 ?에 idx값이 자동으로 매핑되어 쿼리문을 실행합니다.
    * */
    /**/
        connection.beginTransaction(function(err){
          if(err) console.log(err);
          connection.query('update board set hit=hit+1 where idx=?', [idx], function (err) {
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
                })
              }
              else {
                connection.commit(function (err) {
                  if(err) console.log(err);
                  console.log("row : " + rows);
                  res.render('read1',{title:rows[0].title , rows : rows});
                })
              }
            })
        })
    })
  })

  //글쓰기
app.get('/write1',function (req,res,next) {
    res.render('write1',{title:'글 쓰기 페이지'})
  })
  
  // POST 방식의 요청이 들어왔을 때 데이터를 DB에 저장하고 해당하는 DB의 IDX값을
// 가지고 온 후 Read 페이지로 이동합니다.
app.post('/write1',function (req,res,next) {
    /*
    *POST 방식의 요청을 URL에 데이터가 포함되지 않고 BODY에 포함되어 전송됩니다.
    * 때문에 request 객체를 통해 body에 접근 후 데이터를 가지고 옵니다.
     *  */
    var body = req.body;
    var writer = body.writer;
    var title = req.body.title;
    var content = req.body.content;
    var password = req.body.password;
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
                  res.redirect('/read1/'+idx);
                })
              }
            })
      })
    })
  })



app.listen(3000, function(){
    console.log('Conneted 3000 port!');
});

/*

app.use(express.static('public'));


// 사용자가 /로 들어왔을 때
app.get('/', function(req, res){
    res.render('index', {time:Date()});
});


  app.get('/form_receiver', function(req, res){
    var title = req.query.title;
    var writer = req.query.writer;
    var content = req.query.content;
    var password = req.query.password;
    res.send(title+','+writer+', '+content);
});


app.post('/', function(req, res){
    var title = req.body.title;
    var writer = req.body.writer;
    var content = req.body.content;
    res.render('index',{time:Date(), title:title, writer:writer, content:content});
});

  app.get('/read', function(req, res){
    res.render('read',{title:'글읽기', time:Date()});
  })



/*

app.get('/login', function(req, res){
    res.send('<h1>Login please</h1>');
});*/





