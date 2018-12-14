
var express = require('express');
var bodyParser = require('body-parser');
var fs = require('fs');
var app = express();

app.use(bodyParser.urlencoded({extended:false}));

app.locals.pretty =true;
app.set('views', './views_File');
app.set('view engine', 'jade');

app.get('/topic/new', function(req, res){
    fs.readdir('data', function(err, files){
        if(err){
            console.log(err);
            res.status(500).send('Internal Server Error');
        }

    res.render('new', {topics:files});
})
})

app.get(['/topic', '/topic/:id'], function(req, res){
    fs.readdir('data', function(err, files){
        if(err){
            console.log(err);
            res.status(500).send('Internal Server Error');
        }
        var id=req.params.id;
        //id값이 있을때 
        if(id){
        fs.readFile('data/'+id, 'utf8', function(err, data){
            if(err){
                console.log(err);
                res.status(500).send('Internal Server Error');
            }
                res.render('view',{topics:files, title:id, description:data});
    });}
    //id값이 없을때
    else{
        res.render('view',{topics:files, title:'Welcome', description:'Hello, java'});
    }
    })
    
})

app.post('/topic', function(req, res){
    var title = req.body.title;
    var description = req.body.description;
    fs.writeFile('data/'+title, description, function(err){
        if(err){
            res.status(500).send('Internal Server Error');
        }
        res.redirect('/topic/'+title);
    });
})
app.listen(3030, function(){
    console.log('Connected, 3030 port!');
})