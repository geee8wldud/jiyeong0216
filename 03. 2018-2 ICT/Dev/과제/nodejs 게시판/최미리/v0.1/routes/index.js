var express = require('express');
var router = express.Router();

var Board = require('../models/board');
var Comment = require('../models/comment');

// 홈 화면으로
router.get('/', function(req, res, next) {
  Board.find({}, function (err, board) {
      res.render('index', { title: '게시판', board: board });
  });
});

// 글쓰기 페이지로 이동
router.get('/write', function(req, res, next) {
    res.render('write', { title: '글쓰기' });
});

// 게시글 db에 저장
router.post('/board/write', function (req, res) {
  var board = new Board();
  board.title = req.body.title;
  board.contents = req.body.contents;
  board.author = req.body.author;

  board.save(function (err) {
    if(err){
      console.log(err);
      res.redirect('/');
    }
    res.redirect('/');
  });
});

// 게시판 읽기
router.get('/board/:id', function (req, res) {
    Board.findOne({_id: req.params.id}, function (err, board) {
        
        // 조회수 
        board.views++;
        board.save();

        res.render('board', { title: '게시판', board: board });

    })
});



// 글쓰기
router.post('/comment/write', function (req, res){
    var comment = new Comment();
    comment.contents = req.body.contents;
    comment.author = req.body.author;

    Board.findOneAndUpdate({_id : req.body.id}, { $push: { comments : comment}}, function (err, board) {
        if(err){
            console.log(err);
            res.redirect('/');
        }
        res.redirect('/board/'+req.body.id);
    });
});

// 글 삭제하기
router.delete("/board/:id",function(req,res){
    Board.remove({_id:req.params.id},function(err){
        if(err) return res.json(err);
        res.redirect('/');
    });
});

// 글 편집하기
router.get('/board/:id/edit',function(req,res){
    Board.findOne({_id:req.params.id}, function(err,board){
        if(err) return res.json(err);
        res.render('edit',{title: '수정', board:board});
    });
});

// 글 업데이트 하기
router.put("/board/:id",function(req,res){
    req.body.board_date = Date.now();
    Board.findOneAndUpdate({_id:req.params.id}, req.body, function(err,board){
        if(err) return res.json(err);
        res.redirect('/');
    });
});



// 검색하기
/*router.get('/board/search', function(req, res){ 
        var search_word = req.param('searchWord'); 
        var searchCondition = {$regex:search_word}; 
     
        Board.find({deleted:false, $or:[{title:searchCondition},{contents:searchCondition},{author:searchCondition}]}).sort({date:-1}).exec(function(err, searchContents){ 
            if(err) throw err; 
     
            res.render('/',{title:"게시판"}); 
        }); 
    }); 
    

*/



/*
app.get('/board/:author', function(req, res){
    Board.find({author: req.params.author}, function(err, board){
        if(err) return res.status(500).json({error: err});
        if(board.length === 0) return res.status(404).json({error:'book not found'});
        res.redirect('/');
    })
});
*/
module.exports = router;

